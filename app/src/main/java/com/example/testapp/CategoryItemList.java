package com.example.testapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;
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
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class CategoryItemList extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference ref,ref2;
    private String uid;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private RecyclerView categoryitemlist;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent=new Intent(CategoryItemList.this,sellerhome.class);
        intent.putExtra("page","3");
        startActivity(intent);
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_item_list);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        user= FirebaseAuth.getInstance().getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReference("users");
        ref2=FirebaseDatabase.getInstance().getReference("items");
        uid=user.getUid();
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        categoryitemlist=findViewById(R.id.itemscontainer);

        String category=getIntent().getStringExtra("category");
        LinearLayoutManager layoutManager =new LinearLayoutManager(getBaseContext());
        categoryitemlist.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);


        ref.child(uid).child("categories").child(category).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<items> item=new ArrayList<>();
                ArrayList<String> itemuuid=new ArrayList<>();

                Activity act = (Activity) decorView.findViewById(android.R.id.content).getContext();
                CategoryItemAdapter test2=new CategoryItemAdapter(act,getIntent().getStringExtra("category"));
                test2.setCategoryItem(item,itemuuid);
                categoryitemlist.setAdapter(test2);

                item.add(new items("Click to Add Item","","available","","","","","","",0));
                if(snapshot.child("items").exists()){
                    Map<String, Map<String,Object>> map= (Map)snapshot.child("items").getValue();
                    Set<String> test=map.keySet();
                    String[] myArray = new String[test.size()];
                    test.toArray(myArray);

                    Log.d("Array List", Arrays.toString(myArray));



                    ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for(int i=0;i<myArray.length;i++){
                                items itemprof=snapshot.child(myArray[i]).getValue(items.class);
                                itemuuid.add(0,myArray[i]);
                                item.add(0,itemprof);
                                test2.notifyDataSetChanged();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

                        @Override
                        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                            return false;
                        }

                        @Override
                        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                            int position = viewHolder.getAdapterPosition();

                            ref2.child(myArray[position]).removeValue();
                            ref.child(uid).child("categories").child(category).child("items").child(myArray[position]).removeValue();
                            StorageReference imageOfCategory = storageReference.child("images2/"+myArray[position]);
                            imageOfCategory.delete();

                            item.remove(position);
                            itemuuid.remove(position);


                            test2.notifyDataSetChanged();

                        }
                    };
                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
                    itemTouchHelper.attachToRecyclerView(categoryitemlist);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}