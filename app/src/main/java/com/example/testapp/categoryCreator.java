package com.example.testapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class categoryCreator extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference ref;
    private String uid;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    public FloatingActionButton picselect;
    public Button confirm;
    public EditText categoryname;
    public ImageView image;
    public Uri imageurl;
    public int checker=0;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(categoryCreator.this,sellerhome.class);
        intent.putExtra("page","3");
        startActivity(intent);
        finish();
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_creator);

        picselect=findViewById(R.id.selectorcategory);
        confirm=findViewById(R.id.categorybutton);
        categoryname= findViewById(R.id.categorynameinput);
        image=findViewById(R.id.categoryimage);

        user= FirebaseAuth.getInstance().getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReference("users");
        uid=user.getUid();
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();


        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        picselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosepic();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createcategory();
            }
        });

    }

    public void createcategory(){
        String categoryinput=categoryname.getText().toString().trim();
        if(categoryinput.isEmpty()){
            categoryname.setError("Input Name of Category");
            categoryname.requestFocus();
        }else{
            ref.child(uid).child("categories").child(categoryname.getText().toString()).child("url").setValue(checker==1?"available":"none");
            Intent intent=new Intent(categoryCreator.this,sellerhome.class);
            intent.putExtra("page","3");
            startActivity(intent);
            finish();
            return;
        }
    }

    private void choosepic() {
        String categoryinput=categoryname.getText().toString().trim();
        if(categoryinput.isEmpty()){
            categoryname.setError("Input Name of Category");
            categoryname.requestFocus();
        }else{
            Intent intent=new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent,1);
        }
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
        StorageReference mountainImagesRef = storageReference.child("image3/"+uid+"/"+categoryname.getText().toString());
        mountainImagesRef.putFile(imageurl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getBaseContext(), "Successfully Uploaded", Toast.LENGTH_SHORT).show();
            }
        });

    }


}