package com.example.myapplication6;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        getSupportActionBar().setTitle("Выполнила Прокопеня Карина");

        ImageView imageView = findViewById(R.id.imageView);
        Button backButton = findViewById(R.id.backButton);

        // Получаем URI фото из Intent
        String imageUriString = getIntent().getStringExtra("imageUri");
        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            imageView.setImageURI(imageUri); // Устанавливаем фото
        } else {
            Toast.makeText(this, "Фото не найдено!", Toast.LENGTH_SHORT).show();
        }

        backButton.setOnClickListener(v -> finish());
    }
}
