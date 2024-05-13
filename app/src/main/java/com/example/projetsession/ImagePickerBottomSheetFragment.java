package com.example.projetsession;

import android.Manifest;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.example.projetsession.databinding.ImagePickerBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImagePickerBottomSheetFragment extends BottomSheetDialogFragment {

    private ImagePickerBinding binding;

    private ActivityResultLauncher<String[]> permissionsLauncher;
    private ActivityResultLauncher<Uri> cameraLauncher;
    private Uri imageUri;

    private OnImageCapturedListener listener;


    public interface OnImageCapturedListener {
        void onImageCaptured(Uri imageUri);
    }


    public void setOnImageCapturedListener(OnImageCapturedListener listener) {
        this.listener = listener;
    }


    public static ImagePickerBottomSheetFragment newInstance() {
        return new ImagePickerBottomSheetFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissionsLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(), permissions -> {
                    Boolean cameraPermission = permissions.getOrDefault(Manifest.permission.CAMERA, false);
                    if (cameraPermission) {
                        launchCamera();
                    } else {
                        Toast.makeText(getContext(), "Camera permission is required.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
            if (result) {
                handleImageCapture(imageUri); // Notify listener
            } else {
                Toast.makeText(getContext(), "Failed to capture image.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void handleImageCapture(Uri imageUri) {
        if (listener != null) {
            listener.onImageCaptured(imageUri);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ImagePickerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up the click listeners for the camera and photo buttons
        binding.camera.setOnClickListener(v -> {
            if (getContext() != null && getActivity() != null) {
                permissionsLauncher.launch(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE});
            }
        });
    }



    private void launchCamera() {
        // Create a content URI for saving an image
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        // Use MediaStore to save images so no permission is required for access
        imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        // Launch camera intent
        cameraLauncher.launch(imageUri);
    }


    private File createImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    "JPEG_" + timeStamp,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException ex) {
            // Error occurred while creating the File
            Toast.makeText(getContext(), "Failed to create image file.", Toast.LENGTH_SHORT).show();
        }
        return image;
    }
}


