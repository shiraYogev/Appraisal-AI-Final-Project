package com.example.finalprojectappraisal.classifer;

import android.net.Uri;
import android.view.View;
import com.example.finalprojectappraisal.databinding.ActivityFlooringDetectionBinding;

/**
 * Activity for flooring detection and classification
 */
public class FlooringDetectionActivity
        extends BaseClassifierActivity<ActivityFlooringDetectionBinding> {

    // Google Cloud Storage bucket name & folder
    private static final String BUCKET_NAME   = "flooring-classifier-bucket";
    private static final String BUCKET_FOLDER = "flooring";

    @Override
    protected ActivityFlooringDetectionBinding createViewBinding() {
        return ActivityFlooringDetectionBinding.inflate(getLayoutInflater());
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
        return "ריצוף – זיהוי סוג (" +
                "אבן טבעית, שיש, טרצו, גרניט, קרמיקה, פסיפס, פורצלן גרניט, אריחי בטון, " +
                "בטון מוחלק, פרקט, p.v.c, פרלטו, פורצלן דמוי פרקט, אחר, טרם רוצף" +
                ")\n" +
                "וגם מידות (" +
                "120X120, 100X100, 90X90, 80X80, 60X60, 50X50, 45X45, 40X40, 30X30, 45X90, 60X120, 22X90, 20X120" +
                ")\n" +
                "תשובה תמציתית בשתי שורות:\n" +
                "סוג: <קטגוריה>\n" +
                "מידה: <ערך>";
    }

    @Override
    protected void displayResult(String result) {
        binding.textViewResult.setText(result);
    }

    @Override
    protected void loadImageFromUri(Uri uri) {
        binding.imageViewFlooring.setImageURI(uri);
    }

    @Override
    protected void showLoading(boolean isLoading) {
        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        binding.buttonClassify.setEnabled(!isLoading);
        binding.buttonGallery.setEnabled(!isLoading);
        binding.buttonTakePhoto.setEnabled(!isLoading);
    }
}
