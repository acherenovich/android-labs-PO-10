package com.example.lab16;

import android.gesture.Gesture;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.gesture.GestureOverlayView;
import androidx.appcompat.app.AppCompatActivity;

public class CreateGestureActivity extends AppCompatActivity {

    private EditText editTextGestureName;
    private GestureOverlayView gestureOverlayView;
    private Button buttonDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_gesture);

        // Initialize widgets
        editTextGestureName = findViewById(R.id.editText_gesture_name);
        gestureOverlayView = findViewById(R.id.gesture_overlay_create);
        buttonDone = findViewById(R.id.button_done);

        // Setup Done button
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveGesture();
            }
        });
    }

    private void saveGesture() {
        String gestureName = editTextGestureName.getText().toString().trim();

        if (gestureName.isEmpty()) {
            Toast.makeText(this, "Please enter a name for the gesture", Toast.LENGTH_SHORT).show();
            return;
        }

        Gesture gesture = gestureOverlayView.getGesture();

        if (gesture == null || gesture.getStrokesCount() == 0) {
            Toast.makeText(this, "No gesture detected", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add gesture to the library
        MainActivity.getGestureLibrary().addGesture(gestureName, gesture);
        Toast.makeText(this, "Gesture saved successfully", Toast.LENGTH_SHORT).show();
        finish(); // Close the activity
    }
}