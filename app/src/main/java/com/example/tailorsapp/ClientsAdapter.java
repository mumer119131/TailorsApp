package com.example.tailorsapp;

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



import java.util.ArrayList;


public class ClientsAdapter extends RecyclerView.Adapter<ClientsAdapter.Holder> {
    private Context ctx;
    private ArrayList<ClientModel> list;
    public ClientsAdapter(Context ctx, ArrayList<ClientModel> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.list_view_row,parent,false);

        return new Holder(v);
    }



    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        ClientModel model = list.get(position);
        String id=model.getID()+".";
        holder.userID.setText(id);
        holder.userName.setText(model.getUserName());
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

    public class Holder extends RecyclerView.ViewHolder{
         TextView userID;
         TextView userName;
         LinearLayout linearLayout;
        public Holder(@NonNull View itemView) {
            super(itemView);
            userID=itemView.findViewById(R.id.userID);
            userName=itemView.findViewById(R.id.tvUserName);
            linearLayout=itemView.findViewById(R.id.linearLayout);

        }
    }

    public interface  OnNoteListener{
        void onNoteClick(int position);
    }

}
