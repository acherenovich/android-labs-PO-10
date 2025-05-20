package com.example.lab6;

import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu; // Оставь, если хочешь меню для Info
import android.view.MenuItem; // Оставь, если хочешь меню для Info
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
// Убираем Toolbar импорты, если не нужны для меню
// import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private GestureLibrary gestureLibrary;
    private TextView numberDisplay;
    private StringBuilder userInput = new StringBuilder();
    private int secretNumber;

    // Добавляем переменную для кнопки добавления жеста
    private Button addGestureButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numberDisplay = findViewById(R.id.number_display);
        GestureOverlayView gestureOverlay = findViewById(R.id.gestureOverlay);
        Button checkButton = findViewById(R.id.check_button);
        // Находим кнопку добавления жеста по ID
        addGestureButton = findViewById(R.id.add_gesture_button);

        // Загружаем библиотеку жестов
        File gestureFile = new File(getFilesDir(), "gestures");
        gestureLibrary = GestureLibraries.fromFile(gestureFile);

        if (!gestureLibrary.load()) {
            // Используем строки из ресурсов
            Toast.makeText(this, R.string.error_loading_gestures, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.gestures_loaded_or_created, Toast.LENGTH_SHORT).show();
        }

        // Генерируем случайное число
        secretNumber = new Random().nextInt(100); // 0-99

        // Открытие GestureSaveActivity по нажатию на НОВУЮ кнопку внизу
        addGestureButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GestureSaveActivity.class);
            startActivity(intent);
        });

        // Обработчик жестов
        gestureOverlay.addOnGesturePerformedListener((overlay, gesture) -> {
            List<Prediction> predictions = gestureLibrary.recognize(gesture);

            if (!predictions.isEmpty() && predictions.get(0).score > 1.5) { // Порог можно настроить
                String recognizedGesture = predictions.get(0).name;
                // Можно показать распознанный жест для отладки
                // Toast.makeText(this, String.format(getString(R.string.gesture_recognized_format), recognizedGesture), Toast.LENGTH_SHORT).show();
                handleGesture(recognizedGesture);

            } else {
                Toast.makeText(this, R.string.gesture_not_recognized, Toast.LENGTH_SHORT).show();
            }
        });

        // Проверка введённого числа
        checkButton.setOnClickListener(v -> checkNumber());

        // Начальная установка текста
        updateNumberDisplay();
    }

    private void checkNumber() {
        if (userInput.length() == 0) {
            Toast.makeText(this, R.string.enter_number_prompt, Toast.LENGTH_SHORT).show();
            return;
        }

        try { // Добавим обработку ошибок на случай нечислового жеста
            int enteredNumber = Integer.parseInt(userInput.toString());

            if (enteredNumber == secretNumber) {
                Toast.makeText(this, String.format(getString(R.string.correct_guess_format), secretNumber), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, String.format(getString(R.string.incorrect_guess_format), secretNumber), Toast.LENGTH_LONG).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Ошибка: введены не только цифры!", Toast.LENGTH_SHORT).show();
        }


        // Сброс и генерация нового числа после попытки
        userInput.setLength(0);
        updateNumberDisplay(); // Обновляем текст
        secretNumber = new Random().nextInt(100);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Перезагружаем библиотеку жестов при возвращении на экран
        if (!gestureLibrary.load()) {
            // Можно убрать этот Toast во onResume, чтобы не появлялся каждый раз
            // Toast.makeText(this, R.string.error_loading_gestures, Toast.LENGTH_SHORT).show();
        }
    }

    private void handleGesture(String gestureName) {
        // Обрабатываем системные жесты
        switch (gestureName.toLowerCase()) { // Приводим к нижнему регистру для надежности
            case "exit":
                Toast.makeText(this, "Выход", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case "create":
                // Этот жест больше не нужен, т.к. есть кнопка
                // startActivity(new Intent(this, GestureSaveActivity.class));
                Toast.makeText(this, "Используйте кнопку 'Добавить жест'", Toast.LENGTH_SHORT).show();
                break;
            case "menu":
                // Если хочешь оставить открытие меню по жесту
                // openOptionsMenu();
                Toast.makeText(this, "Меню больше не используется или открывается иначе", Toast.LENGTH_SHORT).show();
                break;
            case "clear":
                userInput.setLength(0);
                updateNumberDisplay();
                Toast.makeText(this, R.string.input_cleared, Toast.LENGTH_SHORT).show();
                break;
            case "ok":
            case "s": // Добавим 's' как синоним 'ok'
                checkNumber();
                break;
            default:
                // Проверяем, является ли жест цифрой
                if (gestureName.matches("\\d")) { // Проверяем, что строка состоит только из цифр
                    userInput.append(gestureName);
                    updateNumberDisplay();
                } else {
                    // Сообщаем пользователю, что жест не является цифрой или командой
                    Toast.makeText(this, "Жест '" + gestureName + "' не цифра и не команда", Toast.LENGTH_SHORT).show();
                }
                break; // Не забываем break
        }
    }

    // Метод для обновления TextView
    private void updateNumberDisplay() {
        if (userInput.length() == 0) {
            numberDisplay.setText(R.string.initial_prompt);
        } else {
            numberDisplay.setText(userInput.toString());
        }
    }


}