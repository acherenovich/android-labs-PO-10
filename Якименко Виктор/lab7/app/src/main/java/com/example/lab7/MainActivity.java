package com.example.lab7; // Убедитесь, что пакет правильный

import android.content.Intent;
import android.os.Bundle;
// Убираем ненужные импорты, если они были только для ImageButton
// import android.widget.ImageButton;
import android.view.View; // Оставляем для Toolbar

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

// Добавляем импорт для MaterialCardView
import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialCardView cameraCard = findViewById(R.id.cameraCard);
        MaterialCardView videoCard = findViewById(R.id.videoCard);
        MaterialCardView galleryCard = findViewById(R.id.galleryCard);
        MaterialCardView audioCard = findViewById(R.id.audioCard);

        cameraCard.setOnClickListener(v -> {
            Intent intent = new Intent(this, CameraActivity.class);
            startActivity(intent);
        });

        videoCard.setOnClickListener(v -> {
            Intent intent = new Intent(this, VideoActivity.class);
            startActivity(intent);
        });

        audioCard.setOnClickListener(v -> {
            Intent intent = new Intent(this, AudioActivity.class);
            startActivity(intent);
        });

        galleryCard.setOnClickListener(v -> {
            Intent intent = new Intent(this, GalleryActivity.class);
            startActivity(intent);
        });
    }
}