package com.example.testapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class ShipAdapter extends RecyclerView.Adapter<ShipAdapter.ViewHolder>{

    private ArrayList<ArrayList<ShipItem>> itemA=new ArrayList<>();
    private ArrayList<ArrayList<String>> itemuuid=new ArrayList<>();
    private ArrayList<String> pricelist=new ArrayList<>();
    private Context context;
    private Activity activity;

    public ShipAdapter(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.shiplist, parent, false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.order.setText("Order "+(position+1));
        holder.subtotal.setText(pricelist.get(position));

        LinearLayoutManager layoutManager =new LinearLayoutManager(activity);
        holder.rc.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        ShipInnerAdapter test=new ShipInnerAdapter(context);
        test.setItem(itemA.get(position),itemuuid.get(position));
        holder.rc.setAdapter(test);
    }

    @Override
    public int getItemCount() {
        return itemA.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setItem(ArrayList<ArrayList<ShipItem>> item,ArrayList<ArrayList<String>> itemuuid,ArrayList<String> pricelist) {
        this.itemA = item;
        this.itemuuid=itemuuid;
        this.pricelist=pricelist;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView order;
        private RecyclerView rc;
        private TextView subtotal;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            order=itemView.findViewById(R.id.ordernum);
            rc=itemView.findViewById(R.id.innerrecycler);
            subtotal=itemView.findViewById(R.id.totalpriceperorder);
        }

    }
}
