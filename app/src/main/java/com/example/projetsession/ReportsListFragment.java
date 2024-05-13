package com.example.projetsession;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.projetsession.adapters.ReportsAdapter;
import com.example.projetsession.models.Reports;
import com.example.projetsession.repository.ReportRepository;
import com.example.projetsession.viewmodels.ReportsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ReportsListFragment extends Fragment {

    private ReportsViewModel reportsViewModel;
    private RecyclerView recyclerView;
    private ReportsAdapter adapter;

    public ReportsListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the ViewModel here to ensure it is done before the fragment's view is created
        reportsViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(ReportsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports_list, container, false);
        setupRecyclerView(view);
        observeReports();
        return view;
    }

    private void setupRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.reports_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ReportsAdapter(new ArrayList<>(), this::onReportLongClicked);
        recyclerView.setAdapter(adapter);
    }

    private void observeReports() {
        // Observe the LiveData, updating the UI any time the data changes
        //write code to get userID form shared preferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int myuserID = sharedPreferences.getInt("USER_ID", 99);
        Log.i("VVVVVVVVV", "User ID: " + myuserID);

        reportsViewModel.getReportsByUserId(myuserID).observe(getViewLifecycleOwner(), reports -> {
            // Update the adapter's dataset
            adapter.setReports(reports);
        });
    }

    public void onReportLongClicked(Reports report) {
        // Call ViewModel to delete the report

    reportsViewModel.deleteReport(report, new ReportRepository.ReportCallback() {
        @Override
        public void onSuccess() {
            getActivity().runOnUiThread(() -> {
                // Possibly update UI or show a toast message
                Toast.makeText(getContext(), "Report deleted successfully", Toast.LENGTH_SHORT).show();
            });
        }

        @Override
        public void onError(Exception e) {
            getActivity().runOnUiThread(() -> {
                // Handle errors possibly by showing an error message
                Toast.makeText(getContext(), "Failed to delete report: " + e.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("ReportDelete", "Failed to delete report", e);
            });
        }
    });

    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupAddReportButton(view);
    }

    private void setupAddReportButton(View view) {
        FloatingActionButton addReportButton = view.findViewById(R.id.addreport_button);
        addReportButton.setOnClickListener(v -> navigateToAddReportFragment());
    }

    private void navigateToAddReportFragment() {
        Fragment addReportFragment = new AddReportFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_citoyen, addReportFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}
