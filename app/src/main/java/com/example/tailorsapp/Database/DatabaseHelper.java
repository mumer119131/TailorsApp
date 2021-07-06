package com.example.tailorsapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="sqlDatabase.db";

    public static  final String TABLE_NAME="Clients";
    public static  final String ID="ID";
    public static  final String NAME="NAME";
    public static  final String PHONE="PHONE";
    public static  final String LEG="LEG";
    public static  final String ARM="ARM";
    public static  final String CHEST="CHEST";
    public static  final String NECK="NECK";
    public static  final String FRONT_SIDE="FRONT";
    public static  final String BACK_SIDE="BACK";
    public static  final String DATE="DATE";
    public static  final String ADDITIONAL_DETAILS="DETAILS";



    private Context context;

    public DatabaseHelper(@Nullable Context context) {
        super(context,DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+"("
                +ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +NAME+" TEXT,"
                 +PHONE+" TEXT,"
                 +LEG+" INTEGER,"
                 +ARM+" INTEGER,"
                 +CHEST+" INTEGER,"
                 +NECK+" INTEGER,"
                 +FRONT_SIDE+" INTEGER,"
                 +BACK_SIDE+" INTEGER,"
                 +DATE+" DATE,"
                 +ADDITIONAL_DETAILS+" TEXT"
                 +");"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insert_in_clients(String name,String phone,String leg, String arm,String chest,String neck,String front,String back,String date,String details ){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(NAME,name);
        values.put(PHONE,phone);
        values.put(LEG,leg);
        values.put(ARM,arm);
        values.put(CHEST,chest);
        values.put(NECK,neck);
        values.put(FRONT_SIDE,front);
        values.put(BACK_SIDE,back);
        values.put(DATE,date);
        values.put(ADDITIONAL_DETAILS,details);
        long result= db.insert(TABLE_NAME,null,values);
        if(result==-1)
            return false;
        else
            return true;
    }
    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result=db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        return result;
    }
    public Cursor getDatabyID(int id){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor result=db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+ID+"="+id,null);
        return result;

    }
    public boolean DeleteDataByID(int cli_id){
        String id = Integer.toString(cli_id);
        SQLiteDatabase db=this.getWritableDatabase();
        long result=db.delete(TABLE_NAME,ID+"=?",new String[]{id});
        if(result==-1)
            return false;
        else
            return true;
    }
    public Cursor clientsData(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        return cursor;
    }

}
