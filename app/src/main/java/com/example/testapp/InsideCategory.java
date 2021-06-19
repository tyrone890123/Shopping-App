package com.example.testapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class InsideCategory extends AppCompatActivity {
    private FirebaseUser user;
    private DatabaseReference ref,ref2;
    private String uid;
    private FirebaseStorage storage;
    private RecyclerView rv1;
    private TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_category);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        user= FirebaseAuth.getInstance().getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReference("users");
        ref2= FirebaseDatabase.getInstance().getReference("items");
        uid=user.getUid();
        storage=FirebaseStorage.getInstance();
        String storeuuid=getIntent().getStringExtra("storeuuid");
        String category=getIntent().getStringExtra("category");
        name=findViewById(R.id.categorieslabel);

        rv1=findViewById(R.id.itemsincategory);
        name.setText(category);

        LinearLayoutManager layoutManager =new LinearLayoutManager(this);
        rv1.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        ref.child(storeuuid).child("categories").child(category).child("items").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map <String, Map<String,String>> map = (Map)snapshot.getValue();
                Set<String> test=map.keySet();
                String[] myArray = new String[test.size()];
                test.toArray(myArray);
                ArrayList<items> itemlist=new ArrayList<>();
                ArrayList<String> itemuuid=new ArrayList<>();

                ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(int i=0;i<myArray.length;i++){
                            items item=snapshot.child(myArray[i]).getValue(items.class);
                            itemlist.add(item);
                            itemuuid.add(myArray[i]);
                        }
                        itemAdapter adapter=new itemAdapter(getBaseContext());
                        adapter.setItem(itemlist,itemuuid);
                        rv1.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}