package com.example.lab7;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button btnCamera, btnMedia, btnGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCamera = findViewById(R.id.btnCamera);
        btnMedia = findViewById(R.id.btnMedia);
        btnGallery = findViewById(R.id.btnGallery);

        Button btnHelp = findViewById(R.id.btnHelp);
        btnHelp.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HelpActivity.class);
            startActivity(intent);
        });



        btnCamera.setOnClickListener(v -> startActivity(new Intent(this, CameraActivity.class)));
        btnMedia.setOnClickListener(v -> startActivity(new Intent(this, MediaActivity.class)));
        btnGallery.setOnClickListener(v -> startActivity(new Intent(this, GalleryActivity.class)));
    }
}