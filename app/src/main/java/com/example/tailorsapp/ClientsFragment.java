package com.example.tailorsapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tailorsapp.Database.DatabaseHelper;

import java.util.ArrayList;

public class ClientsFragment extends Fragment {

    RecyclerView recyclerView;
    ClientsAdapter adapter;
    LinearLayoutManager manager;
    ArrayList<ClientModel> list;
    TextView userName,totalClientsTV,noClients;
    LinearLayout recycleLinearLayout;
    int totalClients;
    View clientsView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root= (ViewGroup) inflater.inflate(R.layout.activity_clients_fragment,container,false);
        list = new ArrayList<>();
        manager = new LinearLayoutManager(getActivity());
        recyclerView = root.findViewById(R.id.rvClients);
        recyclerView.setLayoutManager(manager);
        userName=root.findViewById(R.id.clientName);
        totalClientsTV = root.findViewById(R.id.totalClients);
        noClients = root.findViewById(R.id.noClientsFrag);
        recycleLinearLayout = root.findViewById(R.id.linearLayoutClients);
        clientsView = root.findViewById(R.id.clientsView);

        SharedPreferences preferences = this.getActivity().getSharedPreferences("Name", Context.MODE_PRIVATE);
        String Name = preferences.getString("UserName","");
        userName.setText(Name);

        FetchData();
        String strClients = Integer.toString(totalClients);
        totalClientsTV.setText(" ("+strClients+")");
        return root;
    }

    private void FetchData() {
        DatabaseHelper helper= new DatabaseHelper(getActivity());
        Cursor data = helper.getAllData();
        totalClients = data.getCount();
        if(data.getCount()>0){
            while(data.moveToNext()){
                list.add(new ClientModel(data.getString(1), data.getString(0)));
            }
            adapter = new ClientsAdapter(getActivity(),list);
            recyclerView.setAdapter(adapter);
        }else{
            noClients.setVisibility(View.VISIBLE);
            recycleLinearLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            clientsView.setVisibility(View.GONE);
        }
    }


}
