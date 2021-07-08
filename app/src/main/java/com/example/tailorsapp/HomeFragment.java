package com.example.tailorsapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private PieChart pieChart;
    private TextView userName,totalClients,noClients,noClientBackup;
    private RecyclerView homeClientsRV;
    ClientsAdapter adapter;
    LinearLayoutManager manager;
    ArrayList<ClientModel> list;
    private Button signOut;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root= (ViewGroup) inflater.inflate(R.layout.fragment_home,container,false);
        String Name ="";
        String noOfClientsBackup="";
        try {
            SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("Name", Context.MODE_PRIVATE);
             Name= sharedPreferences.getString("UserName","");
            SharedPreferences preferences = getActivity().getSharedPreferences("BackupClients",Context.MODE_PRIVATE);
            noOfClientsBackup = preferences.getString("NoOfClients","");
        }catch (ClassCastException e){
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        }


        userName=root.findViewById(R.id.userNameTV);
        userName.setText(Name);

        noClients = root.findViewById(R.id.noClients);
        mAuth = FirebaseAuth.getInstance();
        list = new ArrayList<>();
        manager = new LinearLayoutManager(getActivity());
        homeClientsRV = root.findViewById(R.id.homeClientsRV);
        homeClientsRV.setLayoutManager(manager);
        FetchData();

        totalClients = root.findViewById(R.id.totalClientsOfHome);
        int total_Clients = getTotalClients();
        totalClients.setText(Integer.toString(total_Clients));

        signOut=root.findViewById(R.id.btnSignOut);
        noClientBackup = root.findViewById(R.id.noClientBackup);
        getBackupClientsNo();

        noClientBackup.setText(noOfClientsBackup);

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

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(getActivity(),LoginActivirty.class));

            }
        });

        return root;


    }

    private void getBackupClientsNo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Data");
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long noOfClientsBackUp = 0;
                noOfClientsBackUp =snapshot.getChildrenCount();
                SharedPreferences preferences = getActivity().getSharedPreferences("BackupClients",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("NoOfClients",Long.toString(noOfClientsBackUp));
                editor.apply();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
