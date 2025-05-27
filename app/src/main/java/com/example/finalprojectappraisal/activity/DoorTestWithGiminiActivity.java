package com.example.finalprojectappraisal.activity;


//import static android.os.Build.VERSION_CODES.R;
import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

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

import com.example.finalprojectappraisal.databinding.ActivityDoortestwithgiminiBinding;
import com.example.finalprojectappraisal.R;
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

public class DoorTestWithGiminiActivity extends AppCompatActivity {
    private ActivityDoortestwithgiminiBinding binding;

    private String currentPhotoPath;
    private Uri selectedImageUri;
    private File photoFile;

    // Google Cloud Storage bucket name
    private static final String BUCKET_NAME = "door-classifier-bucket-123";

    // Gemini API Key
    private static final String API_KEY = "AIzaSyDhFwyH9JqdiElWGTKMPBnw_fAYxhk5pYo";
    private GenerativeModelFutures generativeModel;

    // Executor for background tasks
    private final Executor executor = Executors.newSingleThreadExecutor();

    private final ActivityResultLauncher<String> cameraPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    openCamera();
                } else {
                    Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<String> storagePermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    openGallery();
                } else {
                    Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<Uri> takePictureLauncher =
            registerForActivityResult(new ActivityResultContracts.TakePicture(), success -> {
                if (success && photoFile != null) {
                    selectedImageUri = Uri.fromFile(photoFile);
                    loadImageFromUri(selectedImageUri);
                }
            });

    private final ActivityResultLauncher<String> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    loadImageFromUri(selectedImageUri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoortestwithgiminiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Gemini model
        generativeModel = GenerativeModelFutures.from(
                new GenerativeModel("gemini-1.5-pro", API_KEY)
        );

        setupListeners();

    }

    private void setupListeners() {
        binding.buttonTakePhoto.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
            }
        });

        binding.buttonGallery.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    storagePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            } else {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    storagePermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
                }
            }
        });

        binding.buttonClassify.setOnClickListener(v -> {
            if (selectedImageUri != null) {
                uploadImageAndClassify();
            } else {
                Toast.makeText(this, R.string.no_image_selected, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openCamera() {
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = getExternalFilesDir(null);

            photoFile = File.createTempFile(
                    imageFileName,
                    ".jpg",
                    storageDir
            );

            currentPhotoPath = photoFile.getAbsolutePath();

            Uri photoURI = FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".fileprovider",
                    photoFile
            );

            takePictureLauncher.launch(photoURI);
        } catch (IOException e) {
            Toast.makeText(this, "Error creating image file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void openGallery() {
        pickImageLauncher.launch("image/*");
    }

    private void loadImageFromUri(Uri uri) {
        if (uri != null) {
            binding.imageViewDoor.setImageURI(uri);
        }
    }

    private void uploadImageAndClassify() {
        showLoading(true);

        executor.execute(() -> {
            try {
                String imageUrl = uploadImageToGoogleCloudStorage();
                classifyImageWithGemini(selectedImageUri);
            } catch (Exception e) {
                runOnUiThread(() -> {
                    binding.textViewResult.setText(e.getMessage());
                    showLoading(false);
                    Toast.makeText(getApplicationContext(), R.string.upload_failure, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private String uploadImageToGoogleCloudStorage() throws Exception {
        try {
            // Load credentials from raw resource
            InputStream credentialsStream = getResources().openRawResource(R.raw.service_account);
            GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream);
            credentialsStream.close();

            // Create storage instance
            Storage storage = StorageOptions.newBuilder()
                    .setCredentials(credentials)
                    .build()
                    .getService();

            String fileName = "door_" + UUID.randomUUID() + ".jpg";
            BlobId blobId = BlobId.of(BUCKET_NAME, "doors/" + fileName);

            if (selectedImageUri != null) {
                // Convert image to byte array
                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                byte[] imageBytes = baos.toByteArray();

                // Define file metadata
                BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                        .setContentType("image/jpeg")
                        .build();

                // Upload file
                storage.create(blobInfo, imageBytes);

                // Return download URL
                return "https://storage.googleapis.com/" + BUCKET_NAME + "/doors/" + fileName;
            } else {
                throw new Exception("Failed to process image");
            }
        } catch (Exception e) {
            throw new Exception("GCS Upload failed: " + e.getMessage());
        }
    }

    private void classifyImageWithGemini(Uri imageUri) {
        if (imageUri == null) {
            runOnUiThread(() -> {
                binding.textViewResult.setText("No image selected");
                showLoading(false);
            });
            return;
        }

        try {
            Bitmap bitmap;
            if ("file".equals(imageUri.getScheme())) {
                bitmap = BitmapFactory.decodeFile(imageUri.getPath());
            } else {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            }

            // Create content for Gemini
            Content content = new Content.Builder()
                    .addImage(bitmap)
                    .addText("סווג את הדלת בתמונה לאחת מהקטגוריות הבאות: פלדלת, פלדלת מעוצבת, דמוי עץ, עץ, מעוצבת ממתכת (חרש), טרם הותקן. תן תשובה תמציתית עם שם הקטגוריה בלבד.")
                    .build();

            // Get response from Gemini
            ListenableFuture<GenerateContentResponse> responseFuture =
                    generativeModel.generateContent(content);

            Futures.addCallback(responseFuture, new FutureCallback<GenerateContentResponse>() {
                @Override
                public void onSuccess(GenerateContentResponse result) {
                    String classification = result.getText() != null ?
                            result.getText().trim() : "לא הצלחתי לזהות את סוג הדלת";

                    runOnUiThread(() -> {
                        binding.textViewResult.setText(getString(R.string.classification_result, classification));
                        showLoading(false);
                        Toast.makeText(getApplicationContext(), R.string.upload_success, Toast.LENGTH_SHORT).show();
                    });
                }

                @Override
                public void onFailure(Throwable t) {
                    runOnUiThread(() -> {
                        binding.textViewResult.setText("שגיאה בזיהוי: " + t.getMessage());
                        showLoading(false);
                    });
                }
            }, MoreExecutors.directExecutor());

        } catch (Exception e) {
            runOnUiThread(() -> {
                binding.textViewResult.setText("שגיאה בזיהוי: " + e.getMessage());
                showLoading(false);
            });
        }
    }

    private void showLoading(boolean isLoading) {
        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        binding.buttonClassify.setEnabled(!isLoading);
        binding.buttonGallery.setEnabled(!isLoading);
        binding.buttonTakePhoto.setEnabled(!isLoading);
    }
}