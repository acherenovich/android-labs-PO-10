package com.example.lab7;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build; // <<< Добавить импорт
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
    private static final int REQUEST_PERMISSION_READ_MEDIA = 101; // Изменим код запроса для ясности

    private ImageView imageView;
    private TextView textInfo;
    private ImageButton btnPrev, btnNext;
    // private Button btnBack; // Закомментировано, т.к. не используется в коде

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
        // btnBack = findViewById(R.id.btnBack); // Если кнопка есть в layout, раскомментируйте

        checkAndRequestPermissions(); // Переименуем для ясности

        btnPrev.setOnClickListener(v -> {
            if (!imageUris.isEmpty() && currentIndex > 0) {
                currentIndex--;
                updateImage();
            }
        });

        btnNext.setOnClickListener(v -> {
            if (!imageUris.isEmpty() && currentIndex < imageUris.size() - 1) {
                currentIndex++;
                updateImage();
            }
        });

        // Пример обработчика для кнопки "Назад", если она нужна
        /*
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                finish(); // Закрыть текущую активность
            });
        }
        */
    }

    // Обновленный метод для проверки и запроса разрешений
    private void checkAndRequestPermissions() {
        String permissionToRequest;

        // Выбираем нужное разрешение в зависимости от версии SDK
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13 (API 33) и выше
            permissionToRequest = Manifest.permission.READ_MEDIA_IMAGES;
        } else { // Android ниже 13 (включая 11 и 12)
            permissionToRequest = Manifest.permission.READ_EXTERNAL_STORAGE;
        }

        // Проверяем, есть ли уже разрешение
        if (ContextCompat.checkSelfPermission(this, permissionToRequest) == PackageManager.PERMISSION_GRANTED) {
            // Разрешение уже есть, загружаем изображения
            loadImagesFromGallery();
        } else {
            // Разрешения нет, запрашиваем его у пользователя
            ActivityCompat.requestPermissions(this, new String[]{permissionToRequest}, REQUEST_PERMISSION_READ_MEDIA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION_READ_MEDIA) {
            // Проверяем, было ли предоставлено разрешение
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Разрешение получено, загружаем изображения
                loadImagesFromGallery();
            } else {
                // Пользователь отказал в предоставлении разрешения
                Toast.makeText(this, "Разрешение на чтение изображений не предоставлено!", Toast.LENGTH_LONG).show();
                // Можно добавить дополнительную логику, например, показать объяснение
                // или закрыть активность, если без разрешения она бесполезна.
                textInfo.setText("Нет доступа к галерее");
                imageView.setImageResource(android.R.drawable.ic_menu_gallery); // Показать заглушку
            }
        }
    }

    private void loadImagesFromGallery() {
        imageUris.clear(); // Очищаем список перед загрузкой
        ContentResolver contentResolver = getContentResolver();

        // Определяем URI для запроса изображений из внешнего хранилища
        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { // API 29+
            collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        // Какие данные мы хотим получить для каждого изображения
        String[] projection = {
                MediaStore.Images.Media._ID, // ID нужен для создания content URI
                // MediaStore.Images.Media.DISPLAY_NAME, // Имя файла (можно добавить)
                // MediaStore.Images.Media.DATE_ADDED // Дата добавления (для сортировки)
        };

        // --- УБИРАЕМ ФИЛЬТРАЦИЮ ПО ПАПКЕ ---
        String selection = null; // null означает "выбрать все"
        String[] selectionArgs = null; // null, так как нет плейсхолдеров (?) в selection
        // ------------------------------------

        // Сортируем по дате добавления, новые сверху
        String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";

        try (Cursor cursor = contentResolver.query(
                collection,
                projection,
                selection,     // <-- null, чтобы получить все изображения
                selectionArgs, // <-- null
                sortOrder
        )) {
            if (cursor != null) {
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);

                while (cursor.moveToNext()) {
                    long id = cursor.getLong(idColumn);
                    // Создаем URI для конкретного изображения
                    Uri contentUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Long.toString(id));
                    imageUris.add(contentUri);
                }
                // cursor.close() вызывается автоматически благодаря try-with-resources
            }
        } catch (Exception e) {
            // Обработка возможных ошибок при запросе
            e.printStackTrace();
            Toast.makeText(this, "Ошибка при загрузке изображений", Toast.LENGTH_SHORT).show();
        }

        // Обновляем интерфейс после загрузки
        if (!imageUris.isEmpty()) {
            currentIndex = 0;
            updateImage(); // Показываем первое изображение
            // Управляем доступностью кнопок
            btnPrev.setEnabled(currentIndex > 0);
            btnNext.setEnabled(currentIndex < imageUris.size() - 1);
        } else {
            // Сообщение, если вообще нет изображений во внешнем хранилище
            Toast.makeText(this, "Изображения не найдены", Toast.LENGTH_SHORT).show();
            textInfo.setText("Нет изображений");
            imageView.setImageResource(android.R.drawable.ic_menu_gallery); // Показываем стандартную иконку галереи
            btnPrev.setEnabled(false);
            btnNext.setEnabled(false);
        }
    }
    // --- КОНЕЦ ИЗМЕНЕННОГО МЕТОДА ---

    private void updateImage() {
        if (currentIndex >= 0 && currentIndex < imageUris.size()) {
            try {
                imageView.setImageURI(imageUris.get(currentIndex));
                textInfo.setText("Изображение " + (currentIndex + 1) + " из " + imageUris.size());
                // Управляем доступностью кнопок
                btnPrev.setEnabled(currentIndex > 0);
                btnNext.setEnabled(currentIndex < imageUris.size() - 1);
            } catch (SecurityException se) {
                // Это может произойти, если URI стал недействительным или разрешение было отозвано
                se.printStackTrace();
                Toast.makeText(this, "Ошибка доступа к изображению", Toast.LENGTH_SHORT).show();
                // Можно попробовать удалить недействительный URI из списка и перейти к следующему/предыдущему
            } catch (Exception e) {
                // Другие возможные ошибки при загрузке URI
                e.printStackTrace();
                Toast.makeText(this, "Не удалось загрузить изображение", Toast.LENGTH_SHORT).show();
            }
        }
    }
}