package com.example.finalproject_appraisal.activity.newProject;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalproject_appraisal.R;
import com.example.finalproject_appraisal.model.Image;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertyDetailsActivity extends AppCompatActivity {

    // מעדכנים את הסוג לקבלת רשימת תמונות לכל קטגוריה
    private HashMap<Image.Category, List<Image>> imageMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_property_details);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // קבלת המפה מהאינטנט, תוך המרה לסוג הנכון
        imageMap = (HashMap<Image.Category, List<Image>>) getIntent().getSerializableExtra("propertyImagesMap");

        // דוגמה להצגת מספר הקטגוריות ומספר התמונות בכל קטגוריה
        TextView tvInfo = findViewById(R.id.tvImageInfo);
        if (imageMap != null) {
            StringBuilder info = new StringBuilder("מספר קטגוריות עם תמונות: " + imageMap.size() + "\n");
            for (Map.Entry<Image.Category, List<Image>> entry : imageMap.entrySet()) {
                info.append(entry.getKey().name())
                        .append(": ")
                        .append(entry.getValue().size())
                        .append(" תמונות\n");
            }
            tvInfo.setText(info.toString());
        } else {
            tvInfo.setText("לא הועלו תמונות");
        }
    }
}
