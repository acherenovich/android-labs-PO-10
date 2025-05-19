package com.example.lab6rpomp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;
import android.util.Log; // Для отладки
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements GestureOverlayView.OnGesturePerformedListener {

    private static final String TAG = "GestureDemo"; // Тег для логов
    private static final double MIN_PREDICTION_SCORE = 2.0; // Порог уверенности (можно подбирать)

    private GestureLibrary gestureLibrary;
    private TextView tvProblem;
    private TextView tvFeedback;
    private TextView tvUserInput; // Добавлено
    private GestureOverlayView gestureOverlay;

    private int num1, num2, answer;
    private String currentUserInput = ""; // Хранит введенный пользователем ответ (как строку)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvProblem = findViewById(R.id.tvProblem);
        tvFeedback = findViewById(R.id.tvFeedback);
        tvUserInput = findViewById(R.id.tvUserInput); // Инициализация
        gestureOverlay = findViewById(R.id.gestureOverlay);

        // Загрузка библиотеки жестов
        gestureLibrary = GestureLibraries.fromRawResource(this, R.raw.my_gestures);
        if (!gestureLibrary.load()) {
            Log.e(TAG, "Не удалось загрузить библиотеку жестов!");
            Toast.makeText(this, "Ошибка: Не удалось загрузить библиотеку жестов!", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Log.i(TAG, "Библиотека жестов успешно загружена.");
            Toast.makeText(this, "Библиотека жестов загружена.", Toast.LENGTH_SHORT).show();
        }

        gestureOverlay.addOnGesturePerformedListener(this);
        generateNewProblem();
    }

    private void generateNewProblem() {
        Random random = new Random();
        // Генерируем примеры, чтобы ответ был однозначным (0-9) для простоты
        num1 = random.nextInt(5); // 0-4
        num2 = random.nextInt(5); // 0-4
        answer = num1 + num2; // Ответ будет 0-8

        currentUserInput = ""; // Сброс ввода пользователя
        displayProblem();
        displayUserInput(); // Обновляем отображение ввода
        tvFeedback.setText("Нарисуйте цифру-ответ"); // Подсказка
    }

    private void displayProblem() {
        // Всегда показываем знак вопроса
        tvProblem.setText(num1 + " + " + num2 + " = ?");
    }

    // Отображение введенного пользователем ответа
    private void displayUserInput() {
        tvUserInput.setText("Ваш ответ: " + (currentUserInput.isEmpty() ? "?" : currentUserInput));
    }

    // Проверка ответа пользователя
    private void checkUserAnswer() {
        if (currentUserInput.isEmpty()) {
            tvFeedback.setText("Сначала введите ответ жестом!");
            Toast.makeText(this, "Введите ответ (нарисуйте цифру)", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int userAnswerInt = Integer.parseInt(currentUserInput);
            if (userAnswerInt == answer) {
                tvFeedback.setText("Правильно! Молодец!");
                Toast.makeText(this, "Верно!", Toast.LENGTH_SHORT).show();
                // Можно автоматически переходить к следующему вопросу после правильного ответа
                // generateNewProblem();
            } else {
                tvFeedback.setText("Неправильно. Верный ответ: " + answer + ". Попробуйте еще раз или следующий пример.");
                Toast.makeText(this, "Ошибка!", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Log.e(TAG, "Ошибка парсинга введенного ответа: " + currentUserInput, e);
            tvFeedback.setText("Ошибка ввода. Попробуйте нарисовать цифру еще раз.");
        }
    }

    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        ArrayList<Prediction> predictions = gestureLibrary.recognize(gesture);

        if (predictions != null && predictions.size() > 0) {
            Prediction bestPrediction = predictions.get(0);

            Log.d(TAG, "Распознанный жест: " + bestPrediction.name + ", Score: " + bestPrediction.score); // Логирование

            // Проверяем уверенность распознавания
            if (bestPrediction.score >= MIN_PREDICTION_SCORE) { // Используем константу
                String gestureName = bestPrediction.name;
                handleGesture(gestureName);
            } else {
                tvFeedback.setText("Жест не распознан достаточно уверенно.");
                // Показываем лучший вариант для отладки
                Toast.makeText(this, "Неуверенный жест: " + bestPrediction.name + " (score: " + String.format("%.2f", bestPrediction.score) + ")", Toast.LENGTH_SHORT).show();
            }
        } else {
            tvFeedback.setText("Жест не распознан.");
            Toast.makeText(this, "Жест не найден в библиотеке", Toast.LENGTH_SHORT).show();
            Log.w(TAG, "Жест не найден в библиотеке.");
        }
    }

    // Обработка распознанного жеста
    private void handleGesture(String gestureName) {
        Toast.makeText(this, "Распознан жест: " + gestureName, Toast.LENGTH_SHORT).show();

        // Проверяем, является ли жест цифрой
        if (gestureName.startsWith("digit_")) {
            try {
                // Извлекаем цифру из имени жеста ("digit_X")
                String digit = gestureName.substring(gestureName.length() - 1);
                currentUserInput = digit; // Заменяем текущий ввод на новую цифру (для однозначных ответов)
                displayUserInput(); // Обновляем UI
                tvFeedback.setText("Ответ " + digit + " введен. Нарисуйте '?' для проверки.");
            } catch (Exception e) {
                Log.e(TAG, "Ошибка обработки жеста цифры: " + gestureName, e);
                tvFeedback.setText("Ошибка обработки жеста цифры.");
            }
        } else {
            // Обработка управляющих жестов
            switch (gestureName) {
                case "next_question":
                    tvFeedback.setText("Переход к следующему примеру...");
                    generateNewProblem(); // Генерация и сброс ввода
                    break;
                case "check_answer": // Теперь это проверка
                    tvFeedback.setText("Проверка ответа...");
                    checkUserAnswer();
                    break;
                case "show_help":
                    tvFeedback.setText("Открытие справки...");
                    Intent helpIntent = new Intent(MainActivity.this, HelpActivity.class);
                    startActivity(helpIntent);
                    break;
                default:
                    tvFeedback.setText("Неизвестный управляющий жест: " + gestureName);
                    Log.w(TAG, "Неизвестный управляющий жест: " + gestureName);
                    break;
            }
        }
    }
}