package com.example.testapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class tab4Seller extends Fragment {
    private FirebaseUser user;
    private DatabaseReference ref,ref2;
    private String uid;
    private FirebaseStorage storage;
    private RecyclerView rv1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab4_seller, container, false);
        user= FirebaseAuth.getInstance().getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReference("users");
        ref2=FirebaseDatabase.getInstance().getReference("items");
        uid=user.getUid();
        storage=FirebaseStorage.getInstance();
        rv1=rootView.findViewById(R.id.reviewrecycle);
        LinearLayoutManager layoutManager =new LinearLayoutManager(getActivity());
        rv1.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(uid).child("ratingnotif").exists()){
                    Map<String, Map<String,String>> map=(Map)snapshot.child(uid).child("ratingnotif").getValue();
                    Set<String> test=map.keySet();
                    String[] myArray = new String[test.size()]; //item uid
                    test.toArray(myArray);

                    ArrayList<usercred> users=new ArrayList<>();
                    ArrayList<items> itemlist=new ArrayList<>();
                    ArrayList<String> messages=new ArrayList<>();
                    ArrayList<String> uids=new ArrayList<>();
                    ArrayList<String> useruid=new ArrayList<>();

                    for(int i=0;i<myArray.length;i++){
                        Map<String,String>map3=(Map)snapshot.child(uid).child("ratingnotif").child(myArray[i]).getValue();
                        Map<String, Map<String,String>> map2=(Map)snapshot.child(uid).child("ratingnotif").child(myArray[i]).getValue();
                        Set<String> test2=map2.keySet();
                        String[] myArray2 = new String[test2.size()]; //user uid
                        String currentitem=myArray[i];
                        test2.toArray(myArray2);

                        for(int j=0;j<myArray2.length;j++){
                            usercred user=snapshot.child(myArray2[j]).getValue(usercred.class);
                            messages.add(map3.get(myArray2[j]));
                            users.add(user);
                            useruid.add(myArray2[j]);

                        }
                        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(int k=0;k<myArray2.length;k++){
                                    items item=snapshot.child(currentitem).getValue(items.class);
                                    itemlist.add(item);
                                    uids.add(currentitem);
                                }

                                    ReviewAdapter adapter=new ReviewAdapter(rootView.getContext());
                                    adapter.setItem(users,itemlist,uids,messages,useruid);
                                    rv1.setAdapter(adapter);
//                                    Log.d("value1",String.valueOf(users.size()));
//                                    Log.d("value2",String.valueOf(itemlist.size()));
//                                Log.d("value2",messages.toString());
//                                Log.d("value2",uids.toString());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }





                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return rootView;
    }
}