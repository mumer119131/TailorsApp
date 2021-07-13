package com.example.tailorsapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
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
import com.example.tailorsapp.Database.OrderDataBaseHelper;
import com.example.tailorsapp.Database.UserDatabaseHelper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {
    private PieChart pieChart;
    private TextView userName, totalClients, noClients, noClientBackup, noOrderedAdded, noOfOrdersInBackup, noOfTotalOrders;
    private Button signOut;
    private FirebaseAuth mAuth;
    private String name, email, clients_in_backup, orders_in_backup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);


        userName = root.findViewById(R.id.userNameTV);
        noOfOrdersInBackup = root.findViewById(R.id.totalOrdersInBackup);
        totalClients = root.findViewById(R.id.totalClientsOfHome);
        noClientBackup = root.findViewById(R.id.noClientBackup);
        noClientBackup = root.findViewById(R.id.noClientBackup);
        pieChart = root.findViewById(R.id.PieChart);
        signOut = root.findViewById(R.id.btnSignOut);
        noOfTotalOrders = root.findViewById(R.id.totalOrderLocal);
        mAuth = FirebaseAuth.getInstance();


        //Setup User Name and Clients Data
        UserDatabaseHelper db = new UserDatabaseHelper(getActivity());
        Cursor cursor = db.getAllData();
        if (cursor.moveToFirst()) {
            name = cursor.getString(1);
            email = cursor.getString(2);

        }

        userName.setText(name);
        cursor.close();

        updateBackupClientsNo();
        getBackupClientsNo();

        int total_Clients = getTotalClients();
        totalClients.setText(Integer.toString(total_Clients));


        noOfTotalOrders = root.findViewById(R.id.totalOrderLocal);
        noOfTotalOrders.setText(Integer.toString(getTotalOrders()));

        //Pie Chart Setter


        int[] valuesOfOrder = getValues();
        if (valuesOfOrder[0] == 0 && valuesOfOrder[1] == 0) {
            noOrderedAdded = root.findViewById(R.id.noOrdersAdded);
            noOrderedAdded.setVisibility(View.VISIBLE);
            pieChart.setVisibility(View.GONE);
        }

        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);
        List<PieEntry> values = new ArrayList<>();
        values.add(new PieEntry(valuesOfOrder[1], "Completed"));
        values.add(new PieEntry(valuesOfOrder[0], "Pending"));
        PieDataSet pieDataSet = new PieDataSet(values, "Orders");
        pieDataSet.setValueTextColor(Color.BLACK);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.animateXY(1400, 1400);


        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(getActivity(), LoginActivirty.class));

            }
        });

        return root;


    }

    private int getTotalOrders() {
        OrderDataBaseHelper db = new OrderDataBaseHelper(getActivity());
        Cursor cursor = db.GetAllData();
        return cursor.getCount();
    }

    private void getBackupClientsNo() {
        UserDatabaseHelper db = new UserDatabaseHelper(getActivity());
        Cursor cursor = db.getAllData();

        if (cursor.moveToFirst()) {
            String clients_in_back_up = cursor.getString(3);
            String orders_in_backup = cursor.getString(4);
            Toast.makeText(getActivity(), orders_in_backup, Toast.LENGTH_SHORT).show();
            noClientBackup.setText(clients_in_back_up);
            noOfOrdersInBackup.setText(orders_in_backup);
        }


    }

    private int[] getValues() {
        OrderDataBaseHelper helper = new OrderDataBaseHelper(getActivity());
        Cursor pendingCursor = helper.getOrdersCount("Pending");
        Cursor completeCursor = helper.getOrdersCount("Completed");
        int[] data = new int[2];
        data[0] = pendingCursor.getCount();
        data[1] = completeCursor.getCount();
        return data;

    }


    private void updateBackupClientsNo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Data");
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orders_in_backup = Long.toString(snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Clients").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    clients_in_backup = Long.toString(snapshot.getChildrenCount());
                    UserDatabaseHelper db = new UserDatabaseHelper(getActivity());
                    db.EditIntoTable("1", name, email, clients_in_backup, orders_in_backup);
                } catch (SQLiteCantOpenDatabaseException e) {
                    return;
                }

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
}
