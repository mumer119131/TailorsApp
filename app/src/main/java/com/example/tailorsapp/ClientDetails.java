package com.example.tailorsapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tailorsapp.RoomDataBase.ClientViewModel;
import com.example.tailorsapp.RoomDataBase.OrderViewModel;

public class ClientDetails extends AppCompatActivity {

    String id;
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

    TextView dateTV,phoneTV,armTV,legTV,chestTV,neckTV,frontTV,backTV,clientName,idTV,fatherTV;

    ImageView btnBack;
    String scale="inches";
    Toolbar toolbar;
    private ClientViewModel clientViewModel;


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
        clientViewModel = new ViewModelProvider(this).get(ClientViewModel.class);





        fetchData();








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
                        i.putExtra("Name", name);
                        i.putExtra("ID", id);
                        i.putExtra("Father", fatherName);
                        i.putExtra("leg", leg);
                        i.putExtra("Arm", arm);
                        i.putExtra("Chest", chest);
                        i.putExtra("Neck", neck);
                        i.putExtra("FRONT", front);
                        i.putExtra("back", back);
                        i.putExtra("Phone", phone);
                        startActivityForResult(i,2);
                        break;
                    case R.id.delete_icon:
                        AlertDialog.Builder builder = new AlertDialog.Builder(ClientDetails.this);
                        builder.setTitle("Delete");
                        builder.setCancelable(false);
                        builder.setIcon(R.drawable.ic_baseline_warning_24);
                        builder.setMessage("Are you sure to delete the client ?\n\n"+getString(R.string.warning)+"\n(By deleting client all the corresponding orders will also delete)")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DeleteAnitem(finalId1);
                                        deleteOrders(finalId1);
                                        Toast.makeText(ClientDetails.this, "Client Data Deleted", Toast.LENGTH_SHORT).show();
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

    private void deleteOrders(String finalId1) {
        OrderViewModel orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        orderViewModel.deleteByClientID(finalId1);

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
                armTV.setText(arm + " " + scale);
                legTV.setText(leg + " " + scale);
                chestTV.setText(chest + " " + scale);
                neckTV.setText(neck + " " + scale);
                frontTV.setText(front + " " + scale);
                backTV.setText(back + " " + scale);
                clientName.setText(name);
                dateTV.setText(date);
                fatherTV.setText(fatherName);
            }

        });


    }
    private  void DeleteAnitem(String id){

      clientViewModel.deleteClient(Integer.parseInt(id));

    }


}