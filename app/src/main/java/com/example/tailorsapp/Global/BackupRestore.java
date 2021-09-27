package com.example.tailorsapp.Global;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.tailorsapp.ClientsAllDataModel;
import com.example.tailorsapp.PersonModel.OrderDataBackupModel;
import com.example.tailorsapp.RoomDataBase.Client;
import com.example.tailorsapp.RoomDataBase.ClientViewModel;
import com.example.tailorsapp.RoomDataBase.Order;
import com.example.tailorsapp.RoomDataBase.OrderViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BackupRestore {
    Context context;
    ClientViewModel clientViewModel;
    OrderViewModel orderViewModel;
    List<Client> client_list = new ArrayList<>();
    List<Order> order_list = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    ClientsAllDataModel model;
    ArrayList<String> local_clients_id;
    ArrayList<String> local_orders_id;


    public BackupRestore(Context context) {
        this.context = context;
    }

    public boolean pushData() {
        final Boolean[] isSuccessful = new Boolean[1];
        firebaseDatabase = FirebaseDatabase.getInstance();
        clientViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(ClientViewModel.class);

        clientViewModel.getAllClients().observe((LifecycleOwner) context, clients -> {
            if (clients != null) {
                client_list.addAll(clients);
            }
        });
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Backup Data");
        progressDialog.show();
        if (client_list.size() > 0) {
            for (Client client : client_list) {
                final String phone = client.getPhoneNumber();
                final String leg = client.getLeg();
                final String arm = client.getArm();
                final String chest = client.getChest();
                final String neck = client.getNeck();
                final String front = client.getFrontSide();
                final String back = client.getBackSide();
                final String date = client.getDate();
                final String id = Integer.toString(client.getId());
                final String fatherName = client.getFatherName();
                final String name = client.getName();
                model = new ClientsAllDataModel(id, name, fatherName, phone, leg, arm, chest, neck, front, back, date);
                firebaseDatabase.getReference("Data").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("Clients").child(id).setValue(model)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                isSuccessful[0] = true;
                            }
                        });
            }
        } else {
            Toast.makeText(context, "No Clients to Backup", Toast.LENGTH_SHORT).show();
        }
        progressDialog.dismiss();
     return isSuccessful[0] && BackupOrder();
    }

    private boolean BackupOrder() {
        final Boolean[] isSuccessful = new Boolean[1];
        orderViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(OrderViewModel.class);
        orderViewModel.getAllOrders().observe((LifecycleOwner) context, orders -> {
            if (orders != null) {
                order_list.addAll(orders);
            }
        });
        if (order_list.size() > 0) {
            for (Order order : order_list) {
                final String orderID = Integer.toString(order.getORDER_ID());
                final String clientID = order.getCLIENT_ID();
                final String name = order.getNAME();
                final String price = order.getPRICE();
                final String type = order.getTYPE();
                final String dateOrdered = order.getDATE_ORDERED();
                final String dateDelivered = order.getDATE_RECEIVED();
                final String status = order.getSTATUS();
                final String furtherDetails = order.getFURTHER_DETAILS();
                OrderDataBackupModel model = new OrderDataBackupModel(orderID, clientID, name, type, price, dateOrdered, dateDelivered, status, furtherDetails);
                firebaseDatabase.getReference("Data").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("Orders").child(orderID).setValue(model)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                isSuccessful[0] =true;
                            }
                        });
            }
            Toast.makeText(context, "Orders Backup Done", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "No Orders to Backup", Toast.LENGTH_SHORT).show();
        }
    return isSuccessful[0];
    }

    private void RestoreData(Context ctx) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        firebaseDatabase = FirebaseDatabase.getInstance();
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Restoring Data");
        progressDialog.show();
        firebaseDatabase.getReference("Data").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Clients").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot snap : snapshot.getChildren()) {

                    String phone = snap.child("phoneNumber").getValue().toString();
                    String leg = snap.child("leg").getValue().toString();
                    String arm = snap.child("arm").getValue().toString();
                    String chest = snap.child("chest").getValue().toString();
                    String neck = snap.child("neck").getValue().toString();
                    String front = snap.child("frontSide").getValue().toString();
                    String back = snap.child("backSide").getValue().toString();
                    String date = snap.child("date").getValue().toString();
                    String id = snap.child("id").getValue().toString();
                    String fatherName = snap.child("fatherName").getValue().toString();
                    String name = snap.child("name").getValue().toString();
                    Client client = new Client(name, fatherName, phone, leg, arm, chest, neck, front, back, date);

                    client.id = Integer.parseInt(id);

                    if (!local_clients_id.contains(id)) {
                        Log.e("Client id is : ", id + " " + local_orders_id.toString());
                        clientViewModel.insertClients(client);
                    }

                }
                restoreOrders();
                progressDialog.dismiss();
                Toast.makeText(ctx, "Data Restored", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ctx, "Data Restore Cancelled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void restoreOrders() {
        firebaseDatabase.getReference("Data").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    final String orderID = snap.child("orderID").getValue().toString();
                    final String clientID = snap.child("clientID").getValue().toString();
                    final String name = snap.child("name").getValue().toString();
                    final String price = snap.child("price").getValue().toString();
                    final String type = snap.child("type").getValue().toString();
                    final String dateOrdered = snap.child("dateOrdered").getValue().toString();
                    final String dateDelivered = snap.child("dateReceived").getValue().toString();
                    final String status = snap.child("status").getValue().toString();
                    final String furtherDetails = snap.child("furtherDetails").getValue().toString();

                    Order order = new Order(clientID, name, price, type, dateOrdered, dateDelivered, status, furtherDetails);
                    order.ORDER_ID = Integer.parseInt(orderID);

                    if (!local_orders_id.contains(orderID)) {
                        orderViewModel.insertOrder(order);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
