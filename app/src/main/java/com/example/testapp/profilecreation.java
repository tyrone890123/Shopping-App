package com.example.testapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class profilecreation extends AppCompatActivity {
    private FirebaseUser user;
    private DatabaseReference ref;
    private String uid;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    public Button proceed;
    public EditText name,phone,adress;
    public RadioGroup gender;
    public FloatingActionButton picselect;
    public ImageView image;
    public ProgressBar progress;
    public Uri imageurl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilecreation);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        //instantiation
        proceed=findViewById(R.id.profilecreationbutton);
        name= findViewById(R.id.nameinput);
        phone=findViewById(R.id.phoneinput);
        adress=findViewById(R.id.adressinput);
        gender=findViewById(R.id.groupedradiobuttonsprof);
        picselect=findViewById(R.id.picselect);
        user= FirebaseAuth.getInstance().getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReference("users");
        uid=user.getUid();
        image=findViewById(R.id.profcreationimage);
        progress=findViewById(R.id.progressbarcreation);
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();

        picselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosepic();
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setprofile();
            }
        });
    }

    private void choosepic() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null&&data.getData()!=null){
            imageurl=data.getData();
            image.setImageURI(imageurl);
            ref.child(uid).child("pic").setValue("available");
            uploadimage();
        }
    }

    private void uploadimage() {
        StorageReference mountainImagesRef = storageReference.child("images/"+uid);
        mountainImagesRef.putFile(imageurl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(profilecreation.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setprofile() {
        String nameinput = name.getText().toString().trim();
        String phoneinput = phone.getText().toString().trim();
        String adressinput = adress.getText().toString().trim();
        if (gender.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Type not selected", Toast.LENGTH_SHORT).show();
        } else {
            String genderinput = ((RadioButton) findViewById(gender.getCheckedRadioButtonId())).getText().toString().trim();
            if (nameinput.isEmpty()) {
                name.setError("Email is required");
                name.requestFocus();
                if (phoneinput.isEmpty()) {
                    phone.setError("Email is required");
                    phone.requestFocus();
                    if (adressinput.isEmpty()) {
                        adress.setError("Email is required");
                        adress.requestFocus();
                    }
                }
            }else{
                ref.child(uid).child("name").setValue(nameinput);
                ref.child(uid).child("phone").setValue(phoneinput);
                ref.child(uid).child("gender").setValue(genderinput);
                ref.child(uid).child("adress").setValue(adressinput);
                if(ref.child(uid).child("type").get().equals("Customer")){
                    ref.child(uid).child("cart").child("subtotal").setValue("0");
                }else{
                    ref.child(uid).child("sales").setValue("0");
                    ref.child(uid).child("rating").setValue("0");
                    ref.child(uid).child("total").setValue("0");
                    ref.child(uid).child("inputs").setValue("0");
                }
                ref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        usercred user=snapshot.getValue(usercred.class);
                        if(user.type.equals("Customer")){
                            startActivity(new Intent(profilecreation.this,customerhome.class));
                        }else if(user.type.equals("Seller")){
                            Intent sellerpage=new Intent(profilecreation.this,sellerhome.class);
                            sellerpage.putExtra("page","1");
                            startActivity(sellerpage);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        }
    }
}