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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ShipInnerAdapter extends RecyclerView.Adapter<ShipInnerAdapter.ViewHolder> {
    private ArrayList<ShipItem> shipA=new ArrayList<>();
    private ArrayList<String> itemuuid=new ArrayList<>();
    private Context context;

    public ShipInnerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_ship_item, parent, false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(shipA.get(position).getName());
        holder.quantity.setText(shipA.get(position).getQuantity());
        holder.price.setText(shipA.get(position).getPrice());
        holder.seller.setText(shipA.get(position).getBrand());
        holder.status.setText(shipA.get(position).getStatus());

        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference storageReference=storage.getReference();

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

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setItem(ArrayList<ShipItem> item,ArrayList<String> itemuuid) {
        this.shipA = item;
        this.itemuuid=itemuuid;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private ImageView picture;
        private TextView quantity;
        private TextView seller;
        private TextView price;
        private TextView status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.nameofshipitem);
            quantity=itemView.findViewById(R.id.quantityshipitem);
            seller=itemView.findViewById(R.id.sellerofshipitem);
            picture=itemView.findViewById(R.id.imageshipitem);
            price=itemView.findViewById(R.id.priceshipitem);
            status=itemView.findViewById(R.id.statusshipitem);
        }
    }
}
