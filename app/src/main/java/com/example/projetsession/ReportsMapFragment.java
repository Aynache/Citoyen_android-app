package com.example.projetsession;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projetsession.models.Reports;
import com.example.projetsession.viewmodels.ReportsViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Objects;


public class ReportsMapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private final int FINE_PERMISSION_CODE = 1;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;

    private ReportsViewModel reportsViewModel;


    public ReportsMapFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //reportsViewModel = new ViewModelProvider(this).get(ReportsViewModel.class);
        //observeReports();
        return inflater.inflate(R.layout.fragment_reports_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //fusedLocationProviderClient = Location
        // Initialize ViewModel
        reportsViewModel = new ViewModelProvider(this).get(ReportsViewModel.class);

        // After ViewModel is initialized, setup the map fragment and start observing data
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Now it's safe to observe reports
        observeReports();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        loadReportsAndAddMarkers();

        // Add a marker in New York, USA, and move the camera.
        //LatLng newYork = new LatLng(40.7128, -74.0060);
        //mMap.addMarker(new MarkerOptions().position(newYork).title("Marker in New York"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newYork, 12)); // Adjust zoom as needed
    }

    private void loadReportsAndAddMarkers() {
        ReportsViewModel reportsViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(ReportsViewModel.class);
        reportsViewModel.getAllReports().observe(getViewLifecycleOwner(), reports -> {
            if (reports != null) {
                for (int i = 0; i < reports.size(); i++) {
                    Reports report = reports.get(i);
                    geocodeAddressAndAddMarker(report.getAdresse(), report.getName(), i == 0);
                }
            }
        });
    }

    private void geocodeAddressAndAddMarker(String address, String title, boolean isFirstReport) {
        new Thread(() -> {
            Geocoder geocoder = new Geocoder(getContext());
            try {
                List<Address> addresses = geocoder.getFromLocationName(address, 1);
                if (!addresses.isEmpty()) {
                    Address location = addresses.get(0);
                    getActivity().runOnUiThread(() -> {
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(latLng).title(title));
                        if (isFirstReport) {
                            // Move camera to the first report and zoom closer
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10)); // Zoom can be adjusted
                        }
                    });
                }
            } catch (IOException e) {
                Log.e("Geocode", "Failed to geocode address", e);
            }
        }).start();
    }



    private void observeReports() {
        reportsViewModel.getAllReports().observe(getViewLifecycleOwner(), reports -> {
            if (reports != null && !reports.isEmpty()) {
                for (int i = 0; i < reports.size(); i++) {
                    Reports report = reports.get(i);
                    // Pass true if it's the first report to zoom on it
                    geocodeAddressAndAddMarker(report.getAdresse(), report.getName(), i == 0);
                }
            }
        });
    }

}