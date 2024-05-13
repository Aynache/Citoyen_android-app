package com.example.projetsession.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.example.projetsession.Database.AppDatabase;
import com.example.projetsession.dao.UsersDao;
import com.example.projetsession.models.Users;

import java.util.List;

public class UserRepository {

    private final UsersDao daoUsers;

    private final AppDatabase db;

    // Constructor initializes the UserDao and LiveData lists
    public UserRepository(Context context) {
        db = AppDatabase.getDatabase(context);
        daoUsers = db.getUsersDAO();
    }

    public Users findUserByUsernameAndPassword(String username, String password) {
        return daoUsers.findUserByUsernameAndPassword(username, password); // This must be defined in UserDao
    }

    public Users findUserByUsername(String username) {
        return daoUsers.findUserByUsername(username); // This must be defined in UserDao
    }

    public Users findUserById(int id) {
        return daoUsers.findUserById(id); // This must be defined in UserDao
    }

    // Get all users
    public LiveData<List<Users>> getAllUsers() {
        return daoUsers.getAllUsers();
    }

    // Get basic users
    public LiveData<List<Users>> getBasicUsers() {
        return daoUsers.getBasicUsers();
    }

    // Get admin users
    public LiveData<List<Users>> getAdminUsers() {
        return daoUsers.getAdminUsers();
    }


    public interface UserCallback {
        void onSuccess();
        void onError(Exception e);
    }

    public void insertUser(Users user, UserCallback callback) {
        db.write(() -> {
            try {
                daoUsers.insertUser(user);
                if (callback != null) {
                    new Handler(Looper.getMainLooper()).post(callback::onSuccess);
                }
            } catch (Exception e) {
                if (callback != null) {
                    new Handler(Looper.getMainLooper()).post(() -> callback.onError(e));
                }
            }
        });
    }

    // Update a user
    public void updateUser(Users user, UserCallback callback) {
        db.write(() -> {
            try {
                daoUsers.updateUser(user);
                if (callback != null) {
                    new Handler(Looper.getMainLooper()).post(callback::onSuccess);
                }
            } catch (Exception e) {
                if (callback != null) {
                    new Handler(Looper.getMainLooper()).post(() -> callback.onError(e));
                }
            }
        });
    }

    // Delete a user
    public void deleteUser(Users user, UserCallback callback) {
        db.write(() -> {
            try {
                daoUsers.deleteUser(user);
                if (callback != null) {
                    new Handler(Looper.getMainLooper()).post(callback::onSuccess);
                }
            } catch (Exception e) {
                if (callback != null) {
                    new Handler(Looper.getMainLooper()).post(() -> callback.onError(e));
                }
            }
        });
    }

}