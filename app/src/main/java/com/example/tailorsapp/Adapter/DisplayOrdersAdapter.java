package com.example.tailorsapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tailorsapp.ClientDetails;
import com.example.tailorsapp.PersonModel.OrderDisplayModel;
import com.example.tailorsapp.R;
import com.example.tailorsapp.RoomDataBase.OrderViewModel;


import java.text.SimpleDateFormat;

import java.util.Date;

import java.util.List;


public class DisplayOrdersAdapter extends RecyclerView.Adapter<DisplayOrdersAdapter.Holder> {
    private final Context context;
    private final List<OrderDisplayModel> list;
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
        Log.i("BindViewHolder","Entered");
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


        holder.cardView.setOnClickListener(v -> {
            Log.i("ONCLICK","work");
            com.google.android.material.bottomsheet.BottomSheetDialog bottomSheetDialog = new com.google.android.material.bottomsheet.BottomSheetDialog(context,R.style.SheetDialog);
            View view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_layout,null);
            TextView completedBS = view.findViewById(R.id.CompletedBS);
            TextView furtherDetailsBS = view.findViewById(R.id.FurtherDetailsBS);
            TextView deleteBS = view.findViewById(R.id.DeleteBS);
            TextView clientDetailsBS = view.findViewById(R.id.clientDetailsBS);
            ImageView statusImg = view.findViewById(R.id.statusImgBS);
            bottomSheetDialog.setContentView(view);
            bottomSheetDialog.show();
            if(model.getStatus().equals("Completed")){
                completedBS.setText("Pending");
                statusImg.setImageResource(R.drawable.ic_baseline_cancel_24);
            }
            clientDetailsBS.setOnClickListener(v14 -> {
                Intent i = new Intent(context, ClientDetails.class);
                i.putExtra("ID",model.getClient_id());
                context.startActivity(i);
                bottomSheetDialog.dismiss();
            });

            completedBS.setOnClickListener(v1 -> {
                bottomSheetDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(false);
                String status = model.getStatus();
                if(status.equals("Pending")){
                    builder.setTitle(model.getName());
                    builder.setMessage("Is this Order Completed");
                    builder.setPositiveButton("Yes", (dialog, which) -> orderCompletedChange(model,status));
                    builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }else{
                    builder.setTitle(model.getName());
                    builder.setMessage("Is this order still pending");
                    builder.setPositiveButton("Yes", (dialog, which) -> {
                        orderCompletedChange(model,status);
                        dialog.dismiss();
                    });
                    builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }
            });
            furtherDetailsBS.setOnClickListener(v12 -> {
                showFurtherDetails(position,model);
                bottomSheetDialog.dismiss();
            });
            deleteBS.setOnClickListener(v13 -> {
                deleteOrder(position,model);
                bottomSheetDialog.dismiss();
            });


        });

    }

    private void deleteOrder(int position, OrderDisplayModel model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle("Delete Order");
        builder.setIcon(R.drawable.ic_baseline_warning_24);
        builder.setMessage("Client Name "+model.getName()+"\n\nAre you sure to delete the order?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            OrderViewModel orderViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(OrderViewModel.class);
            orderViewModel.deleteOrderByID(Integer.parseInt(model.getIndexNo()));
        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showFurtherDetails(int position, OrderDisplayModel model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(model.getName());
        if(model.getFurtherDetails().equals("")){
            builder.setMessage("No Further Details");
        }else {
            builder.setMessage(model.getFurtherDetails());
        }
        builder.setNeutralButton("OK", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void orderCompletedChange(OrderDisplayModel model,String status) {
        OrderViewModel orderViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(OrderViewModel.class);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = sdf.format(new Date());
        if(status.equals("Pending")) {

            orderViewModel.updateOrderData(Integer.parseInt(model.getIndexNo()),model.getClient_id(),model.getName(),model.getPrice(),model.getType(),model.getDateOrdered(),currentDate,"Completed",model.getFurtherDetails());
                }
        else{
            orderViewModel.updateOrderData(Integer.parseInt(model.getIndexNo()),model.getClient_id(),model.getName(),model.getPrice(),model.getType(),model.getDateOrdered(),currentDate,"Pending",model.getFurtherDetails());
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



}
