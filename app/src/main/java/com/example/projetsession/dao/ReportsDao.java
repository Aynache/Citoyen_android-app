package com.example.projetsession.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.projetsession.models.Reports;

import java.util.List;

@Dao
public interface ReportsDao {
    @Query("SELECT * FROM reports")
    LiveData<List<Reports>> getAllReports();

    @Query("SELECT * FROM reports WHERE userId = :userId")
    LiveData<List<Reports>> findReportsByUserId(int userId);

    @Insert
    long insertReport(Reports reports);

    @Delete
    int deleteReport(Reports reports);

    @Update
    int updateReport(Reports report);  // Also consider returning int here to denote number of rows affected
}
