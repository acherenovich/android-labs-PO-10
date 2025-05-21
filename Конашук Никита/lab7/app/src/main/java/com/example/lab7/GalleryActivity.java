package com.example.lab7;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import com.github.chrisbanes.photoview.PhotoView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GalleryActivity extends AppCompatActivity {
    PhotoView photoView;
    Button btnPickImage;
    static final int REQUEST_IMAGE = 103;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        photoView = findViewById(R.id.photoView);  // Инициализация PhotoView
        btnPickImage = findViewById(R.id.btnPickImage);

        btnPickImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_IMAGE);  // Запуск выбора изображения
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();  // Получаем URI выбранного изображения
            if (photoView != null) {
                photoView.setImageURI(imageUri);  // Устанавливаем изображение в PhotoView
            }
        }
    }
}
