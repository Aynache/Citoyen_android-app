package com.example.projetsession;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

import com.example.projetsession.models.Agents;
import com.example.projetsession.repository.AgentRepository;
import com.example.projetsession.viewmodels.AgentsViewModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            // Prepare a new Agent to insert
            Agents newUser = new Agents();
            newUser.setFirstname("admin");
            newUser.setLastname("admin");
            newUser.setUsername("admin");
            newUser.setPassword("admin");
            newUser.setVille("Trois-Rivières");

            // Use ViewModel to handle database operations
            AgentsViewModel viewModel = new ViewModelProvider(this).get(AgentsViewModel.class);
            viewModel.insertAgent(newUser, new AgentRepository.AgentCallback() {
                @Override
                public void onSuccess() {
                    Log.i("UserInsert", "Agent inserted successfully");
                }

                @Override
                public void onError(Exception e) {
                    Log.e("UserInsert", "Failed to insert agent", e);
                }
            });


            // Initialize the LoginFragment
            LoginFragment loginFragment = new LoginFragment();
            // Perform the fragment transaction to add it to the contabindinginer
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, loginFragment)
                    .commit();
        }
    }
}
