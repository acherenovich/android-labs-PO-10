package com.example.lab7;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSION = 100;

    private ImageView imageView;
    private TextView textInfo;
    private ImageButton btnPrev, btnNext;
    private Button btnBack;

    private int currentIndex = 0;
    private List<Uri> imageUris = new ArrayList<>(); // Список URI изображений из галереи

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        imageView = findViewById(R.id.imageView);
        textInfo = findViewById(R.id.textInfo);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);

        checkPermissions();

        btnPrev.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                updateImage();
            }
        });

        btnNext.setOnClickListener(v -> {
            if (currentIndex < imageUris.size() - 1) {
                currentIndex++;
                updateImage();
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_PERMISSION);
        } else {
            loadImagesFromGallery();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadImagesFromGallery();
            } else {
                Toast.makeText(this, "Разрешение не предоставлено!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadImagesFromGallery() {
        imageUris.clear();
        ContentResolver contentResolver = getContentResolver();
        Uri collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.Images.Media._ID};
        String selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " = ?";
        String[] selectionArgs = new String[]{"Camera"}; // Выбираем только папку "Камера"
        String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC"; // Последние фото первыми

        Cursor cursor = contentResolver.query(collection, projection, selection, selectionArgs, sortOrder);

        if (cursor != null) {
            int columnIndexId = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            while (cursor.moveToNext()) {
                long imageId = cursor.getLong(columnIndexId);
                Uri imageUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, String.valueOf(imageId));
                imageUris.add(imageUri);
            }
            cursor.close();
        }

        if (!imageUris.isEmpty()) {
            currentIndex = 0;
            updateImage();
        } else {
            Toast.makeText(this, "Альбом 'Камера' пуст", Toast.LENGTH_SHORT).show();
        }
    }


    private void updateImage() {
        if (!imageUris.isEmpty()) {
            imageView.setImageURI(imageUris.get(currentIndex));
            textInfo.setText("Изображение " + (currentIndex + 1) + " из " + imageUris.size());
        }
    }
}