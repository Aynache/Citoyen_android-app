package com.example.projetsession.repository;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.example.projetsession.Database.AppDatabase;
import com.example.projetsession.dao.ReportsDao;
import com.example.projetsession.models.Reports;

import java.util.List;

public class ReportRepository {

    private final AppDatabase db;
    private final ReportsDao reportsDao;
    private final LiveData<List<Reports>> allReports;
    private final Context context;

    public ReportRepository(Application application) {
        db = AppDatabase.getDatabase(application);
        reportsDao = db.getreportsDao();
        allReports = reportsDao.getAllReports();
        this.context = application.getApplicationContext();
    }

    public LiveData<List<Reports>> getAllReports() {
        return allReports;
    }

    public LiveData<List<Reports>> getReportsByUserId(int userId) {
        return reportsDao.findReportsByUserId(userId);
    }


    public interface ReportCallback {
        void onSuccess();
        void onError(Exception e);}

    public void insertReport(Reports report, ReportCallback callback) {
        db.write(() -> {
            try {
                reportsDao.insertReport(report);
                if (callback != null) {
                    new Handler(Looper.getMainLooper()).post(callback::onSuccess);
                }
            } catch (Exception e) {
                if (callback != null) {
                    new Handler(Looper.getMainLooper()).post(() -> callback.onError(e));
                }
            }
        });
    }


    public void deleteReport(Reports report, ReportCallback callback) {
        db.write(() -> {
            try {
                reportsDao.deleteReport(report);
                if (callback != null) {
                    new Handler(Looper.getMainLooper()).post(callback::onSuccess);
                }
            } catch (Exception e) {
                if (callback != null) {
                    //new Handler(Looper.getMainLooper()).post(() -> callback.onError(e));
                    new Handler(Looper.getMainLooper()).post(() -> callback.onError(e));
                }
            }
        });
    }

    public void updateReport(Reports report, ReportCallback callback) {
        db.write(() -> {
            try {
                reportsDao.updateReport(report);
                if (callback != null) {
                    new Handler(Looper.getMainLooper()).post(callback::onSuccess);
                }
            } catch (Exception e) {
                if (callback != null) {
                    new Handler(Looper.getMainLooper()).post(() -> callback.onError(e));
                }
            }
        });
    }

}

