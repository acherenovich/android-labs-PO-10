package com.example.lab17;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonCamera = findViewById(R.id.buttonCamera);
        Button buttonMedia = findViewById(R.id.buttonMedia);
        Button buttonGallery = findViewById(R.id.buttonGallery);
        Button buttonHelp = findViewById(R.id.buttonHelp); // Новая кнопка
        buttonCamera.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CameraActivity.class);
            startActivity(intent);
        });

        buttonMedia.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MediaActivity.class);
            startActivity(intent);
        });

        buttonGallery.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
            startActivity(intent);
        });

        buttonHelp.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HelpActivity.class);
            startActivity(intent);
        });
    }
}