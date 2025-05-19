package com.example.lab1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private EditText serverUrlEditText;
    private EditText rowsCountEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        serverUrlEditText = findViewById(R.id.serverUrlEditText);
        rowsCountEditText = findViewById(R.id.rowsCountEditText);

        // Загружаем сохраненные настройки
        SharedPreferences preferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        String serverUrl = preferences.getString("server_url", "https://api.jsonbin.io/v3/b/67aef009e41b4d34e48d7caa?meta=false");
        int rowsCount = preferences.getInt("rows_count", 10);

        serverUrlEditText.setText(serverUrl);
        rowsCountEditText.setText(String.valueOf(rowsCount));

        // Сохраняем настройки при нажатии на кнопку
        findViewById(R.id.btnSaveSettings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serverUrl = serverUrlEditText.getText().toString();
                int rowsCount = Integer.parseInt(rowsCountEditText.getText().toString());

                // Сохраняем настройки
                SharedPreferences.Editor editor = getSharedPreferences("AppSettings", MODE_PRIVATE).edit();
                editor.putString("server_url", serverUrl);
                editor.putInt("rows_count", rowsCount);
                editor.apply();

                // Подтверждение сохранения
                Toast.makeText(SettingsActivity.this, "Настройки сохранены", Toast.LENGTH_SHORT).show();
                finish();  // Закрываем экран настроек
            }
        });
    }
}
