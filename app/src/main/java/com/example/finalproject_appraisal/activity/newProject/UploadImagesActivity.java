package com.example.finalproject_appraisal.activity.newProject;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalproject_appraisal.R;
import com.example.finalproject_appraisal.model.Image;

public class UploadImagesActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_images);

        // Set up buttons
        setupButton(R.id.btnUploadExterior, Image.Category.EXTERIOR);
        setupButton(R.id.btnUploadLivingRoom, Image.Category.LIVING_ROOM);
        setupButton(R.id.btnUploadKitchen, Image.Category.KITCHEN);
        setupButton(R.id.btnUploadBathroom, Image.Category.BATHROOM);
        setupButton(R.id.btnUploadBedroom, Image.Category.BEDROOM);
        setupButton(R.id.btnUploadView, Image.Category.VIEW);
    }

    private void setupButton(int buttonId, Image.Category category) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(v -> {
            // Here you can add functionality to open the camera or image picker
            openImagePicker();
        });
    }

    private void openImagePicker() {
        // Implementation of image picker logic
    }
}
