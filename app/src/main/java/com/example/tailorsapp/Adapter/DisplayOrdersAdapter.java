package com.example.tailorsapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.text.TextUtils;
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

import com.example.tailorsapp.BottomSheetDialog;
import com.example.tailorsapp.Database.OrderDataBaseHelper;
import com.example.tailorsapp.FragmentCompletedOrders;
import com.example.tailorsapp.PersonModel.OrderDisplayModel;
import com.example.tailorsapp.R;


import org.w3c.dom.Text;

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
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        OrderDisplayModel model = list.get(position);
        String price = "Price : "+model.getPrice();
        String type = "Type : "+model.getType();
        String dateOrdered = "Date Ordered "+model.getDateOrdered();
        String dateToReceive = "Delivery Date : "+model.getDateReceived();
        orderID = model.getIndexNo();
        holder.indexNoOrders.setText(model.getIndexNo());
        holder.clientName.setText(model.getName());
        holder.price.setText(price);
        if(TextUtils.isEmpty(type)){
            holder.type.setText("Not Set");
        }else {
            holder.type.setText(type);
        }
        holder.dateOrdered.setText(dateOrdered);
        holder.dateDelivery.setText(dateToReceive);


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(false);
                String status = model.getStatus();
                if(status.equals("Pending")){
                    builder.setTitle(model.getName());
                    String furtherDetails = model.getFurtherDetails();
                    if(furtherDetails.equals("")){
                        furtherDetails = "(No Further Details)";
                    }
                    builder.setMessage("("+furtherDetails+")\n\nIs this Order Completed");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            orderCompletedChange(model.getIndexNo());
                            list.remove(position);
                            notifyItemRemoved(position);


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
                if(status.equals("Completed")){
                    showDialog(model.getName(),model.getFurtherDetails());
                }
            }
        });

    }

    private void showDialog(String name, String furtherDetails) {
        if(furtherDetails.equals("")){
            furtherDetails = "No Further Details";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(name);
        builder.setMessage(furtherDetails);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

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
