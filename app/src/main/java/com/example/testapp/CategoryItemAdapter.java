package com.example.testapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CategoryItemAdapter extends RecyclerView.Adapter<CategoryItemAdapter.ViewHolder>{


    private ArrayList<items> itemA=new ArrayList<>();
    private ArrayList<String> itemuuid=new ArrayList<>();
    private Activity act;
    private String category;

    public CategoryItemAdapter(Activity act,String category){
        this.act=act;
        this.category=category;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.categoryitemlist, parent, false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(itemA.get(position).getName());
        holder.category.setText(itemA.get(position).getCategory());
        holder.rating.setText(itemA.get(position).getRating());
        holder.price.setText(itemA.get(position).getPrice());


        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference storageReference=storage.getReference();

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position==itemA.size()-1){
                    Intent intent=new Intent(act.getBaseContext(),CreateItem.class);
                    intent.putExtra("category",category);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    act.startActivity(intent);
                    act.finish();
                    return;
                }else{
                    Intent intent=new Intent(act.getBaseContext(),ItemEdit.class);
                    intent.putExtra("itemuuid",itemuuid.get(position));
                    intent.putExtra("category",category);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    act.startActivity(intent);
                    act.finish();
                }
            }
        });


        if(itemA.get(position).getUrl().equals("available")){
            if(position==itemA.size()-1){
                storageReference.child("imageholders/add.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(act.getBaseContext())
                                .load(uri.toString().trim())
                                .into(holder.picture);
                    }
                });
            }else{
                storageReference.child("images2/"+itemuuid.get(position)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(act.getBaseContext())
                                .load(uri.toString().trim())
                                .into(holder.picture);
                    }
                });
            }
        }
    }
    public void setCategoryItem(ArrayList<items> item,ArrayList<String> itemuuid) {
        this.itemA = item;
        this.itemuuid=itemuuid;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return itemA.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private ImageView picture;
        private MaterialCardView parent;
        private TextView rating;
        private TextView category;
        private TextView price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.nameofcategoryitem);
            parent=itemView.findViewById(R.id.categoryitemholder);
            rating=itemView.findViewById(R.id.ratingofcategoryitem);
            category=itemView.findViewById(R.id.categoryofcategoryitem);
            picture=itemView.findViewById(R.id.imagecategoryitem);
            price=itemView.findViewById(R.id.pricecategoryitem);
        }
    }

}
