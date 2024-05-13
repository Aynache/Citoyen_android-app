package com.example.projetsession.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.example.projetsession.Database.AppDatabase;
import com.example.projetsession.dao.AgentsDao;
import com.example.projetsession.models.Agents;

public class AgentRepository {

    private final AgentsDao daoAgents;

    private final AppDatabase db;

    public AgentRepository(Context context) {
        this.db = AppDatabase.getDatabase(context);
        this.daoAgents = db.getAgentsDAO();
    }

    public LiveData<Agents> getAllAgents() {return daoAgents.getAllAgents();}

    public Agents findAgentByUsernameAndPassword(String username, String password) {
        return daoAgents.findAgentByUsernameAndPassword(username, password);
    }

    public Agents findAgentByUsername(String username) {
        return daoAgents.findAgentByUsername(username);
    }

    public Agents findAgentById(int id) {
        return daoAgents.findAgentById(id);
    }

    public interface AgentCallback {
        void onSuccess();
        void onError(Exception e);
    }

    public void insertAgent(Agents agents, AgentCallback callback) {
        db.write(() -> {
            try {
                daoAgents.insertAgent(agents);
                new Handler(Looper.getMainLooper()).post(callback::onSuccess);
            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onError(e));
            }
        });
    }
}
