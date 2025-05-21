package com.example.finalproject_appraisal.classifer;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.finalproject_appraisal.classifer.ClassifierFactory;
import com.example.finalproject_appraisal.databinding.ActivityControllerBinding;

public class ControllerActivity extends AppCompatActivity {
    private ActivityControllerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityControllerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupListeners();
    }

    private void setupListeners() {
        binding.buttonKitchenClassifier.setOnClickListener(v ->
                ClassifierFactory.startClassifier(
                        this,
                        ClassifierFactory.ClassifierType.KITCHEN_CABINET
                )
        );

        binding.buttonDoorClassifier.setOnClickListener(v ->
                ClassifierFactory.startClassifier(
                        this,
                        ClassifierFactory.ClassifierType.DOOR
                )
        );

        binding.buttonFlooringClassifier.setOnClickListener(v ->
                ClassifierFactory.startClassifier(
                        this,
                        ClassifierFactory.ClassifierType.FLOORING
                )
        );

        // ועוד לחצנים אם צריך...
    }
}