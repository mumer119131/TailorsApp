package com.example.tailorsapp.RoomDataBase;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "userTable")
public class User {

    @PrimaryKey
    @NonNull
    private String UID;
    private String NAME;
    private String EMAIL;
    private String CLIENTS_IN_BACKUP;
    private String ORDERS_IN_BACKUP;

    public User(String UID, String NAME, String EMAIL, String CLIENTS_IN_BACKUP, String ORDERS_IN_BACKUP) {
        this.UID = UID;
        this.NAME = NAME;
        this.EMAIL = EMAIL;
        this.CLIENTS_IN_BACKUP = CLIENTS_IN_BACKUP;
        this.ORDERS_IN_BACKUP = ORDERS_IN_BACKUP;
    }

    public String getUID() {
        return UID;
    }

    public String getNAME() {
        return NAME;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public String getCLIENTS_IN_BACKUP() {
        return CLIENTS_IN_BACKUP;
    }

    public String getORDERS_IN_BACKUP() {
        return ORDERS_IN_BACKUP;
    }
}
