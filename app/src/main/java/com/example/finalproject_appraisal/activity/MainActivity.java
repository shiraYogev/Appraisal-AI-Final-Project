package com.example.finalproject_appraisal.activity;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject_appraisal.R;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {

    private Button loginButton, signUpButton,homePageButton, googleButton;
    SignInClient oneTapClient;
    BeginSignInRequest signUpRequest;
    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    private boolean showOneTapUI = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("MainActivity", "onCreate called");

        //התחברות עם גוגל שיראל מוסיפה
        googleButton= findViewById(R.id.btnSignInGoogle);
        oneTapClient = Identity.getSignInClient(this);
        signUpRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.web_client_id))
                        // Show all accounts on the device.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();

        ActivityResultLauncher<IntentSenderRequest> activityResultLauncher=
                registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result){
                      if(result.getResultCode() == MainActivity.RESULT_OK){
                          try {
                              SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(result.getData());
                              String idToken = credential.getGoogleIdToken();
                              if (idToken !=  null) {
                                  // Got an ID token from Google. Use it to authenticate
                                  // with your backend.
                                  Log.d(TAG, "Got ID token.");
                                  String email= credential.getId();
                                  Toast.makeText(getApplicationContext(), "Email: "+ email, Toast.LENGTH_SHORT ).show();
                              }
                          } catch (ApiException e) {
                              e.printStackTrace();
                          }
                      }
                    }
                });

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneTapClient.beginSignIn(signUpRequest)
                        .addOnSuccessListener(MainActivity.this, new OnSuccessListener<BeginSignInResult>() {
                            @Override
                            public void onSuccess(BeginSignInResult result) {
                                IntentSenderRequest intentSenderRequest= new IntentSenderRequest.Builder(result.getPendingIntent().getIntentSender()).build();
                                activityResultLauncher.launch(intentSenderRequest);
                            }
                        })
                        .addOnFailureListener(MainActivity.this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // No Google Accounts found. Just continue presenting the signed-out UI.
                                Log.d(TAG, e.getLocalizedMessage());
                            }
                        });
            }
        });


        // אם יש לך אובייקטים שאתה יוצר, הוסף כאן לוגים
        loginButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.signUpButton);
        homePageButton = findViewById(R.id.homePageButton);

        loginButton.setOnClickListener(v -> {
            Log.d("MainActivity", "Login button clicked");
            //Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            //startActivity(intent);
        });

        signUpButton.setOnClickListener(v -> {
            Log.d("MainActivity", "SignUp button clicked");
            //Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
           // startActivity(intent);
        });


        /////// tmp button for home page ////////
        homePageButton.setOnClickListener(v -> {
            Log.d("MainActivity", "home Page button clicked");
            Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
            startActivity(intent);
        });
    }

}
