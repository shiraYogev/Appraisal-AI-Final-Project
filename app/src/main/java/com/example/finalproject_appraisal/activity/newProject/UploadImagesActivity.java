package com.example.finalproject_appraisal.activity.newProject;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.finalproject_appraisal.R;
import com.example.finalproject_appraisal.model.Image;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class UploadImagesActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;
    private static final int GALLERY_PERMISSION_REQUEST_CODE = 102;

    private Image.Category selectedCategory;
    // מפת התמונות: לכל קטגוריה רשימה של תמונות
    private HashMap<Image.Category, List<Image>> propertyImagesMap = new HashMap<>();

    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private Uri cameraImageUri;

    // המיכל הראשי (LinearLayout) מה־XML
    private LinearLayout container;
    // מפת מכולות תמונות לכל קטגוריה (UI)
    private HashMap<Image.Category, LinearLayout> imageContainerMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_images);

        // ודאי שה־XML כולל LinearLayout עם id="container"
        container = findViewById(R.id.container);

        setupLaunchers();

        // הגדרת לחצנים עבור כל קטגוריה
        setupButton(R.id.btnUploadExterior, Image.Category.EXTERIOR);
        setupButton(R.id.btnUploadLivingRoom, Image.Category.LIVING_ROOM);
        setupButton(R.id.btnUploadKitchen, Image.Category.KITCHEN);
        setupButton(R.id.btnUploadBathroom, Image.Category.BATHROOM);
        setupButton(R.id.btnUploadBedroom, Image.Category.BEDROOM);
        setupButton(R.id.btnUploadView, Image.Category.VIEW);

        // כפתור Next שמעביר את המפה לאקטיביטי הבא
        Button nextButton = findViewById(R.id.btnNext);
        nextButton.setOnClickListener(v -> {
            Intent intent = new Intent(UploadImagesActivity.this, PropertyDetailsActivity.class);
            intent.putExtra("propertyImagesMap", propertyImagesMap);
            startActivity(intent);
        });
    }

    private void setupLaunchers() {
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        handleImageResult(imageUri, selectedCategory);
                    }
                }
        );

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        handleImageResult(cameraImageUri, selectedCategory);
                    }
                }
        );
    }

    private void setupButton(int buttonId, Image.Category category) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(v -> {
            selectedCategory = category;
            showImageSourceDialog();
        });
    }

    private void showImageSourceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("בחר מקור תמונה")
                .setItems(new CharSequence[]{"גלריה", "מצלמה"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            checkGalleryPermission();
                        } else if (which == 1) {
                            checkCameraPermission();
                        }
                    }
                });
        builder.show();
    }

    protected void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            openCamera();
        }
    }

    protected void checkGalleryPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                        GALLERY_PERMISSION_REQUEST_CODE);
            } else {
                openGallery();
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        GALLERY_PERMISSION_REQUEST_CODE);
            } else {
                openGallery();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "הרשאת מצלמה נדחתה", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == GALLERY_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "הרשאת גלריה נדחתה", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                File photoFile = createImageFile();
                if (photoFile != null) {
                    cameraImageUri = FileProvider.getUriForFile(
                            this,
                            "com.example.finalproject_appraisal.fileprovider", // עדכנו בהתאם לחבילה שלכם
                            photoFile
                    );
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    cameraLauncher.launch(takePictureIntent);
                }
            } catch (IOException ex) {
                Log.e("Camera", "שגיאה ביצירת קובץ תמונה: " + ex.getMessage());
                Toast.makeText(this, "שגיאה ביצירת קובץ תמונה", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "אין אפליקציית מצלמה זמינה", Toast.LENGTH_SHORT).show();
        }
    }

    protected void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        galleryLauncher.launch(galleryIntent);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "PROJECT_IMG_" + selectedCategory.toString() + "_" + timeStamp;
        File storageDir = getExternalFilesDir("MyProjectImages");
        if (storageDir != null && !storageDir.exists()) {
            storageDir.mkdirs();
        }
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    private void handleImageResult(Uri imageUri, Image.Category category) {
        if (imageUri != null) {
            // הוספת תמונה למפה: לכל קטגוריה רשימה של תמונות
            Image image = new Image(imageUri.toString(), category, "PROJECT_ID", "", new ArrayList<>(), false);
            List<Image> imagesForCategory = propertyImagesMap.get(category);
            if (imagesForCategory == null) {
                imagesForCategory = new ArrayList<>();
                propertyImagesMap.put(category, imagesForCategory);
            }
            imagesForCategory.add(image);

            // עדכון ה-UI: הוספת ImageView חדש למכולת התמונות של הקטגוריה
            addImageViewForCategory(category, imageUri);
        } else {
            Toast.makeText(this, "לא נבחרה תמונה", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * מוסיפה ImageView חדש עם התמונה ל-UI עבור הקטגוריה.
     * אם עבור הקטגוריה לא קיימת מכולה, נוצרה אחת ונוספת מתחת לכפתור המתאים.
     */
    private void addImageViewForCategory(Image.Category category, Uri imageUri) {
        LinearLayout categoryContainer = imageContainerMap.get(category);
        // אם המכולה לא קיימת, נוצרה חדשה
        if (categoryContainer == null) {
            categoryContainer = new LinearLayout(this);
            categoryContainer.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            containerParams.topMargin = 8;
            categoryContainer.setLayoutParams(containerParams);

            // מציאת כפתור ההעלאה עבור הקטגוריה והוספת המכולה מתחתיו
            int buttonId = getButtonIdForCategory(category);
            if (buttonId != 0) {
                Button button = findViewById(buttonId);
                int index = container.indexOfChild(button);
                container.addView(categoryContainer, index + 1);
            } else {
                container.addView(categoryContainer);
            }
            imageContainerMap.put(category, categoryContainer);
        }

        // יצירת holder (CardView) ל-ImageView
        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                300, // רוחב קבוע לדוגמה, ניתן לשנות או להשתמש ב-wrap_content
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(4, 4, 4, 4);
        cardView.setLayoutParams(cardParams);
        cardView.setRadius(8);
        cardView.setCardElevation(4);

        ImageView imageView = new ImageView(this);
        FrameLayout.LayoutParams imageParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        imageView.setLayoutParams(imageParams);
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageURI(imageUri);

        cardView.addView(imageView);
        // הוספת ה-cardView למכולת הקטגוריה
        categoryContainer.addView(cardView);
    }

    private int getButtonIdForCategory(Image.Category category) {
        switch (category) {
            case EXTERIOR:
                return R.id.btnUploadExterior;
            case LIVING_ROOM:
                return R.id.btnUploadLivingRoom;
            case KITCHEN:
                return R.id.btnUploadKitchen;
            case BATHROOM:
                return R.id.btnUploadBathroom;
            case BEDROOM:
                return R.id.btnUploadBedroom;
            case VIEW:
                return R.id.btnUploadView;
            default:
                return 0;
        }
    }
}
