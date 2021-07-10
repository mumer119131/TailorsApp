package com.example.tailorsapp;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.tailorsapp.Adapter.DisplayOrdersAdapter;
import com.example.tailorsapp.Database.OrderDataBaseHelper;
import com.example.tailorsapp.PersonModel.OrderDisplayModel;

import java.util.ArrayList;

public class FragmentCompletedOrders extends Fragment {
    private RecyclerView recyclerView;
    ArrayList<OrderDisplayModel> list;
    DisplayOrdersAdapter adapter;
    LinearLayoutManager manager;
    SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root= (ViewGroup) inflater.inflate(R.layout.completed_orders_fragment,container,false);

        refreshLayout = root.findViewById(R.id.swipeRefreshLayoutCompleted);
        list= new ArrayList<>();
        manager = new LinearLayoutManager(getActivity());
        recyclerView = root.findViewById(R.id.completedOrdersRV);
        recyclerView.setLayoutManager(manager);

        FetchData();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }
        });




        return root;
    }

    private void FetchData() {
        OrderDataBaseHelper helper = new OrderDataBaseHelper(getActivity());
        Cursor cursor = helper.GetAllData();

        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                String status = cursor.getString(7);
                if(status.equals("Completed")){
                    list.add(new OrderDisplayModel(cursor.getString(2),cursor.getString(4),cursor.getString(3),cursor.getString(5),cursor.getString(6),cursor.getString(0),cursor.getString(7)));

                }
            }
            adapter = new DisplayOrdersAdapter(getActivity(),list);
            recyclerView.setAdapter(adapter);




        }
        else
            Toast.makeText(getActivity(), "No Data Present", Toast.LENGTH_SHORT).show();
    }
}

