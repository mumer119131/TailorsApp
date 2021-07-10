package com.example.tailorsapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tailorsapp.Database.OrderDataBaseHelper;
import com.example.tailorsapp.PersonModel.OrderDisplayModel;
import com.example.tailorsapp.R;


import java.text.SimpleDateFormat;

import java.util.Date;

import java.util.List;


public class DisplayOrdersAdapter extends RecyclerView.Adapter<DisplayOrdersAdapter.Holder> {
    private Context context;
    private List<OrderDisplayModel> list;
    String orderID;

    public DisplayOrdersAdapter(Context context, List<OrderDisplayModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.pending_and_completed_order_row,parent,false);
        Log.e("onCreateView Holder","crea 8 teViewHolder Created");
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        OrderDisplayModel model = list.get(position);
        Log.e("onBindView Holder","bindViewHolder Created");
        String price = "Price : "+model.getPrice();
        String type = "Type : "+model.getType();
        String dateOrdered = "Date Ordered "+model.getDateOrdered();
        String dateToReceive = "Delivery Date : "+model.getDateReceived();
        orderID = model.getIndexNo()+".";
        holder.indexNoOrders.setText(model.getIndexNo());
        holder.clientName.setText(model.getName());
        holder.price.setText(price);
        holder.type.setText(type);
        holder.dateOrdered.setText(dateOrdered);
        holder.dateDelivery.setText(dateToReceive);


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(false);
                String status = model.getStatus();
                if(status.equals("Pending")){
                    builder.setTitle(model.getName());
                    builder.setMessage("Is this Order Completed");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            orderCompletedChange(model.getIndexNo());
                            refreshData(list);

                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                        }
                    });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                }
            }
        });

    }

    public void setItems(List<OrderDisplayModel> list) {
        this.list = list;
    }

    private void orderCompletedChange(String order_no) {
        OrderDataBaseHelper helper = new OrderDataBaseHelper(context);
        Cursor cursor = helper.getDataByID(Integer.parseInt(order_no));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = sdf.format(new Date());
        if(cursor.moveToFirst()) {
            helper.EditDataIntoTable(Integer.parseInt(orderID), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), currentDate, "Completed", cursor.getString(8));
            cursor.close();
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class Holder extends RecyclerView.ViewHolder{
        TextView indexNoOrders,clientName,type,price,dateOrdered,dateDelivery;
        CardView cardView;
        public Holder(@NonNull View itemView) {
        super(itemView);
        indexNoOrders = itemView.findViewById(R.id.indexNoOrder);
        clientName = itemView.findViewById(R.id.orderUserName);
        type = itemView.findViewById(R.id.orderType);
        price = itemView.findViewById(R.id.orderPrice);
        dateDelivery = itemView.findViewById(R.id.oderDeliveryDate);
        dateOrdered = itemView.findViewById(R.id.orderOrderedDate);
        cardView = itemView.findViewById(R.id.cardViewOrders);
        }
    }
    public void refreshData(List<OrderDisplayModel> list){
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }



}
