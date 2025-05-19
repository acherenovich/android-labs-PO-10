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
        helpText.setText("Это приложение позволяет угадывать число с помощью жестов.\n\n" +
                "Доступные жесты:\n" +
                "- Цифры (0–9) — ввод числа\n" +
                "- 'ok' — проверить число\n" +
                "- 'clear' — сброс ввода\n" +
                "- 'exit' — выход из приложения\n" +
                "- 'help' — открыть справку\n" +
                "- 'create' — добавить новый жест\n");
    }
}
