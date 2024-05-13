package com.example.projetsession;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.projetsession.Database.AppDatabase;
import com.example.projetsession.databinding.FragmentSubBinding;
import com.example.projetsession.models.Users;
import com.example.projetsession.repository.UserRepository;
import com.example.projetsession.viewmodels.UsersViewModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SubFragment extends Fragment {

    private FragmentSubBinding binding;

    public SubFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSubBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.goToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment loginFragment = new LoginFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, loginFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        binding.newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstname = binding.firstname.getText().toString();
                String lastname = binding.lastname.getText().toString();
                String username = binding.username.getText().toString();
                String email = binding.email.getText().toString();
                String password = binding.password.getText().toString();
                String numero = binding.number.getText().toString();

                Users newUser = new Users();
                newUser.setFirstname(firstname);
                newUser.setLastname(lastname);
                newUser.setUsername(username);
                newUser.setEmail(email);
                newUser.setPassword(password);
                newUser.setNumero(numero);
                newUser.setRole(1);  // Correctly set the role

                // Use ViewModel to handle database operations
                UsersViewModel viewModel = new ViewModelProvider(getActivity()).get(UsersViewModel.class);
                viewModel.insertUser(newUser, new UserRepository.UserCallback() {
                    @Override
                    public void onSuccess() {
                        getActivity().runOnUiThread(() -> {
                            // Navigate to Login Fragment upon successful insertion
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, new LoginFragment());
                            fragmentTransaction.addToBackStack(null); // Optional: Add to back stack for navigational purposes
                            fragmentTransaction.commit();
                        });
                    }

                    @Override
                    public void onError(Exception e) {
                        // Handle errors possibly by showing an error message
                        Log.e("UserInsert", "Failed to insert user", e);
                    }
                });
            }
        });


    }
}