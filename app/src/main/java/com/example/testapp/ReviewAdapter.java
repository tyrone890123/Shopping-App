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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>{
    private ArrayList<usercred> users=new ArrayList<>();
    private ArrayList<items> item=new ArrayList<>();
    private ArrayList<String> uids=new ArrayList<>();
    private ArrayList<String> messages=new ArrayList<>();
    private ArrayList<String> useruid=new ArrayList<>();
    private Context context;

    public ReviewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews, parent, false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(users.get(position).getName());
        holder.message.setText("Rated "+messages.get(position)+"★ to "+item.get(position).getName());

        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference storageReference=storage.getReference();

        if(!users.get(position).getPic().equals("none")){
            storageReference.child("images/"+useruid.get(position)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context)
                            .load(uri.toString().trim())
                            .into(holder.picturebuyer);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    System.out.print(exception);
                }
            });
        }

        if(item.get(position).getUrl().equals("available")){
            storageReference.child("images2/"+uids.get(position)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context)
                            .load(uri.toString().trim())
                            .into(holder.pictureitem);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setItem(ArrayList<usercred> users, ArrayList<items> item, ArrayList<String> uids,ArrayList<String> messages,ArrayList<String> useruid) {
        this.users = users;
        this.uids=uids;
        this.item=item;
        this.messages=messages;
        this.useruid=useruid;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private ImageView pictureitem;
        private ImageView picturebuyer;
        private TextView message;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.nameofrater);
            message=itemView.findViewById(R.id.messageofrater);
            picturebuyer=itemView.findViewById(R.id.imageofrater);
            pictureitem=itemView.findViewById(R.id.imageofitemrated);
        }
    }
}
