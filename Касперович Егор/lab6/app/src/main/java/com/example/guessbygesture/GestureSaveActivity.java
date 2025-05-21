package com.example.guessbygesture;
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

public class GestureSaveActivity extends Activity {
    private GestureLibrary gestureLibrary;
    private EditText gestureName;
    private Gesture lastGesture = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_save);

        gestureName = findViewById(R.id.gesture_name);
        GestureOverlayView gestureOverlay = findViewById(R.id.gestureOverlay);
        Button saveButton = findViewById(R.id.save_gesture);

        File gestureFile = new File(getFilesDir(), "gestures");
        gestureLibrary = GestureLibraries.fromFile(gestureFile);

        if (!gestureLibrary.load()) {
            Toast.makeText(this, "Создана новая библиотека жестов", Toast.LENGTH_SHORT).show();
        }

        gestureOverlay.addOnGesturePerformedListener((overlay, gesture) -> {
            lastGesture = gesture;
            Toast.makeText(this, "Жест нарисован! Введите имя и нажмите сохранить", Toast.LENGTH_SHORT).show();
        });

        saveButton.setOnClickListener(view -> {
            String name = gestureName.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(this, "Введите имя жеста", Toast.LENGTH_SHORT).show();
                return;
            }

            if (lastGesture == null) {
                Toast.makeText(this, "Сначала нарисуйте жест!", Toast.LENGTH_SHORT).show();
                return;
            }

            gestureLibrary.removeEntry(name);
            gestureLibrary.addGesture(name, lastGesture);
            gestureLibrary.save();
            lastGesture = null;
            Toast.makeText(this, "Жест '" + name + "' сохранён!", Toast.LENGTH_SHORT).show();
        });
    }
}
