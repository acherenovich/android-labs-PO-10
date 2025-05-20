package com.example.lab6;

import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;

public class GestureSaveActivity extends AppCompatActivity {

    // Добавляем TAG для логов
    private static final String TAG = "GestureSaveActivity";

    private GestureLibrary gestureLibrary;
    private TextInputEditText gestureNameEditText;
    private MaterialButton saveButton;
    private TextView statusText;
    private GestureOverlayView gestureOverlay;
    private Gesture lastGesture = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_save);
        Log.d(TAG, "onCreate started"); // Лог начала onCreate

        // Находим View элементы
        gestureNameEditText = findViewById(R.id.gesture_name);
        gestureOverlay = findViewById(R.id.gestureOverlay);
        saveButton = findViewById(R.id.save_gesture);
        statusText = findViewById(R.id.status_text);

        // Проверяем, найдены ли View
        if (gestureNameEditText == null || gestureOverlay == null || saveButton == null || statusText == null) {
            Log.e(TAG, "One or more views were not found!");
            Toast.makeText(this, "Ошибка инициализации интерфейса!", Toast.LENGTH_LONG).show();
            finish(); // Завершаем активность, если интерфейс не найден
            return;
        } else {
            Log.d(TAG, "All views found successfully.");
        }


        // Файл, в который будут сохраняться жесты
        File gestureFile = new File(getFilesDir(), "gestures");
        Log.d(TAG, "Gesture file path: " + gestureFile.getAbsolutePath());
        gestureLibrary = GestureLibraries.fromFile(gestureFile);

        // Загружаем библиотеку жестов
        if (!gestureLibrary.load()) {
            Log.w(TAG, "Gesture library not loaded or empty. A new one might be created on save.");
            Toast.makeText(this, R.string.gestures_loaded_or_created, Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "Gesture library loaded successfully.");
            // Дополнительно: вывести количество загруженных жестов
            Log.d(TAG, "Number of gestures loaded: " + gestureLibrary.getGestureEntries().size());
        }

        // Изначально кнопка сохранения неактивна
        saveButton.setEnabled(false);
        statusText.setText(R.string.draw_gesture_prompt);
        Log.d(TAG, "Save button initially disabled.");

        // Listener для рисования жеста
        gestureOverlay.addOnGesturePerformedListener((overlay, gesture) -> {
            Log.d(TAG, "Gesture performed!"); // Лог: жест нарисован
            lastGesture = gesture;
            statusText.setText(R.string.gesture_drawn_prompt);
            saveButton.setEnabled(true); // <<< Ключевой момент: Активируем кнопку
            Log.d(TAG, "Save button state set to: " + saveButton.isEnabled()); // Лог: состояние кнопки
            // Опционально: можно убрать Toast отсюда, т.к. есть statusText
            // Toast.makeText(this, R.string.gesture_drawn_prompt, Toast.LENGTH_SHORT).show();
        });

        // Listener для кнопки "Сохранить жест"
        saveButton.setOnClickListener(view -> {
            Log.d(TAG, "Save button clicked!"); // Лог: кнопка нажата

            // Проверяем, активна ли кнопка (на всякий случай)
            if (!saveButton.isEnabled() || lastGesture == null) {
                Log.w(TAG, "Save clicked but button is disabled or lastGesture is null.");
                // Можно показать Toast, если такое произошло по ошибке
                Toast.makeText(this, R.string.draw_gesture_first_error, Toast.LENGTH_SHORT).show();
                return; // Выходим, если кнопка не должна была быть нажата
            }

            String name = gestureNameEditText.getText().toString().trim();
            Log.d(TAG, "Gesture name entered: '" + name + "'");

            if (name.isEmpty()) {
                Log.w(TAG, "Gesture name is empty.");
                gestureNameEditText.setError(getString(R.string.enter_gesture_name_error));
                return; // Выходим, если имя пустое
            } else {
                gestureNameEditText.setError(null); // Убираем ошибку, если имя введено
            }

            // Добавляем жест (перезаписываем, если существует)
            // Сначала удаляем старый, если он был, чтобы избежать дублирования
            gestureLibrary.removeEntry(name);
            Log.d(TAG,"Removed existing entry for: " + name + " (if any)");
            gestureLibrary.addGesture(name, lastGesture);
            Log.d(TAG, "Added gesture: " + name);


            // Сохраняем библиотеку
            boolean success = gestureLibrary.save();
            Log.d(TAG, "Attempted to save library. Success: " + success); // Лог: результат сохранения

            if (success) {
                Toast.makeText(this, String.format(getString(R.string.gesture_saved_success), name), Toast.LENGTH_SHORT).show();
                // Сбрасываем состояние после успешного сохранения
                lastGesture = null;
                gestureNameEditText.setText(""); // Очищаем поле ввода
                gestureOverlay.clear(false); // Очищаем область рисования
                saveButton.setEnabled(false); // <<< Делаем кнопку снова неактивной
                statusText.setText(R.string.draw_gesture_prompt); // Возвращаем начальный статус
                Log.d(TAG, "Reset state after successful save. Save button disabled.");
            } else {
                Log.e(TAG, "Failed to save gesture library!"); // Лог: Ошибка сохранения
                Toast.makeText(this, R.string.gesture_save_error, Toast.LENGTH_SHORT).show();
                statusText.setText(R.string.gesture_save_error); // Показываем ошибку и в статусе
            }
        });

        Log.d(TAG, "onCreate finished"); // Лог конца onCreate
    }

    // Добавим логи в другие методы жизненного цикла для полноты картины
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        // Можно перезагрузить библиотеку здесь, если нужно, но это может быть избыточно,
        // так как она загружается в onCreate и сохраняется при добавлении жеста.
        // if (!gestureLibrary.load()) { Log.w(TAG, "Reloading library in onResume failed."); }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}