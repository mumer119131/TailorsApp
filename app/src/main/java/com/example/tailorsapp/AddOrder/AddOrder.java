package com.example.tailorsapp.AddOrder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.tailorsapp.Adapter.OrdersAdapter;
import com.example.tailorsapp.ClientModel;
import com.example.tailorsapp.R;
import com.example.tailorsapp.RoomDataBase.Client;
import com.example.tailorsapp.RoomDataBase.ClientViewModel;

import java.util.ArrayList;

public class AddOrder extends AppCompatActivity {
    private RecyclerView clientsRecyclerView;
    OrdersAdapter adapter;
    LinearLayoutManager manager;
    ArrayList<ClientModel> list;
    ImageView backBtn;
    ClientViewModel clientViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);

        clientsRecyclerView = findViewById(R.id.clientsRecyclerViewOrder);
        backBtn = findViewById(R.id.backBtn);
        clientViewModel = new ViewModelProvider(this).get(ClientViewModel.class);

        list = new ArrayList<>();
        manager = new LinearLayoutManager(this);
        clientsRecyclerView.setLayoutManager(manager);

        FetchData();
     backBtn.setOnClickListener(v -> finish());
    }
    private void FetchData() {
        clientViewModel.getAllClients().observe(this,clients -> {
            list.clear();
            for(Client client: clients){
                list.add(new ClientModel(client.getName(),client.getId()+""));
            }
            if(list.size()>0) {
                adapter = new OrdersAdapter(this, list);
                clientsRecyclerView.setAdapter(adapter);
            }

        });



    }
}