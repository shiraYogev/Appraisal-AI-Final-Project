package com.example.finalprojectappraisal.activity.newProject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finalprojectappraisal.R;
import com.example.finalprojectappraisal.classifer.gemini.GeminiPrompts;
import com.example.finalprojectappraisal.database.ProjectRepository;
import com.example.finalprojectappraisal.model.Image;
import com.example.finalprojectappraisal.classifer.ImageCategorySection;
import com.example.finalprojectappraisal.classifer.gemini.GeminiHelper;
import com.example.finalprojectappraisal.adapter.ImageCategoriesAdapter;
import java.util.Arrays;
import java.util.List;

public class UploadImagesActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 101;

    private List<ImageCategorySection> categories;
    private ImageCategoriesAdapter categoriesAdapter;
    private ImageCategorySection pendingSection; // איזו קטגוריה ממתינה להוספת תמונה
    private String projectId; // חשוב! יש להעביר אותו מ-Intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_images);

        // קבלת projectId מה-Intent
        projectId = getIntent().getStringExtra("projectId");
        if (projectId == null) {
            Toast.makeText(this, "לא נמצא מזהה פרויקט", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        categories = Arrays.asList(
                new ImageCategorySection("דלת כניסה", Image.Category.ENTRANCE_DOOR, GeminiPrompts.ENTRANCE_DOOR_PROMPT),
                new ImageCategorySection("מטבח", Image.Category.KITCHEN, GeminiPrompts.KITCHEN_PROMPT),
                new ImageCategorySection("סלון", Image.Category.LIVING_ROOM, GeminiPrompts.LIVING_ROOM_PROMPT),
                new ImageCategorySection("חזית", Image.Category.EXTERIOR, "זהה מצב חזית הבית..."),
                new ImageCategorySection("חדר רחצה", Image.Category.BATHROOM, GeminiPrompts.BATHROOM_PROMPT),
                new ImageCategorySection("חדר שינה", Image.Category.BEDROOM, GeminiPrompts.BEDROOM_PROMPT),
                new ImageCategorySection("נוף", Image.Category.VIEW, "זהה את הנוף מהדירה...")
        );

        RecyclerView recyclerCategories = findViewById(R.id.recyclerCategories);
        recyclerCategories.setLayoutManager(new LinearLayoutManager(this));
        categoriesAdapter = new ImageCategoriesAdapter(categories, this, section -> {
            // בלחיצה על הוספת תמונה — פותח גלריה
            pendingSection = section;
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_IMAGE_PICK);
        });
        recyclerCategories.setAdapter(categoriesAdapter);

        // כפתור שמירה והמשך
        Button btnSaveAndContinue = findViewById(R.id.btnSaveAndContinue);
        btnSaveAndContinue.setOnClickListener(v -> {
            boolean imageAdded = false;
            for (ImageCategorySection section : categories) {
                for (Image img : section.images) {
                    if (img == null) continue;
                    if (img.getUrl() == null || img.getCategory() == null) continue;
                    ProjectRepository.getInstance().addImageToProject(projectId, img, task -> {});
                    imageAdded = true;
                }
            }
            if (!imageAdded) {
                Toast.makeText(this, "לא נוספה אף תמונה!", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                Intent intent = new Intent(UploadImagesActivity.this, PropertyDetailsActivity.class);
                intent.putExtra("projectId", projectId);
                startActivity(intent);
                finish();
            } catch (Exception e) {
                Toast.makeText(this, "שגיאה במעבר: " + e.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("UploadImagesActivity", "שגיאה במעבר אקטיביטי", e);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            Image img = new Image();
            img.setUrl(imageUri.toString());
            img.setCategory(pendingSection.category);
            img.setDescription("מסווג תמונה, אנא המתן..."); // הודעה ברורה

            // הוסף את התמונה לקטגוריה המתאימה
            pendingSection.images.add(img);

            // עדכון UI מיידי
            int categoryIndex = categories.indexOf(pendingSection);
            if (categoryIndex != -1) {
                categoriesAdapter.notifyImageChanged(categoryIndex);
            }

            // סיווג עם Gemini
            GeminiHelper.classifyImage(this, imageUri, pendingSection.prompt, new GeminiHelper.ClassificationCallback() {
                @Override
                public void onResult(String result) {
                    runOnUiThread(() -> {
                        img.setDescription(result);
                        int categoryIndex = categories.indexOf(pendingSection);
                        if (categoryIndex != -1) categoriesAdapter.notifyImageChanged(categoryIndex);
                        Toast.makeText(UploadImagesActivity.this, "סיווג הושלם!", Toast.LENGTH_SHORT).show();
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        img.setDescription("שגיאה בסיווג: " + error);
                        int categoryIndex = categories.indexOf(pendingSection);
                        if (categoryIndex != -1) categoriesAdapter.notifyImageChanged(categoryIndex);
                        Toast.makeText(UploadImagesActivity.this, "שגיאה בסיווג", Toast.LENGTH_SHORT).show();
                    });
                }
            });
        }
    }
}
