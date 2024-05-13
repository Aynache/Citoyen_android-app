package com.example.projetsession;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
//image_view
public class FileManager {
    private final Context context;
    private Uri currentPath = null;

    public FileManager(Context context) {
        this.context = context;
    }

    public void createNameForImageFile() {
        File image = null;
        try {
            image = File.createTempFile(
                    UUID.randomUUID() + "_",  /* prefix */
                    ".png",                  /* suffix */
                    context.getCacheDir()    /* directory */
                    //getExternalFilesDir("fichiers") /* alternative directory */
            );
            // Creates a temporary file in the cache directory with a unique UUID and .png extension.
            currentPath = FileProvider.getUriForFile(context,
                    context.getPackageName() + ".fileprovider",
                    image);
        } catch (IOException e) {
            currentPath = null;}
    }

    public void initUriForFileName(String fileName) {
        try {
            currentPath = FileProvider.getUriForFile(context,
                    context.getPackageName() + ".fileprovider",
                    new File(context.getCacheDir(), fileName));
        } catch (Exception e) {
            currentPath = null;
        }
    }

    public void setImage(ImageView image_view) {
        Glide.with(context)
                .load(currentPath)
                //.thumbnail(new RequestBuilder<Bi>())
                .error(R.drawable.img_upload)
                .dontTransform()
                .circleCrop()
                .into(image_view);
    }

    public Uri getCurrentPath() {
        return currentPath;
        // Getter method to access the current file URI.
    }
}