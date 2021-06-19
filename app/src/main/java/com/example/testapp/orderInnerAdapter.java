package com.example.testapp;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
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
import java.util.Map;

public class orderInnerAdapter extends RecyclerView.Adapter<orderInnerAdapter.ViewHolder> {
    private FirebaseUser user;
    private DatabaseReference ref;
    private String uid;
    private FirebaseStorage storage;
    private Fragment currentFragment;
    private FragmentTransaction fragmentTransaction;
    private ArrayList<ShipItem> shipA=new ArrayList<>();
    private ArrayList<String> itemuuid=new ArrayList<>();
    private ArrayList<usercred> buyer;
    private ArrayList<String> uuid;
    private ArrayList<String> order;
    private Context context;

    public orderInnerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singleorder, parent, false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        user= FirebaseAuth.getInstance().getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReference("users");
        uid=user.getUid();
        storage=FirebaseStorage.getInstance();

        holder.name.setText(shipA.get(position).getName());
        holder.quantity.setText(shipA.get(position).getQuantity());
        holder.price.setText(shipA.get(position).getPrice());
        holder.status.setText(shipA.get(position).getStatus());
        holder.rg.check(shipA.get(position).getStatus().equals("Shipping")?R.id.radiobuttonshipping:shipA.get(position).getStatus().equals("Processing")?R.id.radiobuttonprocessing:R.id.radiobuttondelivered);
        holder.adress.setText(buyer.get(position).getAdress());

        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference storageReference=storage.getReference();

        holder.rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radiobuttonshipping:
                        holder.status.setText("Shipping");
                        shipA.get(position).setStatus("Shipping");
                        ref.child(uuid.get(position)).child("order").child(order.get(position)).child(uid).child(itemuuid.get(position)).child("status").setValue("Shipping");
                        break;
                    case R.id.radiobuttonprocessing:
                        holder.status.setText("Processing");
                        shipA.get(position).setStatus("Processing");
                        ref.child(uuid.get(position)).child("order").child(order.get(position)).child(uid).child(itemuuid.get(position)).child("status").setValue("Processing");
                        break;
                    case R.id. radiobuttondelivered:
                        holder.status.setText("Delivered");
                        shipA.get(position).setStatus("Delivered");
                        ref.child(uuid.get(position)).child("order").child(order.get(position)).child(uid).child(itemuuid.get(position)).child("status").setValue("Delivered");

                        ref.child(uuid.get(position)).child("order").child(order.get(position)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Map<String,String> test= (Map)snapshot.getValue();
                                ShipItem item=snapshot.child(uid).child(itemuuid.get(position)).getValue(ShipItem.class);
                                Log.d("value",item.toString());
                                double newsubtotal=Double.parseDouble(test.get("subtotal"))-(Double.parseDouble(item.getPrice()))*(Double.parseDouble(item.getQuantity()));
                                Log.d("value",String.valueOf(newsubtotal));
                                if(newsubtotal==0){
                                    ref.child(uuid.get(position)).child("order").child(order.get(position)).removeValue();
                                    ref.child(uid).child("orders").child(uuid.get(position)).child(order.get(position)).removeValue();
                                    ref.child(uuid.get(position)).child("delivered").child(order.get(position)).child(uid).child(itemuuid.get(position)).setValue(item);

                                    fragmentTransaction.detach(currentFragment);
                                    fragmentTransaction.attach(currentFragment);
                                    fragmentTransaction.commit();

                                }else{
                                    if(!snapshot.child(uid).exists()){
                                        ref.child(uid).child("orders").child(uuid.get(position)).child(order.get(position)).removeValue();
                                        notifyDataSetChanged();
                                    }
                                    ref.child(uuid.get(position)).child("order").child(order.get(position)).child("subtotal").setValue(String.valueOf(newsubtotal));
                                    ref.child(uuid.get(position)).child("order").child(order.get(position)).child(uid).child(itemuuid.get(position)).removeValue();
                                    ref.child(uuid.get(position)).child("delivered").child(order.get(position)).child(uid).child(itemuuid.get(position)).setValue(item);

                                    fragmentTransaction.detach(currentFragment);
                                    fragmentTransaction.attach(currentFragment);
                                    fragmentTransaction.commit();


                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        break;
                    default:
                        break;
                }
            }
        });

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.dropdown.getVisibility() == View.VISIBLE) {
                    TransitionManager.beginDelayedTransition(holder.parent, new AutoTransition());
                    holder.dropdown.setVisibility(View.GONE);
                }
                else {
                    TransitionManager.beginDelayedTransition(holder.parent, new AutoTransition());
                    holder.dropdown.setVisibility(View.VISIBLE);
                }
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

    public void setItem(ArrayList<ShipItem> item, ArrayList<String> itemuuid, ArrayList<usercred> buyer, ArrayList<String> uuid, ArrayList<String> order, Fragment currentFragment, FragmentTransaction fragmentTransaction) {
        this.shipA = item;
        this.itemuuid=itemuuid;
        this.buyer=buyer;
        this.uuid=uuid;
        this.order=order;
        this.currentFragment=currentFragment;
        this.fragmentTransaction=fragmentTransaction;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private ImageView picture;
        private TextView quantity;
        private MaterialCardView parent;
        private TextView price;
        private TextView status;
        private MaterialCardView dropdown;
        private RadioGroup rg;
        private TextView adress;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.nameoforderitem);
            quantity=itemView.findViewById(R.id.quantityorderitem);
            picture=itemView.findViewById(R.id.imageorderitem);
            price=itemView.findViewById(R.id.priceorderitem);
            parent=itemView.findViewById(R.id.orderholder);
            dropdown=itemView.findViewById(R.id.drop);
            rg=itemView.findViewById(R.id.orderradiogroup);
            adress=itemView.findViewById(R.id.adressholder);
            status=itemView.findViewById(R.id.statusorderitem);
        }
    }
}
