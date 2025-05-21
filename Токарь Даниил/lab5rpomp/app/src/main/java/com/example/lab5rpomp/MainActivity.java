package com.example.lab5rpomp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Проверяем и запрашиваем разрешение на чтение внешнего хранилища
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_MEDIA_AUDIO,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO
            }, PERMISSION_REQUEST_CODE);
        }
        Button imageButton = findViewById(R.id.imageButton);
        Button audioButton = findViewById(R.id.audioButton);
        Button videoButton = findViewById(R.id.videoButton);

        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ImageActivity.class);
            startActivity(intent);
        });

        audioButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AudioActivity.class);
            startActivity(intent);
        });

        videoButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, VideoActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allPermissionsGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
            if (allPermissionsGranted) {
                Toast.makeText(this, "Разрешения на чтение медиафайлов получены", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Разрешения на чтение медиафайлов отклонены. Приложение не сможет работать корректно.", Toast.LENGTH_LONG).show();
                // Можно добавить логику для закрытия приложения, если разрешение критически важно
            }
        }
    }
}