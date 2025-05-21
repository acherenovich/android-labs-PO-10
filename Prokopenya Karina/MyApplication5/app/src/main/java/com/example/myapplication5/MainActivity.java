package com.example.myapplication5;

import android.content.Intent;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private GestureLibrary gestureLibrary;
    private TextView numberDisplay;
    private StringBuilder userInput = new StringBuilder();
    private StringBuilder currentNote = new StringBuilder();
    private long currentNoteId = -1;
    private NotesDatabaseHelper dbHelper;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu); // Загружаем меню
        return true;  // Возвращаем true для отображения меню
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_student) {
            // Переход к активности с информацией о студенте
            Intent intent = new Intent(this, StudentInfoActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.menu_lab) {
            // Переход к активности с информацией о лабораторной
            Intent intent = new Intent(this, LabInfoActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new NotesDatabaseHelper(this);
        getSupportActionBar().setTitle("Выполнила Прокопеня Карина");

        numberDisplay = findViewById(R.id.number_display);
        GestureOverlayView gestureOverlay = findViewById(R.id.gestureOverlay);
        Button addWordButton = findViewById(R.id.add_word_button);
        Button newNoteButton = findViewById(R.id.new_note_button);
        Button viewNotesButton = findViewById(R.id.view_notes_button);
        Button addGestureButton = findViewById(R.id.add_gesture_button); // Новая кнопка

        File gestureFile = new File(getFilesDir(), "gestures");
        gestureLibrary = GestureLibraries.fromFile(gestureFile);
        if (!gestureLibrary.load()) {
            Toast.makeText(this, "Ошибка загрузки библиотеки жестов", Toast.LENGTH_SHORT).show();
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

        addWordButton.setOnClickListener(v -> {
            if (userInput.length() > 0) {
                currentNote.append(userInput.toString()).append(" ");
                userInput.setLength(0);
                numberDisplay.setText(currentNote.toString());
                if (currentNoteId < 0) {
                    currentNoteId = dbHelper.addNote(currentNote.toString());
                } else {
                    dbHelper.updateNote(currentNoteId, currentNote.toString());
                }
                Toast.makeText(this, "Слово добавлено в заметку", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Нет слова для добавления", Toast.LENGTH_SHORT).show();
            }
        });

        newNoteButton.setOnClickListener(v -> {
            if (currentNote.length() > 0) {
                Toast.makeText(this, "Заметка сохранена", Toast.LENGTH_SHORT).show();
            }
            currentNote.setLength(0);
            userInput.setLength(0);
            currentNoteId = -1;
            numberDisplay.setText("Новая заметка");
        });

        viewNotesButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NotesActivity.class);
            startActivity(intent);
        });

        // Обработчик нажатия на кнопку "Добавить жест"
        addGestureButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GestureActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Перезагружаем библиотеку жестов
        File gestureFile = new File(getFilesDir(), "gestures");
        gestureLibrary = GestureLibraries.fromFile(gestureFile);
        if (!gestureLibrary.load()) {
            Toast.makeText(this, "Ошибка загрузки библиотеки жестов", Toast.LENGTH_SHORT).show();
        }
    }


    private void handleGesture(String gestureName) {
        if (gestureName.equalsIgnoreCase("clear")) {
            userInput.setLength(0);
            Toast.makeText(this, "Ввод слова сброшен", Toast.LENGTH_SHORT).show();
        } else {
            userInput.append(gestureName);
            numberDisplay.setText(currentNote.toString() + userInput.toString());
        }
    }
}
