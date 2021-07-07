package com.example.tailorsapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tailorsapp.Database.DatabaseHelper;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private PieChart pieChart;
    private TextView userName,totalClients,noClients;
    private RecyclerView homeClientsRV;
    ClientsAdapter adapter;
    LinearLayoutManager manager;
    ArrayList<ClientModel> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root= (ViewGroup) inflater.inflate(R.layout.fragment_home,container,false);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("Name", Context.MODE_PRIVATE);
        String Name = sharedPreferences.getString("UserName","");
        userName=root.findViewById(R.id.userNameTV);
        userName.setText(Name);

        noClients = root.findViewById(R.id.noClients);

        list = new ArrayList<>();
        manager = new LinearLayoutManager(getActivity());
        homeClientsRV = root.findViewById(R.id.homeClientsRV);
        homeClientsRV.setLayoutManager(manager);
        FetchData();

        totalClients = root.findViewById(R.id.totalClientsOfHome);
        int total_Clients = getTotalClients();
        totalClients.setText(Integer.toString(total_Clients));




        //Pie Chart Setter
        pieChart=root.findViewById(R.id.PieChart);
        Description description=new Description();
        description.setText("");
        pieChart.setDescription(description);
        pieChart.setUsePercentValues(true);
        List<PieEntry> values = new ArrayList<>();
        values.add(new PieEntry(40f,"Completed"));
        values.add(new PieEntry(60f,"Pending"));
        PieDataSet pieDataSet=new PieDataSet(values,"Orders");
        PieData pieData=new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieChart.animateXY(1400,1400);

        return root;


    }

    private int getTotalClients() {
        DatabaseHelper helper = new DatabaseHelper(getActivity());
        Cursor cursor = helper.getAllData();
        int count = cursor.getCount();
        return count;
    }
    private void FetchData() {
        DatabaseHelper helper= new DatabaseHelper(getActivity());
        Cursor data = helper.getAllData();
        if(data.getCount()>0){
            while(data.moveToNext()){
                list.add(new ClientModel(data.getString(1), data.getString(0)));
            }
            adapter = new ClientsAdapter(getActivity(),list);
            homeClientsRV.setAdapter(adapter);

        }else{
            homeClientsRV.setVisibility(View.GONE);
            noClients.setVisibility(View.VISIBLE);
        }
    }
}
