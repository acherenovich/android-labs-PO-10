package com.example.lab7;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        View customView = getLayoutInflater().inflate(R.layout.custom_actionbar, null);
        getSupportActionBar().setCustomView(customView);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        ImageButton cameraBtn = findViewById(R.id.cameraBtn);
        ImageButton videoBtn = findViewById(R.id.videoBtn);
        ImageButton galleryBtn = findViewById(R.id.galleryBtn);
        ImageButton audioBtn = findViewById(R.id.audioBtn);

        cameraBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, CameraActivity.class);
            startActivity(intent);
        });

        videoBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, VideoActivity.class);
            startActivity(intent);
        });

        audioBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, AudioActivity.class);
            startActivity(intent);
        });

        galleryBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, GalleryActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.labinfo_settings) {
            Intent intent = new Intent(this, LabinfoActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}