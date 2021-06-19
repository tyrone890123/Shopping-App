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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class storeAdapter extends RecyclerView.Adapter<storeAdapter.ViewHolder>{


    private ArrayList<stores> storeA=new ArrayList<>();
    private ArrayList<String> storeuuid=new ArrayList<>();
    private Context context;
    public storeAdapter(Context context) {
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.storelist, parent, false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.store.setText(storeA.get(position).getName());
        holder.adress.setText(storeA.get(position).getAdress());
        holder.rating.setText(storeA.get(position).getRating());

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, InsideStore.class);
                intent.putExtra("storeuuid",storeuuid.get(position));
                ((Activity)context).startActivity(intent);
            }
        });

        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference storageReference=storage.getReference();

        if(storeA.get(position).getPicurl().equals("available")){
            storageReference.child("images/"+storeuuid.get(position)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context)
                            .load(uri.toString().trim())
                            .into(holder.image);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return storeA.size();
    }

    public void setStore(ArrayList<stores> store,ArrayList<String> storeUUID) {
        this.storeA = store;
        this.storeuuid=storeUUID;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView store;
        private MaterialCardView parent;
        private ImageView image;
        private TextView rating;
        private TextView adress;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            store=itemView.findViewById(R.id.namestorepic);
            parent=itemView.findViewById(R.id.singlestore);
            image=itemView.findViewById(R.id.imagestore);
            rating=itemView.findViewById(R.id.rating);
            adress=itemView.findViewById(R.id.adressimage);
        }
    }
}
