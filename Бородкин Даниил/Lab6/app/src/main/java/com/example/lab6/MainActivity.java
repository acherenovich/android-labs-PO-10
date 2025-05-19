package com.example.lab6;

import android.app.AlertDialog;
import android.gesture.*;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GestureOverlayView.OnGesturePerformedListener {
    private GestureLibrary gestureLibrary;
    private StringBuilder inputExpression = new StringBuilder();
    private TextView display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gestureLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures121);
        if (!gestureLibrary.load()) {
            finish();
        }

        display = findViewById(R.id.display);
        GestureOverlayView gestureOverlayView = findViewById(R.id.gestureOverlay);
        gestureOverlayView.addOnGesturePerformedListener(this);

        // Кнопка Подсказки
        Button btnHint = findViewById(R.id.btn_hint);
        btnHint.setOnClickListener(v -> showHintDialog());

        // Заглушка для кнопки "Действие"
        Button btnAction = findViewById(R.id.btn_action);
        btnAction.setOnClickListener(v ->{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Данные о лабораторной работе")
                .setMessage("Сделал: Бородкин Д.В.ПО-10\n" +
                        "1. Создать набор жестов\n" +
                        "2. Использовать созданные жесты в приложении\n" +
                        "3. Разработать простой калькулятор с жестовым вводом чисел и операций.\n")
                .create()
                .show();
    });
    }

    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        ArrayList<Prediction> predictions = gestureLibrary.recognize(gesture);
        if (!predictions.isEmpty() && predictions.get(0).score > 2.0) {
            String recognizedSymbol = predictions.get(0).name;
            handleGesture(recognizedSymbol);
        }
    }

    private void handleGesture(String symbol) {
        if ("0123456789+-/*=".contains(symbol)) {
            if (symbol.equals("=")) {
                evaluateExpression();
            } else {
                inputExpression.append(symbol);
                display.setText(inputExpression.toString());
            }
        } else {
            Toast.makeText(this, "Неизвестный жест", Toast.LENGTH_SHORT).show();
        }
    }

    private void evaluateExpression() {
        try {
            double result = eval(inputExpression.toString());
            display.setText(String.valueOf(result));
            inputExpression.setLength(0);
        } catch (Exception e) {
            display.setText("Ошибка");
            inputExpression.setLength(0);
        }
    }

    private void showHintDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Подсказка по жестам");
        builder.setMessage("0-9 — Цифры\n" +
                "+ — Сложение\n" +
                "- — Вычитание\n" +
                "M — Умножение\n" +
                "/ — Деление\n" +
                "? — Равно");
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    private double eval(String expr) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < expr.length()) ? expr.charAt(pos) : -1;
            }

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
                if (pos < expr.length()) throw new RuntimeException("Ошибка синтаксиса");
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                while (true) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                while (true) {
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
                    x = Double.parseDouble(expr.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Ошибка синтаксиса");
                }
                return x;
            }
        }.parse();
    }
}
