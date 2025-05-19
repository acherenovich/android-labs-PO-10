package com.example.lab7;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ImageActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView fileInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        imageView = findViewById(R.id.imageView);
        fileInfo = findViewById(R.id.fileInfo);

        Uri uri = getIntent().getData();
        if (uri != null) {
            fileInfo.setText("Открыто изображение: " + uri.getLastPathSegment());
            displayImage(uri);
        } else {
            Toast.makeText(this, "Ошибка: URI изображения не найден", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayImage(Uri uri) {
        imageView.setImageURI(uri);
    }
}

