package com.example.testapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.Map;
import java.util.Set;

public class tab1Seller extends Fragment {
    private FirebaseUser user;
    private DatabaseReference ref,ref2;
    private String uid;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private ImageView image;
    private TextView name,purchase,rating,sales;
    private RecyclerView rv1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tab1_seller, container, false);

        user= FirebaseAuth.getInstance().getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReference("users");
        ref2=FirebaseDatabase.getInstance().getReference("items");
        uid=user.getUid();
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();

        name=rootView.findViewById(R.id.namestore);
        purchase=rootView.findViewById(R.id.actualpurchases);
        rating=rootView.findViewById(R.id.actualrating);
        sales=rootView.findViewById(R.id.actualsales);
        rv1=rootView.findViewById(R.id.notiflist);

        image=rootView.findViewById(R.id.imagestore);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(), openProfile.class);
                startActivity(intent);
            }
        });

        LinearLayoutManager layoutManager =new LinearLayoutManager(getActivity());
        rv1.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String,String> map=(Map) snapshot.child(uid).getValue();
                name.setText(map.get("name"));
                purchase.setText(map.get("inputs"));
                rating.setText(map.get("rating"));
                sales.setText(map.get("sales"));
                if(map.get("pic").equals("available")){
                    storageReference.child("images/"+uid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(rootView.getContext())
                                    .load(uri.toString().trim())
                                    .into(image);
                        }
                    });
                }

                if(snapshot.child(uid).child("notifs").exists()){
                    Map<String,String> map3=(Map)snapshot.child(uid).child("notifs").getValue();
                    Map<String,Map<String,String>> map2=(Map)snapshot.child(uid).child("notifs").getValue();
                    Set<String> test2=map2.keySet();
                    String[] myArray2 = new String[test2.size()];
                    test2.toArray(myArray2);

                    ArrayList<String> picurl=new ArrayList<>();
                    ArrayList<String> names=new ArrayList<>();
                    ArrayList<String> uuids=new ArrayList<>();
                    ArrayList<String> messages=new ArrayList<>();

                    for(int i=0;i<myArray2.length;i++){
                        usercred user=snapshot.child(myArray2[i]).getValue(usercred.class);
                        String name=user.getName();
                        String uuid=myArray2[i];
                        String message=map3.get(myArray2[i]);

                        names.add(name);
                        uuids.add(uuid);
                        messages.add(message);
                        picurl.add(user.getPic());
                    }

                    NotifAdapter adapter=new NotifAdapter(rootView.getContext());
                    adapter.setItem(names,uuids,messages,picurl);
                    rv1.setAdapter(adapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return rootView;
    }
}