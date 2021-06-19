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

public class changeName extends AppCompatActivity {
    public Button saveChanges;
    public ImageButton returnButton;
    public FirebaseUser user;
    public FirebaseAuth fbAuth;
    public FirebaseStorage fbStorage;
    public FirebaseDatabase fbDatabase;
    public StorageReference storageRef;
    public DatabaseReference dbref;
    public TextView accountName;
    public EditText newName;
    public String newNameInput, userUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);

        user= FirebaseAuth.getInstance().getCurrentUser();
        dbref= FirebaseDatabase.getInstance().getReference("users");
        userUID=user.getUid();
        fbStorage=FirebaseStorage.getInstance();
        storageRef=fbStorage.getReference();
        accountName=findViewById(R.id.accountName);
        newName=findViewById(R.id.newName);
        returnButton=findViewById(R.id.returnButton);
        saveChanges=findViewById(R.id.saveChanges);

        dbref.child(userUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usercred profile=snapshot.getValue(usercred.class);
                String nameUser=profile.name;
                accountName.setText(nameUser);
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
                newNameInput=newName.getText().toString().trim();

                if (newNameInput.isEmpty()){
                    newName.setError("New Name required.");
                    newName.requestFocus();
                }
                else{
                    if (isNameChanged()){
                        Toast.makeText(changeName.this, "Name has been changed", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(changeName.this, customerhome.class));
                        finish();
                    }
                    else {
                        Toast.makeText(changeName.this, "Similar input your previous cannot and cannot be updated.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean isNameChanged() {
        if (!newName.equals(accountName.getText().toString())){
            dbref.child(userUID).child("name").setValue(newName.getText().toString());
            return true;
        }
        else{
            return false;
        }
    }
}