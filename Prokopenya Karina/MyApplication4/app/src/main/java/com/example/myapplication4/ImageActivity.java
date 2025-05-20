package com.example.myapplication4;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        getSupportActionBar().setTitle("Выполнила Прокопеня Карина");

        ImageView imageView = findViewById(R.id.imageView);
        Button btnBack = findViewById(R.id.btnBack);

        Uri imageUri = Uri.parse(getIntent().getStringExtra("fileUri"));
        imageView.setImageURI(imageUri);

        btnBack.setOnClickListener(v -> finish());
    }
}
