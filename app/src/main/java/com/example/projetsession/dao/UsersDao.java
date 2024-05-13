package com.example.projetsession.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.projetsession.models.Reports;
import com.example.projetsession.models.Users;

import java.util.List;

@Dao
public interface UsersDao {

    @Query("SELECT * FROM users WHERE role = 1")
    LiveData<List<Users>> getBasicUsers();

    @Query("SELECT * FROM users WHERE role = 2")
    LiveData<List<Users>> getAdminUsers();

    @Query("SELECT * FROM users")
    LiveData<List<Users>> getAllUsers();

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    Users findUserByUsername(String username);

    @Query("SELECT * FROM users WHERE id = :id")
    Users findUserById(int id);

    @Query("SELECT * FROM users WHERE email = :email")
    Users findUserByEmail(String email);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    Users findUserByUsernameAndPassword(String username, String password);

    @Insert
    void insertUser(Users users);

    @Delete
    void deleteUser(Users users);

    @Update
    void updateUser(Users users);

}
