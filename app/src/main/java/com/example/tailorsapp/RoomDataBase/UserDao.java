package com.example.tailorsapp.RoomDataBase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM userTable")
    LiveData<List<User>> allUserData();

    @Insert
    void insertUser(User user);

    @Query("DELETE FROM userTable")
    void deleteAllUsers();

    @Query("UPDATE userTable SET CLIENTS_IN_BACKUP=:backupClients ,ORDERS_IN_BACKUP=:backupOrders WHERE UID=:uid")
    void updateUserByUID(String uid,String backupClients,String backupOrders);


}
