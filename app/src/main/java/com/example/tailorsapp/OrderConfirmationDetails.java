package com.example.tailorsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tailorsapp.Database.DatabaseHelper;

public class OrderConfirmationDetails extends AppCompatActivity {
    String id;
    DatabaseHelper db;
    TextView dateTV,phoneTV,armTV,legTV,chestTV,neckTV,frontTV,backTV,clientName,idTV,fatherTV;
    Cursor cursorData;
    ImageView btnBack;
    Button btnConfirm;
    String scale="inches";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            id = bundle.getString("ID");
        }
        Toast.makeText(this, "Order Details Started", Toast.LENGTH_SHORT).show();

        SharedPreferences preferences = this.getSharedPreferences("SCALE",MODE_PRIVATE);
        scale=preferences.getString("SCALEVALUE","");


        btnBack =findViewById(R.id.backBtnClientDetails);
        dateTV=findViewById(R.id.dateTV);
        phoneTV=findViewById(R.id.phoneTV);
        armTV=findViewById(R.id.armTV);
        legTV=findViewById(R.id.legTV);
        chestTV=findViewById(R.id.chestTV);
        neckTV=findViewById(R.id.neckTV);
        frontTV=findViewById(R.id.frontTV);
        backTV=findViewById(R.id.backTV);
        clientName=findViewById(R.id.clientName);
        idTV=findViewById(R.id.idTV);
        fatherTV = findViewById(R.id.fatherTV);
        btnConfirm = findViewById(R.id.btnConfirm);


        cursorData = fetchData();
        String phone = "";
        String leg="";
        String arm="";
        String chest="";
        String neck="";
        String front="";
        String back="";
        String date="";
        String id="";
        String fatherName = "";
        String name = "";

        if(cursorData != null && cursorData.moveToFirst()) {
            id=cursorData.getString(0);
            name = cursorData.getString(1);
            phone = cursorData.getString(2);
            leg = cursorData.getString(3);
            arm = cursorData.getString(4);
            chest = cursorData.getString(5);
            neck = cursorData.getString(6);
            front = cursorData.getString(7);
            back = cursorData.getString(8);
            date = cursorData.getString(9);
            fatherName = cursorData.getString(10);

        }
        cursorData.close();


        idTV.setText(id);
        phoneTV.setText(phone);
        armTV.setText(arm+" "+scale);
        legTV.setText(leg+" "+scale);
        chestTV.setText(chest+" "+scale);
        neckTV.setText(neck+" "+scale);
        frontTV.setText(front+" "+scale);
        backTV.setText(back+" "+scale);
        clientName.setText(name);
        dateTV.setText(date);
        fatherTV.setText(fatherName);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String finalId = id;
        btnConfirm.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(OrderConfirmationDetails.this,OrderFinalConfirmation.class);
            i.putExtra("clientID", finalId);
            startActivity(i);
        }
    });
    }

    private Cursor fetchData() {
        db =new DatabaseHelper(OrderConfirmationDetails.this);
        int ID=Integer.parseInt(id);
        Cursor cursor = db.getDatabyID(ID);

        return cursor;
    }
}