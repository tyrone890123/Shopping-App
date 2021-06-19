package com.example.testapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.ViewHolder>{
    private ArrayList<items> itemA=new ArrayList<>();
    private ArrayList<String> itemuuid=new ArrayList<>();
    private ArrayList<String> quantity=new ArrayList<>();
    private Context context;
    public CheckoutAdapter(Context context) {
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.checkoutlist, parent, false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(itemA.get(position).getName());
        holder.quantity.setText(quantity.get(position));
        holder.price.setText(itemA.get(position).getPrice());
        holder.seller.setText(itemA.get(position).getBrand());

        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference storageReference=storage.getReference();

        if(itemA.get(position).getUrl().equals("available")){
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
        return itemA.size();
    }

    public void setCheckout(ArrayList<items> item,ArrayList<String> itemuuid,ArrayList<String> quantity) {
        this.itemA = item;
        this.itemuuid=itemuuid;
        this.quantity=quantity;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private ImageView picture;
        private TextView quantity;
        private TextView seller;
        private TextView price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.nameofcheckoutitem);
            quantity=itemView.findViewById(R.id.quantity);
            seller=itemView.findViewById(R.id.sellerofcheckoutitem);
            picture=itemView.findViewById(R.id.imagecheckoutitem);
            price=itemView.findViewById(R.id.pricecheckoutitem);
        }
    }
}
