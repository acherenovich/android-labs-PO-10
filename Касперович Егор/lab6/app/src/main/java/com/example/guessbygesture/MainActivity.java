package com.example.guessbygesture;




import android.content.Intent;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private GestureLibrary gestureLibrary;
    private TextView numberDisplay;
    private StringBuilder userInput = new StringBuilder();
    private int secretNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numberDisplay = findViewById(R.id.number_display);
        GestureOverlayView gestureOverlay = findViewById(R.id.gestureOverlay);
        Button checkButton = findViewById(R.id.check_button);
        Button addGestureButton = findViewById(R.id.add_gesture_button);

        File gestureFile = new File(getFilesDir(), "gestures");
        gestureLibrary = GestureLibraries.fromFile(gestureFile);

        if (!gestureLibrary.load()) {
            Toast.makeText(this, "Ошибка загрузки библиотеки жестов", Toast.LENGTH_SHORT).show();
        }

        secretNumber = new Random().nextInt(100);

        addGestureButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GestureSaveActivity.class);
            startActivity(intent);
        });

        gestureOverlay.addOnGesturePerformedListener((overlay, gesture) -> {
            List<Prediction> predictions = gestureLibrary.recognize(gesture);

            if (!predictions.isEmpty() && predictions.get(0).score > 2.0) {
                String recognizedGesture = predictions.get(0).name;
                handleGesture(recognizedGesture);
            } else {
                Toast.makeText(this, "Жест не распознан", Toast.LENGTH_SHORT).show();
            }
        });

        checkButton.setOnClickListener(v -> checkNumber());
    }

    private void checkNumber() {
        if (userInput.length() == 0) {
            Toast.makeText(this, "Введите число", Toast.LENGTH_SHORT).show();
            return;
        }

        int enteredNumber = Integer.parseInt(userInput.toString());
        if (enteredNumber == secretNumber) {
            Toast.makeText(this, "Правильно! Загаданное число: " + secretNumber, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Неправильно! Загаданное число: " + secretNumber, Toast.LENGTH_LONG).show();
        }

        userInput.setLength(0);
        numberDisplay.setText("Введите число жестами");
        secretNumber = new Random().nextInt(100);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gestureLibrary.load();
    }

    private void handleGesture(String gestureName) {
        switch (gestureName) {
            case "exit":
                Toast.makeText(this, "Выход", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case "create":
                startActivity(new Intent(this, GestureSaveActivity.class));
                break;
            case "help":
                startActivity(new Intent(this, HelpActivity.class));
                break;
            case "clear":
                userInput.setLength(0);
                numberDisplay.setText("Введите число жестами");
                Toast.makeText(this, "Ввод сброшен", Toast.LENGTH_SHORT).show();
                break;
            case "ok":
                checkNumber();
                break;
            default:
                userInput.append(gestureName);
                numberDisplay.setText(userInput.toString());
                break;
        }
    }

}