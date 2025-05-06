package com.example.finalproject_appraisal.activity;

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

import com.example.finalproject_appraisal.R;
import com.example.finalproject_appraisal.databinding.ActivityKitchencabinetdetectionBinding;
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

public class KitchenCabinetDetectionActivity extends AppCompatActivity {
    private ActivityKitchencabinetdetectionBinding binding;

    private String currentPhotoPath;
    private Uri selectedImageUri;
    private File photoFile;

    // Google Cloud Storage bucket name
    private static final String BUCKET_NAME = "kitchen-classifier-bucket-123";

    // Gemini API Key
    private static final String API_KEY = "AIzaSyDhFwyH9JqdiElWGTKMPBnw_fAYxhk5pYo";
    private GenerativeModelFutures generativeModel;

    // Executor for background tasks
    private final Executor executor = Executors.newSingleThreadExecutor();

    private final ActivityResultLauncher<String> cameraPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) openCamera();
                else Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
            });

    private final ActivityResultLauncher<String> storagePermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) openGallery();
                else Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
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
        binding = ActivityKitchencabinetdetectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Gemini model
        generativeModel = GenerativeModelFutures.from(
                new GenerativeModel("gemini-1.5-pro", API_KEY)
        );

        setupListeners();
    }

    private void setupListeners() {
        binding.buttonTakePhoto.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
            }
        });

        binding.buttonGallery.setOnClickListener(v -> {
            String perm = Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2
                    ? Manifest.permission.READ_EXTERNAL_STORAGE
                    : Manifest.permission.READ_MEDIA_IMAGES;

            if (ContextCompat.checkSelfPermission(this, perm)
                    == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                storagePermissionLauncher.launch(perm);
            }
        });

        binding.buttonClassify.setOnClickListener(v -> {
            if (selectedImageUri != null) uploadImageAndClassify();
            else Toast.makeText(this, R.string.no_image_selected, Toast.LENGTH_SHORT).show();
        });
    }

    private void openCamera() {
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

    private void openGallery() {
        pickImageLauncher.launch("image/*");
    }

    private void loadImageFromUri(Uri uri) {
        if (uri != null) {
            binding.imageViewKitchen.setImageURI(uri);
        }
    }

    private void uploadImageAndClassify() {
        showLoading(true);
        executor.execute(() -> {
            try {
                uploadImageToGoogleCloudStorage();
                classifyImageWithGemini(selectedImageUri);
            } catch (Exception e) {
                runOnUiThread(() -> {
                    binding.textViewResult.setText(e.getMessage());
                    showLoading(false);
                    Toast.makeText(this, R.string.upload_failure, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private String uploadImageToGoogleCloudStorage() throws Exception {
        InputStream credStream = getResources().openRawResource(R.raw.service_account);
        GoogleCredentials creds = GoogleCredentials.fromStream(credStream);
        credStream.close();

        Storage storage = StorageOptions.newBuilder()
                .setCredentials(creds).build().getService();

        String fileName = "kitchen_" + UUID.randomUUID() + ".jpg";
        BlobId blobId = BlobId.of(BUCKET_NAME, "cabinets/" + fileName);

        InputStream in = getContentResolver().openInputStream(selectedImageUri);
        Bitmap bmp = BitmapFactory.decodeStream(in);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 80, baos);

        BlobInfo info = BlobInfo.newBuilder(blobId)
                .setContentType("image/jpeg").build();
        storage.create(info, baos.toByteArray());

        return "https://storage.googleapis.com/" + BUCKET_NAME + "/cabinets/" + fileName;
    }

    private void classifyImageWithGemini(Uri imageUri) {
        if (imageUri == null) {
            runOnUiThread(() -> {
                binding.textViewResult.setText("לא נבחרה תמונה");
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
                    .addText("זהה בתמונת המטבח את סוג הארונות " +
                            "(עליונים בלבד, תחתונים בלבד, עליונים ותחתונים, אין ארונות) " +
                            "וגם את סוג משטח העבודה " +
                            "(שיש, עץ, מתכת). " +
                            "תן תשובה תמציתית בשתי שורות:\n" +
                            "ארונות: <קטגוריה>\n" +
                            "משטח עבודה: <קטגוריה>")
                    .build();

            ListenableFuture<GenerateContentResponse> future =
                    generativeModel.generateContent(content);

            Futures.addCallback(future, new FutureCallback<GenerateContentResponse>() {
                @Override public void onSuccess(GenerateContentResponse result) {
                    String output = result.getText() != null ?
                            result.getText().trim() :
                            "לא הצלחתי לזהות";
                    runOnUiThread(() -> {
                        binding.textViewResult.setText(output);
                        showLoading(false);
                        Toast.makeText(KitchenCabinetDetectionActivity.this,
                                R.string.upload_success,
                                Toast.LENGTH_SHORT).show();
                    });
                }
                @Override public void onFailure(Throwable t) {
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
