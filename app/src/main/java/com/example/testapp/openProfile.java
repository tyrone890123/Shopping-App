package com.example.testapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.AccessController;

public class openProfile extends AppCompatActivity {
    public FirebaseUser user;
    public FirebaseAuth fbAuth;
    public FirebaseStorage fbStorage;
    public FirebaseDatabase fbDatabase;
    public StorageReference storageRef;
    public DatabaseReference dbref;
    public ImageView profilePicture;
    public ImageButton returnButton, logoutButton;
    public TextView changeName, changePhoneNum, changePassword, changeAddress;
    public TextView name, address, phoneNum, password;
    public String userUID;
    public FloatingActionButton selectPicture;
    public Uri imageURL;
    private static final int pickImage=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_profile);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        user=FirebaseAuth.getInstance().getCurrentUser();
        dbref= FirebaseDatabase.getInstance().getReference("users");
        userUID=user.getUid();
        fbStorage=FirebaseStorage.getInstance();
        storageRef=fbStorage.getReference();
        name=findViewById(R.id.accountName);
        address=findViewById(R.id.accountAddress);
        phoneNum=findViewById(R.id.accountPhoneNum);
        password=findViewById(R.id.accountPass);

        returnButton=findViewById(R.id.returnButton);
        logoutButton=findViewById(R.id.logoutButton);
        changeName=findViewById(R.id.changeName);
        changePhoneNum=findViewById(R.id.changePhoneNum);
        changePassword=findViewById(R.id.changePassword);
        changeAddress=findViewById(R.id.changeAddress);
        profilePicture=findViewById(R.id.profilePicture);
        selectPicture=findViewById(R.id.selectPicture);


        dbref.child(userUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usercred profile=snapshot.getValue(usercred.class);
                String nameUser=profile.name;
                name.setText(nameUser);
                String addressUser=profile.adress;
                address.setText(addressUser);
                String phoneNumUser=profile.phone;
                phoneNum.setText(phoneNumUser);

                if(profile!=null){

                    String pic=profile.pic;

                    if(!pic.equals("none")){
                        // Reference to an image file in Cloud Storage
                        storageRef.child("images/"+userUID).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
//                                System.out.print(uri.toString().trim());
                                Glide.with(openProfile.this)
                                        .load(uri.toString().trim())
                                        .into(profilePicture);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                System.out.print(exception);
                            }
                        });
                    }
                }
                returnButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                        if (profile.getType().equals("Customer")){
                            startActivity(new Intent(openProfile.this, customerhome.class));
                        }
                        else {
                            Intent intent = new Intent(openProfile.this, sellerhome.class);
                            intent.putExtra("page","1");
                            startActivity(intent);
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.print("mistake");
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fbAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(openProfile.this, MainActivity.class));
            }
        });

        changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(openProfile.this, changeName.class));
            }
        });

        changePhoneNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(openProfile.this, changePhoneNum.class));
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(openProfile.this, changePassword.class));
            }
        });

        changeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(openProfile.this, changeAddress.class));
            }
        });

        selectPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(intent, pickImage);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            if(requestCode==pickImage && resultCode==RESULT_OK && data!=null && data.getData()!=null){
                imageURL=data.getData();
                profilePicture.setImageURI(imageURL);
                dbref.child(userUID).child("pic").setValue("available");

                StorageReference mountainImagesRef = storageRef.child("images/"+userUID);
                mountainImagesRef.putFile(imageURL).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(openProfile.this, "Image uploaded successfully.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}