package com.example.projetsession.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.projetsession.models.Agents;
import com.example.projetsession.models.Users;

import java.util.List;

@Dao
public interface AgentsDao {

    @Query("SELECT * FROM agents WHERE username = :username LIMIT 1")
    Agents findAgentByUsername(String username);

    @Query("SELECT * FROM agents WHERE id = :id")
    Agents findAgentById(int id);

    @Query("SELECT * FROM agents WHERE username = :username AND password = :password LIMIT 1")
    Agents findAgentByUsernameAndPassword(String username, String password);

    @Query("SELECT * FROM agents")
    LiveData<Agents> getAllAgents();

    @Insert
    void insertAgent(Agents agents);

}
