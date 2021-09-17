package com.example.tailorsapp.RoomDataBase;

import android.content.Context;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Client.class},version = 1,exportSchema = false)
public abstract class ClientsDatabases extends RoomDatabase {

    public abstract ClientDao clientDao();
    private static volatile ClientsDatabases INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static ClientsDatabases getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (ClientsDatabases.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ClientsDatabases.class,"client_database")
                            .build();
                }
            }
        }
    return INSTANCE;
    }


}
