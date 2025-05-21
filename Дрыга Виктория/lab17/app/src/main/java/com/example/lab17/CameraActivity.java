package com.example.lab17;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.OutputStream;


public class CameraActivity extends AppCompatActivity {
    private Camera camera;
    private SurfaceView preview;
    private SurfaceHolder surfaceHolder;
    private Button shotBtn;

    private void saveImageToGallery(byte[] data) {
        if (data == null || data.length == 0) {
            Toast.makeText(this, "Ошибка: пустые данные изображения", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "photo_" + System.currentTimeMillis() + ".jpg");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

        Uri imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        if (imageUri != null) {
            try (OutputStream outputStream = getContentResolver().openOutputStream(imageUri)) {
                if (outputStream != null) {
                    outputStream.write(data);
                    outputStream.flush();
                    Toast.makeText(this, "Фото сохранено в Галерею!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Ошибка записи файла", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Ошибка сохранения", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Ошибка: не удалось создать файл", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        preview = findViewById(R.id.surfaceCamera);
        shotBtn = findViewById(R.id.bCameraShot);
        Button backButton = findViewById(R.id.bCameraBack);

        surfaceHolder = preview.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); // Для корректного отображения камеры
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (camera != null) {
                    try {
                        camera.setPreviewDisplay(holder);
                        camera.startPreview();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                if (camera != null) {
                    camera.stopPreview();
                    try {
                        camera.setPreviewDisplay(holder);
                        camera.startPreview();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (camera != null) {
                    camera.stopPreview();
                }
            }
        });

        shotBtn.setOnClickListener(v -> {
            if (camera != null) {
                camera.autoFocus((success, camera) -> {
                    if (success) {
                        camera.takePicture(null, null, (data, cam) -> {
                            if (data != null) {
                                saveImageToGallery(data);
                            } else {
                                Toast.makeText(this, "Ошибка: данные изображения отсутствуют", Toast.LENGTH_SHORT).show();
                            }
                            camera.startPreview();
                        });
                    }
                });
            }
        });

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(CameraActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            camera = Camera.open();
            camera.setDisplayOrientation(90); // Поворот камеры для корректного отображения
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 200);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }
}
