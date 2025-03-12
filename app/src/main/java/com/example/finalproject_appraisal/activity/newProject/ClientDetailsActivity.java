package com.example.finalproject_appraisal.activity.newProject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject_appraisal.R;
import com.example.finalproject_appraisal.model.Client;

import java.util.ArrayList;

/**
 * ClientDetailsActivity manages the user interface for entering and updating client information in the appraisal system.
 * It allows users to input a client's ID, name, email, and phone number. The class uses a layout with EditTexts
 * for data entry and a Save button to collect and save this data, potentially to a database.
 *
 * Usage involves creating or updating client records with the information provided. The process is triggered by
 * a button click, which invokes the saveClient method to handle data collection and persistence.
 */

public class ClientDetailsActivity extends AppCompatActivity {

    private EditText clientIdEditText;
    private EditText fullNameEditText;
    private EditText emailEditText;
    private EditText phoneNumberEditText;
    private Button saveClientButton;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_details);

        // Initializing the views
        clientIdEditText = findViewById(R.id.clientIdEditText);
        fullNameEditText = findViewById(R.id.fullNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        saveClientButton = findViewById(R.id.saveClientButton);

        // Set up save button click listener
        saveClientButton.setOnClickListener(v -> saveClient());
    }

    private void saveClient() {
        String clientId = clientIdEditText.getText().toString();
        String fullName = fullNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phoneNumber = phoneNumberEditText.getText().toString();

        Client newClient = new Client(clientId, fullName, email, phoneNumber, new ArrayList<>());

        // Log or save the client to your database here
        System.out.println("Saved client: " + newClient);



        ////// finish saving //////

        // Navigate to PropertyDetailsActivity
        intent = new Intent(ClientDetailsActivity.this, PropertyDetailsActivity.class);
        // Optionally, add extra data to the intent if needed
        intent.putExtra("clientId", clientId);
        startActivity(intent);
    }

}
