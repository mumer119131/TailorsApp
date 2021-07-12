package com.example.tailorsapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tailorsapp.ClientModel;
import com.example.tailorsapp.OrderConfirmationDetails;
import com.example.tailorsapp.R;

import java.util.ArrayList;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.Holder> {
    private Context context;
    private ArrayList<ClientModel> list;

    public OrdersAdapter(Context context, ArrayList<ClientModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_view_row,parent,false);

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
                Intent intent = new Intent(context, OrderConfirmationDetails.class);
                intent.putExtra("ID",list.get(position).getID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class Holder extends RecyclerView.ViewHolder {
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
}
