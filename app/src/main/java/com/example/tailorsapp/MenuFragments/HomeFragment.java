package com.example.tailorsapp.MenuFragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.graphics.Color;
import android.os.Bundle;
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
import androidx.lifecycle.ViewModelProvider;

import com.example.tailorsapp.Global.BackupRestore;
import com.example.tailorsapp.Global.NetworkTest;
import com.example.tailorsapp.LoginActivity;
import com.example.tailorsapp.R;
import com.example.tailorsapp.RoomDataBase.ClientViewModel;
import com.example.tailorsapp.RoomDataBase.Order;
import com.example.tailorsapp.RoomDataBase.OrderViewModel;
import com.example.tailorsapp.RoomDataBase.User;
import com.example.tailorsapp.RoomDataBase.UserViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {
    private PieChart pieChart;
    private TextView userName, totalClients, noClients, noClientBackup, noOrderedAdded, noOfOrdersInBackup, noOfTotalOrders,emailTV;
    private Button signOut;
    private FirebaseAuth mAuth;
    private String name, email;
    private String clients_in_backup ="0";
    private String orders_in_backup = "0";
    private int total_clients_local,total_orders_local;
    private ClientViewModel clientViewModel;
    private OrderViewModel orderViewModel;
    private UserViewModel userViewModel;
    private NetworkTest networkTest;


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
        noOrderedAdded = root.findViewById(R.id.noOrdersAdded);
        mAuth = FirebaseAuth.getInstance();
        emailTV = root.findViewById(R.id.emailTV);
        clientViewModel = new ViewModelProvider(getActivity()).get(ClientViewModel.class);
        orderViewModel = new ViewModelProvider(getActivity()).get(OrderViewModel.class);
        noOfTotalOrders = root.findViewById(R.id.totalOrderLocal);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);



        getValues();
        updateBackupClientsNo();
        getTotalClients();
        getTotalOrders();


        //Pie Chart Setter








        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkTest = new NetworkTest();
                if(networkTest.networkConnected(requireActivity())) {
                    MaterialAlertDialogBuilder mb = new MaterialAlertDialogBuilder(requireActivity());
                    mb.setTitle("Sign Out");
                    mb.setMessage("By SignOut all of your local data will be lost." +
                            "\n(It is recommneded to Backup your data)");
                    mb.setPositiveButton("Backup & SignOut", (dialogInterface, i) -> {
                        BackupRestore backup = new BackupRestore(requireActivity());
                        backup.pushData();
                        clientViewModel.deleteAllClients();
                        orderViewModel.deleteAllOrders();
                        userViewModel.deleteAllUserData();
                        mAuth.signOut();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                    });
                    mb.setNegativeButton("Cancel", (dialogInterface, i) -> {

                    });
                    mb.setNeutralButton("SignOut", (dialogInterface, i) -> {
                        userViewModel.deleteAllUserData();
                        clientViewModel.deleteAllClients();
                        orderViewModel.deleteAllOrders();
                        mAuth.signOut();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                    });
                    mb.show();

                }else{
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        });
        //Setup User Name and Clients Data
        UserViewModel userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        userViewModel.getAllUserData().observe(getActivity(),users -> {
            if(users !=null) {
                for (User user : users) {
                     name = user.getNAME();
                     email = user.getEMAIL();
                     clients_in_backup = user.getCLIENTS_IN_BACKUP();
                     orders_in_backup = user.getORDERS_IN_BACKUP();
                    userName.setText(name);
                    emailTV.setText(email);
                    noClientBackup.setText(clients_in_backup);
                    noOfOrdersInBackup.setText(orders_in_backup);

                }
            }
        });



        return root;


    }

    private void getTotalOrders() {
        orderViewModel.getAllOrders().observe(requireActivity(), orders -> {
            total_orders_local = orders.size();
            noOfTotalOrders.setText(Integer.toString(total_orders_local));

        });
    }

    private void getValues() {

        orderViewModel.getAllOrders().observe(requireActivity(), orders -> {
            int completedOrders = 0;
            int pendingOrders = 0;
            if(orders != null){
                for(Order order:orders){
                    if(order.getSTATUS().equals("Completed")){
                        completedOrders = completedOrders + 1;
                    }else{
                        pendingOrders = pendingOrders + 1;
                    }
                }
                Log.e("Pending",Integer.toString(pendingOrders));
                Log.e("Completed",Integer.toString(completedOrders));
                setPieChart(pendingOrders,completedOrders);
            }


        });




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
                    if(orders_in_backup == null || clients_in_backup == null){
                        return;
                    }else {
                        userViewModel.updateUser(mAuth.getCurrentUser().getUid(),clients_in_backup,orders_in_backup);
                    }
                } catch (SQLiteCantOpenDatabaseException e) {
                    return;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getTotalClients() {
        clientViewModel.getAllClients().observe(getActivity(),clients -> {
            total_clients_local = clients.size();
            totalClients.setText(Integer.toString(total_clients_local));

        });
    }

    private void setPieChart(int pendingOrder,int completedOrder){
        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);
        List<PieEntry> values = new ArrayList<>();
        values.add(new PieEntry(completedOrder, "Completed"));
        values.add(new PieEntry(pendingOrder, "Pending"));
        PieDataSet pieDataSet = new PieDataSet(values, "Orders");
        pieDataSet.setValueTextColor(Color.BLACK);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.animateXY(1400, 1400);

        if (pendingOrder == 0 && completedOrder == 0) {
            noOrderedAdded.setVisibility(View.VISIBLE);
            pieChart.setVisibility(View.GONE);
        }
    }
}
