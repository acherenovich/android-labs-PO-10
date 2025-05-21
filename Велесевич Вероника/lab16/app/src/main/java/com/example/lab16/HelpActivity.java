package com.example.lab16;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        // Инициализация TextView для отображения справочной информации
        TextView textViewHelp = findViewById(R.id.textView_help);
        textViewHelp.setText("Это приложение позволяет создавать и распознавать жесты.\n"
                + "Используйте экран для рисования жестов и добавляйте их в библиотеку.");
    }
}