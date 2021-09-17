package com.example.tailorsapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.tailorsapp.PersonModel.OrderDisplayModel;

import java.util.ArrayList;
import java.util.List;


public class ClientsAdapter extends RecyclerView.Adapter<ClientsAdapter.Holder> implements Filterable {
    private Context ctx;
    private ArrayList<ClientModel> list;
    private ArrayList<ClientModel> listFull;
    public ClientsAdapter(Context ctx, ArrayList<ClientModel> list) {
        this.ctx = ctx;
        this.list = list;
        listFull = new ArrayList<>(list);
    }

    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.list_view_row,parent,false);

        return new Holder(v);
    }



    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        ClientModel model = list.get(position);
        String id="ID: "+model.getID();
        holder.userID.setText(id);
        holder.userName.setText(model.getUserName());
        holder.indexNo.setText(Integer.toString(position+1)+".");
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx,ClientDetails.class);
                intent.putExtra("ID",list.get(position).getID());
                ctx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return filterClients;
    }

    private Filter filterClients = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ClientModel> filterList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                filterList.addAll(listFull);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(ClientModel item : listFull){
                    if(item.getUserName().toLowerCase().contains(filterPattern)){
                        filterList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filterList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
              list.clear();
              list.addAll((List)results.values);
              notifyDataSetChanged();
        }
    };

    public class Holder extends RecyclerView.ViewHolder{
         TextView userID;
         TextView userName,indexNo;
         LinearLayout linearLayout;
        public Holder(@NonNull View itemView) {
            super(itemView);
            userID=itemView.findViewById(R.id.userID);
            userName=itemView.findViewById(R.id.tvUserName);
            linearLayout=itemView.findViewById(R.id.linearLayout);
            indexNo = itemView.findViewById(R.id.indexNo);

        }
    }

    public void refreshData(ArrayList<ClientModel> newData){
        this.list.clear();
        list.addAll(newData);
        notifyDataSetChanged();
    }




}
