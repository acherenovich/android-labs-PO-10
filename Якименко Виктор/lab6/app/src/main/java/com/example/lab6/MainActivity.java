package com.example.lab6;

import android.content.Intent;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private GestureLibrary gestureLibrary;
    private TextView problemDisplay; // Переименовано для ясности
    private StringBuilder userInput = new StringBuilder();

    // Переменные для математической задачи
    private int num1;
    private int num2;
    private int correctAnswer;
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Отключаем стандартный заголовок
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        problemDisplay = findViewById(R.id.problem_display); // Используем новый ID из XML
        GestureOverlayView gestureOverlay = findViewById(R.id.gestureOverlay);
        Button checkButton = findViewById(R.id.check_button);
        Button addGestureButton = findViewById(R.id.add_gesture_button);

        // Загружаем библиотеку жестов
        File gestureFile = new File(getFilesDir(), "gestures");
        gestureLibrary = GestureLibraries.fromFile(gestureFile);

        if (!gestureLibrary.load()) {
            Toast.makeText(this, "Библиотека жестов не найдена или пуста. Создайте жесты 0-9.", Toast.LENGTH_LONG).show();
            // Можно перенаправить на GestureSaveActivity или просто показать сообщение
        }

        // Генерируем первый пример
        generateNewProblem();

        // Открытие GestureSaveActivity
        addGestureButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GestureSaveActivity.class);
            startActivity(intent);
        });

        // Обработчик жестов
        gestureOverlay.addOnGesturePerformedListener((overlay, gesture) -> {
            List<Prediction> predictions = gestureLibrary.recognize(gesture);

            if (!predictions.isEmpty() && predictions.get(0).score > 1.5) { // Порог можно подстроить
                String recognizedGesture = predictions.get(0).name;
                handleGesture(recognizedGesture);
            } else {
                Toast.makeText(this, "Жест не распознан", Toast.LENGTH_SHORT).show();
            }
        });

        // Проверка введённого ответа по кнопке
        checkButton.setOnClickListener(v -> checkAnswer());
    }

    // Генерирует новый математический пример
    private void generateNewProblem() {
        num1 = random.nextInt(10); // Числа от 0 до 9
        num2 = random.nextInt(10);
        correctAnswer = num1 + num2; // Пока только сложение

        userInput.setLength(0); // Очищаем предыдущий ввод
        updateProblemDisplay(); // Обновляем TextView
    }

    // Обновляет TextView с текущим примером и вводом пользователя
    private void updateProblemDisplay() {
        String problemText = num1 + " + " + num2 + " = ";
        problemDisplay.setText(problemText + userInput.toString());
    }

    // Обрабатывает распознанный жест
    private void handleGesture(String gestureName) {
        // Проверяем, является ли жест цифрой
        if (isDigit(gestureName)) {
            userInput.append(gestureName);
            updateProblemDisplay(); // Обновляем отображение с новой цифрой
        } else {
            // Обрабатываем специальные команды или показываем ошибку
            switch (gestureName.toLowerCase()) { // Используем toLowerCase для надежности
                case "exit":
                    Toast.makeText(this, "Выход", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case "create": // Оставим для совместимости, если такой жест есть
                case "add":    // Или 'add'
                    Toast.makeText(this, "Переход к добавлению жеста", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, GestureSaveActivity.class));
                    break;
                case "menu":
                    Toast.makeText(this, "Открытие меню", Toast.LENGTH_SHORT).show();
                    openOptionsMenu();
                    break;
                case "clear":
                case "delete": // Можно добавить синоним
                    userInput.setLength(0);
                    updateProblemDisplay(); // Обновляем отображение (убираем ввод)
                    Toast.makeText(this, "Ввод сброшен", Toast.LENGTH_SHORT).show();
                    break;
                case "ok":
                case "enter": // Можно добавить синоним
                case "s":     // Оставим 'S' если нужно
                    checkAnswer();
                    break;
                default:
                    // Если жест не цифра и не команда - показываем ошибку
                    Toast.makeText(this, "Жест '" + gestureName + "' не является числом. Попробуйте снова.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    // Проверяет, является ли строка одной цифрой
    private boolean isDigit(String s) {
        return s != null && s.length() == 1 && Character.isDigit(s.charAt(0));
        // Или более строгая проверка, если жесты названы "0", "1" и т.д.:
        // return s != null && s.matches("[0-9]");
    }

    // Проверяет введенный ответ
    private void checkAnswer() {
        if (userInput.length() == 0) {
            Toast.makeText(this, "Введите ответ жестами", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int enteredAnswer = Integer.parseInt(userInput.toString());

            if (enteredAnswer == correctAnswer) {
                Toast.makeText(this, "Правильно!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Неправильно! Верный ответ: " + correctAnswer, Toast.LENGTH_LONG).show();
            }

            // Генерируем новый пример после проверки
            generateNewProblem();

        } catch (NumberFormatException e) {
            // Эта ошибка не должна возникать, если handleGesture работает правильно,
            // но оставим на всякий случай
            Toast.makeText(this, "Ошибка ввода. Попробуйте снова.", Toast.LENGTH_SHORT).show();
            userInput.setLength(0); // Очищаем некорректный ввод
            updateProblemDisplay();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Перезагружаем библиотеку жестов при возвращении в активность
        // (на случай, если жесты были добавлены в GestureSaveActivity)
        if (!gestureLibrary.load()) {
            // Можно снова показать сообщение, если библиотека все еще не загружена
            Toast.makeText(this, "Ошибка загрузки библиотеки жестов", Toast.LENGTH_SHORT).show();
        }
        // Генерируем новый пример при возобновлении, чтобы избежать путаницы
        // Если не хотите сбрасывать прогресс, закомментируйте строку ниже
        generateNewProblem();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.labinfo_settings) {
            Intent intent = new Intent(this, LabinfoActivity.class);
            startActivity(intent);
            return true; // Важно вернуть true, если обработали пункт меню
        }
        return super.onOptionsItemSelected(item);
    }
}