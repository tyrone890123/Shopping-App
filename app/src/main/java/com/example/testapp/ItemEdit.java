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
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import java.util.Map;
import java.util.Set;

public class ItemEdit extends AppCompatActivity {

    private int checker=0;
    private FirebaseUser user;
    private DatabaseReference ref,ref2;
    private String uid;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    public Uri imageurl;
    private String uuid;

    private ImageView image;
    private FloatingActionButton select;
    private Button confirm;
    private EditText name,stock,price,desc;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(ItemEdit.this,CategoryItemList.class);
        intent.putExtra("category",getIntent().getStringExtra("category"));
        startActivity(intent);
        finish();
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_edit);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        user= FirebaseAuth.getInstance().getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReference("users");
        ref2=FirebaseDatabase.getInstance().getReference("items");
        uid=user.getUid();
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();

        String category=getIntent().getStringExtra("category");
        uuid=getIntent().getStringExtra("itemuuid");

        image=findViewById(R.id.itemeditimage);
        select=findViewById(R.id.picselectitemedit);
        name=findViewById(R.id.nameedititem);
        stock=findViewById(R.id.stockedititem);
        price=findViewById(R.id.priceedititem);
        desc=findViewById(R.id.descriptionedititem);
        confirm=findViewById(R.id.edititembutton);

        ref2.child(uuid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items itemProf=snapshot.getValue(items.class);
                name.setText(itemProf.getName());
                stock.setText(itemProf.getStock());
                price.setText(itemProf.getPrice());
                desc.setText(itemProf.getDescription());

                if(itemProf.getUrl().equals("available")){
                    storageReference.child("images2/"+uuid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(getBaseContext())
                                    .load(uri.toString().trim())
                                    .into(image);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameinput=name.getText().toString().trim();
                String stockinput=stock.getText().toString().trim();
                String priceinput=price.getText().toString().trim();
                String descinput=desc.getText().toString().trim();

                    if (nameinput.isEmpty()) {
                        name.setError("Email is required");
                        name.requestFocus();
                        if (stockinput.isEmpty()) {
                            stock.setError("stock is required");
                            stock.requestFocus();
                            if (priceinput.isEmpty()) {
                                price.setError("price is required");
                                price.requestFocus();
                                if (descinput.isEmpty()) {
                                    desc.setError("description is required");
                                    desc.requestFocus();
                                }
                            }
                        }
                    }else{
                        ref2.child(uuid).child("name").setValue(nameinput);
                        ref2.child(uuid).child("stock").setValue(stockinput);
                        ref2.child(uuid).child("price").setValue(priceinput);
                        ref2.child(uuid).child("description").setValue(descinput);
                        Intent intent=new Intent(ItemEdit.this,CategoryItemList.class);
                        intent.putExtra("category",getIntent().getStringExtra("category"));
                        startActivity(intent);
                        finish();
                        return;
                    }
            }
        });

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosepic();
            }
        });
    }

    public void choosepic(){
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
            checker=1;
            uploadimage();
        }
    }

    private void uploadimage() {
        StorageReference mountainImagesRef = storageReference.child("images2/"+uuid);
        mountainImagesRef.putFile(imageurl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getBaseContext(), "Successfully Uploaded", Toast.LENGTH_SHORT).show();
            }
        });
    }

}