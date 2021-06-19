package com.example.testapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{
    private ArrayList<ArrayList<ShipItem>> itemA=new ArrayList<>();
    private ArrayList<ArrayList<String>> itemuuid=new ArrayList<>();
    private ArrayList<ArrayList<usercred>> users=new ArrayList<>();
    private ArrayList<ArrayList<String>> uuid=new ArrayList<>();
    private ArrayList<ArrayList<String>> order=new ArrayList<>();
    private Fragment currentFragment;
    private FragmentTransaction fragmentTransaction;
    private ArrayList<String> names=new ArrayList<>();
    private Context context;
    private Activity activity;

    public OrderAdapter(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.orderlist, parent, false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String val="Order of "+names.get(position);
        holder.order.setText(val);

        LinearLayoutManager layoutManager =new LinearLayoutManager(activity);
        holder.rc.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        orderInnerAdapter test=new orderInnerAdapter(context);
        test.setItem(itemA.get(position),itemuuid.get(position), users.get(position),uuid.get(position),order.get(position),currentFragment,fragmentTransaction);
        holder.rc.setAdapter(test);
    }

    @Override
    public int getItemCount() {
        return itemA.size();
    }

    public void setItem(ArrayList<ArrayList<ShipItem>> item,ArrayList<ArrayList<String>> itemuuid,ArrayList<ArrayList<usercred>> users,ArrayList<ArrayList<String>> uuid,ArrayList<ArrayList<String>> order,ArrayList<String> names,Fragment currentFragment,FragmentTransaction fragmentTransaction) {
        this.itemA = item;
        this.itemuuid=itemuuid;
        this.users=users;
        this.uuid=uuid;
        this.order=order;
        this.names=names;
        this.currentFragment=currentFragment;
        this.fragmentTransaction=fragmentTransaction;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView order;
        private RecyclerView rc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            order=itemView.findViewById(R.id.ordernumseller);
            rc=itemView.findViewById(R.id.innerrecyclerseller);
        }

    }
}
