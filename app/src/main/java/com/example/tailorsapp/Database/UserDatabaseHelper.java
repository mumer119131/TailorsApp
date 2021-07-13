package com.example.tailorsapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class UserDatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    public static final String DATABASE_NAME = "userDatabase.db";
    private static final String TABLE_NAME = "USERS_DATA";
    private static  final String ID = "ID";
    private static final String NAME = "NAME";
    private static final String EMAIL = "EMAIL";
    private static final String CLIENTS_IN_BACKUP = "CLIENTS_IN_BACKUP";
    private static final String ORDERS_IN_BACKUP = "ORDERS_IN_BACKUP";

    public UserDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+"("
        +ID+" TEXT ,"
        +NAME+" TEXT ,"
        +EMAIL+" TEXT ,"
        +CLIENTS_IN_BACKUP+" TEXT, "
        +ORDERS_IN_BACKUP+" TEXT );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean InsertIntoTable(String id,String name,String email,String clients_in_backup,String orders_in_backup){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID,id);
        values.put(NAME,name);
        values.put(EMAIL,email);
        values.put(CLIENTS_IN_BACKUP,clients_in_backup);
        values.put(ORDERS_IN_BACKUP,orders_in_backup);
        long result = db.insert(TABLE_NAME,null,values);
        if(result == -1){
            return false;
        }else
            return true;
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result=db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        return result;
    }


    public boolean EditIntoTable(String id,String name,String email,String clients_in_backup,String orders_in_backup){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID,id);
        values.put(NAME,name);
        values.put(EMAIL,email);
        values.put(CLIENTS_IN_BACKUP,clients_in_backup);
        values.put(ORDERS_IN_BACKUP,orders_in_backup);
        long result = db.update(TABLE_NAME,values,"ID = "+id,null);
        if(result == -1){
            return false;
        }else
            return true;
    }

}
