package com.example.tailorsapp.MenuFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.tailorsapp.ClientModel;
import com.example.tailorsapp.ClientsAdapter;
import com.example.tailorsapp.R;
import com.example.tailorsapp.RoomDataBase.Client;
import com.example.tailorsapp.RoomDataBase.ClientViewModel;

import java.util.ArrayList;

public class ClientsFragment extends Fragment {
    SearchView searchView;
    RecyclerView recyclerView;
    ClientsAdapter adapter;
    LinearLayoutManager manager;
    ArrayList<ClientModel> list;
    TextView userName, totalClientsTV, noClients;
    LinearLayout recycleLinearLayout;
    LottieAnimationView noClientAnim;
    int totalClients;
    View clientsView;
    private ClientViewModel clientViewModel;

    @Override
    public void onResume() {
        super.onResume();
        ClientsFragment frgInstance = new ClientsFragment();
        getFragmentManager().beginTransaction().detach(frgInstance).attach(frgInstance).commit();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_clients_fragment, container, false);
        list = new ArrayList<>();
        manager = new LinearLayoutManager(getActivity());
        recyclerView = root.findViewById(R.id.rvClients);
        recyclerView.setLayoutManager(manager);
        userName = root.findViewById(R.id.clientName);
        totalClientsTV = root.findViewById(R.id.totalClients);
        noClients = root.findViewById(R.id.noClientsFrag);
        recycleLinearLayout = root.findViewById(R.id.linearLayoutClients);
        clientsView = root.findViewById(R.id.clientsView);
        searchView = root.findViewById(R.id.searchClients);
        clientViewModel = new ViewModelProvider(this).get(ClientViewModel.class);
        noClientAnim = root.findViewById(R.id.emptyAnimClient);


        SharedPreferences preferences = this.getActivity().getSharedPreferences("Name", Context.MODE_PRIVATE);
        String Name = preferences.getString("UserName", "");
        userName.setText(Name);

        FetchData();
        String strClients = Integer.toString(totalClients);
        totalClientsTV.setText(" (" + strClients + ")");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return root;
    }

    private void FetchData() {

        clientViewModel.getAllClients().observe(getActivity(),clients -> {
            list.clear();
            Log.i("Oberserver","Started");
            for(Client client: clients){

                list.add(new ClientModel(client.getName(),client.getId()+""));
            }
            if (list.size() > 0) {
                adapter = new ClientsAdapter(getActivity(),list);
                recyclerView.setAdapter(adapter);
            } else {
                noClients.setVisibility(View.VISIBLE);
                noClientAnim.setVisibility(View.VISIBLE);
                recycleLinearLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                clientsView.setVisibility(View.GONE);
                searchView.setVisibility(View.GONE);
            }
        });

    }


}
