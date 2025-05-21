package com.example.lab7;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView fileInfo;
    private ActivityResultLauncher<Intent> filePickerLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fileInfo = findViewById(R.id.fileInfo);
        Button btnSelectFile = findViewById(R.id.btnSelectFile);
        Button btnTakePhoto = findViewById(R.id.btnTakePhoto);

        requestPermissions();

        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        handleFile(uri);
                    }
                }
        );

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = new Intent(this, ImageActivity.class);
                        intent.setData(photoUri);
                        startActivity(intent);
                    }
                });

        btnSelectFile.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            filePickerLauncher.launch(intent);
        });

        btnTakePhoto.setOnClickListener(v -> takePhoto());
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissions = new ArrayList<>();

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.CAMERA);
            }

            if (!permissions.isEmpty()) {
                requestPermissions(permissions.toArray(new String[0]), 100);
            }
        }
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                File photoFile = createImageFile();
                if (photoFile != null) {
                    photoUri = FileProvider.getUriForFile(this, "com.example.lab7.fileprovider", photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    cameraLauncher.launch(takePictureIntent);
                }
            } catch (IOException e) {
                Toast.makeText(this, "Ошибка создания файла", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Камера недоступна", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File storageDir = getExternalFilesDir(null);
        return File.createTempFile("IMG_" + timeStamp, ".jpg", storageDir);
    }

    private void handleFile(Uri uri) {
        String mimeType = getContentResolver().getType(uri);

        if (mimeType == null) {
            Toast.makeText(this, "Не удалось определить тип файла", Toast.LENGTH_SHORT).show();
            return;
        }

        fileInfo.setText("Открыт файл: " + uri.getLastPathSegment());

        Intent intent;
        if (mimeType.startsWith("image")) {
            intent = new Intent(this, ImageActivity.class);
        } else if (mimeType.startsWith("audio")) {
            intent = new Intent(this, AudioActivity.class);
        } else if (mimeType.startsWith("video")) {
            intent = new Intent(this, VideoActivity.class);
        } else {
            Toast.makeText(this, "Неподдерживаемый формат файла", Toast.LENGTH_SHORT).show();
            return;
        }

        intent.setData(uri);
        startActivity(intent);
    }
}
