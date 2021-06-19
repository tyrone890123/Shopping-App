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
import java.util.Map;
import java.util.Set;

public class Tab3ship extends Fragment {
    private FirebaseUser user;
    private DatabaseReference ref,ref2;
    private String uid;
    private FirebaseStorage storage;
    private RecyclerView rv1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tab3ship, container, false);

        rv1=rootView.findViewById(R.id.torateitems);
        user= FirebaseAuth.getInstance().getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReference("users");
        ref2=FirebaseDatabase.getInstance().getReference("items");
        uid=user.getUid();
        storage=FirebaseStorage.getInstance();

        LinearLayoutManager layoutManager =new LinearLayoutManager(getActivity());
        rv1.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        ref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("torate").exists()){
                    Map <String, Map<String,String>> map = (Map)snapshot.child("torate").getValue();
                    Set<String> test=map.keySet();
                    String[] myArray = new String[test.size()];
                    test.toArray(myArray);
                    ArrayList<ShipItem> rateitems=new ArrayList<>();
                    ArrayList<String> itemuuid=new ArrayList<>();

                    for(int i=0;i<myArray.length;i++){
                        ShipItem item=snapshot.child("torate").child(myArray[i]).getValue(ShipItem.class);
                        rateitems.add(item);
                        itemuuid.add(myArray[i]);
                    }
                    Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.innerfrag);
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    rateAdapter adapter=new rateAdapter(rootView.getContext());
                    adapter.setItem(rateitems,itemuuid,currentFragment,fragmentTransaction);
                    rv1.setAdapter(adapter);

                    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

                        @Override
                        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                            return false;
                        }

                        @Override
                        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                            int position=viewHolder.getAdapterPosition();

                            ref.child(uid).child("torate").child(myArray[position]).removeValue();
//                            ShipItem item=snapshot.child("torate").child(myArray[position]).getValue(ShipItem.class);
                            rateitems.remove(position);
                            itemuuid.remove(position);
//                            Log.d("value",item.toString());
                            adapter.notifyDataSetChanged();



                        }
                    };
                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
                    itemTouchHelper.attachToRecyclerView(rv1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return rootView;
    }
}