package com.example.tailorsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditClient extends AppCompatActivity {
    EditText name, fatherName, phoneNumber, leg, arm, chest, neck, frontSide, backSide;
    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_client);

        name = findViewById(R.id.etName);
        fatherName = findViewById(R.id.etFatherName);
        phoneNumber = findViewById(R.id.etPhoneNumber);
        leg = findViewById(R.id.etLeg);
        arm = findViewById(R.id.etArm);
        chest = findViewById(R.id.etChest);
        neck = findViewById(R.id.etNeck);
        frontSide = findViewById(R.id.etFrontSide);
        backSide = findViewById(R.id.etBackSide);
        saveBtn.findViewById(R.id.btnSave);

        getIntentExtras();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }

    private void getIntentExtras() {
        String str_id;
        String str_name = "";
        String str_fatherName = "";
        String str_phoneNumber = "";
        String str_leg = "";
        String str_Arm = "";
        String str_chest = "";
        String str_neck = "";
        String str_front = "";
        String str_back = "";


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            str_id = bundle.getString("ID");
            str_name = bundle.getString("Name");
            str_fatherName = bundle.getString("Father");
            str_leg = bundle.getString("leg");
            str_Arm = bundle.getString("Arm");
            str_chest = bundle.getString("Chest");
            str_neck = bundle.getString("Neck");
            str_front = bundle.getString("FRONT");
            str_back = bundle.getString("back");
            str_phoneNumber = bundle.getString("Phone");
        }
        name.setText(str_name);
        phoneNumber.setText(str_phoneNumber);
        fatherName.setText(str_fatherName);
        leg.setText(str_leg);
        arm.setText(str_Arm);
        chest.setText(str_chest);
        neck.setText(str_neck);
        frontSide.setText(str_front);
        backSide.setText(str_back);
        phoneNumber.setText(str_phoneNumber);
    }
}