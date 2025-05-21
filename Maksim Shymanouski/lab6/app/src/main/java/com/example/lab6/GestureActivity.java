package com.example.lab6;

import android.app.Activity;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class GestureActivity extends Activity {
    private GestureLibrary gestureLibrary;
    private EditText gestureName;
    private Gesture lastGesture = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture);

        gestureName = findViewById(R.id.gesture_name);
        GestureOverlayView gestureOverlay = findViewById(R.id.gestureOverlay);
        Button saveButton = findViewById(R.id.save_gesture);

        File gestureFile = new File(getFilesDir(), "gestures");
        gestureLibrary = GestureLibraries.fromFile(gestureFile);

        if (!gestureLibrary.load()) {
            Toast.makeText(this, "New gesture library created", Toast.LENGTH_SHORT).show();
        }

        gestureOverlay.addOnGesturePerformedListener((overlay, gesture) -> {
            lastGesture = gesture;
            Toast.makeText(this, "Gesture drawn! Enter a name and save.", Toast.LENGTH_SHORT).show();
        });

        saveButton.setOnClickListener(view -> {
            String name = gestureName.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "Enter a gesture name", Toast.LENGTH_SHORT).show();
                return;
            }
            if (lastGesture == null) {
                Toast.makeText(this, "First, draw a gesture!", Toast.LENGTH_SHORT).show();
                return;
            }

            gestureLibrary.removeEntry(name);
            gestureLibrary.addGesture(name, lastGesture);
            gestureLibrary.save();

            if (!gestureLibrary.load()) {
                Toast.makeText(this, "Error updating gesture library", Toast.LENGTH_SHORT).show();
            }

            lastGesture = null;
            Toast.makeText(this, "Gesture '" + name + "' saved!", Toast.LENGTH_SHORT).show();
        });
    }
}
