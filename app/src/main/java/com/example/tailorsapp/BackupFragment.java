package com.example.tailorsapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tailorsapp.Database.DatabaseHelper;
import com.example.tailorsapp.Database.OrderDataBaseHelper;
import com.example.tailorsapp.PersonModel.OrderDataBackupModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class BackupFragment extends Fragment {
    ImageButton btnBackup;
    ImageButton btnRestore;
    FirebaseDatabase firebaseDatabase;
    ClientsAllDataModel model;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_backup, container, false);
        btnBackup = root.findViewById(R.id.btnBackup);
        btnRestore = root.findViewById(R.id.btnRestore);
        firebaseDatabase = FirebaseDatabase.getInstance();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


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
                Cursor dataCursor = fetchData();

                pushData(dataCursor);


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
        DatabaseHelper db = new DatabaseHelper(ctx);
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
                    Cursor c = db.getDatabyID(Integer.parseInt(id));
                    c.moveToFirst();

                    if (c.getCount() > 0 && (c.getString(1).equals(name)) && (c.getString(10).equals(fatherName))) {
                        continue;
                    } else {
                        db.insert_in_clients(name, phone, leg, arm, chest, neck, front, back, date, fatherName);
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
        OrderDataBaseHelper helper = new OrderDataBaseHelper(getActivity());
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

                    Cursor cursor = helper.getDataByID(Integer.parseInt(orderID));
                    cursor.moveToFirst();
                    if(cursor.getCount()>0 && cursor.getString(1).equals(clientID) && cursor.getString(2).equals(name)){
                        continue;
                    }else{
                        helper.InsertDataIntoTable(Integer.parseInt(clientID),name,price,type,dateOrdered,dateDelivered,status,furtherDetails);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void pushData(Cursor dataCursor) {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Backup Data");
        progressDialog.show();
        if (dataCursor.moveToFirst()) {

            do {
                final String phone = dataCursor.getString(2);
                final String leg = dataCursor.getString(3);
                final String arm = dataCursor.getString(4);
                final String chest = dataCursor.getString(5);
                final String neck = dataCursor.getString(6);
                final String front = dataCursor.getString(7);
                final String back = dataCursor.getString(8);
                final String date = dataCursor.getString(9);
                final String id = dataCursor.getString(0);
                final String fatherName = dataCursor.getString(10);
                final String name = dataCursor.getString(1);
                model = new ClientsAllDataModel(id, name, fatherName, phone, leg, arm, chest, neck, front, back, date);
                firebaseDatabase.getReference("Data").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Clients").child(id).setValue(model);

            } while (dataCursor.moveToNext());
            {
                Toast.makeText(getActivity(), "Data Backup Done", Toast.LENGTH_SHORT).show();
                dataCursor.close();
            }

        }
        btnBackupOrder();
        progressDialog.dismiss();
        return;

    }

    private void btnBackupOrder() {
        OrderDataBaseHelper helper = new OrderDataBaseHelper(getActivity());
        Cursor cursor = helper.GetAllData();
        if (cursor.moveToFirst()) {
            do {
                final String orderID = cursor.getString(0);
                final String clientID = cursor.getString(1);
                final String name = cursor.getString(2);
                final String price = cursor.getString(3);
                final String type = cursor.getString(4);
                final String dateOrdered = cursor.getString(5);
                final String dateDelivered = cursor.getString(6);
                final String status = cursor.getString(7);
                final String furtherDetails = cursor.getString(8);
                OrderDataBackupModel model = new OrderDataBackupModel(orderID, clientID, name, type, price, dateOrdered, dateDelivered, status, furtherDetails);
                firebaseDatabase.getReference("Data").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Orders").child(orderID).setValue(model);
            } while (cursor.moveToNext());
            {
                cursor.close();
            }
        }
    }

    private Cursor fetchData() {
        DatabaseHelper db = new DatabaseHelper(getActivity());
        Cursor cursor = db.getAllData();
        return cursor;
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
