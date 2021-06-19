package com.example.testapp;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

public class deliveredAdapter extends RecyclerView.Adapter<deliveredAdapter.ViewHolder>{
    private FirebaseUser user;
    private DatabaseReference ref,ref2;
    private String uid;
    private FirebaseStorage storage;

    private ArrayList<ShipItem> shipA=new ArrayList<>();
    private ArrayList<String> itemuuid=new ArrayList<>();
    private ArrayList<String> orderdivide=new ArrayList<>();
    private ArrayList<String> seller=new ArrayList<>();
    private Fragment currentFragment;
    private FragmentTransaction fragmentTransaction;
    private Context context;

    public deliveredAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.deliveredlist, parent, false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        user= FirebaseAuth.getInstance().getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReference("users");
        ref2=FirebaseDatabase.getInstance().getReference("items");
        uid=user.getUid();
        storage=FirebaseStorage.getInstance();

        holder.name.setText(shipA.get(position).getName());
        holder.quantity.setText(shipA.get(position).getQuantity());
        holder.price.setText(shipA.get(position).getPrice());
        holder.seller.setText(shipA.get(position).getBrand());
        holder.status.setText(shipA.get(position).getStatus());
        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference storageReference=storage.getReference();

        holder.delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.child(uid).child("delivered").child(orderdivide.get(position)).child(seller.get(position)).child(itemuuid.get(position)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ShipItem curritem=snapshot.getValue(ShipItem.class);
                        ref.child(curritem.getSellerUUID()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Map<String,String> map=(Map) snapshot.getValue();
                                double total=Double.parseDouble(map.get("sales"))+(Double.parseDouble(curritem.getPrice())*Double.parseDouble(curritem.getQuantity()));
                                ref.child(curritem.getSellerUUID()).child("sales").setValue(String.valueOf(total));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        ref.child(uid).child("delivered").child(orderdivide.get(position)).child(seller.get(position)).child(itemuuid.get(position)).removeValue();
                        ref.child(uid).child("torate").child(itemuuid.get(position)).setValue(curritem);
                        ref.child(seller.get(position)).child("notifs").child(uid).setValue("Order Recieved");
                        fragmentTransaction.detach(currentFragment);
                        fragmentTransaction.attach(currentFragment);
                        fragmentTransaction.commit();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        holder.notdelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ShipItem curritem=snapshot.child(uid).child("delivered").child(orderdivide.get(position)).child(seller.get(position)).child(itemuuid.get(position)).getValue(ShipItem.class);
                        curritem.setStatus("Processing");
                        if(snapshot.child(uid).child("order").child(orderdivide.get(position)).child("subtotal").exists()){
                            Map<String, String> map= (Map) snapshot.child(uid).child("order").child(orderdivide.get(position)).getValue();
                            double total=Double.parseDouble(map.get("subtotal"))+(Double.valueOf(curritem.getPrice())*Double.valueOf(curritem.getQuantity()));
                            ref.child(uid).child("delivered").child(orderdivide.get(position)).child(seller.get(position)).child(itemuuid.get(position)).removeValue();
                            ref.child(uid).child("order").child(orderdivide.get(position)).child(seller.get(position)).child(itemuuid.get(position)).setValue(curritem);
                            ref.child(uid).child("order").child(orderdivide.get(position)).child("subtotal").setValue(String.valueOf(total));
                            ref.child(seller.get(position)).child("orders").child(uid).child(orderdivide.get(position)).setValue("1");

                            Log.d("total",String.valueOf(total));
                        }else{
                            ref.child(uid).child("delivered").child(orderdivide.get(position)).child(seller.get(position)).child(itemuuid.get(position)).removeValue();
                            ref.child(uid).child("order").child(orderdivide.get(position)).child(seller.get(position)).child(itemuuid.get(position)).setValue(curritem);
                            ref.child(uid).child("order").child(orderdivide.get(position)).child("subtotal").setValue(String.valueOf((Double.valueOf(curritem.getPrice())*Double.valueOf(curritem.getQuantity()))));
                            ref.child(seller.get(position)).child("orders").child(uid).child(orderdivide.get(position)).setValue("1");
                        }
                        ref.child(seller.get(position)).child("notifs").child(uid).setValue("Order NOT Recieved");
                        fragmentTransaction.detach(currentFragment);
                        fragmentTransaction.attach(currentFragment);
                        fragmentTransaction.commit();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        if(shipA.get(position).getUrl().equals("available")){
            storageReference.child("images2/"+itemuuid.get(position)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context)
                            .load(uri.toString().trim())
                            .into(holder.picture);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return shipA.size();
    }

    public void setItem(ArrayList<ShipItem> item,ArrayList<String> itemuuid,ArrayList<String> orderdivide,ArrayList<String> seller, Fragment currentFragment, FragmentTransaction fragmentTransaction) {
        this.shipA = item;
        this.itemuuid=itemuuid;
        this.orderdivide=orderdivide;
        this.seller=seller;
        this.currentFragment=currentFragment;
        this.fragmentTransaction=fragmentTransaction;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private ImageView picture;
        private TextView quantity;
        private TextView seller;
        private TextView price;
        private TextView status;
        private Button delivered;
        private ImageButton notdelivered;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.nameofdelivereditem);
            quantity=itemView.findViewById(R.id.quantitydelivereditem);
            seller=itemView.findViewById(R.id.sellerofdelivereditem);
            picture=itemView.findViewById(R.id.imagedelivereditem);
            price=itemView.findViewById(R.id.pricedelivereditem);
            status=itemView.findViewById(R.id.statusdelivereditem);
            delivered=itemView.findViewById(R.id.recieved);
            notdelivered=itemView.findViewById(R.id.notrecieved);
        }
    }
}
