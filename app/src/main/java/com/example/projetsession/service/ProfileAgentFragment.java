package com.example.projetsession.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.projetsession.R;
import com.example.projetsession.databinding.FragmentProfileAgentBinding;
import com.example.projetsession.models.Agents;
import com.example.projetsession.viewmodels.AgentsViewModel;

public class ProfileAgentFragment extends Fragment {

    private FragmentProfileAgentBinding binding;

    public AgentsViewModel agentsViewModel;


    public ProfileAgentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileAgentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModel
        agentsViewModel = new ViewModelProvider(this).get(AgentsViewModel.class);

        // Observe the LiveData for agent information
        agentsViewModel.getAgentLiveData().observe(getViewLifecycleOwner(), agent -> {
            if (agent != null) {
                displayAgentData(agent);
            } else {
                // Display a toast message if the agent information is null
                Toast.makeText(getContext(), "Agent information is null", Toast.LENGTH_SHORT).show();
            }
        });

        // Fetch agent information
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);
        if (username != null) {
            agentsViewModel.fetchAgentByUsername(username);
        } else {
            // Display a toast message if the username is null
            Toast.makeText(getContext(), "Username is null", Toast.LENGTH_SHORT).show();
        }

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save the agent information
                saveAgentData();
            }
        });
    }

    private void saveAgentData() {
    }

    private void displayAgentData(Agents agent) {
        binding.firstnameEditText.setText(agent.getFirstname());
        binding.lastnameEditText.setText(agent.getLastname());
        binding.usernameEditText.setText(agent.getUsername());
        binding.passwordEditText.setText(agent.getPassword());
        binding.villeEditText.setText(agent.getVille());

    }
}