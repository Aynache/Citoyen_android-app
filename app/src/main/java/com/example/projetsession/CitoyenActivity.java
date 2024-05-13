package com.example.projetsession;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.projetsession.databinding.ActivityCitoyenBinding;
import com.example.projetsession.service.ProfileAgentFragment;
import com.example.projetsession.viewmodels.AgentsViewModel;
import com.example.projetsession.viewmodels.UsersViewModel;

public class CitoyenActivity extends AppCompatActivity {

    private ActivityCitoyenBinding binding; // Binding object
    public UsersViewModel usersViewModel;
    public AgentsViewModel agentsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCitoyenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = this.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String role = sharedPreferences.getString("role", null);
        String username = sharedPreferences.getString("username", null);

        if (role.equals("user")) {
            setupUserInterface(); // Setup UI for user
        } else if (role.equals("agent")) {
            setupAgentInterface(); // Setup UI for agent
        }
    }

    private void setupUserInterface() {
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container_citoyen) == null) {
            displayFragment(new ReportsListFragment());
        }

        binding.buttonProfil.setOnClickListener(v -> displayFragment(new ProfileFragment()));
        binding.buttonMap.setOnClickListener(v -> displayFragment(new ReportsMapFragment()));
        binding.buttonReports.setOnClickListener(v -> displayFragment(new ReportsListFragment()));
    }

    private void setupAgentInterface() {
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container_citoyen) == null) {
            displayFragment(new ReportsListFragment());
        }

        binding.buttonProfil.setOnClickListener(v -> displayFragment(new ProfileAgentFragment()));
        binding.buttonMap.setOnClickListener(v -> displayFragment(new ReportsMapFragment()));
        binding.buttonReports.setOnClickListener(v -> displayFragment(new ReportsListAgentFragment()));
    }

    private void displayFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_citoyen, fragment)
                .commit();
    }
}
