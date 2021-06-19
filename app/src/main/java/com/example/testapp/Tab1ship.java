package com.example.testapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
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


public class Tab1ship extends Fragment {
    private FirebaseUser user;
    private DatabaseReference ref,ref2;
    private String uid;
    private FirebaseStorage storage;
    private RecyclerView rc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tab1ship, container, false);

        user= FirebaseAuth.getInstance().getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReference("users");
        ref2=FirebaseDatabase.getInstance().getReference("items");
        uid=user.getUid();
        storage=FirebaseStorage.getInstance();
        rc=rootView.findViewById(R.id.outerrecycler);
        LinearLayoutManager layoutManager =new LinearLayoutManager(getActivity());
        rc.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        ref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("order").exists()){
                    Map <String, Map<String,String>> map = (Map)snapshot.child("order").getValue();
                    Set<String> test=map.keySet();
                    String[] myArray = new String[test.size()];
                    test.toArray(myArray);
//                    Log.d("values", Arrays.toString(myArray));//value of orders
                    ArrayList<ArrayList<ShipItem>>shipslists=new ArrayList<>();
                    ArrayList<ArrayList<String>> itemuuids=new ArrayList<>();
                    ArrayList<String> price=new ArrayList<>();

                    for(int i=0;i<myArray.length;i++){
                        Map <String, Map<String,String>> map2 = (Map)snapshot.child("order").child(myArray[i]).getValue();
                        Set<String> test2=map2.keySet();
                        String[] myArray2 = new String[test2.size()];
                        test2.toArray(myArray2);
                        ArrayList<ShipItem> shipslist= new ArrayList<>();
                        ArrayList<String> itemuuid= new ArrayList<>();
                        price.add(map.get(myArray[i]).get("subtotal"));
//                        Log.d("values", Arrays.toString(myArray2));//value of shop names


                        for(int j=0;j<myArray2.length;j++){
                            try{
                                Map <String, Map<String,String>> map3 = (Map)snapshot.child("order").child(myArray[i]).child(myArray2[j]).getValue();
                                Set<String> test3=map3.keySet();
                                String[] myArray3 = new String[test3.size()];
                                test3.toArray(myArray3);

//                                Log.d("values", Arrays.toString(myArray3));//value of item names

                                for(int k=0;k<myArray3.length;k++){
                                    ShipItem item=snapshot.child("order").child(myArray[i]).child(myArray2[j]).child(myArray3[k]).getValue(ShipItem.class);
                                    itemuuid.add(myArray3[k]);
                                    shipslist.add(item);

                                }
                            }catch (Exception e){
                            }
                        }
                        shipslists.add(shipslist);
                        itemuuids.add(itemuuid);
                    }

                    ShipAdapter adapter=new ShipAdapter(rootView.getContext(),getActivity());
                    adapter.setItem(shipslists,itemuuids,price);
                    rc.setAdapter(adapter);

                    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

                        @Override
                        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                            return false;
                        }

                        @Override
                        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                            int position=viewHolder.getAdapterPosition();



                            ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    ArrayList<ShipItem> itemsreturned =  shipslists.get(position);
                                    ArrayList<String> itemsreturneduuid = itemuuids.get(position);
                                    for(int q=0;q<itemsreturned.size();q++){
                                        items itemProf=snapshot.child(itemsreturneduuid.get(q)).getValue(items.class);
                                        int newstock=Integer.valueOf(itemsreturned.get(q).getQuantity())+Integer.valueOf(itemProf.getStock());
                                        ref2.child(itemsreturneduuid.get(q)).child("stock").setValue(String.valueOf(newstock));
                                    }
                                    ref.child(uid).child("order").child(myArray[position]).removeValue();
                                    shipslists.remove(position);
                                    itemuuids.remove(position);
                                    adapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });










                        }
                    };
                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
                    itemTouchHelper.attachToRecyclerView(rc);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return rootView;
    }
}