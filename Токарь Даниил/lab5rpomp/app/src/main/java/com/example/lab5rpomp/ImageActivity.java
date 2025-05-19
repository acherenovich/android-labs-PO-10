package com.example.lab5rpomp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

public class ImageActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        imageView = findViewById(R.id.imageView);
        Button selectImageButton = findViewById(R.id.selectImageButton);

        selectImageButton.setOnClickListener(v -> openFileChooser());
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT); // Используем ACTION_OPEN_DOCUMENT для выбора файлов из разных источников
        intent.addCategory(Intent.CATEGORY_OPENABLE); // Добавляем категорию OPENABLE
        intent.setType("image/*"); // Указываем MIME-тип "image/*" для фильтрации только изображений
        startActivityForResult(intent, PICK_IMAGE_REQUEST); // Запускаем intent с ожиданием результата
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            imageView.setImageURI(imageUri); // Устанавливаем URI изображения в ImageView
        }
    }
}