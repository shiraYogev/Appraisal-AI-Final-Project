package com.example.finalproject_appraisal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject_appraisal.R;

public class MainActivity extends AppCompatActivity {

    private Button loginButton, signUpButton,homePageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("MainActivity", "onCreate called");

        // אם יש לך אובייקטים שאתה יוצר, הוסף כאן לוגים
        loginButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.signUpButton);
        homePageButton = findViewById(R.id.homePageButton);

        loginButton.setOnClickListener(v -> {
            Log.d("MainActivity", "Login button clicked");
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        signUpButton.setOnClickListener(v -> {
            Log.d("MainActivity", "SignUp button clicked");
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
        });


        /////// tmp button for home page ////////
        homePageButton.setOnClickListener(v -> {
            Log.d("MainActivity", "home Page button clicked");
            Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
            startActivity(intent);
        });
    }

}
