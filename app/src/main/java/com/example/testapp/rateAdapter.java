package com.example.testapp;

import android.content.Context;
import android.net.Uri;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Map;

public class rateAdapter extends RecyclerView.Adapter<rateAdapter.ViewHolder> {
    private FirebaseUser user;
    private DatabaseReference ref,ref2;
    private String uid;
    private FirebaseStorage storage;

    private ArrayList<ShipItem> shipA=new ArrayList<>();
    private ArrayList<String> itemuuid=new ArrayList<>();
    private Fragment currentFragment;
    private FragmentTransaction fragmentTransaction;
    private Context context;

    public rateAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.ratelist, parent, false);
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
        holder.price.setText(shipA.get(position).getPrice());
        holder.seller.setText(shipA.get(position).getBrand());
        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference storageReference=storage.getReference();

        holder.rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.drop.getVisibility() == View.VISIBLE) {
                    TransitionManager.beginDelayedTransition(holder.parent, new AutoTransition());
                    holder.drop.setVisibility(View.GONE);
                }
                else {
                    TransitionManager.beginDelayedTransition(holder.parent, new AutoTransition());
                    holder.drop.setVisibility(View.VISIBLE);
                }
            }
        });

        holder.rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                ref2.child(itemuuid.get(position)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child("total").exists()){
                            Map<String,String> map=(Map)snapshot.getValue();
                            double total=Double.valueOf(map.get("total"))+rating;
                            int inputs=Integer.valueOf(map.get("inputs"))+1;
                            ref2.child(itemuuid.get(position)).child("total").setValue(String.valueOf(total));
                            ref2.child(itemuuid.get(position)).child("rating").setValue(String.valueOf(round(total/inputs,2)));
                            ref2.child(itemuuid.get(position)).child("inputs").setValue(String.valueOf(inputs));

//                            Log.d("val",);
                        }else{
                            Map<String,String> map=(Map)snapshot.getValue();
                            ref2.child(itemuuid.get(position)).child("total").setValue(String.valueOf(rating));
                            ref2.child(itemuuid.get(position)).child("rating").setValue(String.valueOf(rating));
                            ref2.child(itemuuid.get(position)).child("inputs").setValue("1");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                ref.child(shipA.get(position).getSellerUUID()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Map<String,String> map=(Map)snapshot.getValue();
                        double total=Double.valueOf(map.get("total"))+rating;
                        int inputs=Integer.valueOf(map.get("inputs"))+1;

                        ref.child(shipA.get(position).getSellerUUID()).child("total").setValue(String.valueOf(total));
                        ref.child(shipA.get(position).getSellerUUID()).child("rating").setValue(String.valueOf(round(total/inputs,2)));
                        ref.child(shipA.get(position).getSellerUUID()).child("inputs").setValue(String.valueOf(inputs));
                        ref.child(shipA.get(position).getSellerUUID()).child("ratingnotif").child(itemuuid.get(position)).child(uid).setValue(String.valueOf(rating));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                ref.child(uid).child("torate").child(itemuuid.get(position)).removeValue();
                fragmentTransaction.detach(currentFragment);
                fragmentTransaction.attach(currentFragment);
                fragmentTransaction.commit();
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

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public int getItemCount() {
        return shipA.size();
    }

    public void setItem(ArrayList<ShipItem> item,ArrayList<String> itemuuid, Fragment currentFragment, FragmentTransaction fragmentTransaction) {
        this.shipA = item;
        this.itemuuid=itemuuid;
        this.currentFragment=currentFragment;
        this.fragmentTransaction=fragmentTransaction;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private ImageView picture;
        private TextView seller;
        private TextView price;
        private ImageButton rate;
        private MaterialCardView parent;
        private MaterialCardView drop;
        private RatingBar rating;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.nameofrateitem);
            seller=itemView.findViewById(R.id.sellerofrateitem);
            picture=itemView.findViewById(R.id.imagerateitem);
            price=itemView.findViewById(R.id.pricerateitem);
            rate=itemView.findViewById(R.id.rate);
            drop=itemView.findViewById(R.id.droprating);
            rating=itemView.findViewById(R.id.rating);
            parent=itemView.findViewById(R.id.ratingholder);
        }
    }
}
