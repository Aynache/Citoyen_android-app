package com.example.projetsession.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.projetsession.models.Reports;
import com.example.projetsession.repository.ReportRepository;

import java.util.List;

public class ReportsViewModel extends AndroidViewModel {
    private ReportRepository reportRepository;
    private LiveData<List<Reports>> allReports;
    private MutableLiveData<Boolean> reportOperationStatus;

    public ReportsViewModel(@NonNull Application application) {
        super(application);
        reportRepository = new ReportRepository(application);
        allReports = reportRepository.getAllReports();  // Load all reports on initialization
        reportOperationStatus = new MutableLiveData<>();
    }

    public LiveData<List<Reports>> getAllReports() {
        return allReports;
    }

    public LiveData<List<Reports>> getReportsByUserId(int userId) {
        return reportRepository.getReportsByUserId(userId);
    }

    public MutableLiveData<Boolean> getReportOperationStatus() {
        return reportOperationStatus;
    }

    public void insertReport(Reports report) {
        reportRepository.insertReport(report, new ReportRepository.ReportCallback() {
            @Override
            public void onSuccess() {
                reportOperationStatus.postValue(true);
            }

            @Override
            public void onError(Exception e) {
                reportOperationStatus.postValue(false);
            }
        });
    }

    public void deleteReport(Reports report, ReportRepository.ReportCallback reportCallback) {
        reportRepository.deleteReport(report, new ReportRepository.ReportCallback() {
            @Override
            public void onSuccess() {
                reportOperationStatus.postValue(true);
            }

            @Override
            public void onError(Exception e) {
                reportOperationStatus.postValue(false);
            }
        });
    }

    public void updateReport(Reports report) {
        reportRepository.updateReport(report, new ReportRepository.ReportCallback() {
            @Override
            public void onSuccess() {
                reportOperationStatus.postValue(true);
            }

            @Override
            public void onError(Exception e) {
                reportOperationStatus.postValue(false);
            }
        });
    }
}
