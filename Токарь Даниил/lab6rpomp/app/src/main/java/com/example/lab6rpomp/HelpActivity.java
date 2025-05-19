package com.example.lab6rpomp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        // Можно добавить кнопку "Назад" в ActionBar, если нужно
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Справка"); // Установить заголовок
        }
    }

    // Обработка нажатия кнопки "Назад" в ActionBar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // Стандартное поведение кнопки "Назад"
        return true;
    }
}