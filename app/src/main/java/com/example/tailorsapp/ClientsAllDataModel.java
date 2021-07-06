package com.example.tailorsapp;

public class ClientsAllDataModel {
    String id;
    String name;
    String fatherName;
    String phoneNumber;
    String leg;
    String Arm;
    String chest;
    String neck;
    String frontSide;
    String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLeg() {
        return leg;
    }

    public void setLeg(String leg) {
        this.leg = leg;
    }

    public String getArm() {
        return Arm;
    }

    public void setArm(String arm) {
        Arm = arm;
    }

    public String getChest() {
        return chest;
    }

    public void setChest(String chest) {
        this.chest = chest;
    }

    public String getNeck() {
        return neck;
    }

    public void setNeck(String neck) {
        this.neck = neck;
    }

    public String getFrontSide() {
        return frontSide;
    }

    public void setFrontSide(String frontSide) {
        this.frontSide = frontSide;
    }

    public String getBackSide() {
        return backSide;
    }

    public void setBackSide(String backSide) {
        this.backSide = backSide;
    }

    public ClientsAllDataModel(String id, String name, String fatherName, String phoneNumber, String leg, String arm, String chest, String neck, String frontSide, String backSide,String date) {
        this.id = id;
        this.name = name;
        this.fatherName = fatherName;
        this.phoneNumber = phoneNumber;
        this.leg = leg;
        Arm = arm;
        this.chest = chest;
        this.neck = neck;
        this.frontSide = frontSide;
        this.backSide = backSide;
        this.date = date;
    }

    String backSide;
}
