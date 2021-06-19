package com.example.testapp;


import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class categoriesAdapter extends RecyclerView.Adapter<categoriesAdapter.ViewHolder> {

    private ArrayList<categories> categoryA=new ArrayList<>();
    private View rootView;

    public categoriesAdapter(View rootview) {
        this.rootView=rootview;

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

        String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference storageReference=storage.getReference();

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(rootView.getContext(),position==categoryA.size()-1?categoryCreator.class:CategoryItemList.class);
                intent.putExtra("category",categoryA.get(position).name);
                rootView.getContext().startActivity(intent);
                ((Activity)rootView.getContext()).finish();
            }
        });

        if(categoryA.get(position).getUrl().equals("available")){
            if(position==categoryA.size()-1){
                storageReference.child("imageholders/add.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(rootView.getContext())
                                .load(uri.toString().trim())
                                .into(holder.picture);
                    }
                });
            }else{
                storageReference.child("image3/"+uid+"/"+categoryA.get(position).name).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(rootView.getContext())
                                .load(uri.toString().trim())
                                .into(holder.picture);
                    }
                });
//                Log.d("loc","image3/"+uid+"/"+categoryA.get(position).name+".png");
            }
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

    public void setCategory(ArrayList<categories> categoryA) {
        this.categoryA = categoryA;
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
