package com.example.projetsession.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.projetsession.models.Users;
import com.example.projetsession.repository.UserRepository;

import java.util.List;

public class UsersViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    private LiveData<List<Users>> allUsers;
    private LiveData<List<Users>> basicUsers;
    private LiveData<List<Users>> adminUsers;
    private MutableLiveData<Users> userLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> loginFailedLiveData = new MutableLiveData<>();


    // Constructor
    public UsersViewModel(Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    // Get all users
    public LiveData<List<Users>> getAllUsers() {
        return allUsers;
    }

    // Get users LiveData
    public LiveData<Users> getUserLiveData() {
        return userLiveData;
    }


    // Insert a user with callback
    public void insertUser(Users user, UserRepository.UserCallback callback) {
        userRepository.insertUser(user, callback);
    }

    // Update a user with callback
    public void updateUser(Users user, UserRepository.UserCallback callback) {
        userRepository.updateUser(user, callback);
    }

    // Delete a user with callback
    public void deleteUser(Users user, UserRepository.UserCallback callback) {
        userRepository.deleteUser(user, callback);
    }


    public LiveData<Boolean> getLoginFailedLiveData() {
        return loginFailedLiveData;
    }

    public void login(String username, String password) {
        new Thread(() -> {
            Users user = userRepository.findUserByUsernameAndPassword(username, password);
            if (user != null) {
                userLiveData.postValue(user);
            } else {
                loginFailedLiveData.postValue(true);
            }
        }).start();
    }

    public void fetchUserByUsername(String username) {
        new Thread(() -> {
            Users user = userRepository.findUserByUsername(username);
            userLiveData.postValue(user); // Post the user to be observed
        }).start();
    }

}