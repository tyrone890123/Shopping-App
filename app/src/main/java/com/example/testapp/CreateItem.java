package com.example.testapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class CreateItem extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference ref,ref2;
    private String uid;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    public FloatingActionButton picselect;
    public Button confirm;
    public EditText itemname,stock,description,priceitem;
    public ImageView image;

    public Uri imageurl;
    public int checker=0;
    public String uuidAsString;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(CreateItem.this,CategoryItemList.class);
        intent.putExtra("category",getIntent().getStringExtra("category"));
        startActivity(intent);
        finish();
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        user= FirebaseAuth.getInstance().getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReference("users");
        ref2= FirebaseDatabase.getInstance().getReference("items");
        uid=user.getUid();
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();

        picselect=findViewById(R.id.picselectitem);
        confirm=findViewById(R.id.itemcreationbutton);
        itemname=findViewById(R.id.nameinputitem);
        stock=findViewById(R.id.stockinputitem);
        description=findViewById(R.id.descriptioninputitem);
        priceitem=findViewById(R.id.priceinputitem);
        image=findViewById(R.id.itemcreationimage);

        UUID uuid = UUID. randomUUID();
        uuidAsString = uuid. toString();

        picselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosepic();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameinput=itemname.getText().toString().trim();
                String stockinput=stock.getText().toString().trim();
                String priceinput=priceitem.getText().toString().trim();
                String descinput=description.getText().toString().trim();
                if (nameinput.isEmpty()) {
                    itemname.setError("Email is required");
                    itemname.requestFocus();
                    if (stockinput.isEmpty()) {
                        stock.setError("stock is required");
                        stock.requestFocus();
                        if (priceinput.isEmpty()) {
                            priceitem.setError("price is required");
                            priceitem.requestFocus();
                            if (descinput.isEmpty()) {
                                description.setError("description is required");
                                description.requestFocus();
                            }
                        }
                    }
                }else{
                    createitem();
                }
            }
        });
    }

    public void createitem(){
        String category=getIntent().getStringExtra("category");
        ref.child(uid).child("categories").child(category).child("items").child(uuidAsString).child("url").setValue(checker==1?"available":"none");
        ref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, String> map= (Map)snapshot.getValue();
                Set<String> test=map.keySet();
                String[] myArray = new String[test.size()];
                test.toArray(myArray);
                ref2.child(uuidAsString).child("brand").setValue(map.get("name"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        items toadd=new items(itemname.getText().toString().toLowerCase(),category,checker==1?"available":"none","0",priceitem.getText().toString(),stock.getText().toString(),"initial",description.getText().toString(),uid,0);
        ref2.child(uuidAsString).setValue(toadd);
        Intent intent=new Intent(CreateItem.this,CategoryItemList.class);
        intent.putExtra("category",category);
        startActivity(intent);
        finish();
        return;
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
        StorageReference mountainImagesRef = storageReference.child("images2/"+uuidAsString);
        mountainImagesRef.putFile(imageurl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getBaseContext(), "Successfully Uploaded", Toast.LENGTH_SHORT).show();
            }
        });
    }
}