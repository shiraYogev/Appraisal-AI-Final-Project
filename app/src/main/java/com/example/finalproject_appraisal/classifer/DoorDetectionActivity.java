package com.example.finalproject_appraisal.classifer;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.finalproject_appraisal.R;
import com.example.finalproject_appraisal.databinding.ActivityDoordetectionBinding;

/**
 * Activity for door detection and classification
 */
public class DoorDetectionActivity extends BaseClassifierActivity<ActivityDoordetectionBinding> {

    // Google Cloud Storage bucket name
    private static final String BUCKET_NAME = "door-classifier-bucket-123";
    private static final String BUCKET_FOLDER = "doors";

    @Override
    protected ActivityDoordetectionBinding createViewBinding() {
        return ActivityDoordetectionBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setupListeners() {
        binding.buttonTakePhoto.setOnClickListener(v -> checkCameraPermission());
        binding.buttonGallery.setOnClickListener(v -> checkStoragePermission());
        binding.buttonClassify.setOnClickListener(v -> {
            if (selectedImageUri != null) uploadImageAndClassify();
            else displayResult("לא נבחרה תמונה");
        });
    }

    @Override
    protected String getBucketName() {
        return BUCKET_NAME;
    }

    @Override
    protected String getBucketFolderName() {
        return BUCKET_FOLDER;
    }

    @Override
    protected String getClassificationPrompt() {
        return "זהה בתמונה את סוג הדלת " +
                "(פנימית, חיצונית, ביטחון, הזזה) " +
                "וגם את חומר הדלת " +
                "(עץ, מתכת, זכוכית, פלסטיק). " +
                "תן תשובה תמציתית בשתי שורות:\n" +
                "סוג דלת: <קטגוריה>\n" +
                "חומר: <קטגוריה>";
    }

    @Override
    protected void displayResult(String result) {
        binding.textViewResult.setText(result);
    }

    @Override
    protected void loadImageFromUri(Uri uri) {
        binding.imageViewDoor.setImageURI(uri);
    }

    @Override
    protected void showLoading(boolean isLoading) {
        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        binding.buttonClassify.setEnabled(!isLoading);
        binding.buttonGallery.setEnabled(!isLoading);
        binding.buttonTakePhoto.setEnabled(!isLoading);
    }
}