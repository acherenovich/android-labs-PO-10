package com.example.lab17;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        TextView helpTextView = findViewById(R.id.helpTextView);
        helpTextView.setText("Многооконное приложение\n\n" +
                "Аннотация: Разработка многооконного приложения, предоставляющего возможности:\n" +
                "воспроизведения аудио и видео файлов, создания и отображения фотоснимков.\n\n" +
                "Цель лабораторной работы:\n" +
                "Разработка многооконного приложения, предоставляющего возможности:\n" +
                "воспроизведения аудио и видео файлов, создания и отображения фотоснимков.\n\n" +
                "Задачи лабораторной работы:\n" +
                "• Настроить интерфейс и реализовать логику активности для работы с камерой;\n" +
                "• Настроить интерфейс и реализовать логику активности для воспроизведения аудио и видео;\n" +
                "• Настроить интерфейс и реализовать логику активности для просмотра изображений;\n" +
                "• Настроить интерфейс и реализовать логику главной активности приложения.");

        // Добавление кнопки выхода в главное меню
        Button buttonReturnToMainMenu = findViewById(R.id.buttonReturnToMainMenu);
        buttonReturnToMainMenu.setOnClickListener(v -> {
            Intent intent = new Intent(HelpActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Закрывает HelpActivity
        });
    }
}