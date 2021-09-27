package com.example.tailorsapp.MenuFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.tailorsapp.Adapter.SectionPagerAdapter;
import com.example.tailorsapp.AddOrder.AddOrder;
import com.example.tailorsapp.FragmentCompletedOrders;
import com.example.tailorsapp.PendingOrdersFragment;
import com.example.tailorsapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class OrdersFragment extends Fragment {


    ViewPager viewPager;
    TabLayout tabLayout;
    private FloatingActionButton addOrderBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root= (ViewGroup) inflater.inflate(R.layout.fragment_orders,container,false);
        viewPager = root.findViewById(R.id.ordersViewPager);
        tabLayout = root.findViewById(R.id.OrderTabLayout);
        addOrderBtn = root.findViewById(R.id.addOrderFloatingAction);


        addOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddOrder.class));
            }
        });



        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpViewPager(ViewPager viewPager) {
        SectionPagerAdapter adapter = new SectionPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new PendingOrdersFragment(),"Pending");
        adapter.addFragment(new FragmentCompletedOrders(),"Completed");
        viewPager.setAdapter(adapter);
    }
}
