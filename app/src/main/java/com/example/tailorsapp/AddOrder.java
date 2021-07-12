package com.example.tailorsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tailorsapp.Adapter.OrdersAdapter;
import com.example.tailorsapp.Database.DatabaseHelper;

import java.util.ArrayList;

public class AddOrder extends AppCompatActivity {
    private RecyclerView clientsRecyclerView;
    OrdersAdapter adapter;
    LinearLayoutManager manager;
    ArrayList<ClientModel> list;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);

        clientsRecyclerView = findViewById(R.id.clientsRecyclerViewOrder);
        backBtn = findViewById(R.id.backBtn);

        list = new ArrayList<>();
        manager = new LinearLayoutManager(this);
        clientsRecyclerView.setLayoutManager(manager);
        FetchData();
     backBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             finish();
         }
     });
    }
    private void FetchData() {
        DatabaseHelper helper= new DatabaseHelper(this);
        Cursor data = helper.getAllData();
        if(data.getCount()>0){
            while(data.moveToNext()){
                list.add(new ClientModel(data.getString(1), data.getString(0)));
            }
            adapter = new OrdersAdapter(this,list);
            clientsRecyclerView.setAdapter(adapter);

        }else{
            Toast.makeText(this, "No Data Present", Toast.LENGTH_SHORT).show();
        }
    }
}