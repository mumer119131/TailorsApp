package com.example.tailorsapp.RoomDataBase;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;

import java.util.List;

public class ClientsRepository {

    private ClientDao clientDao;
    private LiveData<List<Client>> allClients;


    ClientsRepository(Application application){
        ClientsDatabases clientsDatabases = ClientsDatabases.getDatabase(application);
        clientDao = clientsDatabases.clientDao();
        allClients = clientDao.getAllClients();

    }

    LiveData<List<Client>> getAllClients(){
        return allClients;
    }

    void insertClients(Client client){
        ClientsDatabases.databaseWriteExecutor.execute(()->{
            clientDao.insertClients(client);
        });
    }

    LiveData<Client> getClientByID(int id){

            return clientDao.getClientByID(id);
    }

    void updateClientData(int id,String name,String father,String phone,String arm,String leg,String chest,String neck, String front, String back, String date){
        ClientsDatabases.databaseWriteExecutor.execute(()->{
            clientDao.updateClientData(id,name,father,phone,arm,leg,chest,neck,front,back,date);
        });
    }
    void deleteClient(int id){
        ClientsDatabases.databaseWriteExecutor.execute(()->{
            clientDao.deleteClient(id);
        });
    }
    void deleteAllClients(){
        ClientsDatabases.databaseWriteExecutor.execute(()->{
            clientDao.deleteAllClients();
        });
    }

}
