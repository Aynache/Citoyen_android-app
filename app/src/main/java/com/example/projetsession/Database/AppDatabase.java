package com.example.projetsession.Database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.projetsession.dao.AgentsDao;
import com.example.projetsession.dao.ReportsDao;
import com.example.projetsession.dao.UsersDao;
import com.example.projetsession.models.Agents;
import com.example.projetsession.models.Reports;
import com.example.projetsession.models.Users;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Annotate the class to be a Room database, declare all entities and set the version
@Database(entities = {Reports.class, Users.class, Agents.class}, version = 4  , exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private final ExecutorService writingPool = Executors.newSingleThreadExecutor();
    // Creates a single-threaded executor for database write operations to ensure serialized access.
    private final ExecutorService readingPool = Executors.newFixedThreadPool(4);
    // Creates a thread pool with four threads for concurrent read operations, improving read performance.

    // Singleton pattern to prevent having multiple instances of the database opened at the same time.
    private static volatile AppDatabase INSTANCE;

    // Get the database, create if it's not already created
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "my_app_database")
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            // Note: Migration is not part of this basic implementation.
                            .fallbackToDestructiveMigration()
                            // Consider removing allowMainThreadQueries for production apps
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    public void read(Runnable r){
        // Method to submit read operations to the readingPool executor.
        readingPool.submit(r);
        // Submits a runnable task for execution in the reading thread pool.
    }
    public void write(Runnable r){
        // Method to submit write operations to the writingPool executor.
        writingPool.submit(r);
        // Submits a runnable task for execution in the writing single-thread executor.
    }


    public abstract UsersDao getUsersDAO();

    public abstract ReportsDao getreportsDao();

    public abstract AgentsDao getAgentsDAO();
    // Abstract method declaration to provide the DAO for users entities. Room will generate the implementation.
}
