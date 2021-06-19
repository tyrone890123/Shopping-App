package com.example.testapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class tab2 extends Fragment {

    private EditText searchbar;
    private FloatingActionButton button;
    private RecyclerView list,list2;
    private DatabaseReference ref,ref2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tab2, container, false);
        searchbar=rootView.findViewById(R.id.search);
        button=rootView.findViewById(R.id.buttonsearch);
        list=rootView.findViewById(R.id.searchresultuser);
        list2=rootView.findViewById(R.id.searchresultitem);

        LinearLayoutManager layoutManager =new LinearLayoutManager(getActivity());
        LinearLayoutManager layoutManager2 =new LinearLayoutManager(getActivity());

        list.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        list2.setLayoutManager(layoutManager2);
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);


        ref= FirebaseDatabase.getInstance().getReference("users");
        ref2= FirebaseDatabase.getInstance().getReference("items");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchedval=searchbar.getText().toString();
                searchdatabase(rootView.getContext(),searchedval);

            }
        });
        return rootView;
    }

    private void searchdatabase(Context context,String val) {
        Query firebasesearch2=ref2.orderByChild("name").startAt(val).endAt(val+"\uf8ff");
        Query firebasesearch=ref.orderByChild("name").startAt(val).endAt(val+"\uf8ff");


        firebasesearch.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    list.setVisibility(View.VISIBLE);
                    Map<String, Map<String,String>> storeProf= (Map)snapshot.getValue();
                    Set<String> keys=storeProf.keySet();
                    String[] myArray = new String[keys.size()];
                    keys.toArray(myArray);

                    ArrayList<stores> store=new ArrayList<>();
                    ArrayList<String> storeuuid=new ArrayList<>();

                    for(int i=0;i<(myArray.length<6?myArray.length:6);i++){
                        if(storeProf.get(myArray[i]).get("type").equals("Seller")){
                            Map <String, String> fields = storeProf.get(myArray[i]);
                            store.add(new stores(fields.get("name"),fields.get("pic"),fields.get("rating"),fields.get("adress")));
                            storeuuid.add(myArray[i]);
                        }
                    }

                    storeAdapter test=new storeAdapter(context);
                    test.setStore(store,storeuuid);
                    list.setAdapter(test);
                }catch (Exception e){
                    if(list.getVisibility()==View.VISIBLE){
                        list.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        firebasesearch2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    list2.setVisibility(View.VISIBLE);
                    Map<String, Map<String,Object>> storeProf= (Map)snapshot.getValue();
                    Set<String> keys=storeProf.keySet();
                    String[] myArray = new String[keys.size()];
                    keys.toArray(myArray);

                    ArrayList<items> item=new ArrayList<>();
                    ArrayList<String> itemuuid=new ArrayList<>();

                    for(int i=0;i<myArray.length;i++){
                        Map<String, Object> fields = storeProf.get(myArray[i]);
                        itemuuid.add(myArray[i]);
                        item.add(new items(fields.get("name").toString(), fields.get("category").toString(), fields.get("url").toString(), fields.get("rating").toString(), fields.get("price").toString(),fields.get("stock").toString(), fields.get("brand").toString(), fields.get("description").toString(), fields.get("sellerUUID").toString(),((Long) fields.get("quantitySold")).intValue()));
                    }

                    itemAdapter test2=new itemAdapter(context);
                    test2.setItem(item,itemuuid);
                    list2.setAdapter(test2);

                }catch (Exception e){
                    if(list2.getVisibility()==View.VISIBLE){
                        list2.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}