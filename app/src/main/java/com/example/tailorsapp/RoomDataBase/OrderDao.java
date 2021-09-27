package com.example.tailorsapp.RoomDataBase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.airbnb.lottie.L;

import java.util.List;

@Dao
public interface OrderDao {

    @Query("SELECT * FROM orders_table")
    LiveData<List<Order>> getAllOrders();

    @Insert
    void insertOrder(Order order);

    @Query("DELETE FROM orders_table WHERE ORDER_ID=:order_id")
    void deleteOrderByID(int order_id);

    @Query("SELECT * FROM orders_table WHERE ORDER_ID= :order_id")
    LiveData<Order> getOrderByID(int order_id);

    @Query("DELETE FROM orders_table")
    void deleteAllOrders();

    @Query("DELETE FROM orders_table WHERE CLIENT_ID =:client_id")
    void deleteByClientID(String client_id);

    @Query("UPDATE orders_table SET CLIENT_ID=:client_id ,NAME=:name ,PRICE=:price , TYPE=:type ,DATE_ORDERED=:date_ordered ,DATE_RECEIVED=:date_received ,STATUS=:status ,FURTHER_DETAILS=:further_details WHERE ORDER_ID=:order_id")
    void updateOrder(int order_id,String client_id,String name,String price,String type,String date_ordered,String date_received, String status,String further_details);
}
