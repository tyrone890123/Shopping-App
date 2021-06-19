package com.example.testapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class NotifAdapter extends RecyclerView.Adapter<NotifAdapter.ViewHolder> {
    private ArrayList<String> names=new ArrayList<>();
    private ArrayList<String> uuids=new ArrayList<>();
    private ArrayList<String> messages=new ArrayList<>();
    private ArrayList<String> pic=new ArrayList<>();
    private Context context;

    public NotifAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.notif, parent, false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(names.get(position));
        holder.message.setText(messages.get(position));

        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference storageReference=storage.getReference();

        if(!pic.get(position).equals("none")){
            storageReference.child("images/"+uuids.get(position)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context)
                            .load(uri.toString().trim())
                            .into(holder.picture);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    System.out.print(exception);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public void setItem(ArrayList<String> names, ArrayList<String> uuids, ArrayList<String> messages,ArrayList<String> pic) {
        this.names = names;
        this.uuids=uuids;
        this.messages=messages;
        this.pic=pic;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private ImageView picture;
        private TextView message;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.nameofbuyer);
            message=itemView.findViewById(R.id.messageofbuyer);
            picture=itemView.findViewById(R.id.imageofbuyer);
        }
    }
}
