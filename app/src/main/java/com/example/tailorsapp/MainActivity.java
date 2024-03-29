package com.example.tailorsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.tailorsapp.MenuFragments.AddFragment;
import com.example.tailorsapp.MenuFragments.BackupFragment;
import com.example.tailorsapp.MenuFragments.ClientsFragment;
import com.example.tailorsapp.MenuFragments.HomeFragment;
import com.example.tailorsapp.MenuFragments.OrdersFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);


        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                new HomeFragment(),"HomeFrag").commit();
    }


    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment= null;
                    switch (item.getItemId()){
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_orders:
                            selectedFragment=new BackupFragment();
                            break;
                        case R.id.nav_add:
                            selectedFragment = new AddFragment();
                            break;
                        case R.id.nav_client:
                            selectedFragment=new ClientsFragment();
                            break;
                        case R.id.nav_profile:
                            selectedFragment=new OrdersFragment();
                            break;
                    }
                  getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                          selectedFragment,"ClientsFrag").commit();
                    return true;
                }
            };
}