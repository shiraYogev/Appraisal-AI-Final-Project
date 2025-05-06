package com.example.finalproject_appraisal.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalproject_appraisal.R;
import com.example.finalproject_appraisal.activity.newProject.ClientDetailsActivity;

public class HomePageActivity extends AppCompatActivity {
    Button newProjectButton, myProjectsButton;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // כפתור ליצירת פרויקט חדש
        newProjectButton = findViewById(R.id.button_new_project);
        newProjectButton.setOnClickListener(v -> {
            // פתיחת אקטיביטי חדש ליצירת פרויקט
            intent = new Intent(HomePageActivity.this, ClientDetailsActivity.class);
            startActivity(intent);
        });

        // כפתור להציג את הפרויקטים שלי
        myProjectsButton = findViewById(R.id.button_my_projects);
        myProjectsButton.setOnClickListener(v -> {
            // פתיחת אקטיביטי של הפרויקטים שלי
            intent = new Intent(HomePageActivity.this, MyProjectsActivity.class);
            startActivity(intent);
        });
    }
}

