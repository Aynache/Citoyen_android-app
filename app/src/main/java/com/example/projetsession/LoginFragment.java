package com.example.projetsession;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.projetsession.databinding.FragmentLoginBinding;
import com.example.projetsession.models.Agents;
import com.example.projetsession.models.Users;
import com.example.projetsession.viewmodels.AgentsViewModel;
import com.example.projetsession.viewmodels.UsersViewModel;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    private UsersViewModel usersViewModel;

    private AgentsViewModel agentsViewModel;

    public LoginFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usersViewModel = new ViewModelProvider(requireActivity()).get(UsersViewModel.class);
        agentsViewModel = new ViewModelProvider(requireActivity()).get(AgentsViewModel.class);
        usersViewModel.getUserLiveData().observe(this, user -> {
            if (user != null) {
                saveUserData(user);
                navigateToUserFragment();  // Redirect regular users
            }
        });

        agentsViewModel.getAgentLiveData().observe(this, agent -> {
            if (agent != null) {
                saveAgentData(agent);
                navigateToUserFragment();   // Redirect agents
                //navigateToAdminFragment();
            }
        });



        usersViewModel.getLoginFailedLiveData().observe(this, failed -> {
            if (Boolean.TRUE.equals(failed)) {
                Toast.makeText(getContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.goToSubcribeButton.setOnClickListener(v -> replaceFragment(new SubFragment()));
        binding.forgotPasswordButton.setOnClickListener(v -> replaceFragment(new ForgotPasswordFragment()));
        binding.loginButton.setOnClickListener(v -> {
                attemptLoginAgent();
                if (agentsViewModel.getAgentLiveData().getValue() == null) {
                    attemptLogin();
                }
        });
    }

    private void attemptLogin() {
        String username = binding.loginUsername.getText().toString();
        String password = binding.loginPassword.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Username and password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        usersViewModel.login(username, password);
    }

    private void attemptLoginAgent() {
        String username = binding.loginUsername.getText().toString();
        String password = binding.loginPassword.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Username and password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        agentsViewModel.login(username, password);
    }

    private void replaceFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void navigateToAdminFragment() {
        Fragment adminFragment = new ReportsListAgentFragment();  // Admin activity
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, adminFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void navigateToUserFragment() {
        Intent intent = new Intent(getActivity(), CitoyenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    private void saveUserData(Users user) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", user.getUsername());
        editor.putString("USER_ROLE", "user");
        editor.putInt("USER_ID", user.getId());
        editor.apply();
    }

    private void saveAgentData(Agents agent) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", agent.getUsername());
        editor.putString("role", "agent");
        editor.putInt("USER_ID", agent.getId());
        editor.apply();
    }
}
