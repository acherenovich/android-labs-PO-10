package com.example.lab14;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ImageDisplayActivity extends AppCompatActivity {
    private ImageView imageView;
    private Button btnBackToMain;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display); // Убедитесь, что используется правильный layout

        imageView = findViewById(R.id.imageViewFull);
        btnBackToMain = findViewById(R.id.btnBackToMain); // Находим кнопку

        // Получаем URI изображения из Intent
        Uri imageUri = getIntent().getParcelableExtra("imageUri");
        if (imageUri != null) {
            imageView.setImageURI(imageUri);
        }

        // Обработчик кнопки "Вернуться в главное меню"
        btnBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Закрываем текущую активность и возвращаемся в MainActivity
                finish();
            }
        });
    }
}