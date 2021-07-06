package com.example.tailorsapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tailorsapp.Database.DatabaseHelper;
import com.example.tailorsapp.PersonModel.PersonModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BackupFragment extends Fragment {
    Button btnBackup;
    Button btnRestore;
    FirebaseDatabase firebaseDatabase;
    ClientsAllDataModel model;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root= (ViewGroup) inflater.inflate(R.layout.fragment_orders,container,false);
        btnBackup = root.findViewById(R.id.btnBackup);
        btnRestore= root.findViewById(R.id.btnRestore);
        firebaseDatabase = FirebaseDatabase.getInstance();

        btnBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!networkConnected(getActivity())){
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    return;

                }
                Cursor dataCursor = fetchData();
                pushData(dataCursor);
            }
        });
        btnRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!networkConnected(getActivity())){
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    return;
                }
                RestoreData(getActivity());
            }
        });
        return root;

    }

    private void RestoreData(Context ctx) {
        DatabaseHelper db = new DatabaseHelper(ctx);
        firebaseDatabase.getReference("Data").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()){
                    String phone = snap.child("phoneNumber").getValue().toString();
                    String leg=snap.child("leg").getValue().toString();
                    String arm=snap.child("arm").getValue().toString();
                    String chest=snap.child("chest").getValue().toString();
                    String neck=snap.child("neck").getValue().toString();
                    String front=snap.child("frontSide").getValue().toString();
                    String back=snap.child("backSide").getValue().toString();
                    String date=snap.child("date").getValue().toString();
                    String id=snap.child("id").getValue().toString();
                    String fatherName = snap.child("fatherName").getValue().toString();
                    String name =snap.child("name").getValue().toString();
                    Cursor c = db.getDatabyID(Integer.parseInt(id));
                    if(c.getCount()>0){
                        continue;
                    }else {
                        db.insert_in_clients(name, phone, leg, arm, chest, neck, front, back, date, fatherName);
                    }
                }
                Toast.makeText(ctx, "Data Restored", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void pushData(Cursor dataCursor) {
        if(dataCursor.moveToFirst()){

            do{
                final String phone = dataCursor.getString(2);
                final String leg=dataCursor.getString(3);
                final String arm=dataCursor.getString(4);
                final String chest=dataCursor.getString(5);
                final String neck=dataCursor.getString(6);
                final String front=dataCursor.getString(7);
                final String back=dataCursor.getString(8);
                final String date=dataCursor.getString(9);
                final String id = dataCursor.getString(0);
                final String fatherName = dataCursor.getString(10);
                final String name = dataCursor.getString(1);
                model = new ClientsAllDataModel(id,name,fatherName,phone,leg,arm,chest,neck,front,back,date);
                firebaseDatabase.getReference("Data").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(id).setValue(model);

            }while(dataCursor.moveToNext());{
                Toast.makeText(getActivity(), "Data Backup Done", Toast.LENGTH_SHORT).show();
                dataCursor.close();
                return;
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

        if((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected()) ){
            return true;
        }else{
            return false;
        }

    }



}
