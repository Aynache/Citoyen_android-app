package com.example.projetsession;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.projetsession.Database.AppDatabase;
import com.example.projetsession.databinding.FragmentProfileBinding;
import com.example.projetsession.models.Users;
import com.example.projetsession.repository.UserRepository;
import com.example.projetsession.viewmodels.UsersViewModel;

import java.util.concurrent.atomic.AtomicReference;

public class ProfileFragment extends Fragment  {

    private FragmentProfileBinding binding;

    public UsersViewModel usersViewModel;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModel
        usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);

        // Observe the LiveData for user information
        usersViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                displayUserData(user);
            } else {
                Toast.makeText(getContext(), "User not found", Toast.LENGTH_SHORT).show();
                // Handle redirection to login or registration
            }
        });

        // Fetch the user data
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);
        if (username != null) {
            usersViewModel.fetchUserByUsername(username);
        } else {
            Toast.makeText(getContext(), "No username found. Please log in.", Toast.LENGTH_LONG).show();
            // Redirect to login or registration
        }

        binding.saveButton.setOnClickListener(v -> saveUserData());
    }

    private void displayUserData(Users user) {
        binding.passwordEditText.setText(user.getPassword());
        binding.firstnameEditText.setText(user.getFirstname());
        binding.lastnameEditText.setText(user.getLastname());
        binding.usernameEditText.setText(user.getUsername());
        binding.emailEditText.setText(user.getEmail());
        binding.numeroEditText.setText(user.getNumero());
    }

    private void saveUserData() {
        Users user = new Users();
        user.setPassword(binding.passwordEditText.getText().toString());
        user.setFirstname(binding.firstnameEditText.getText().toString());
        user.setLastname(binding.lastnameEditText.getText().toString());
        user.setUsername(binding.usernameEditText.getText().toString());
        user.setEmail(binding.emailEditText.getText().toString());
        user.setNumero(binding.numeroEditText.getText().toString());

        // Update user data in database through ViewModel
        usersViewModel.updateUser(user, new UserRepository.UserCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(), "User updated successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(getContext(), "Error updating user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}