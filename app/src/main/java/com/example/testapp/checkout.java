package com.example.testapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
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
import java.util.UUID;

public class checkout extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference ref,ref2;
    private String uid;
    private FirebaseStorage storage;

    private ImageButton location,paymentOption,back;
    private Button placeOrder,confirmAdress;
    private TextView adress,total,method;
    private RecyclerView checkedOutList;
    private MaterialCardView adresseditor;
    private EditText adressinput;

    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent=new Intent(checkout.this,customerhome.class);
        intent.putExtra("page","3");
        startActivity(intent);
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        user= FirebaseAuth.getInstance().getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReference("users");
        ref2=FirebaseDatabase.getInstance().getReference("items");
        uid=user.getUid();
        storage=FirebaseStorage.getInstance();

        location=findViewById(R.id.locaitonicon);
        paymentOption=findViewById(R.id.choosepayment);
        placeOrder=findViewById(R.id.placeorderbutton);
        adress=findViewById(R.id.adressOfUser);
        total=findViewById(R.id.placeorderprice);
        checkedOutList=findViewById(R.id.checkoutlist);
        method=findViewById(R.id.selectedmethod);
        confirmAdress=findViewById(R.id.adressconfirmaiton);
        adresseditor=findViewById(R.id.adresscard);
        adressinput=findViewById(R.id.adresseditor);
        back=findViewById(R.id.backfromedit);
        LinearLayoutManager layoutManager =new LinearLayoutManager(this);
        checkedOutList.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        String selected= getIntent().getStringExtra("chosen");
        editlorem();

        if(selected.equals("none")){
            method.setText("");
            placeOrder.setEnabled(false);
        }else{
            method.setText(selected);
            placeOrder.setEnabled(true);
        }

        ref.child(uid).child("cart").child("items").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map <String, Map<String,String>> map = (Map)snapshot.getValue();
                Set<String> test=map.keySet();
                String[] myArray = new String[test.size()];
                test.toArray(myArray);

                ArrayList<items> item=new ArrayList<>();
                ArrayList<String> itemuuid=new ArrayList<>();
                ArrayList<String> quantity=new ArrayList<>();

                CheckoutAdapter adapter=new CheckoutAdapter(getBaseContext());
                adapter.setCheckout(item,itemuuid,quantity);
                checkedOutList.setAdapter(adapter);

                for(int i=0;i<myArray.length;i++){
                    String quantityItem=map.get(myArray[i]).get("quantity");
                    String id=myArray[i];

                    ref2.child(myArray[i]).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            items itemProf=snapshot.getValue(items.class);
                            item.add(itemProf);
                            itemuuid.add(id);
                            quantity.add(quantityItem);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setvalue();
                if(adresseditor.getVisibility()==View.VISIBLE){
                    adresseditor.setVisibility(View.GONE);
                }
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adresseditor.getVisibility()==View.GONE){
                    adresseditor.setVisibility(View.VISIBLE);
                }
            }
        });

        confirmAdress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adresseditor.getVisibility()==View.VISIBLE){
                    adresseditor.setVisibility(View.GONE);
                    String adresstext=adressinput.getText().toString().trim();
                    if(adresstext.isEmpty()){
                        adressinput.setError("Address is Required");
                        adressinput.requestFocus();
                    }else{
                        ref.child(uid).child("adress").setValue(adresstext);
                        adress.setText(adresstext);
                    }
                }
            }
        });

        paymentOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(checkout.this,PaymentOptions.class);
                intent.putExtra("chosen",selected);
                startActivity(intent);
            }
        });

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(checkout.this,customerhome.class);
                startActivity(intent);
                transfer(selected);
                finish();
                return;
            }
        });
    }

    public void setvalue(){
        ref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usercred user=snapshot.getValue(usercred.class);
                adressinput.setText(user.getAdress());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void editlorem(){
        ref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usercred user=snapshot.getValue(usercred.class);
                adress.setText(user.getAdress());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ref.child(uid).child("cart").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map <String, String> map = (Map)snapshot.getValue();
                total.setText(map.get("subtotal"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void transfer(String choice){
        UUID uuid = UUID. randomUUID();
        String uuidAsString = uuid. toString();
        ref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map <String, Map<String,Map<String,String>>> itemlist=(Map)snapshot.getValue();
                Set<String> test=itemlist.get("cart").get("items").keySet();
                String[] myArray = new String[test.size()];
                test.toArray(myArray);

                Log.d("test","test1");
                Map <String, Map<String,Map<String,Map<String,String>>>> map2=(Map)snapshot.getValue();
                Map <String, Map<String,String>> map = (Map)snapshot.getValue();

                ref.child(uid).child("order").child(uuidAsString).child("subtotal").setValue(map.get("cart").get("subtotal"));
                ref.child(uid).child("order").child(uuidAsString).child("payment").setValue(choice);
                for(int i=0;i<myArray.length;i++){
                    String quantityItem=map2.get("cart").get("items").get(myArray[i]).get("quantity");
                    String id=myArray[i];

                    ref2.child(myArray[i]).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            items itemProf=snapshot.getValue(items.class);
                            int newstock=Integer.valueOf(itemProf.getStock())-Integer.valueOf(quantityItem);
                            ref.child(uid).child("order").child(uuidAsString).child(itemProf.getSellerUUID()).child(id).setValue(itemProf);
                            ref.child(uid).child("order").child(uuidAsString).child(itemProf.getSellerUUID()).child(id).child("quantity").setValue(quantityItem);
                            ref.child(uid).child("order").child(uuidAsString).child(itemProf.getSellerUUID()).child(id).child("status").setValue("Processing");

                            ref2.child(id).child("stock").setValue(String.valueOf(newstock));
                            ref.child(itemProf.getSellerUUID()).child("orders").child(uid).child(uuidAsString).setValue(quantityItem);
                            ref.child(itemProf.getSellerUUID()).child("notifs").child(uid).setValue("An Order is Made");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                ref.child(uid).child("cart").removeValue();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}