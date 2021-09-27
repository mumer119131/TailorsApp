package com.example.tailorsapp.RoomDataBase;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class OrderViewModel extends AndroidViewModel {

    private OrderRepository orderRepository;
    private final LiveData<List<Order>> allOrders;


    public OrderViewModel(@NonNull Application application) {
        super(application);
        orderRepository = new OrderRepository(application);
        allOrders = orderRepository.getAllOrders();
    }

    public LiveData<List<Order>> getAllOrders(){

        return allOrders;
    }

    public void insertOrder(Order order){
        orderRepository.insertOrder(order);
    }

    public LiveData<Order> getOrderByID(int id){
           return orderRepository.getOrderByID(id);

    }

    public void updateOrderData(int order_id,String client_id,String name,String price,String type,String date_ordered,String date_received, String status,String further_details){
        orderRepository.updateOrderData(order_id,client_id,name,price,type,date_ordered,date_received,status,further_details);
    }
    public void deleteOrderByID(int id){
        orderRepository.deleteOrderByID(id);
    }
    public void deleteAllOrders(){
        orderRepository.deleteAllOrders();
    }
    public void deleteByClientID(String client_id){
        orderRepository.deleteByClientID(client_id);
    }
}
