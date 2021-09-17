package com.example.tailorsapp.RoomDataBase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ClientDao {

    @Query("SELECT * FROM clients_table")
    LiveData<List<Client>> getAllClients();

    @Insert
    void insertClients(Client client);

    @Query("DELETE FROM clients_table WHERE userID=:client_id")
    void deleteClient(int client_id);

    @Query("SELECT * FROM clients_table WHERE userID= :client_id")
    LiveData<Client> getClientByID(int client_id);

    @Query("DELETE FROM clients_table")
    void deleteAllClients();

    @Query("UPDATE CLIENTS_TABLE SET userName =:name ,fathername= :father, phoneNumber= :phone, leg= :leg, arm = :arm, chest=:chest, neck=:neck, frontSide=:front,backSide=:back,date=:date WHERE userID= :userID")
    void updateClientData(int userID,String name,String father, String phone, String leg, String arm, String chest, String neck, String front,String back,String date);


}
