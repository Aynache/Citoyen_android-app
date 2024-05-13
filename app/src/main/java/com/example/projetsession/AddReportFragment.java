package com.example.projetsession;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.projetsession.Database.AppDatabase;
import com.example.projetsession.models.Reports;
import com.example.projetsession.models.Users;
import com.example.projetsession.viewmodels.LocationViewModel;
import com.example.projetsession.viewmodels.ReportsViewModel;
import com.example.projetsession.databinding.FragmentAddReportBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AddReportFragment extends Fragment implements ImagePickerBottomSheetFragment.OnImageCapturedListener {

    private GoogleMap mMap;
    private LocationViewModel viewModel;

    private FragmentAddReportBinding binding;
    private ReportsViewModel reportsViewModel; // Declare the ViewModel at the class level

    private String adresseUser;

    private Uri capturedImageUri;


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private ActivityResultLauncher<String> locationPermissionRequest;
    private FusedLocationProviderClient locationClient;



    public AddReportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the ViewModel here
        reportsViewModel = new ViewModelProvider(this).get(ReportsViewModel.class);
        locationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        locationPermissionRequest = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                getLocation();
            } else {
                Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddReportBinding.inflate(inflater, container, false);
        //View view = inflater.inflate(R.layout.fragment_add_report, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Fetch the user data and display it
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        // Get the username from the shared preferences
        String username = sharedPreferences.getString("username", null);

        //Set the current location button
        binding.currentLocationButton.setOnClickListener(v -> {
            checkLocationPermission();
        });

        //Set the image picker button
        binding.imageView.setOnClickListener(v -> {
            showImagePicker();
        });



        if (username != null) {
            fetchUserDataAndDisplay(view, username);
        } else {
            // Handle the case where no username is found
            // Possibly show a login screen or registration prompt
        }



        final EditText nameEditText = view.findViewById(R.id.name);
        final EditText descriptionEditText = view.findViewById(R.id.description);
        final EditText dateEditText = view.findViewById(R.id.date);
        Button newReportButton = view.findViewById(R.id.new_repport_button);



        newReportButton.setOnClickListener(v -> {
            Reports report = new Reports();
            report.setName(nameEditText.getText().toString());
            report.setDescription(descriptionEditText.getText().toString());
            report.setDate(dateEditText.getText().toString());
            report.setAdresse(adresseUser);
            report.setPhoto(capturedImageUri != null ? capturedImageUri.toString() : "path_to_default_image");
            // Get the current user ID and set it
            int userId = sharedPreferences.getInt("USER_ID", -1);
            report.setUserId(userId);
            if (userId != -1) {
                Log.i("MON_USER", "report: " + report.getName() + " " + report.getDescription() + " " + report.getDate() + " " + report.getPhoto() + " " + report.getUserId());
                reportsViewModel.insertReport(report);
                Toast.makeText(getContext(), "Report added successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUserDataAndDisplay(View view, String username) {
        AppDatabase db = AppDatabase.getDatabase(getContext());

        db.read(() -> {
            Users user = db.getUsersDAO().findUserByUsername(username);

            new Handler(Looper.getMainLooper()).post(() -> {
                if (user != null) {
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", user.getUsername());
                    editor.putInt("USER_ID", user.getId());
                    editor.apply();
                } else {
                    // Handle user not found
                    Toast.makeText(getContext(), "User not found", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }



    private final ActivityResultLauncher<String[]> requestPermissionLauncher
            = registerForActivityResult(new ActivityResultContracts
                    .RequestMultiplePermissions()
            , isGranted -> {
                if (isGranted.values().stream().allMatch(g -> g)) {
                    requestUserPosition();
                } else {
                    Toast.makeText(requireContext(), "Permission denied, no way to cotinue, restart!", Toast.LENGTH_SHORT)
                            .show();
                }
            });
    public void requestUserPosition() {

        if (requireActivity().checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED
                || requireActivity().checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissionLauncher.launch(new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION});

            return;
        }
        mMap.setMyLocationEnabled(true);
        viewModel.requestLocation();
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            getLocation();
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    getAddressFromLocation(location);
                } else {
                    Toast.makeText(getContext(), "Failed to get location", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void getAddressFromLocation(Location location) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                adresseUser = address.getAddressLine(0);  // Storing the address in the variable
                Toast.makeText(getContext(), "Address: " + adresseUser, Toast.LENGTH_LONG).show();
                binding.adresse.setText(adresseUser);
            } else {
                adresseUser = "Location not available";  // Default or error message
                Toast.makeText(getContext(), "Address not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            adresseUser = "Location not available";  // Default or error message
            Toast.makeText(getContext(), "Failed to get address", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onImageCaptured(Uri imageUri) {
        if (binding != null) {
            // Load the image into the ImageView using a library like Glide
            Glide.with(this).load(imageUri).into(binding.imageView);
            // Store the URI in the class-level variable
            capturedImageUri = imageUri;
        }
    }

    private void showImagePicker() {
        ImagePickerBottomSheetFragment bottomSheetFragment = ImagePickerBottomSheetFragment.newInstance();
        bottomSheetFragment.setOnImageCapturedListener(this);
        bottomSheetFragment.show(getParentFragmentManager(), bottomSheetFragment.getTag());
    }
}
