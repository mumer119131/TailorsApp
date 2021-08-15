package com.example.tailorsapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItem;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tailorsapp.Database.DatabaseHelper;

import java.util.Objects;

public class ClientDetails extends AppCompatActivity {
    String id;
    DatabaseHelper db;
    TextView dateTV,phoneTV,armTV,legTV,chestTV,neckTV,frontTV,backTV,clientName,idTV,fatherTV;
    Cursor cursorData;
    ImageView btnBack;
    String scale="inches";
    Toolbar toolbar;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2 && resultCode==RESULT_OK && data != null){
            String status = data.getStringExtra("STATUS");
            if(status.equals("DONE")){
                startActivity(getIntent());
                finish();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.client_details_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_details);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            id = bundle.getString("ID");
        }

        SharedPreferences preferences = this.getSharedPreferences("SCALE",MODE_PRIVATE);
        scale=preferences.getString("SCALEVALUE","");

        toolbar = findViewById(R.id.toolbar_client_details);
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

        String finalName = name;
        String finalId = id;
        String finalFatherName = fatherName;
        String finalLeg = leg;
        String finalArm = arm;
        String finalChest = chest;
        String finalNeck = neck;
        String finalFront = front;
        String finalBack = back;
        String finalPhone = phone;

        String finalId1 = id;

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.edit_icon:
                        Intent i = new Intent(ClientDetails.this,EditClient.class);
                        i.putExtra("Name", finalName);
                        i.putExtra("ID", finalId);
                        i.putExtra("Father", finalFatherName);
                        i.putExtra("leg", finalLeg);
                        i.putExtra("Arm", finalArm);
                        i.putExtra("Chest", finalChest);
                        i.putExtra("Neck", finalNeck);
                        i.putExtra("FRONT", finalFront);
                        i.putExtra("back", finalBack);
                        i.putExtra("Phone", finalPhone);
                        startActivityForResult(i,2);
                        break;
                    case R.id.delete_icon:
                        AlertDialog.Builder builder = new AlertDialog.Builder(ClientDetails.this);
                        builder.setTitle("Delete");
                        builder.setCancelable(false);
                        builder.setMessage("Are you sure to delete the client ?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DeleteAnitem(finalId1);
                                        Toast.makeText(ClientDetails.this, "Client Data Deleted", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(ClientDetails.this,MainActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);
                                        finish();
                                    }
                                });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        break;
                }
            return true;
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }




    private Cursor fetchData() {
        db =new DatabaseHelper(ClientDetails.this);
        int ID=Integer.parseInt(id);
        Cursor cursor = db.getDatabyID(ID);

        return cursor;
    }
    private  void DeleteAnitem(String id){
        DatabaseHelper db = new DatabaseHelper(ClientDetails.this);
        db.DeleteDataByID(Integer.parseInt(id));

    }


}