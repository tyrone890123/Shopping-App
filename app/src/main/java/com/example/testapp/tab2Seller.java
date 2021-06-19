package com.example.testapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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

public class tab2Seller extends Fragment {

    private FirebaseUser user;
    private DatabaseReference ref,ref2;
    private String uid;
    private FirebaseStorage storage;
    private RecyclerView rc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab2_seller, container, false);
        user= FirebaseAuth.getInstance().getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReference("users");
        ref2=FirebaseDatabase.getInstance().getReference("items");
        uid=user.getUid();
        storage=FirebaseStorage.getInstance();
        rc=rootView.findViewById(R.id.orderrecycle);
        LinearLayoutManager layoutManager =new LinearLayoutManager(getActivity());
        rc.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(uid).child("orders").exists()){
                    Map <String, Map<String,String>> map = (Map)snapshot.child(uid).child("orders").getValue();
                    Set<String> test=map.keySet();
                    String[] myArray = new String[test.size()];
                    test.toArray(myArray);

                    ArrayList<String[]> items=new ArrayList<>();
                    ArrayList<ArrayList<ShipItem>>shipslists=new ArrayList<>();
                    ArrayList<ArrayList<String>> itemuuids=new ArrayList<>();
                    ArrayList<ArrayList<String>> useruids=new ArrayList<>();
                    ArrayList<ArrayList<usercred>> userprofs=new ArrayList<>();
                    ArrayList<ArrayList<String>> orderuuids=new ArrayList<>();
                    ArrayList<String> names=new ArrayList<>();

                    for(int i=0;i<myArray.length;i++){
                        Map <String, Map<String,String>> map2 = (Map)snapshot.child(uid).child("orders").child(myArray[i]).getValue();
                        Set<String> test2=map2.keySet();
                        String[] myArray2 = new String[test2.size()];
                        test2.toArray(myArray2);
                        items.add(myArray2);
                    }

                    for(int j=0;j<myArray.length;j++){
                        for(int k=0;k<items.get(j).length;k++){
                            try{
                                Map <String, Map<String,String>> map3 = (Map)snapshot.child(myArray[j]).child("order").child(items.get(j)[k]).child(uid).getValue();
                                usercred buyer2=snapshot.child(myArray[j]).getValue(usercred.class);
                                Set<String> test3=map3.keySet();
                                String[] myArray3 = new String[test3.size()];
                                test3.toArray(myArray3);
                                ArrayList<ShipItem> shipslist= new ArrayList<>();
                                ArrayList<String> itemuuid= new ArrayList<>();
                                ArrayList<usercred> users=new ArrayList<>();
                                ArrayList<String> uuid=new ArrayList<>();
                                ArrayList<String> orderuuid=new ArrayList<>();
                                names.add(buyer2.getName());

                                for(int l=0;l<myArray3.length;l++){
                                    ShipItem item=snapshot.child(myArray[j]).child("order").child(items.get(j)[k]).child(uid).child(myArray3[l]).getValue(ShipItem.class);
                                    usercred buyer=snapshot.child(myArray[j]).getValue(usercred.class);
                                    users.add(buyer);
                                    uuid.add(myArray[j]);
                                    orderuuid.add(items.get(j)[k]);
                                    shipslist.add(item);
                                    itemuuid.add(myArray3[l]);
                                }
                                shipslists.add(shipslist);
                                itemuuids.add(itemuuid);
                                userprofs.add(users);
                                useruids.add(uuid);
                                orderuuids.add(orderuuid);
                            }catch (Exception e){
                                ref.child(uid).child("orders").child(myArray[j]).child(items.get(j)[k]).removeValue();
                                ref.child(uid).child("notifs").child(myArray[j]).setValue("An Order is Cancelled");
                            }

                        }
                    }

                    Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragSeller);
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    OrderAdapter adapter=new OrderAdapter(rootView.getContext(),getActivity());
                    adapter.setItem(shipslists,itemuuids,userprofs,useruids,orderuuids,names,currentFragment,fragmentTransaction);
                    rc.setAdapter(adapter);


                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return rootView;
    }

}