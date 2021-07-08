package com.example.tailorsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.tailorsapp.Database.DatabaseHelper;
import com.example.tailorsapp.Database.OrderDataBaseHelper;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseHelper myDB;
    private OrderDataBaseHelper orderDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mAuth=FirebaseAuth.getInstance();
        myDB=new DatabaseHelper(this);
        orderDB = new OrderDataBaseHelper(this);
        SQLiteDatabase db=myDB.getWritableDatabase();

        boolean tableExist=DoesTableExists(db,"Clients");
        boolean orderTableExist = DoesTableExists(db,"ORDERS");
        if(!tableExist){
            myDB.onCreate(db);
            Toast.makeText(this, "Clients Database Created", Toast.LENGTH_SHORT).show();
        }
        if(!orderTableExist){
            orderDB.onCreate(db);
            Toast.makeText(this, "Orders Database Created", Toast.LENGTH_SHORT).show();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mAuth.getCurrentUser()!=null) {
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }else{
                   startActivity(new Intent(SplashScreen.this,LoginActivirty.class));
                   finish();
                }

            }
        },3000);
    }

    public boolean DoesTableExists( SQLiteDatabase db,String tableName){
        try {
            db.rawQuery("SELECT * FROM "+tableName,null);
            return true;
        }catch (SQLException e){
            return false;
        }

    }





}