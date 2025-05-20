package com.example.lab6;

import android.content.Intent;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private GestureLibrary gestureLibrary;
    private TextView display;
    private StringBuilder userInput = new StringBuilder();
    private String currentOperator = "";
    private double firstOperand = 0;
    private boolean isCalculatorMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.number_display);
        GestureOverlayView gestureOverlay = findViewById(R.id.gestureOverlay);
        Button clearButton = findViewById(R.id.clear_button);
        Button addGestureButton = findViewById(R.id.add_gesture_button);
        Button toggleModeButton = findViewById(R.id.toggle_mode_button);

        File gestureFile = new File(getFilesDir(), "gestures");
        gestureLibrary = GestureLibraries.fromFile(gestureFile);

        if (!gestureLibrary.load()) {
            Toast.makeText(this, "Error loading gesture library", Toast.LENGTH_SHORT).show();
        }

        addGestureButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GestureActivity.class);
            startActivity(intent);
        });

        toggleModeButton.setOnClickListener(v -> toggleMode(toggleModeButton));

        gestureOverlay.addOnGesturePerformedListener((overlay, gesture) -> {
            try {
                List<Prediction> predictions = gestureLibrary.recognize(gesture);

                if (predictions == null || predictions.isEmpty()) {
                    throw new Exception("No gesture predictions");
                }

                Prediction bestPrediction = predictions.get(0);

                if (bestPrediction.score > 2.0) {
                    String recognizedGesture = bestPrediction.name;
                    handleGesture(recognizedGesture);
                } else {
                    throw new Exception("Gesture has too low a score");
                }
            } catch (Exception e) {
                Toast.makeText(this, "Gesture not recognized", Toast.LENGTH_SHORT).show();
            }
        });

        clearButton.setOnClickListener(v -> clearDisplay());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!gestureLibrary.load()) {
            Toast.makeText(this, "New gesture library created", Toast.LENGTH_SHORT).show();
        }
    }

    private void toggleMode(Button toggleModeButton) {
        isCalculatorMode = !isCalculatorMode;
        if (isCalculatorMode) {
            display.setHint("Enter a gesture for the calculator...");
            toggleModeButton.setText("Mode: Calculator");
        } else {
            display.setHint("Enter a gesture for display...");
            toggleModeButton.setText("Mode: Gestures");
        }
        clearDisplay();
    }

    private void handleGesture(String gestureName) {
        if (isCalculatorMode) {
            switch (gestureName) {
                case "clear":
                    clearDisplay();
                    break;
                case "plus":
                    handleOperator("+");
                    break;
                case "minus":
                    handleOperator("-");
                    break;
                case "multiply":
                    handleOperator("*");
                    break;
                case "divide":
                    handleOperator("/");
                    break;
                case "equals":
                    calculateResult();
                    break;
                case "info":
                    Intent infoIntent = new Intent(MainActivity.this, InfoActivity.class);
                    startActivity(infoIntent);
                    break;
                case "arrow":
                    Intent gestureIntent = new Intent(MainActivity.this, GestureActivity.class);
                    startActivity(gestureIntent);
                    break;
                default:
                    handleNumber(gestureName);
            }
        } else {
            display.setText("Gesture: " + gestureName);
        }
    }

    private void handleNumber(String number) {
        if (currentOperator.isEmpty()) {
            firstOperand = (firstOperand * 10) + Integer.parseInt(number);
            display.setText(String.valueOf(firstOperand));
        } else {
            userInput.append(number);
            display.setText(firstOperand + " " + currentOperator + " " + userInput);
        }
    }

    private void handleOperator(String operator) {
        if (!userInput.toString().isEmpty()) {
            calculateResult();
        }
        currentOperator = operator;
        display.setText(firstOperand + " " + operator);
        userInput.setLength(0);
    }

    private void calculateResult() {
        if (userInput.toString().isEmpty()) return;

        double secondOperand = Double.parseDouble(userInput.toString());
        double result = 0;

        switch (currentOperator) {
            case "+":
                result = firstOperand + secondOperand;
                break;
            case "-":
                result = firstOperand - secondOperand;
                break;
            case "*":
                result = firstOperand * secondOperand;
                break;
            case "/":
                if (secondOperand != 0) {
                    result = firstOperand / secondOperand;
                } else {
                    Toast.makeText(this, "Error: division by zero", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
        }

        display.setText(String.valueOf(result));
        firstOperand = result;
        userInput.setLength(0);
        currentOperator = "";
    }

    private void clearDisplay() {
        firstOperand = 0;
        userInput.setLength(0);
        currentOperator = "";
        display.setText("");
    }
}
