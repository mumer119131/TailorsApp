package com.example.tailorsapp.RoomDataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Order.class} ,version = 1 ,exportSchema = false)
public abstract class OrdersDatabase extends RoomDatabase {

    public abstract OrderDao orderDao();
    private static volatile OrdersDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static OrdersDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (OrdersDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            OrdersDatabase.class,"order_database")
                            .build();
                }
            }
        }
    return INSTANCE;
    }



}
