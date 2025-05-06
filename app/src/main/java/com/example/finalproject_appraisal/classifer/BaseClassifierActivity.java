package com.example.finalproject_appraisal.classifer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.viewbinding.ViewBinding;

import com.example.finalproject_appraisal.R;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Base activity class for image classification tasks
 * To be extended by specific classifiers (Kitchen, Doors, etc.)
 */
public abstract class BaseClassifierActivity<T extends ViewBinding> extends AppCompatActivity {
    protected T binding;

    protected String currentPhotoPath;
    protected Uri selectedImageUri;
    protected File photoFile;

    // Gemini API Key
    protected static final String API_KEY = "AIzaSyDhFwyH9JqdiElWGTKMPBnw_fAYxhk5pYo";
    protected GenerativeModelFutures generativeModel;

    // Executor for background tasks
    protected final Executor executor = Executors.newSingleThreadExecutor();

    protected final ActivityResultLauncher<String> cameraPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) openCamera();
                else Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
            });

    protected final ActivityResultLauncher<String> storagePermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) openGallery();
                else Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();

            });

    protected final ActivityResultLauncher<Uri> takePictureLauncher =
            registerForActivityResult(new ActivityResultContracts.TakePicture(), success -> {
                if (success && photoFile != null) {
                    selectedImageUri = Uri.fromFile(photoFile);
                    loadImageFromUri(selectedImageUri);
                }
            });

    protected final ActivityResultLauncher<String> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    loadImageFromUri(selectedImageUri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = createViewBinding();
        setContentView(binding.getRoot());

        // Initialize Gemini model
        generativeModel = GenerativeModelFutures.from(
                new GenerativeModel("gemini-1.5-pro", API_KEY)
        );

        setupListeners();
    }

    /**
     * Create and return the ViewBinding for the specific layout
     */
    protected abstract T createViewBinding();

    /**
     * Setup UI event listeners for the specific classifier
     */
    protected abstract void setupListeners();

    /**
     * Get the bucket name for Google Cloud Storage
     */
    protected abstract String getBucketName();

    /**
     * Get the folder name in the bucket
     */
    protected abstract String getBucketFolderName();

    /**
     * Get the prompt to send to Gemini for classification
     */
    protected abstract String getClassificationPrompt();

    /**
     * Display the classification result in the UI
     */
    protected abstract void displayResult(String result);

    /**
     * Load image from URI into the ImageView
     */
    protected abstract void loadImageFromUri(Uri uri);

    /**
     * Show/hide loading indicators and disable/enable buttons
     */
    protected abstract void showLoading(boolean isLoading);

    protected void openCamera() {
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                    .format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = getExternalFilesDir(null);

            photoFile = File.createTempFile(imageFileName, ".jpg", storageDir);
            currentPhotoPath = photoFile.getAbsolutePath();

            Uri photoURI = FileProvider.getUriForFile(
                    this, getPackageName() + ".fileprovider", photoFile);
            takePictureLauncher.launch(photoURI);
        } catch (IOException e) {
            Toast.makeText(this, "Error creating image file: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    protected void openGallery() {
        pickImageLauncher.launch("image/*");
    }

    protected void uploadImageAndClassify() {
        showLoading(true);
        executor.execute(() -> {
            try {
                uploadImageToGoogleCloudStorage();
                classifyImageWithGemini(selectedImageUri);
            } catch (Exception e) {
                runOnUiThread(() -> {
                    displayResult(e.getMessage());
                    showLoading(false);
                    Toast.makeText(this, R.string.upload_failure, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    protected String uploadImageToGoogleCloudStorage() throws Exception {
        InputStream credStream = getResources().openRawResource(R.raw.service_account);
        GoogleCredentials creds = GoogleCredentials.fromStream(credStream);
        credStream.close();

        Storage storage = StorageOptions.newBuilder()
                .setCredentials(creds).build().getService();

        String fileName = getBucketFolderName() + "_" + UUID.randomUUID() + ".jpg";
        BlobId blobId = BlobId.of(getBucketName(), getBucketFolderName() + "/" + fileName);

        InputStream in = getContentResolver().openInputStream(selectedImageUri);
        Bitmap bmp = BitmapFactory.decodeStream(in);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 80, baos);

        BlobInfo info = BlobInfo.newBuilder(blobId)
                .setContentType("image/jpeg").build();
        storage.create(info, baos.toByteArray());

        return "https://storage.googleapis.com/" + getBucketName() + "/" + getBucketFolderName() + "/" + fileName;
    }

    protected void classifyImageWithGemini(Uri imageUri) {
        if (imageUri == null) {
            runOnUiThread(() -> {
                displayResult("לא נבחרה תמונה");
                showLoading(false);
            });
            return;
        }

        try {
            Bitmap bitmap = "file".equals(imageUri.getScheme())
                    ? BitmapFactory.decodeFile(imageUri.getPath())
                    : MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

            Content content = new Content.Builder()
                    .addImage(bitmap)
                    .addText(getClassificationPrompt())
                    .build();

            ListenableFuture<GenerateContentResponse> future =
                    generativeModel.generateContent(content);

            Futures.addCallback(future, new FutureCallback<GenerateContentResponse>() {
                @Override public void onSuccess(GenerateContentResponse result) {
                    String output = result.getText() != null ?
                            result.getText().trim() :
                            "לא הצלחתי לזהות";
                    runOnUiThread(() -> {
                        displayResult(output);
                        showLoading(false);
                        Toast.makeText(BaseClassifierActivity.this,
                                R.string.upload_success,
                                Toast.LENGTH_SHORT).show();
                    });
                }
                @Override public void onFailure(Throwable t) {
                    runOnUiThread(() -> {
                        displayResult("שגיאה בזיהוי: " + t.getMessage());
                        showLoading(false);
                    });
                }
            }, MoreExecutors.directExecutor());

        } catch (Exception e) {
            runOnUiThread(() -> {
                displayResult("שגיאה בזיהוי: " + e.getMessage());
                showLoading(false);
            });
        }
    }

    protected void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    protected void checkStoragePermission() {
        String perm = Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2
                ? Manifest.permission.READ_EXTERNAL_STORAGE
                : Manifest.permission.READ_MEDIA_IMAGES;

        if (ContextCompat.checkSelfPermission(this, perm)
                == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            storagePermissionLauncher.launch(perm);
        }
    }
}