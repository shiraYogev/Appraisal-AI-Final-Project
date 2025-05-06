package com.example.finalproject_appraisal.classifer;


import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.finalproject_appraisal.R;
import com.example.finalproject_appraisal.databinding.ActivityKitchencabinetdetectionBinding;

/**
 * Activity for kitchen cabinet classification using the base classifier
 */
public class KitchenCabinetDetectionActivity extends BaseClassifierActivity<ActivityKitchencabinetdetectionBinding> {

    // Google Cloud Storage bucket name
    private static final String BUCKET_NAME = "kitchen-classifier-bucket-123";
    private static final String BUCKET_FOLDER = "cabinets";

    @Override
    protected ActivityKitchencabinetdetectionBinding createViewBinding() {
        return ActivityKitchencabinetdetectionBinding.inflate(getLayoutInflater());
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
        return "זהה בתמונת המטבח את סוג הארונות " +
                "(עליונים בלבד, תחתונים בלבד, עליונים ותחתונים, אין ארונות) " +
                "וגם את סוג משטח העבודה " +
                "(שיש, עץ, מתכת). " +
                "תן תשובה תמציתית בשתי שורות:\n" +
                "ארונות: <קטגוריה>\n" +
                "משטח עבודה: <קטגוריה>";
    }

    @Override
    protected void displayResult(String result) {
        binding.textViewResult.setText(result);
    }

    @Override
    protected void loadImageFromUri(Uri uri) {
        binding.imageViewKitchen.setImageURI(uri);
    }

    @Override
    protected void showLoading(boolean isLoading) {
        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        binding.buttonClassify.setEnabled(!isLoading);
        binding.buttonGallery.setEnabled(!isLoading);
        binding.buttonTakePhoto.setEnabled(!isLoading);
    }
}