package com.example.projetsession.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.projetsession.models.Agents;
import com.example.projetsession.repository.AgentRepository;

import java.util.List;

public class AgentsViewModel extends AndroidViewModel {

    private AgentRepository agentRepository;
    private LiveData<List<Agents>> allAgents;

    private MutableLiveData<Agents> agentLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> loginFailedLiveData = new MutableLiveData<>();


    // Constructor
    public AgentsViewModel(Application application) {
        super(application);
        agentRepository = new AgentRepository(application);
    }

    // Get all agents
    public LiveData<List<Agents>> getAllAgents() {
        return allAgents;
    }

    // Get agent LiveData
    public MutableLiveData<Agents> getAgentLiveData() {
        return agentLiveData;
    }

    public MutableLiveData<Boolean> getLoginFailedLiveData() {
        return loginFailedLiveData;
    }

    public void login(String username, String password) {
        Agents agents = agentRepository.findAgentByUsernameAndPassword(username, password);
        if (agents != null) {
            agentLiveData.setValue(agents);
        } else {
            loginFailedLiveData.setValue(true);
        }
    }

    public void insertAgent(Agents agents, AgentRepository.AgentCallback callback) {
        agentRepository.insertAgent(agents, callback);
    }

    public void fetchAgentByUsername(String username){
        new Thread(() -> {
            Agents agents = agentRepository.findAgentByUsername(username);
            agentLiveData.postValue(agents);
        }).start();
    }



    public Agents findAgentByUsernameAndPassword(String username, String password) {
        return agentRepository.findAgentByUsernameAndPassword(username, password);
    }

    public Agents findAgentByUsername(String username) {
        return agentRepository.findAgentByUsername(username);
    }

    public Agents findAgentById(int id) {
        return agentRepository.findAgentById(id);
    }



}
