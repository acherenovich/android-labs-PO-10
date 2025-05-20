package com.example.lab4;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private EditText etPageId;
    private Button btnDownload, btnView, btnDelete;

    private static final String PREFS_NAME = "AppPrefs";
    private static final String PREF_SHOW_POPUP = "showPopup";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etPageId = findViewById(R.id.etPageId);
        btnDownload = findViewById(R.id.btnDownload);
        btnView = findViewById(R.id.btnView);
        btnDelete = findViewById(R.id.btnDelete);
        Button labDataButton = findViewById(R.id.load_data_button);
        btnDownload.setOnClickListener(v -> downloadPdf());
        btnView.setOnClickListener(v -> viewPdf());
        btnDelete.setOnClickListener(v -> deletePdf());
        labDataButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Данные о лабораторной работе")
                    .setMessage("Сделал: Замойский А.С. ПО-10\n" +"Задание 1. Реализуйте пример подключения к сети.\n" +
                            "Задание 2. Реализуйте коды приложений в примерах из источника (запросы, взаимодействие с сервером через сокеты).\n" +
                            "Задание 3. Разработайте мобильное приложение согласно заданию 3 источника, позволяющее пользователю асинхронно скачивать файлы журнала Научно-технический вестник (возможно взять другой источник файлов подобной структуры).\n" +
                            "Задание 4. Хранение и чтение настроек. При запуске приложения пользователю должно выводиться всплывающее полупрозрачное уведомление (popupWindow), с краткой инструкцией по использованию приложения (можете написать случайный текст), чекбоксом «Больше не показывать» и кнопкой «ОК».")
                    .create()
                    .show();
        });
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean showPopup = preferences.getBoolean(PREF_SHOW_POPUP, true);

        if (showPopup) {
            showPopupDialog();
        }
    }
    private void showPopupDialog() {
        // Создаем AlertDialog с чекбоксом
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View customView = getLayoutInflater().inflate(R.layout.popup_window, null);
        CheckBox checkBox = customView.findViewById(R.id.checkboxDontShowAgain);

        builder.setView(customView)
                .setTitle("Инструкция по использованию")
                .setMessage("Это краткая инструкция по использованию приложения.")
                .setPositiveButton("ОК", (dialog, which) -> {
                    if (checkBox.isChecked()) {
                        // Сохраняем предпочтение, чтобы больше не показывать
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean(PREF_SHOW_POPUP, false);
                        editor.apply();
                    }
                    dialog.dismiss();
                })
                .setCancelable(false)  // Запрещаем закрывать окно кнопками назад или вне окна
                .create()
                .show();
    }
    private void downloadPdf() {
        String pageId = etPageId.getText().toString().trim();

        if (pageId.isEmpty()) {
            Toast.makeText(this, "Введите ID", Toast.LENGTH_SHORT).show();
            return;
        }

        // Путь к файлу
        File pdfFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS + "/pdf_files"), pageId + ".pdf");

        // Проверка, существует ли файл
        if (pdfFile.exists()) {
            Toast.makeText(this, "Файл уже существует", Toast.LENGTH_SHORT).show();
            return; // Если файл существует, прекращаем скачивание
        }

        // Запуск асинхронной загрузки PDF
        HttpClientJournal.downloadPdf(this, pageId);
        Toast.makeText(this, "Загрузка начата", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Файл загружен", Toast.LENGTH_SHORT).show();
    }

    private void viewPdf() {
        String pageId = etPageId.getText().toString().trim();
        if (pageId.isEmpty()) {
            Toast.makeText(this, "Введите ID", Toast.LENGTH_SHORT).show();
            return;
        }

        File pdfFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS + "/pdf_files"), pageId + ".pdf");

        // Проверка, скачан ли файл
        if (!pdfFile.exists()) {
            Log.e("MainActivity", "Файл не найден по пути: " + pdfFile.getAbsolutePath());
            Toast.makeText(this, "Файл не найден", Toast.LENGTH_SHORT).show();
            return;
        }

        Uri pdfUri = FileProvider.getUriForFile(this, "com.example.lab4.fileprovider", pdfFile);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(pdfUri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

    private void deletePdf() {
        String pageId = etPageId.getText().toString().trim();
        if (pageId.isEmpty()) {
            Toast.makeText(this, "Введите ID", Toast.LENGTH_SHORT).show();
            return;
        }

        File pdfFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS + "/pdf_files"), pageId + ".pdf");
        if (pdfFile.exists() && pdfFile.delete()) {
            Toast.makeText(this, "Файл удалён", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Не удалось удалить файл", Toast.LENGTH_SHORT).show();
        }
    }

}