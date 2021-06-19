package com.example.testapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;


public class tab3Seller extends Fragment {

    private FirebaseUser user;
    private DatabaseReference ref,ref2;
    private String uid;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private RecyclerView categorylist;

    private View rootView;
    public Uri imageurl;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_tab3_seller, container, false);
        categorylist=rootView.findViewById(R.id.categorycontainer);

        user= FirebaseAuth.getInstance().getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReference("users");
        ref2= FirebaseDatabase.getInstance().getReference("items");
        uid=user.getUid();
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();



        GridLayoutManager layoutManager =new GridLayoutManager(getActivity(),2);
        categorylist.setLayoutManager(layoutManager);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        ref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<categories> category= new ArrayList<>();

                category.add(new categories("Click To Add Category","available"));

                if(snapshot.child("categories").exists()){
                    Map<String, Map<String,String>> map= (Map)snapshot.child("categories").getValue();
                    Set<String> test=map.keySet();
                    String[] myArray = new String[test.size()];
                    test.toArray(myArray);
                    for(int i=0;i<myArray.length;i++){
                        category.add(0,new categories(myArray[i],map.get(myArray[i]).get("url")));
                    }
                }

                categoriesAdapter cartAd=new categoriesAdapter(rootView);
                cartAd.setCategory(category);
                cartAd.notifyDataSetChanged();

                categorylist.setAdapter(cartAd);

                ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        int position = viewHolder.getAdapterPosition();

                        ref.child(uid).child("categories").child(category.get(position).name).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.child("items").exists()){
                                    Map<String, String> map= (Map)snapshot.child("items").getValue();
                                    Set<String> test=map.keySet();
                                    String[] myArray = new String[test.size()];
                                    test.toArray(myArray);

                                    for(int i=0;i<myArray.length;i++){
                                        ref2.child(myArray[i]).removeValue();
                                        StorageReference imageOfItem = storageReference.child("images2/"+myArray[i]);
                                        imageOfItem.delete();
                                    }
                                    
                                    ref.child(uid).child("categories").child(category.get(position).name).removeValue();
                                    category.remove(position);
                                    cartAd.notifyDataSetChanged();
                                }else{
                                    ref.child(uid).child("categories").child(category.get(position).name).removeValue();
                                    category.remove(position);
                                    cartAd.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });




                        StorageReference imageOfCategory = storageReference.child("image3/"+uid+"/"+category.get(position).name);
                        imageOfCategory.delete();


                    }
                };
                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
                itemTouchHelper.attachToRecyclerView(categorylist);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return rootView;
    }

}