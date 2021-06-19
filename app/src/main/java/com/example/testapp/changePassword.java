package com.example.testapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class changePassword extends AppCompatActivity {
    public EditText currentPassword, newPassword, confirmPassword;
    public Button saveChanges;
    public ImageButton returnButton;
    public String currentPassInput, newPassInput, confirmPassInput;
    public FirebaseUser user;
    public FirebaseAuth fbAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        user=FirebaseAuth.getInstance().getCurrentUser();
        fbAuth=FirebaseAuth.getInstance();
        currentPassword=findViewById(R.id.currentPassword);
        newPassword=findViewById(R.id.newPassword);
        confirmPassword=findViewById(R.id.confirmPassword);
        returnButton=findViewById(R.id.returnButton1);
        saveChanges=findViewById(R.id.saveChanges);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                return;
            }
        });
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPassInput=currentPassword.getText().toString().trim();
                newPassInput=newPassword.getText().toString().trim();
                confirmPassInput=confirmPassword.getText().toString().trim();

                if (currentPassInput.isEmpty()){
                    currentPassword.setError("Current password is required.");
                    currentPassword.requestFocus();
                    if (newPassInput.isEmpty()){
                        newPassword.setError("New password is required.");
                        newPassword.requestFocus();
                        if (confirmPassInput.isEmpty()){
                            confirmPassword.setError("Confirm password to save changes.");
                            confirmPassword.requestFocus();
                        }
                    }
                }
                else{
                    if (newPassInput.equals(confirmPassInput)){
                        if (user != null && user.getEmail() != null){
                            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassInput);

                            // Prompt the user to re-provide their sign-in credentials
                            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(changePassword.this, "Re-authentication success.", Toast.LENGTH_SHORT).show();

                                        user.updatePassword(newPassInput).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(changePassword.this, "User password updated.", Toast.LENGTH_SHORT).show();

                                                    fbAuth.getInstance().signOut();
                                                    Intent intent = new Intent(changePassword.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        });
                                    }
                                    else{
                                        Toast.makeText(changePassword.this, "Re-authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else{
                            Intent intent = new Intent(changePassword.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    else{
                        Toast.makeText(changePassword.this, "New password not confirmed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}