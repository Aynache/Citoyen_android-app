package com.example.projetsession.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.model.Marker;
//import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;




//import retrofit2.Call;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;

public class LocationViewModel extends AndroidViewModel {
    private final FusedLocationProviderClient flp;
    private final MutableLiveData<Location> currentLocation;
    // Member variables for handling location and weather API, along with LiveData for reactivity.

    private ExecutorService singleThread = Executors.newSingleThreadExecutor();
    private MutableLiveData<Marker> currentMarker;
    // Executor for background tasks and LiveData for current marker details.

    public LocationViewModel(@NonNull Application application) {
        super(application);
        // Initializes the WeatherAPI using Retrofit with Gson converter.

        flp = LocationServices.getFusedLocationProviderClient(application);
        // Gets an instance of the Fused Location Provider Client for managing location updates.

        currentLocation = new MutableLiveData<>();
        currentMarker = new MutableLiveData<>();
        // Initializes the MutableLiveData objects.

    }

    public LiveData<Location> currentLocation() {
        return currentLocation;
        // Provides public access to the current location LiveData.
    }

    //Explain what this metod does
    //


    @Override
    protected void onCleared() {
        flp.removeLocationUpdates(callback);
        // Cleans up by removing location updates when the ViewModel is no longer used.
        super.onCleared();
    }

    private final LocationCallback callback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            Location loc = locationResult.getLastLocation();
            // Receives location updates.
            if (loc != null) {
                currentLocation.postValue(loc);
                // Updates the currentLocation LiveData.
            }
        }

        @Override
        public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {
            if (!locationAvailability.isLocationAvailable()) {
                currentLocation.postValue(null);
                // Updates the LiveData to null if location is no longer available.
            }
        }
    };

    @SuppressLint("MissingPermission")
    public void requestLocation() {
        flp.getLastLocation().addOnSuccessListener(location -> {
            currentLocation.postValue(location);
            // Posts the last known location to the LiveData.
        });
        LocationRequest request = new LocationRequest.Builder(1000L)
                .setIntervalMillis(1000)
                .setMinUpdateDistanceMeters(10000)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .build();
        // Builds a location request with high accuracy and frequent updates.

        flp.requestLocationUpdates(request, callback, Looper.getMainLooper());
        // Requests location updates with the specified settings.
    }

    LiveData<Marker> getLiveMarker() {
        return currentMarker;
        // Provides access to the LiveData for the current marker.
    }

    public boolean setCurrentMarker(Marker marker) {
        currentMarker.postValue(marker);
        // Updates the current marker.
        return false;
    }
}

