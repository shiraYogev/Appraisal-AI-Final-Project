package com.example.finalproject_appraisal.classifer;


import android.content.Context;
import android.content.Intent;

import com.example.finalproject_appraisal.classifer.DoorDetectionActivity;
import com.example.finalproject_appraisal.classifer.KitchenCabinetDetectionActivity;

/**
 * Factory class to start different classifier activities
 */
public class ClassifierFactory {

    // Enumeration of available classifier types
    public enum ClassifierType {
        KITCHEN_CABINET,
        DOOR,
        // Add more classifier types here as needed
    }

    /**
     * Start the appropriate classifier activity based on type
     * @param context The context to start the activity from
     * @param type The type of classifier to start
     */
    public static void startClassifier(Context context, ClassifierType type) {
        Intent intent;

        switch (type) {
            case KITCHEN_CABINET:
                intent = new Intent(context, KitchenCabinetDetectionActivity.class);
                break;
            case DOOR:
                intent = new Intent(context, DoorDetectionActivity.class);
                break;
            default:
                throw new IllegalArgumentException("Unknown classifier type: " + type);
        }

        context.startActivity(intent);
    }
}