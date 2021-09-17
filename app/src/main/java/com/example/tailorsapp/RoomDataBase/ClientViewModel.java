package com.example.tailorsapp.RoomDataBase;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class ClientViewModel extends AndroidViewModel {

    private ClientsRepository clientsRepository;
    private final LiveData<List<Client>> allClients;


    public ClientViewModel(@NonNull Application application) {
        super(application);
        clientsRepository = new ClientsRepository(application);
        allClients = clientsRepository.getAllClients();
    }

    public LiveData<List<Client>> getAllClients(){
        return allClients;
    }

    public void insertClients(Client client){
        clientsRepository.insertClients(client);
    }

    public LiveData<Client> getClientByID(int id){
        return clientsRepository.getClientByID(id);
    }

    public void updateClientData(int id,String name,String father,String phone,String arm,String leg,String chest,String neck, String front, String back, String date){
        clientsRepository.updateClientData(id,name,father,phone,arm,leg,chest,neck,front,back,date);
    }
    public void deleteClient(int id){
        clientsRepository.deleteClient(id);
    }
    public void deleteAllClients(){
        clientsRepository.deleteAllClients();
    }
}
