package com.example.tailorsapp.PersonModel;

public class OrderDisplayModel {
    private String name,type,price,dateOrdered,dateReceived,indexNo,status,furtherDetails;

    public OrderDisplayModel(String name, String type, String price, String dateOrdered, String dateReceived, String indexNo,String status,String furtherDetails) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.dateOrdered = dateOrdered;
        this.dateReceived = dateReceived;
        this.indexNo = indexNo;
        this.status = status;
        this.furtherDetails = furtherDetails;
    }

    public String getFurtherDetails() {
        return furtherDetails;
    }

    public void setFurtherDetails(String furtherDetails) {
        this.furtherDetails = furtherDetails;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getIndexNo() {
        return indexNo;
    }

    public void setIndexNo(String indexNo) {
        this.indexNo = indexNo;
    }
}
