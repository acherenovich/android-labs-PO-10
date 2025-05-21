package com.example.lab6;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.Prediction;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private GestureLibrary gestureLibrary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация GestureOverlayView
        GestureOverlayView gestureOverlay = findViewById(R.id.gestureOverlay);
        EditText expressionInput = findViewById(R.id.expressionInput);
        Button showDialogButton = findViewById(R.id.showDialogButton);

        // Загружаем жесты из res/raw/gestures
        gestureLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
        if (!gestureLibrary.load()) {
            Toast.makeText(this, "Ошибка загрузки жестов!", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Обработка жестов
        gestureOverlay.addOnGesturePerformedListener((overlay, gesture) -> {
            ArrayList<Prediction> predictions = gestureLibrary.recognize(gesture);
            if (!predictions.isEmpty() && predictions.get(0).score > 2.0) {
                String action = predictions.get(0).name;
                handleGesture(action);
            }
        });

        // Очистка поля ввода
        findViewById(R.id.clearButton).setOnClickListener(v -> expressionInput.setText(""));

        // Кнопка для вывода диалогового окна
        showDialogButton.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Информация")
                    .setMessage("Приложение созданно студентом группы ПО-10, Конашуком Н.В. Это приложение распознает жесты и преобразует их в математические выражения. Вы можете рисовать цифры и знаки операций, а затем вычислять результат. Для очистки используйте кнопку 'Очистить'.")
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }

    // Обработка распознанных жестов
    private void handleGesture(String action) {
        EditText expressionInput = findViewById(R.id.expressionInput);

        switch (action) {
            case "1":
                expressionInput.append("1");
                break;
            case "2":
                expressionInput.append("2");
                break;
            case "+":
                expressionInput.append("+");
                break;
            case "minus":
                expressionInput.append("-");
                break;
            case "div":
                expressionInput.append("/");
                break;
            case "mul":
                expressionInput.append("*");
                break;
            case "3":
                expressionInput.append("3");
                break;
            case "4":
                expressionInput.append("4");
                break;
            case "5":
                expressionInput.append("5");
                break;
            case "equal":
                evaluateExpression();
                break;
            default:
                Toast.makeText(this, "Неизвестный жест", Toast.LENGTH_SHORT).show();
        }
    }

    // Вычисление выражения
    private void evaluateExpression() {
        EditText expressionInput = findViewById(R.id.expressionInput);
        String expression = expressionInput.getText().toString();

        try {
            double result = evaluateMathExpression(expression);
            expressionInput.setText(String.valueOf(result));
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка в выражении", Toast.LENGTH_SHORT).show();
        }
    }

    // Метод для вычисления математических выражений с использованием exp4j
    private double evaluateMathExpression(String expression) {
        Expression expr = new ExpressionBuilder(expression).build();
        return expr.evaluate();
    }
}
