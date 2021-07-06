package com.example.tailorsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class EditClient extends AppCompatActivity {
     EditText name,fatherName,phoneNumber,leg,arm,chest,neck,frontSide,backSide;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_client);
    }
}