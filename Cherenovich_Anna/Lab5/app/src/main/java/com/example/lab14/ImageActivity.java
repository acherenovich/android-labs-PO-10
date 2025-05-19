package com.example.lab14;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        String imageUriString = getIntent().getStringExtra("imageUri");
        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "Ошибка: изображение не найдено", Toast.LENGTH_SHORT).show();
        }
    }
}
