package com.example.tailorsapp.MenuFragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tailorsapp.ClientsAllDataModel;
import com.example.tailorsapp.PersonModel.OrderDataBackupModel;
import com.example.tailorsapp.R;
import com.example.tailorsapp.RoomDataBase.Client;
import com.example.tailorsapp.RoomDataBase.ClientViewModel;
import com.example.tailorsapp.RoomDataBase.Order;
import com.example.tailorsapp.RoomDataBase.OrderViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

public class BackupFragment extends Fragment {
    ImageButton btnBackup;
    ImageButton btnRestore;
    FirebaseDatabase firebaseDatabase;
    ClientsAllDataModel model;
    ClientViewModel clientViewModel;
    OrderViewModel orderViewModel;
    List<Client> client_list;
    List<Order> order_list;
    ArrayList<String> local_clients_id;
    ArrayList<String> local_orders_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_backup, container, false);
        btnBackup = root.findViewById(R.id.btnBackup);
        btnRestore = root.findViewById(R.id.btnRestore);
        firebaseDatabase = FirebaseDatabase.getInstance();
        clientViewModel = new ViewModelProvider(getActivity()).get(ClientViewModel.class);
        orderViewModel = new ViewModelProvider(getActivity()).get(OrderViewModel.class);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        client_list = new ArrayList<>();
        order_list = new ArrayList<>();
        local_clients_id = new ArrayList<>();
        local_orders_id = new ArrayList<>();

        clientViewModel.getAllClients().observe(getActivity(),clients -> {
            local_clients_id.clear();
            for (Client client:clients){
                local_clients_id.add(client.getId()+"");
            }
        });
        orderViewModel.getAllOrders().observe(getActivity(),orders -> {
            local_orders_id.clear();
            for (Order order:orders){
                local_orders_id.add(order.getORDER_ID()+"");
            }
        });



        btnBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!networkConnected(getActivity())) {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isOnline()) {
                    Toast.makeText(getActivity(), "Internet not working", Toast.LENGTH_SHORT).show();
                    return;
                }

                    pushData();



            }
        });
        btnRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!networkConnected(getActivity())) {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isOnline()) {
                    Toast.makeText(getActivity(), "Internet not working", Toast.LENGTH_SHORT).show();
                    return;
                }

                RestoreData(getActivity());
            }
        });
        return root;

    }

    private void RestoreData(Context ctx) {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
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
                    Client client = new Client(name,fatherName,phone,leg,arm,chest,neck,front,back,date);

                    client.id = Integer.parseInt(id);

                    if (!local_clients_id.contains(id)) {
                        Log.e("Client id is : ",id+" "+local_orders_id.toString());
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
                for(DataSnapshot snap : snapshot.getChildren()){
                    final String orderID = snap.child("orderID").getValue().toString();
                    final String clientID = snap.child("clientID").getValue().toString();
                    final String name = snap.child("name").getValue().toString();
                    final String price = snap.child("price").getValue().toString();
                    final String type = snap.child("type").getValue().toString();
                    final String dateOrdered = snap.child("dateOrdered").getValue().toString();
                    final String dateDelivered = snap.child("dateReceived").getValue().toString();
                    final String status = snap.child("status").getValue().toString();
                    final String furtherDetails = snap.child("furtherDetails").getValue().toString();

                    Order order = new Order(clientID,name,price,type,dateOrdered,dateDelivered,status,furtherDetails);
                    order.ORDER_ID = Integer.parseInt(orderID);

                    if(!local_orders_id.contains(orderID)) {
                        orderViewModel.insertOrder(order);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void pushData() {
        clientViewModel.getAllClients().observe(getActivity(),clients -> {
            if(clients != null){
                for (Client client:clients){
                    client_list.add(client);
                }
            }
        });
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Backup Data");
        progressDialog.show();
        if (client_list.size()>0) {
            for(Client client:client_list) {
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
                firebaseDatabase.getReference("Data").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Clients").child(id).setValue(model).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getActivity(), "Clients Backup Done", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }else{
            Toast.makeText(getActivity(), "No Clients to Backup", Toast.LENGTH_SHORT).show();
        }
        BackupOrder();
        progressDialog.dismiss();
        return;

    }

    private void BackupOrder() {
        orderViewModel.getAllOrders().observe(getActivity(), orders -> {
            if (orders != null) {
                for (Order order : orders) {
                    order_list.add(order);
                }
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
                firebaseDatabase.getReference("Data").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Orders").child(orderID).setValue(model);
            }
            Toast.makeText(getActivity(), "Orders Backup Done", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "No Orders to Backup", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean networkConnected(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiConn != null && wifiConn.isConnected() && wifiConn.isAvailable()) || (mobileConn != null && mobileConn.isConnected() && mobileConn.isAvailable())) {
            return true;
        } else {
            return false;
        }

    }

    public boolean isOnline() {
        try {
            int timeoutMs = 1500;
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

            sock.connect(sockaddr, timeoutMs);
            sock.close();

            return true;
        } catch (IOException e) {


            return false;
        }
    }


}
