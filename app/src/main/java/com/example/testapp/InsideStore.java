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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class InsideStore extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference ref,ref2;
    private String uid;
    private FirebaseStorage storage;
    private RecyclerView rv1,rv2;
    private TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_store);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        user= FirebaseAuth.getInstance().getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReference("users");
        ref2= FirebaseDatabase.getInstance().getReference("items");
        uid=user.getUid();
        storage=FirebaseStorage.getInstance();

        name=findViewById(R.id.namestoreowner);
        rv1=findViewById(R.id.categorylist);
        rv2=findViewById(R.id.itemlist);
        String storeuuid=getIntent().getStringExtra("storeuuid");

        LinearLayoutManager layoutManager =new LinearLayoutManager(this);
        LinearLayoutManager layoutManager2 =new LinearLayoutManager(this);

        rv1.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        rv2.setLayoutManager(layoutManager2);
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);

        ref.child(storeuuid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usercred storeprof=snapshot.getValue(usercred.class);
                Map <String, Map<String,String>> map = (Map)snapshot.child("categories").getValue();
                Set<String> test=map.keySet();
                String[] myArray = new String[test.size()];
                test.toArray(myArray);
                name.setText(storeprof.getName());
                ArrayList<categories> categorylist=new ArrayList<>();
                ArrayList<items> itemlist=new ArrayList<>();
                ArrayList<String> itemuuid=new ArrayList<>();

                for(int i=0;i<myArray.length;i++){
                    categorylist.add(new categories(myArray[i],map.get(myArray[i]).get("url")));

                    try{
                        Map <String, Map<String,String>> map2 = (Map)snapshot.child("categories").child(myArray[i]).child("items").getValue();
                        Set<String> test2=map2.keySet();
                        String[] myArray2 = new String[test2.size()];
                        test2.toArray(myArray2);

                        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(int j=0;j<myArray2.length;j++){
                                    items item=snapshot.child(myArray2[j]).getValue(items.class);
                                    itemlist.add(item);
                                    itemuuid.add(myArray2[j]);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }catch (Exception e){

                    }



                }



                categorystoreAdapter adapter=new categorystoreAdapter(getBaseContext());
                adapter.setAdapter(categorylist,storeuuid);
                rv1.setAdapter(adapter);

                itemAdapter adapter2=new itemAdapter(getBaseContext());
                adapter2.setItem(itemlist,itemuuid);
                rv2.setAdapter(adapter2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}