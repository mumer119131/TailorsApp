package com.example.tailorsapp;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tailorsapp.Adapter.DisplayOrdersAdapter;
import com.example.tailorsapp.Database.OrderDataBaseHelper;
import com.example.tailorsapp.PersonModel.OrderDisplayModel;

import java.util.ArrayList;

public class PendingOrdersFragment extends Fragment {
    private RecyclerView pendingOrderRV;
    DisplayOrdersAdapter adapter;
    LinearLayoutManager manager;
    ArrayList<OrderDisplayModel> list;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root= (ViewGroup) inflater.inflate(R.layout.pending_orders_fragment,container,false);
        pendingOrderRV=root.findViewById(R.id.pendingOrderRV);

        list = new ArrayList<>();
        manager = new LinearLayoutManager(getActivity());
        pendingOrderRV.setLayoutManager(manager);
        FetchData();





        return root;
    }

    private void FetchData() {
        OrderDataBaseHelper helper = new OrderDataBaseHelper(getActivity());
        Cursor cursor = helper.GetAllData();

        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                String status = cursor.getString(7);
                if(status.equals("Pending")){
                    list.add(new OrderDisplayModel(cursor.getString(2),cursor.getString(4),cursor.getString(3),cursor.getString(5),cursor.getString(6),cursor.getString(0),cursor.getString(7)));

                }
            }
            adapter = new DisplayOrdersAdapter(getActivity(),list);
            pendingOrderRV.setAdapter(adapter);



        }
        else
            Toast.makeText(getActivity(), "No Data Present", Toast.LENGTH_SHORT).show();
    }
}
