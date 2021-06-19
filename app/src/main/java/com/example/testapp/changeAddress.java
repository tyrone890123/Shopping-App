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

public class changeAddress extends AppCompatActivity {
    public Button saveChanges;
    public ImageButton returnButton;
    public FirebaseUser user;
    public FirebaseAuth fbAuth;
    public FirebaseStorage fbStorage;
    public FirebaseDatabase fbDatabase;
    public StorageReference storageRef;
    public DatabaseReference dbref;
    public TextView accountAddress;
    public EditText newAddress;
    public String newAddressInput, userUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_address);

        user= FirebaseAuth.getInstance().getCurrentUser();
        dbref= FirebaseDatabase.getInstance().getReference("users");
        userUID=user.getUid();
        fbStorage=FirebaseStorage.getInstance();
        storageRef=fbStorage.getReference();
        accountAddress=findViewById(R.id.accountAddress);
        newAddress=findViewById(R.id.newAddress);
        returnButton=findViewById(R.id.returnButton);
        saveChanges=findViewById(R.id.saveChanges);

        dbref.child(userUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usercred profile=snapshot.getValue(usercred.class);
                String addressUser=profile.adress;
                accountAddress.setText(addressUser);
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
                newAddressInput=newAddress.getText().toString().trim();

                if (newAddressInput.isEmpty()){
                    newAddress.setError("New Address required.");
                    newAddress.requestFocus();
                }
                else{
                    if (isAddressChanged()){
                        Toast.makeText(changeAddress.this, "Address has been changed", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(changeAddress.this, customerhome.class));
                        finish();
                    }
                    else {
                        Toast.makeText(changeAddress.this, "Similar input your previous cannot and cannot be updated.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean isAddressChanged() {
        if (!newAddress.equals(accountAddress.getText().toString())){
            dbref.child(userUID).child("adress").setValue(newAddress.getText().toString());
            return true;
        }
        else{
            return false;
        }
    }
}