package com.example.tailorsapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {
    private Button signOut,saveButton;
    private FirebaseAuth mAuth;
    private TextView userName,userEmail;
    private Spinner spinnerScale;
    private String scale="inch";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root= (ViewGroup) inflater.inflate(R.layout.fragment_profile,container,false);
        mAuth=FirebaseAuth.getInstance();
        SharedPreferences preferences = getActivity().getSharedPreferences("Name", Context.MODE_PRIVATE);
        String str_userName = preferences.getString("UserName","");
        String str_userEmail = preferences.getString("UserEmail","");

        signOut=root.findViewById(R.id.btnSignOut);
        userName = root.findViewById(R.id.userName);
        userEmail = root.findViewById(R.id.userEmail);
        spinnerScale=root.findViewById(R.id.spinnerScale);
        saveButton = root.findViewById(R.id.saveBtnProfile);

        userName.setText(str_userName);
        userEmail.setText(str_userEmail);



        spinnerScale.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                scale = (String) parent.getItemAtPosition(position);
                saveButton.setVisibility(View.VISIBLE);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getActivity(), "No item selected", Toast.LENGTH_SHORT).show();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SCALE",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("SCALEVALUE",scale);
                editor.apply();
                saveButton.setVisibility(View.GONE);
            }
        });
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(getActivity(),LoginActivirty.class));


            }
        });
        return root;
    }
}
