package com.example.lab5;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
// import android.view.Menu; // Убрали импорт Menu
// import android.view.MenuItem; // Убрали импорт MenuItem
// import android.view.View; // Убрали импорт View (если не используется где-то еще)
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
// import androidx.appcompat.widget.Toolbar; // Убрали импорт Toolbar
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;


public class MainActivity extends AppCompatActivity {

    //для файлового менеджера
    private static final int PICKFILE_RESULT_CODE = 1;
    private String setType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestMediaPermissions();
        updateButtonStates(); // Проверяем и устанавливаем состояние кнопок

        Button audioBtn = findViewById(R.id.audioBtn);
        Button videoBtn = findViewById(R.id.videoBtn);
        Button imageBtn = findViewById(R.id.imageBtn);


        audioBtn.setOnClickListener(view -> {
            setType="audio/*";
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("audio/*"); //определяем тип
            startActivityForResult(intent, PICKFILE_RESULT_CODE);
        });

        videoBtn.setOnClickListener(view -> {
            setType="video/*";
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("video/*"); //определяем тип
            startActivityForResult(intent, PICKFILE_RESULT_CODE);
        });

        imageBtn.setOnClickListener(view -> {
            setType="image/*";
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*"); //определяем тип
            startActivityForResult(intent, PICKFILE_RESULT_CODE);
        });

    }


    private void requestMediaPermissions() {
        // Проверяем разрешения для каждого типа медиа
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            // Если разрешение не предоставлено, запрашиваем его
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_MEDIA_IMAGES,
                            Manifest.permission.READ_MEDIA_VIDEO,
                            Manifest.permission.READ_MEDIA_AUDIO
                    },
                    100); // requestCode можно выбрать любой
        } else {
            // Разрешения уже предоставлены
            // Toast.makeText(this, "Разрешения уже предоставлены", Toast.LENGTH_SHORT).show(); // Можно убрать, чтобы не мешало
        }
    }

    // Обработка результатов запроса разрешений
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            boolean allGranted = true; // Флаг для проверки всех разрешений
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    Toast.makeText(this, "Разрешение не предоставлено: " + permissions[i], Toast.LENGTH_SHORT).show();
                }
            }
            if (allGranted) {
                Toast.makeText(this, "Все разрешения получены!", Toast.LENGTH_SHORT).show();
            }
            updateButtonStates(); // Обновляем доступность кнопок
        }
    }

    private void updateButtonStates() {
        Button audioBtn = findViewById(R.id.audioBtn);
        Button videoBtn = findViewById(R.id.videoBtn);
        Button imageBtn = findViewById(R.id.imageBtn);

        boolean hasAudioPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED;
        boolean hasVideoPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED;
        boolean hasImagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;

        audioBtn.setEnabled(hasAudioPermission);
        videoBtn.setEnabled(hasVideoPermission);
        imageBtn.setEnabled(hasImagePermission);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICKFILE_RESULT_CODE && resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData();

            if (fileUri == null) {
                Toast.makeText(this, "Не удалось получить файл", Toast.LENGTH_SHORT).show();
                return;
            }

            // Определяем тип контента по URI, а не по setType, что надежнее
            String contentType = getContentResolver().getType(fileUri);

            if (contentType != null) {
                if (contentType.startsWith("audio/")) {
                    openAudio(fileUri);
                } else if (contentType.startsWith("video/")) {
                    openVideo(fileUri);
                } else if (contentType.startsWith("image/")) {
                    openImage(fileUri);
                } else {
                    Toast.makeText(this, "Неподдерживаемый тип файла: " + contentType, Toast.LENGTH_SHORT).show();
                }
            } else {
                // Если тип определить не удалось, пробуем по setType (менее надежно)
                if (setType.equals("audio/*")) {
                    openAudio(fileUri);
                } else if (setType.equals("video/*")) {
                    openVideo(fileUri);
                } else if (setType.equals("image/*")) {
                    openImage(fileUri);
                } else {
                    Toast.makeText(this, "Не удалось определить тип файла", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void openImage(Uri imageUri) {
        Intent intent = new Intent(this, ImageActivity.class);
        intent.putExtra("imageUri", imageUri.toString());
        startActivity(intent);
    }

    private void openAudio(Uri audioUri) {
        Intent intent = new Intent(this, AudioActivity.class);
        intent.putExtra("audioUri", audioUri.toString());
        startActivity(intent);
    }

    private void openVideo(Uri videoUri) {
        Intent intent = new Intent(this, VideoActivity.class);
        intent.putExtra("videoUri", videoUri.toString());
        startActivity(intent);
    }

}