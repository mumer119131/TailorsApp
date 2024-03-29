package com.example.tailorsapp.AddOrder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.example.tailorsapp.R;
import com.example.tailorsapp.RoomDataBase.ClientViewModel;

public class OrderConfirmationDetails extends AppCompatActivity {
    String id;
    TextView dateTV,phoneTV,armTV,legTV,chestTV,neckTV,frontTV,backTV,clientName,idTV,fatherTV;
    Cursor cursorData;
    String scale="inches";
    Toolbar toolbar;
    ClientViewModel clientViewModel;
    String phone = "";
    String leg="";
    String arm="";
    String chest="";
    String neck="";
    String front="";
    String back="";
    String date="";
    String fatherName = "";
    String name = "";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.order_confirmation_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            id = bundle.getString("ID");
        }

        SharedPreferences preferences = this.getSharedPreferences("SCALE",MODE_PRIVATE);
        scale=preferences.getString("SCALEVALUE","");


        toolbar = findViewById(R.id.toolbar_order_details);
        setSupportActionBar(toolbar);
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
        clientViewModel = new ViewModelProvider(this).get(ClientViewModel.class);

        fetchData();



        String finalId = id;
    toolbar.setOnMenuItemClickListener(item -> {
         if(item.getItemId() == R.id.confirmOrderMenu){
                Intent i = new Intent(OrderConfirmationDetails.this, OrderFinalConfirmation.class);
                i.putExtra("clientID", finalId);
                startActivity(i);

        }
        return true;
    });
    toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void fetchData() {
        clientViewModel.getClientByID(Integer.parseInt(id)).observe(this,client -> {

            if (client != null) {

                id = client.getId() + "";
                name = client.getName();
                phone = client.getPhoneNumber();
                leg = client.getLeg();
                arm = client.getArm();
                chest = client.getChest();
                neck = client.getNeck();
                front = client.getFrontSide();
                back = client.getBackSide();
                date = client.getDate();
                fatherName = client.getFatherName();


                idTV.setText(id);
                phoneTV.setText(phone);
                armTV.setText(arm);
                legTV.setText(leg);
                chestTV.setText(chest);
                neckTV.setText(neck);
                frontTV.setText(front);
                backTV.setText(back);
                clientName.setText(name);
                dateTV.setText(date);
                fatherTV.setText(fatherName);
            }

        });
    }
}