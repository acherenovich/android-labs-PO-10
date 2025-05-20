package com.example.lab4;

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
        helpTextView.setText("Лабораторная работа № 13\n " +
                "Задание 1. Реализуйте пример подключения к сети.\n" +
                "Задание 2. Реализуйте коды приложений в примерах из источника (запросы, взаимодействие с сервером через сокеты).\n" +
                "Задание 3. Разработайте мобильное приложение согласно заданию 3 источника, позволяющее пользователю асинхронно скачивать файлы журнала Научно-технический вестник (возможно взять другой источник файлов подобной структуры).\n" +
                "Задание 4. Хранение и чтение настроек. При запуске приложения пользователю должно выводиться всплывающее полупрозрачное уведомление (popupWindow), с краткой инструкцией по использованию приложения (можете написать случайный текст), чекбоксом «Больше не показывать» и кнопкой «ОК».");

        // Добавление кнопки выхода в главное меню
        Button buttonReturnToMainMenu = findViewById(R.id.buttonReturnToMainMenu);
        buttonReturnToMainMenu.setOnClickListener(v -> {
            Intent intent = new Intent(HelpActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Закрывает HelpActivity
        });
    }
}