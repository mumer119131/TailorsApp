package com.example.tailorsapp.RoomDataBase;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class UserViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private LiveData<List<User>> allUsers;


    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        allUsers = userRepository.getAllUserData();
    }

    public LiveData<List<User>> getAllUserData(){
        return allUsers;
    }

    public void insertOrder(User user){
        userRepository.insertOrder(user);
    }
    public void deleteAllUserData(){
        userRepository.deleteAllUserData();
    }
    public void updateUser(String uid,String backupClients,String backupOrders){
        userRepository.updateUser(uid,backupClients,backupOrders);
    }
}
