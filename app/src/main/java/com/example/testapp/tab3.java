package com.example.testapp;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class tab3 extends Fragment {
    private FirebaseUser user;
    private DatabaseReference ref,ref2;
    private String uid;
    private FirebaseStorage storage;
    private Button checkout;
    public TextView price;
    public ArrayList<String> itemuuid;
    public LinearLayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab3, container, false);

        RecyclerView shoppingcart=rootView.findViewById(R.id.shoppingcartcontainer);

        user= FirebaseAuth.getInstance().getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReference("users");
        ref2=FirebaseDatabase.getInstance().getReference("items");
        uid=user.getUid();
        storage=FirebaseStorage.getInstance();
        price=rootView.findViewById(R.id.totalprice);
        checkout=rootView.findViewById(R.id.checkoutbutton);
        layoutManager =new LinearLayoutManager(getActivity());
        shoppingcart.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        ref.child(uid).child("cart").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("items")){
                    Map <String, Map<String,Map<String,String>>> quantity = (Map)snapshot.getValue();
                    Map <String, Map<String,String>> map = (Map)snapshot.getValue();
                    Map <String,String> cost=(Map)snapshot.getValue();
                    Set<String> test=map.get("items").keySet();
                    String[] myArray = new String[test.size()];
                    test.toArray(myArray);
                    price.setText(cost.get("subtotal"));


                    ArrayList<carts> cartitems=new ArrayList<>();
                    itemuuid=new ArrayList<>();

                    cartAdapter adapter=new cartAdapter(rootView);
                    adapter.setCart(cartitems,itemuuid);
                    shoppingcart.setAdapter(adapter);

                    checkout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(rootView.getContext(),checkout.class);
                            intent.putExtra("chosen","none");
                            rootView.getContext().startActivity(intent);
                            ((Activity)rootView.getContext()).finish();
                            return;
                        }
                    });


                    for(int i=0;i<myArray.length;i++){
                        int iteration=i;
                        int quantityItem=Integer.parseInt(quantity.get("items").get(myArray[i]).get("quantity"));
                        String id=myArray[i];

                        ref2.child(myArray[i]).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                try{
                                    double total=0;
                                    items itemProf=snapshot.getValue(items.class);
                                    if (quantityItem>Integer.valueOf(itemProf.getStock())){
                                        cartitems.add(new carts(itemProf.getName(),itemProf.getCategory(),itemProf.getUrl(),itemProf.getRating(),itemProf.getPrice(),itemProf.getStock(),itemProf.getBrand(),itemProf.getDescription(),String.valueOf(quantityItem)));
                                        adapter.notifyDataSetChanged();
                                        checkout.setEnabled(false);
                                        itemuuid.add(id);
                                    }else if(Integer.valueOf(itemProf.getStock())>0){
                                        cartitems.add(new carts(itemProf.getName(),itemProf.getCategory(),itemProf.getUrl(),itemProf.getRating(),itemProf.getPrice(),itemProf.getStock(),itemProf.getBrand(),itemProf.getDescription(),String.valueOf(quantityItem)));
                                        adapter.notifyDataSetChanged();
                                        itemuuid.add(id);
                                    }else{
                                        cartitems.add(new carts("OUT OF STOCK",itemProf.getCategory(),itemProf.getUrl(),itemProf.getRating(),itemProf.getPrice(),itemProf.getStock(),itemProf.getBrand(),itemProf.getDescription(),String.valueOf(quantityItem)));
                                        adapter.notifyDataSetChanged();
                                        checkout.setEnabled(false);
                                        itemuuid.add(id);
                                    }


                                    if(iteration+1==myArray.length){
                                        for(int j=0;j<cartitems.size();j++){
                                            total=total+(Double.parseDouble(cartitems.get(j).getPrice())*Double.parseDouble(quantity.get("items").get(myArray[j]).get("quantity")));
                                        }
                                        if(total!=Double.parseDouble(price.getText().toString())) {
                                            price.setText(String.valueOf(total));
                                            ref.child(uid).child("cart").child("subtotal").setValue(String.valueOf(total));
                                        }
                                    }
                                }catch (Exception e){
                                    ref.child(uid).child("cart").child("items").child(id).removeValue();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }


                    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

                        @Override
                        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                            return false;
                        }

                        @Override
                        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                            int checker=0;
                            int position = viewHolder.getAdapterPosition();
                            double sum=Double.parseDouble(price.getText().toString());
                            sum-=(Double.parseDouble(cartitems.get(position).getPrice())*(Double.parseDouble(quantity.get("items").get(myArray[position]).get("quantity"))));

                            cartitems.remove(position);
                            itemuuid.remove(position);

                            for(int q=0;q<cartitems.size();q++){
                                if((Integer.valueOf(cartitems.get(q).getStock())==0)||(Integer.valueOf(cartitems.get(q).getStock())<Integer.valueOf(cartitems.get(q).getQuantity()))){
                                    checker++;
                                }
                            }

                            if(checker==0){
                                checkout.setEnabled(true);
                            }



                            ref.child(uid).child("cart").child("items").child(myArray[position]).removeValue();
                            ref.child(uid).child("cart").child("subtotal").setValue(String.valueOf(sum));

                            price.setText(String.valueOf(sum));
                            adapter.notifyDataSetChanged();

                        }
                    };
                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
                    itemTouchHelper.attachToRecyclerView(shoppingcart);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return rootView;
    }
}