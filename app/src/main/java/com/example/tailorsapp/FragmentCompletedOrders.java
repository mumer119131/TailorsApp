package com.example.tailorsapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.tailorsapp.Adapter.DisplayOrdersAdapter;
import com.example.tailorsapp.PersonModel.OrderDisplayModel;
import com.example.tailorsapp.RoomDataBase.Order;
import com.example.tailorsapp.RoomDataBase.OrderViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FragmentCompletedOrders extends Fragment {
    private RecyclerView recyclerView;
    ArrayList<OrderDisplayModel> list;
    DisplayOrdersAdapter adapter;
    LinearLayoutManager manager;
    OrderViewModel orderViewModel;
    LottieAnimationView noOrderAnim;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.i("onCreate","C");

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.completed_orders_fragment, container, false);


        noOrderAnim = root.findViewById(R.id.emptyOrderCompleted);
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        list = new ArrayList<>();
        manager = new LinearLayoutManager(getActivity());
        recyclerView = root.findViewById(R.id.completedOrdersRV);
        recyclerView.setLayoutManager(manager);



        FetchData();

        return root;
    }

    private void FetchData() {
        orderViewModel.getAllOrders().observe(getActivity(),orders -> {
            list.clear();
            if (orders != null) {
                for (Order order : orders) {
                    if(order.getSTATUS().equals("Completed"))
                        list.add(new OrderDisplayModel(order.getNAME(), order.getTYPE(), order.getPRICE(), order.getDATE_ORDERED(), order.getDATE_RECEIVED(), Integer.toString(order.getORDER_ID()), order.getSTATUS(), order.getFURTHER_DETAILS(), order.getCLIENT_ID()));
                }
                if(list.size() == 0) {
                    noOrderAnim.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }else{
                    recyclerView.setVisibility(View.VISIBLE);
                    noOrderAnim.setVisibility(View.GONE);
                }
                adapter = new DisplayOrdersAdapter(getActivity(), list);
                recyclerView.setAdapter(adapter);

            }
        });

    }
}

