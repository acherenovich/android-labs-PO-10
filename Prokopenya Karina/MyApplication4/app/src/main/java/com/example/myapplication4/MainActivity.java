package com.example.myapplication4;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private String selectedType = "";

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
        getSupportActionBar().setTitle("Выполнила Прокопеня Карина");


        Button btnSelectImage = findViewById(R.id.btnSelectImage);
        Button btnSelectVideo = findViewById(R.id.btnSelectVideo);
        Button btnSelectAudio = findViewById(R.id.btnSelectAudio);

        btnSelectImage.setOnClickListener(v -> openFileChooser("image/*", "image"));
        btnSelectVideo.setOnClickListener(v -> openFileChooser("video/*", "video"));
        btnSelectAudio.setOnClickListener(v -> openFileChooser("audio/*", "audio"));
    }

    private void openFileChooser(String type, String fileType) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(type);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE);
        selectedType = fileType;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedFileUri = data.getData();
            if (selectedFileUri != null) {
                openMediaActivity(selectedFileUri, selectedType);
            }
        }
    }

    private void openMediaActivity(Uri fileUri, String selectedType) {
        if (fileUri == null) {
            return; // Предотвращаем ошибку, если путь к файлу некорректен
        }

        Intent intent;
        switch (selectedType) {
            case "image":
                intent = new Intent(this, ImageActivity.class);
                break;
            case "video":
                intent = new Intent(this, VideoActivity.class);
                break;
            case "audio":
                intent = new Intent(this, AudioActivity.class);
                break;
            default:
                return; // Выход, если тип файла неизвестен
        }

        intent.putExtra("fileUri", fileUri.toString());  // Передаем строковый путь
        startActivity(intent);
    }
}
