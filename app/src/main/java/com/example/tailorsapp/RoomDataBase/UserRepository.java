package com.example.tailorsapp.RoomDataBase;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class UserRepository {
    private UserDao userDao;
    private LiveData<List<User>> allUserData;

    UserRepository(Application application){
        UserDatabase userDatabase = UserDatabase.getDatabase(application);
        userDao = userDatabase.userDao();
        allUserData = userDao.allUserData();
    }

    LiveData<List<User>> getAllUserData(){
        return allUserData;
    }

    void insertOrder(User user){
        UserDatabase.databaseWriteExecutor.execute(()->{
            userDao.insertUser(user);
        });
    }
    void updateUser(String uid,String backupClients,String backupOrders){
        UserDatabase.databaseWriteExecutor.execute(()->{
            userDao.updateUserByUID(uid,backupClients,backupOrders);
        });
    }
    void deleteAllUserData(){
        UserDatabase.databaseWriteExecutor.execute(()->{
            userDao.deleteAllUsers();
        });
    }
}
