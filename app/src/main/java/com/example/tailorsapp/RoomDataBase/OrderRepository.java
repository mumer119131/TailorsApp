package com.example.tailorsapp.RoomDataBase;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class OrderRepository {

    private OrderDao orderDao;
    private LiveData<List<Order>> allOrders;

    OrderRepository(Application application){
        OrdersDatabase ordersDatabase = OrdersDatabase.getDatabase(application);
        orderDao = ordersDatabase.orderDao();
        allOrders = orderDao.getAllOrders();
    }

    LiveData<List<Order>> getAllOrders(){
        return allOrders;
    }

    void insertOrder(Order order){
        OrdersDatabase.databaseWriteExecutor.execute(()->{
            orderDao.insertOrder(order);
        });
    }

    LiveData<Order> getOrderByID(int id){

        return orderDao.getOrderByID(id);
    }

    void updateOrderData(int order_id,String client_id,String name,String price,String type,String date_ordered,String date_received, String status,String further_details){
        OrdersDatabase.databaseWriteExecutor.execute(()->{
            orderDao.updateOrder(order_id,client_id,name,price,type,date_ordered,date_received,status,further_details);
        });
    }
    void deleteOrderByID(int id){
        OrdersDatabase.databaseWriteExecutor.execute(()->{
            orderDao.deleteOrderByID(id);
        });
    }
    void deleteAllOrders(){
        OrdersDatabase.databaseWriteExecutor.execute(()->{
            orderDao.deleteAllOrders();
        });
    }
    void deleteByClientID(String client_id){
        OrdersDatabase.databaseWriteExecutor.execute(()->{
            orderDao.deleteByClientID(client_id);
        });
    }
}
