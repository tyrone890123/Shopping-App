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


public class Tab2ship extends Fragment {
    private FirebaseUser user;
    private DatabaseReference ref,ref2;
    private String uid;
    private FirebaseStorage storage;
    private RecyclerView rc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab2ship, container, false);
        user= FirebaseAuth.getInstance().getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReference("users");
        ref2=FirebaseDatabase.getInstance().getReference("items");
        uid=user.getUid();
        storage=FirebaseStorage.getInstance();

        rc=rootView.findViewById(R.id.delivereditems);
        LinearLayoutManager layoutManager =new LinearLayoutManager(getActivity());
        rc.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        ref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("delivered").exists()){
                    Map <String, Map<String,String>> map = (Map)snapshot.child("delivered").getValue();
                    Set<String> test=map.keySet();
                    String[] myArray = new String[test.size()];
                    test.toArray(myArray);
//                    Log.d("values", Arrays.toString(myArray));//value of orders
                    ArrayList<ShipItem> shipitems=new ArrayList<>();
                    ArrayList<String> itemuuids=new ArrayList<>();
                    ArrayList<String> orderdivide=new ArrayList<>();
                    ArrayList<String> seller=new ArrayList<>();

                    for(int i=0;i<myArray.length;i++){
                        Map <String, Map<String,String>> map2 = (Map)snapshot.child("delivered").child(myArray[i]).getValue();
                        Set<String> test2=map2.keySet();
                        String[] myArray2 = new String[test2.size()];
                        test2.toArray(myArray2);
//                        Log.d("values", Arrays.toString(myArray2));//value of shop names


                        for(int j=0;j<myArray2.length;j++){
                            try{
                                Map <String, Map<String,String>> map3 = (Map)snapshot.child("delivered").child(myArray[i]).child(myArray2[j]).getValue();
                                Set<String> test3=map3.keySet();
                                String[] myArray3 = new String[test3.size()];
                                test3.toArray(myArray3);

//                                Log.d("values", Arrays.toString(myArray3));//value of item names

                                for(int k=0;k<myArray3.length;k++){
                                    ShipItem item=snapshot.child("delivered").child(myArray[i]).child(myArray2[j]).child(myArray3[k]).getValue(ShipItem.class);
                                    itemuuids.add(myArray3[k]);
                                    shipitems.add(item);
                                    orderdivide.add(myArray[i]);
                                    seller.add(myArray2[j]);
//                                    Log.d("values", item.toString());//value of item
                                }
                            }catch (Exception e){
                            }
                        }
                    }
                    Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.innerfrag);
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                    Log.d("values", String.valueOf(shipitems.size()));
                    deliveredAdapter adapter =new deliveredAdapter(rootView.getContext());
                    adapter.setItem(shipitems,itemuuids,orderdivide,seller,currentFragment,fragmentTransaction);
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