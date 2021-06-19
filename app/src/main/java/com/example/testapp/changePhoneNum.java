package com.example.testapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class changePhoneNum extends AppCompatActivity {
    public Button saveChanges;
    public ImageButton returnButton;
    public FirebaseUser user;
    public FirebaseAuth fbAuth;
    public FirebaseStorage fbStorage;
    public FirebaseDatabase fbDatabase;
    public StorageReference storageRef;
    public DatabaseReference dbref;
    public TextView accountPhoneNum;
    public EditText newPhoneNum;
    public String newPhoneNumInput, userUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone_num);

        user= FirebaseAuth.getInstance().getCurrentUser();
        dbref= FirebaseDatabase.getInstance().getReference("users");
        userUID=user.getUid();
        fbStorage= FirebaseStorage.getInstance();
        storageRef=fbStorage.getReference();
        accountPhoneNum =findViewById(R.id.accountPhoneNum);
        newPhoneNum =findViewById(R.id.newPhoneNum);
        returnButton=findViewById(R.id.returnButton);
        saveChanges=findViewById(R.id.saveChanges);

        dbref.child(userUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usercred profile=snapshot.getValue(usercred.class);
                String phoneNumUser=profile.phone;
                accountPhoneNum.setText(phoneNumUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.print("mistake");
            }
        });

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
                newPhoneNumInput = newPhoneNum.getText().toString().trim();

                if (newPhoneNumInput.isEmpty()){
                    newPhoneNum.setError("New Name required.");
                    newPhoneNum.requestFocus();
                }
                else{
                    if (isPhoneNumChanged()){
                        Toast.makeText(changePhoneNum.this, "Phone number has been changed", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(changePhoneNum.this, customerhome.class));
                        finish();
                    }
                    else {
                        Toast.makeText(changePhoneNum.this, "Similar input your previous cannot and cannot be updated.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean isPhoneNumChanged() {
        if (!newPhoneNum.equals(accountPhoneNum.getText().toString())){
            dbref.child(userUID).child("phone").setValue(newPhoneNum.getText().toString());
            return true;
        }
        else{
            return false;
        }
    }
}