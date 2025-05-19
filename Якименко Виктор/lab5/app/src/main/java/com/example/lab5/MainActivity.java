package com.example.lab5;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class MainActivity extends AppCompatActivity {

    //для файлового менеджера
    private static final int PICKFILE_RESULT_CODE = 1;
    private String setType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestMediaPermissions(); // Запрашиваем разрешение на хранилище
        updateButtonStates();      // Проверяем и устанавливаем состояние кнопок

        Button audioBtn = findViewById(R.id.audioBtn);
        Button videoBtn = findViewById(R.id.videoBtn);
        Button imageBtn = findViewById(R.id.imageBtn);

        audioBtn.setOnClickListener(view -> {
            setType = "audio/*";
            openFilePicker();
        });

        videoBtn.setOnClickListener(view -> {
            setType = "video/*";
            openFilePicker();
        });

        imageBtn.setOnClickListener(view -> {
            setType = "image/*";
            openFilePicker();
        });
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(setType); // устанавливаем тип файлов для выбора (audio/*, video/*, image/*)
        startActivityForResult(intent, PICKFILE_RESULT_CODE);
    }


    private void requestMediaPermissions() {
        // Проверяем разрешение READ_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // Если разрешение не предоставлено, запрашиваем его
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    },
                    100); // requestCode
        } else {
            // Разрешение уже предоставлено
            Toast.makeText(this, "Разрешение на хранилище уже предоставлено", Toast.LENGTH_SHORT).show();
        }
    }

    // Обработка результатов запроса разрешений
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Разрешение предоставлено
                Toast.makeText(this, "Разрешение на хранилище предоставлено", Toast.LENGTH_SHORT).show();
            } else {
                // Разрешение не предоставлено
                Toast.makeText(this, "Разрешение на хранилище не предоставлено", Toast.LENGTH_SHORT).show();
            }
            updateButtonStates(); // Обновляем доступность кнопок
        }
    }

    private void updateButtonStates() {
        Button audioBtn = findViewById(R.id.audioBtn);
        Button videoBtn = findViewById(R.id.videoBtn);
        Button imageBtn = findViewById(R.id.imageBtn);

        // Проверяем только READ_EXTERNAL_STORAGE
        boolean hasExternalStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        audioBtn.setEnabled(hasExternalStoragePermission);
        videoBtn.setEnabled(hasExternalStoragePermission);
        imageBtn.setEnabled(hasExternalStoragePermission);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICKFILE_RESULT_CODE && resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData();

            if (setType.equals("audio/*")) {
                openAudio(fileUri);
            } else if (setType.equals("video/*")) {
                openVideo(fileUri);
            } else if (setType.equals("image/*")) {
                openImage(fileUri);
            } else {
                Toast.makeText(this, "Неподдерживаемый тип файла", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openImage(Uri imageUri) {
        Intent intent = new Intent(this, ImageActivity.class);
        intent.putExtra("imageUri", imageUri.toString());
        startActivity(intent);
    }

    private void openAudio(Uri audioUri) {
        Intent intent = new Intent(this, AudioActivity.class);
        intent.putExtra("audioUri", audioUri.toString());
        startActivity(intent);
    }

    private void openVideo(Uri videoUri) {
        Intent intent = new Intent(this, VideoActivity.class);
        intent.putExtra("videoUri", videoUri.toString());
        startActivity(intent);
    }
}