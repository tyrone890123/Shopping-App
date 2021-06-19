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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class categorystoreAdapter extends RecyclerView.Adapter<categorystoreAdapter.ViewHolder>{
    private Context context;
    private ArrayList<categories> categoryA=new ArrayList<>();
    private String storeuuid;

    public categorystoreAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.categorylist, parent, false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(categoryA.get(position).name);
        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference storageReference=storage.getReference();

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,InsideCategory.class);
                intent.putExtra("storeuuid",storeuuid);
                intent.putExtra("category",categoryA.get(position).name);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        if(categoryA.get(position).getUrl().equals("available")){
                storageReference.child("image3/"+storeuuid+"/"+categoryA.get(position).name).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return categoryA.size();
    }

    public void setAdapter(ArrayList<categories> categoryA,String storeuuid){
        this.categoryA=categoryA;
        this.storeuuid=storeuuid;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private ImageView picture;
        private MaterialCardView parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.nameCategory);
            picture=itemView.findViewById(R.id.imageCategory);
            parent=itemView.findViewById(R.id.singleCategory);
        }
    }
}
