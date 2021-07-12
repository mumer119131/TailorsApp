package com.example.tailorsapp.PersonModel;

public class OrderDataBackupModel {
    private String orderID;
    private String clientID;
    private String name;
    private String type;
    private String price;
    private String dateOrdered;
    private String dateReceived;
    private String status;
    private String furtherDetails;

    public OrderDataBackupModel(String orderID, String clientID, String name, String type, String price, String dateOrdered, String dateReceived, String status, String furtherDetails) {
        this.orderID = orderID;
        this.clientID = clientID;
        this.name = name;
        this.type = type;
        this.price = price;
        this.dateOrdered = dateOrdered;
        this.dateReceived = dateReceived;
        this.status = status;
        this.furtherDetails = furtherDetails;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDateOrdered() {
        return dateOrdered;
    }

    public void setDateOrdered(String dateOrdered) {
        this.dateOrdered = dateOrdered;
    }

    public String getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(String dateReceived) {
        this.dateReceived = dateReceived;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFurtherDetails() {
        return furtherDetails;
    }

    public void setFurtherDetails(String furtherDetails) {
        this.furtherDetails = furtherDetails;
    }
}
