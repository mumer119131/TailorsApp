package com.example.tailorsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tailorsapp.Database.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditClient extends AppCompatActivity {
    private EditText name, fatherName, phoneNumber, leg, arm, chest, neck, frontSide, backSide;
    private Button saveBtn;
    private ImageView backBtn;
    private String str_id="";
    private String str_name = "";
    private String str_fatherName = "";
    private String str_phoneNumber = "";
    private String str_leg = "";
    private String str_Arm = "";
    private String str_chest = "";
    private String str_neck = "";
    private String str_front = "";
    private String str_back = "";

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
        saveBtn=findViewById(R.id.btnToSave);
        backBtn = findViewById(R.id.backBtnEdit);


        getIntentExtras();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameData=name.getText().toString();
                String fatherData=fatherName.getText().toString();
                String phoneData=phoneNumber.getText().toString();
                String legData=leg.getText().toString();
                String armData=arm.getText().toString();
                String chestData=chest.getText().toString();
                String neckData=neck.getText().toString();
                String frontData=frontSide.getText().toString();
                String backData=backSide.getText().toString();

                DatabaseHelper helper = new DatabaseHelper(EditClient.this);
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String currentDate = sdf.format(new Date());
                Boolean update = helper.update_in_clients(Integer.parseInt(str_id),nameData,phoneData,legData,armData,chestData,neckData,frontData,backData,currentDate,fatherData);
                if(update){
                    Toast.makeText(EditClient.this, "Updated", Toast.LENGTH_SHORT).show();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("STATUS","DONE");
                    setResult(RESULT_OK,returnIntent);
                    finish();
                }else{
                    Toast.makeText(EditClient.this, "Failed Editing", Toast.LENGTH_SHORT).show();
                }
            }
        });


     backBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             finish();
         }
     });
    }

    private void getIntentExtras() {



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