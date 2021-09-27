package com.example.tailorsapp.RoomDataBase;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "clients_table")
public class Client {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "userID")
    public int id = 0;
    @ColumnInfo(name = "userName")
    private String name;
    @ColumnInfo(name = "fatherName")
    private String fatherName;
    @ColumnInfo(name = "phoneNumber")
    private String phoneNumber;
    @ColumnInfo(name = "leg")
    private String leg;
    @ColumnInfo(name = "arm")
    private String arm;
    @ColumnInfo(name = "chest")
    private String chest;
    @ColumnInfo(name = "neck")
    private String neck;
    @ColumnInfo(name = "frontSide")
    private String frontSide;
    @ColumnInfo(name = "backSide")
    private String backSide;
    @ColumnInfo(name = "date")
    private String date;

    public Client(String name, String fatherName, String phoneNumber, String leg, String arm, String chest, String neck, String frontSide, String backSide, String date) {

        this.name = name;
        this.fatherName = fatherName;
        this.phoneNumber = phoneNumber;
        this.leg = leg;
        this.arm = arm;
        this.chest = chest;
        this.neck = neck;
        this.frontSide = frontSide;
        this.backSide = backSide;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFatherName() {
        return fatherName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getLeg() {
        return leg;
    }

    public String getArm() {
        return arm;
    }

    public String getChest() {
        return chest;
    }

    public String getNeck() {
        return neck;
    }

    public String getFrontSide() {
        return frontSide;
    }

    public String getBackSide() {
        return backSide;
    }

    public String getDate() {
        return date;
    }
}
