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
import com.example.tailorsapp.Database.UserDatabaseHelper;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseHelper myDB;
    private OrderDataBaseHelper orderDB;
    private UserDatabaseHelper userDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mAuth=FirebaseAuth.getInstance();
        myDB=new DatabaseHelper(this);
        orderDB = new OrderDataBaseHelper(this);
        userDB = new UserDatabaseHelper(this);
        SQLiteDatabase db=myDB.getWritableDatabase();

        boolean tableExist=DoesTableExists(db,"Clients");
        boolean orderTableExist = DoesTableExists(db,"ORDERS");
        boolean userTableExist = DoesTableExists(db,"USERS_DATA");
        if(!tableExist){
            myDB.onCreate(db);
        }
        if(!orderTableExist){
            orderDB.onCreate(db);
        }
        if(!userTableExist){
            userDB.onCreate(db);
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