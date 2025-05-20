package com.example.lab17;

import android.content.Intent; // Импорт для Intent
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.os.Environment;

public class GalleryActivity extends AppCompatActivity {
    private ImageView imageView;
    private Button btnPrevious, btnNext, btnExit;
    private TextView photoCount;
    private List<String> imagePaths;
    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        imageView = findViewById(R.id.imageView);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        btnExit = findViewById(R.id.btnExit);
        photoCount = findViewById(R.id.photoCount);
        imagePaths = new ArrayList<>();

        loadImages(); // Загружаем изображения при создании активности
        updateImage();

        btnPrevious.setOnClickListener(v -> showPreviousImage());
        btnNext.setOnClickListener(v -> showNextImage());
        btnExit.setOnClickListener(v -> exitToMainMenu());
    }

    private void loadImages() {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (storageDir != null) {
            File[] imageFiles = storageDir.listFiles();
            if (imageFiles != null) {
                for (File imageFile : imageFiles) {
                    if (isImageFile(imageFile)) { // Проверка на изображение
                        imagePaths.add(imageFile.getAbsolutePath());
                    }
                }
            }
        } else {
            Toast.makeText(this, "Директория не найдена", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isImageFile(File file) {
        String[] imageExtensions = {".jpg", ".jpeg", ".png", ".gif", ".bmp"};
        for (String extension : imageExtensions) {
            if (file.getName().toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    private void updateImage() {
        if (!imagePaths.isEmpty()) {
            String imagePath = imagePaths.get(currentIndex);
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
               // photoCount.setText((currentIndex + 1) + " / " + imagePaths.size()); // Обновление счётчика изображений
            } else {
                // Если изображение не загружается, пробуем следующее изображение
                showNextImage();
            }
        } else {
            Toast.makeText(this, "Нет изображений для отображения", Toast.LENGTH_SHORT).show();
        }
    }

    private void showPreviousImage() {
        if (currentIndex > 0) {
            currentIndex--;
        } else {
            currentIndex = imagePaths.size() - 1; // Переход к последнему изображению
        }
        updateImage();
    }

    private void showNextImage() {
        currentIndex++;

        // Обновляем индекс для бесконечного прокручивания
        if (currentIndex >= imagePaths.size()) {
            currentIndex = 0; // Переход к первому изображению
        }

        updateImage();
    }

    private void exitToMainMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // Закрываем текущую активность
    }
}