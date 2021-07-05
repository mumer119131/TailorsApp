package com.example.tailorsapp;

public class ClientModel {
    private String userName;
    private String ID;

    public ClientModel(String userName, String ID) {
        this.userName = userName;
        this.ID = ID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
