package com.example.tailorsapp.MenuFragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tailorsapp.R;
import com.example.tailorsapp.RoomDataBase.Client;
import com.example.tailorsapp.RoomDataBase.ClientViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddFragment extends Fragment {
    private TextInputEditText etName,etPhone,etFather,etLeg,etArm,etChest,etNeck,etFrontSide,etBackSide;
    private Button btnSave;
    private String currentDate;
    private ClientViewModel clientViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root= (ViewGroup) inflater.inflate(R.layout.fragment_add,container,false);
        etName=root.findViewById(R.id.etName);
        etPhone=root.findViewById(R.id.etPhoneNumber);
        etFather=root.findViewById(R.id.etFatherName);
        etLeg=root.findViewById(R.id.etLeg);
        etArm=root.findViewById(R.id.etArm);
        etChest=root.findViewById(R.id.etChest);
        etNeck=root.findViewById(R.id.etNeck);
        etFrontSide=root.findViewById(R.id.etFrontSide);
        etBackSide=root.findViewById(R.id.etBackSide);
        btnSave=root.findViewById(R.id.btnToSave);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        currentDate = sdf.format(new Date());
        clientViewModel = new ViewModelProvider(this).get(ClientViewModel.class);


         btnSave.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Getting_Data();
             }
         });
        return root;
    }

    private void Getting_Data() {
        String Name = etName.getText().toString().trim();
        String Phone = etPhone.getText().toString().trim();
        String FatherName = etFather.getText().toString().trim();
        String Leg = etLeg.getText().toString().trim();
        String Arm = etArm.getText().toString().trim();
        String Chest = etChest.getText().toString().trim();
        String Neck = etNeck.getText().toString().trim();
        String Front = etFrontSide.getText().toString().trim();
        String Back = etBackSide.getText().toString().trim();
        if (TextUtils.isEmpty(Name)) {
            etName.setError("Enter the Name");
            etName.requestFocus();
        } else if (TextUtils.isEmpty(Phone)) {
            etPhone.setError("Enter the Phone Number");
            etPhone.requestFocus();
        } else if (TextUtils.isEmpty(FatherName)) {
            etFather.setError("Enter the Father Name");
            etFather.requestFocus();
        } else if (TextUtils.isEmpty(Leg)) {
            etLeg.setError("Enter the Leg Length");
            etLeg.requestFocus();
        } else if (TextUtils.isEmpty(Arm)) {
            etArm.setError("Enter the Arm Length");
            etArm.requestFocus();
        } else if (TextUtils.isEmpty(Chest)) {
            etChest.setError("Enter the Chest Length");
            etChest.requestFocus();
        } else if (TextUtils.isEmpty(Neck)) {
            etChest.setError("Enter the Neck Length");
            etChest.requestFocus();
        } else if (TextUtils.isEmpty(Front)) {
            etFrontSide.setError("Enter the Front Length");
            etFrontSide.requestFocus();
        } else if (TextUtils.isEmpty(Back)) {
            etBackSide.setError("Enter the Back Length");
            etBackSide.requestFocus();
        } else if(Name.contains(".") ||Name.contains("/") ||Name.contains("\\") ||Name.contains("?") ||Name.contains("<") ||Name.contains(">") ||Name.contains("|") ){
            etName.setError("Client name should not contain following characters:\n\\/.?<>|");
            etName.requestFocus();
        }else if(FatherName.contains(".") ||FatherName.contains("/") ||FatherName.contains("\\") ||FatherName.contains("?") ||FatherName.contains("<") ||FatherName.contains(">") ||FatherName.contains("|") ){
            etFather.setError("Client FatherName should not contain following characters:\n\\/.?<>|");
            etFather.requestFocus();
        }
        else if(Name.length()<5){
            etName.setError("Name must contain at least 5 characters");
            etName.requestFocus();
        }
        else if(FatherName.length()<5){
            etFather.setError("Father name must contain at least 5 characters");
            etFather.requestFocus();
        }
        else {
            Client client = new Client(Name,FatherName,Phone,Leg,Arm,Chest,Neck,Front,Back,currentDate);
            clientViewModel.insertClients(client);
                Toast.makeText(getActivity(), "Client Data Saved", Toast.LENGTH_SHORT).show();
                etName.setText("");
                etPhone.setText("");
                etFather.setText("");
                etLeg.setText("");
                etArm.setText("");
                etChest.setText("");
                etNeck.setText("");
                etFrontSide.setText("");
                etBackSide.setText("");
        }
    }
}
