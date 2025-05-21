package com.example.lab16;

import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.gesture.GestureLibraries;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private GestureOverlayView gestureOverlayView;
    private ListView listViewGestures;
    private Button buttonAddGesture;
    private Button buttonHelp;
    private TextView textViewRecognizedGesture;
    private Button buttonGenerateNumber;
    private Button buttonCheckGuess;
    private TextView textViewResult;

    private static GestureLibrary gestureLibrary;
    private List<String> gesturesList = new ArrayList<>();
    private StringBuilder recognizedNumbers = new StringBuilder(); // To store recognized digits
    private int randomNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize widgets
        gestureOverlayView = findViewById(R.id.gesture_overlay);
        listViewGestures = findViewById(R.id.list_gestures);
        buttonAddGesture = findViewById(R.id.button_add_gesture);
        buttonHelp = findViewById(R.id.button_help);
        textViewRecognizedGesture = findViewById(R.id.textView_recognized_gesture);
        buttonGenerateNumber = findViewById(R.id.button_generate_number);
        buttonCheckGuess = findViewById(R.id.button_check_guess);
        textViewResult = findViewById(R.id.textView_result);

        // Setup Gesture Overlay View
        gestureOverlayView.addOnGesturePerformedListener(new GestureOverlayView.OnGesturePerformedListener() {
            @Override
            public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
                ArrayList<Prediction> predictions = gestureLibrary.recognize(gesture);
                if (predictions.size() > 0) {
                    Prediction bestPrediction = predictions.get(0);
                    if (bestPrediction.score > 1.0) {
                        String recognizedName = bestPrediction.name;
                        if (recognizedNumbers.length() > 0) {
                            recognizedNumbers.append(";"); // Append semicolon if not the first number
                        }
                        recognizedNumbers.append(recognizedName); // Append recognized digit
                        textViewRecognizedGesture.setText("Распознанные числа: " + recognizedNumbers.toString());
                        Toast.makeText(MainActivity.this, "Распознан: " + recognizedName, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Жест не распознан", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Нет предсказаний", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Setup button listeners
        buttonAddGesture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewGesture();
            }
        });

        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHelpActivity();
            }
        });

        buttonGenerateNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateRandomNumber();
            }
        });

        buttonCheckGuess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkGuess();
            }
        });

        // Initialize GestureLibrary
        gestureLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
        if (!gestureLibrary.load()) {
            Toast.makeText(this, "Не удалось загрузить жесты", Toast.LENGTH_SHORT).show();
        }

        // Load existing gestures
        reloadGestures();
        generateRandomNumber(); // Generate the first random number on start
    }

    private void openHelpActivity() {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }

    private void addNewGesture() {
        Intent intent = new Intent(this, CreateGestureActivity.class);
        startActivity(intent);
    }

    private void reloadGestures() {
        gesturesList.clear();
        gesturesList.addAll(gestureLibrary.getGestureEntries());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, gesturesList);
        listViewGestures.setAdapter(adapter);
    }

    private void generateRandomNumber() {
        Random random = new Random();
        randomNumber = random.nextInt(5) + 1; // Generate a number between 1 and 5
        textViewResult.setText("Число сгенерировано! Угадайте его.");
        recognizedNumbers.setLength(0); // Clear previous guesses
        textViewRecognizedGesture.setText("Распознанные числа: "); // Reset recognized numbers display
    }

    private void checkGuess() {
        if (recognizedNumbers.length() == 0) {
            Toast.makeText(this, "Пожалуйста, нарисуйте число", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] guesses = recognizedNumbers.toString().split(";");
        boolean correct = false;

        for (String guessStr : guesses) {
            int guess;
            try {
                guess = Integer.parseInt(guessStr.trim());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Неверное число", Toast.LENGTH_SHORT).show();
                return;
            }

            if (guess < 1 || guess > 5) {
                Toast.makeText(this, "Введите число от 1 до 5", Toast.LENGTH_SHORT).show();
                return;
            }

            if (guess == randomNumber) {
                correct = true;
                break;
            }
        }

        if (correct) {
            textViewResult.setText("Молодец! Вы угадали число!");
        } else {
            textViewResult.setText("Не угадали! Было: " + randomNumber);
        }

        // Allow for generating a new number after checking guess
        Toast.makeText(this, "Нарисуйте новое число для следующей попытки!", Toast.LENGTH_SHORT).show();
    }

    // Public static method to access gestureLibrary from other classes
    public static GestureLibrary getGestureLibrary() {
        return gestureLibrary;
    }
}