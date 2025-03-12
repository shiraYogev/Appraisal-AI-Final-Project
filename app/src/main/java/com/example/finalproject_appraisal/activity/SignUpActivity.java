package com.example.finalproject_appraisal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject_appraisal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText emailEditText, passwordEditText, confirmPasswordEditText;
    private Button signUpButton;
    TextView loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        signUpButton = findViewById(R.id.signUpButton);
        CheckBox termsCheckBox = findViewById(R.id.termsCheckBox);

        termsCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            signUpButton.setEnabled(isChecked); // הפעלת הכפתור רק אם ה-CheckBox מסומן
        });

        signUpButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(SignUpActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!termsCheckBox.isChecked()) {
                Toast.makeText(SignUpActivity.this, "אנא קרא/י את התקנון, ואשר/י את התנאים וההגבלות", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                Log.d("SignUpActivity", "User created: " + user.getEmail());

                                // המשך ביצוע פעולה לאחר יצירת המשתמש
                                Map<String, Object> userData = new HashMap<>();
                                userData.put("email", email);
                                userData.put("is_banned", false);

                                db.collection("users")
                                        .document(email)
                                        .set(userData)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(SignUpActivity.this, "User added to Firestore", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(SignUpActivity.this, HomePageActivity.class);
                                            startActivity(intent);
                                            finish();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(SignUpActivity.this, "Failed to add user to Firestore", Toast.LENGTH_SHORT).show();
                                        });
                            } else {
                                // אם המשתמש לא נמצא
                                Log.e("SignUpActivity", "Failed to get the current user.");
                                Toast.makeText(SignUpActivity.this, "Failed to get the user details.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // שגיאה בהרשמה
                            Log.e("SignUpActivity", "Registration failed: " + task.getException());
                            Toast.makeText(SignUpActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        });
    }
}
