package com.example.testapp;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class cartAdapter  extends RecyclerView.Adapter<cartAdapter.ViewHolder>{

    private FirebaseUser user;
    private DatabaseReference ref;
    private String uid;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private TextView totalprice;
    private Button button;
    private ArrayList<carts> cartA=new ArrayList<>();
    private ArrayList<String> itemuuid=new ArrayList<>();
    private Context context;
    private View rootView;

    public cartAdapter(View rootView) {
        this.rootView=rootView;
        this.context=rootView.getContext();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cartlist, parent, false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        user= FirebaseAuth.getInstance().getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReference("users");
        uid=user.getUid();
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        totalprice=rootView.findViewById(R.id.totalprice);
        button=rootView.findViewById(R.id.checkoutbutton);

        holder.name.setText(Integer.parseInt(cartA.get(position).getQuantity())>Integer.parseInt(cartA.get(position).getStock())?"OVER THE STOCK":cartA.get(position).getName());
        holder.category.setText(cartA.get(position).getCategory());
        holder.quantity.setText(cartA.get(position).getQuantity());
        holder.price.setText(cartA.get(position).getPrice());

        //ANOTHER WAY OF ADDING CLICK LISTENER
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,itemorder.class);
                intent.putExtra("item_uuid",itemuuid.get(position));
                context.startActivity(intent);
            }
        });

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentquantity=Integer.parseInt(cartA.get(position).getQuantity());
                double initialamount=Double.parseDouble(cartA.get(position).getPrice());
                double currentamount=Double.parseDouble(totalprice.getText().toString());


                ref.child(uid).child("cart").child("subtotal").setValue(String.valueOf(currentamount+initialamount));
                ref.child(uid).child("cart").child("items").child(itemuuid.get(position)).child("quantity").setValue(String.valueOf(currentquantity+1));

                holder.quantity.setText(String.valueOf(currentquantity+1));
                totalprice.setText(String.valueOf(currentamount+initialamount));
                cartA.get(position).setQuantity(String.valueOf(currentquantity+1));

                if(Integer.parseInt(cartA.get(position).getQuantity())>Integer.parseInt(cartA.get(position).getStock())){
                    holder.name.setText("OVER THE STOCK");
                    button.setEnabled(false);
                }
            }
        });

        holder.sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentquantity=Integer.parseInt(cartA.get(position).getQuantity());
                double initialamount=Double.parseDouble(cartA.get(position).getPrice());
                double currentamount=Double.parseDouble(totalprice.getText().toString());

                int checker=0;

                ref.child(uid).child("cart").child("subtotal").setValue(currentquantity<2?String.valueOf(currentamount):String.valueOf(currentamount-initialamount));
                ref.child(uid).child("cart").child("items").child(itemuuid.get(position)).child("quantity").setValue(currentquantity<3?"1":String.valueOf(currentquantity-1));

                holder.quantity.setText(currentquantity<3?"1":String.valueOf(currentquantity-1));
                totalprice.setText(currentquantity<2?String.valueOf(currentamount):String.valueOf(currentamount-initialamount));
                cartA.get(position).setQuantity(currentquantity<3?"1":String.valueOf(currentquantity-1));

                for(int q=0;q<cartA.size();q++){
                    if(Integer.parseInt(cartA.get(q).getQuantity())>Integer.parseInt(cartA.get(q).getStock())){
                        checker++;
                    }
                }

                if(checker==0){
                    holder.name.setText(cartA.get(position).getName());
                    button.setEnabled(true);
                }
            }
        });

        storageReference.child("images2/"+itemuuid.get(position)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri.toString().trim())
                        .into(holder.picture);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartA.size();
    }

    public void setCart(ArrayList<carts> cart,ArrayList<String> itemuuid) {
        this.cartA = cart;
        this.itemuuid=itemuuid;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private MaterialCardView parent;
        private TextView quantity;
        private TextView category;
        private ImageView picture;
        private ImageButton add;
        private ImageButton sub;
        private TextView price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.nameofcartitem);
            parent=itemView.findViewById(R.id.cartholder);
            quantity=itemView.findViewById(R.id.quantityofcartitem);
            category=itemView.findViewById(R.id.categoryofcartitem);
            picture=itemView.findViewById(R.id.imagecartitem);
            price=itemView.findViewById(R.id.cartitemprice);
            add=itemView.findViewById(R.id.cartadd);
            sub=itemView.findViewById(R.id.cartsub);
        }
    }
}
