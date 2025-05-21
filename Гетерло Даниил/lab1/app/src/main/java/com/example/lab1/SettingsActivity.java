package com.example.lab1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private EditText serverUrlInput, itemsPerPageInput;
    private Button saveButton;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);

        serverUrlInput = findViewById(R.id.server_url);
        itemsPerPageInput = findViewById(R.id.items_per_page);
        saveButton = findViewById(R.id.save_button);

        serverUrlInput.setText(prefs.getString("server_url", "https://raw.githubusercontent.com/Dan4kk777/json-data-repo/main/data.json"));
        itemsPerPageInput.setText(String.valueOf(prefs.getInt("items_per_page", 10)));

        saveButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("server_url", serverUrlInput.getText().toString());
            editor.putInt("items_per_page", Integer.parseInt(itemsPerPageInput.getText().toString()));
            editor.apply();
            finish();
        });
    }
}
