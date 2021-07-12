package com.example.tailorsapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;

public class OrderDataBaseHelper extends SQLiteOpenHelper {

    public  static  final String DATABASE_NAME = "orderSqlDatabase.db";
    public  static  final String TABLE_NAME = "ORDERS" ;
    public  static final String ORDER_ID = "ORDER_ID";
    public  static  final String CLIENT_ID = "ID";
    public  static  final String NAME = "NAME";
    public  static  final String PRICE = "PRICE";
    public  static  final String TYPE = "TYPE";
    public  static  final String DATE_ORDERED = "DATE_ORDERED";
    public  static  final String DATE_RECEIVED = "DATE_RECEIVED";
    public  static  final String STATUS = "STATUS";
    public  static  final String FURTHER_DETAILS = "FURTHER_DETAILS";

    private Context context;

    public OrderDataBaseHelper(@Nullable Context context){
        super(context,DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+"("
         +ORDER_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
        +CLIENT_ID+" INTEGER ,"
        +NAME+" TEXT ,"
        +PRICE+" INTEGER ,"
        +TYPE+" TEXT ,"
        +DATE_ORDERED+" DATE ,"
        +DATE_RECEIVED+" DATE ,"
        +STATUS+" TEXT,"
        +FURTHER_DETAILS+" TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean InsertDataIntoTable(int clientID,String name,String price,String type,String dateOrdered,String dateReceived,String status,String furtherDetails){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(CLIENT_ID,clientID);
        values.put(NAME,name);
        values.put(PRICE,price);
        values.put(TYPE,type);
        values.put(DATE_ORDERED,dateOrdered);
        values.put(DATE_RECEIVED,dateReceived);
        values.put(STATUS,status);
        values.put(FURTHER_DETAILS,furtherDetails);

        long result = db.insert(TABLE_NAME,null,values);
        if(result==-1){
            return false;
        }
        else {
            return true;
        }
    }

    public Cursor GetAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result=db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        return result;
    }

    public Cursor getDataByID(int id){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor result=db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+ORDER_ID+"="+id,null);
        return result;

    }

    public boolean DeleteDataByID(int cli_id){
        String id = Integer.toString(cli_id);
        SQLiteDatabase db=this.getWritableDatabase();
        long result=db.delete(TABLE_NAME,ORDER_ID+"=?",new String[]{id});
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

    public boolean EditDataIntoTable(int orderID,String name,String price,String type,String dateOrdered,String dateReceived,String status,String furtherDetails){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(ORDER_ID,orderID);
        values.put(NAME,name);
        values.put(PRICE,price);
        values.put(TYPE,type);
        values.put(DATE_ORDERED,dateOrdered);
        values.put(DATE_RECEIVED,dateReceived);
        values.put(STATUS,status);
        values.put(FURTHER_DETAILS,furtherDetails);

        long result = db.update(TABLE_NAME,values,"ORDER_ID="+orderID,null);
        if(result==-1){
            return false;
        }
        else {
            return true;
        }
    }

    public Cursor getOrdersCount(String status){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor result=db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+STATUS+"=\""+status+"\"",null);
        return result;
    }


}
