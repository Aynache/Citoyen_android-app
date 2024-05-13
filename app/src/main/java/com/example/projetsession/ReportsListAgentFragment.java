package com.example.projetsession;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.projetsession.adapters.ReportsAdapter;
import com.example.projetsession.databinding.FragmentAddReportBinding;
import com.example.projetsession.databinding.FragmentReportsListAgentBinding;
import com.example.projetsession.models.Reports;
import com.example.projetsession.viewmodels.ReportsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ReportsListAgentFragment extends Fragment {

    private FragmentReportsListAgentBinding binding;

    private ReportsViewModel reportsViewModel;

    private RecyclerView recyclerView;
    private ReportsAdapter adapter;

    public ReportsListAgentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reportsViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(ReportsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports_list, container, false);
        //binding = FragmentReportsListAgentBinding.inflate(inflater, container, false);
        setupRecyclerView(view);
        observeReports();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton buttonAddReport = (FloatingActionButton) view.findViewById(R.id.addreport_button);
        buttonAddReport.setVisibility(View.GONE);
    }

    private void setupRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.reports_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ReportsAdapter(new ArrayList<>(), this::onReportLongClicked);
        recyclerView.setAdapter(adapter);
    }

    private void onReportLongClicked(Reports reports) {
    }

    private void observeReports() {
        // Observe the LiveData, updating the UI any time the data changes
        reportsViewModel.getAllReports().observe(getViewLifecycleOwner(), reports -> {
            // Update the adapter's dataset
            adapter.setReports(reports);
        });
    }
}