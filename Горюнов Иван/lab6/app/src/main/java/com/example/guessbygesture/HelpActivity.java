package com.example.guessbygesture;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HelpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        TextView helpText = findViewById(R.id.help_text);
        helpText.setText("Это калькулятор, который использует жесты для ввода.\n\n" +
                "Доступные жесты:\n" +
                "- Цифры (0-9) — ввод чисел\n" +
                "- '+' — сложение\n" +
                "- '-' — вычитание\n" +
                "- '*' — умножение\n" +
                "- '/' — деление\n" +
                "- '=' — вычисление результата\n" +
                "- 'help' — открыть справку\n");
    }
}
