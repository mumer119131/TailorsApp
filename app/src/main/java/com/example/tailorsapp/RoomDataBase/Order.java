package com.example.tailorsapp.RoomDataBase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "orders_table")
public class Order {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ORDER_ID")
    public int ORDER_ID = 0;

    @ColumnInfo(name = "CLIENT_ID")
    private String CLIENT_ID;

    @ColumnInfo(name = "NAME")
    private String NAME;

    @ColumnInfo(name ="PRICE")
    private String PRICE;

    @ColumnInfo(name = "TYPE")
    private String TYPE;

    @ColumnInfo(name = "DATE_ORDERED")
    private String DATE_ORDERED;

    @ColumnInfo(name = "DATE_RECEIVED")
    private String DATE_RECEIVED;

    @ColumnInfo(name = "STATUS")
    private String STATUS;

    @ColumnInfo(name = "FURTHER_DETAILS")
    private String FURTHER_DETAILS;


    public Order(String CLIENT_ID, String NAME, String PRICE, String TYPE, String DATE_ORDERED, String DATE_RECEIVED, String STATUS, String FURTHER_DETAILS) {
        this.CLIENT_ID = CLIENT_ID;
        this.NAME = NAME;
        this.PRICE = PRICE;
        this.TYPE = TYPE;
        this.DATE_ORDERED = DATE_ORDERED;
        this.DATE_RECEIVED = DATE_RECEIVED;
        this.STATUS = STATUS;
        this.FURTHER_DETAILS = FURTHER_DETAILS;
    }

    public int getORDER_ID() {
        return ORDER_ID;
    }

    public String getCLIENT_ID() {
        return CLIENT_ID;
    }

    public String getNAME() {
        return NAME;
    }

    public String getPRICE() {
        return PRICE;
    }

    public String getTYPE() {
        return TYPE;
    }

    public String getDATE_ORDERED() {
        return DATE_ORDERED;
    }

    public String getDATE_RECEIVED() {
        return DATE_RECEIVED;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public String getFURTHER_DETAILS() {
        return FURTHER_DETAILS;
    }
}
