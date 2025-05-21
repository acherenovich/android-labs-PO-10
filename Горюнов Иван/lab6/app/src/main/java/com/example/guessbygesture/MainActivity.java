package com.example.guessbygesture;

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
    private TextView expressionDisplay;
    private StringBuilder expression = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        expressionDisplay = findViewById(R.id.expression_display);
        GestureOverlayView gestureOverlay = findViewById(R.id.gestureOverlay);
        Button clearButton = findViewById(R.id.clear_button);
        Button addGestureButton = findViewById(R.id.add_gesture_button);

        File gestureFile = new File(getFilesDir(), "gestures");
        gestureLibrary = GestureLibraries.fromFile(gestureFile);

        if (!gestureLibrary.load()) {
            Toast.makeText(this, "Библиотека жестов пуста. Добавьте жесты!", Toast.LENGTH_SHORT).show();
        }

        gestureOverlay.addOnGesturePerformedListener((overlay, gesture) -> {
            List<Prediction> predictions = gestureLibrary.recognize(gesture);
            if (!predictions.isEmpty() && predictions.get(0).score > 2.0) {
                String recognizedGesture = predictions.get(0).name;
                handleGesture(recognizedGesture);
            } else {
                Toast.makeText(this, "Жест не распознан", Toast.LENGTH_SHORT).show();
            }
        });

        clearButton.setOnClickListener(v -> {
            expression.setLength(0);
            expressionDisplay.setText("");
        });

        addGestureButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GestureSaveActivity.class);
            startActivity(intent);
        });
    }

    private void handleGesture(String gestureName) {
        switch (gestureName) {
            case "plus":
                expression.append("+");
                break;
            case "minus":
                expression.append("-");
                break;
            case "multiply":
                expression.append("*");
                break;
            case "divide":
                expression.append("/");
                break;
            case "equal":
                calculateResult();
                return;
            case "help":
                openHelpActivity();
                return;
            default:
                expression.append(gestureName);
                break;
        }
        expressionDisplay.setText(expression.toString());
    }

    private void openHelpActivity() {
        Intent intent = new Intent(MainActivity.this, HelpActivity.class);
        startActivity(intent);
    }
    private void calculateResult() {
        try {
            double result = eval(expression.toString());
            expressionDisplay.setText(expression + " = " + result);
            expression.setLength(0);
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка в выражении", Toast.LENGTH_SHORT).show();
            expression.setLength(0);
            expressionDisplay.setText("");
        }
    }

    private double eval(String expression) {
        return new Object() {
            int pos = -1, ch;
            void nextChar() { ch = (++pos < expression.length()) ? expression.charAt(pos) : -1; }
            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }
            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < expression.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }
            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }
            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if (eat('*')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else return x;
                }
            }
            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();
                double x;
                int startPos = this.pos;
                if (eat('(')) {
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(expression.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }
                return x;
            }
        }.parse();
    }
}
