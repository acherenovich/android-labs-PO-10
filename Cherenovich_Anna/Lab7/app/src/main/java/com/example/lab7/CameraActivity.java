package com.example.lab7;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.Toast;

import android.Manifest;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.FocusMeteringAction;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.MeteringPoint;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CameraActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101; //код запроса разрешения для камеры
    private PreviewView previewView; //элемент интерфейса, где отображается изображение с камеры
    private ImageCapture imageCapture; //объект, отвечающий за захват изображений
    private int currentCameraId = CameraSelector.LENS_FACING_BACK;
    private Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        previewView = findViewById(R.id.previewView);
        Button captureButton = findViewById(R.id.captureButton);
        Button switchCameraButton = findViewById(R.id.switchCameraButton);
        Button showPhotoButton = findViewById(R.id.showPhotoButton);
        Button backButton = findViewById(R.id.backButton);

        if (checkPermission()) {
            startCamera();
        } else {
            requestPermission();
        }

        captureButton.setOnClickListener(v -> takePhoto());
        switchCameraButton.setOnClickListener(v -> switchCamera());
        showPhotoButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, PhotoActivity.class);
            startActivity(intent);
        });
        backButton.setOnClickListener(v -> finish());

        setupTapToFocus();
    }

    private void switchCamera() {
        if (currentCameraId == CameraSelector.LENS_FACING_BACK) {
            currentCameraId = CameraSelector.LENS_FACING_FRONT;
        } else {
            currentCameraId = CameraSelector.LENS_FACING_BACK;
        }
        startCamera();
    }

    private void setupTapToFocus() {
        previewView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                MeteringPoint point = previewView.getMeteringPointFactory().createPoint(event.getX(), event.getY());
                FocusMeteringAction action = new FocusMeteringAction.Builder(point).build();
                camera.getCameraControl().startFocusAndMetering(action);
            }
            return true;
        });
    }

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(this, "Камера не доступна без разрешения", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this); // Получаем Future

        cameraProviderFuture.addListener(() -> { // Указываем слушатель
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get(); // Извлекаем объект

                // Настроим Preview
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                // Настроим ImageCapture
                imageCapture = new ImageCapture.Builder().build();

                // Выбираем камеру (заднюю)
                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(currentCameraId)
                        .build();



                // Отвязываем все камеры, чтобы не было конфликтов
                cameraProvider.unbindAll();

                // Привязываем камеры к жизненному циклу
                camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void takePhoto() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "photo_" + System.currentTimeMillis() + ".jpg");
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM + "/Camera"); // Папка камеры

        Uri imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        if (imageUri == null) {
            Log.e("TAKEPHOTO", "imageUri == null");
            runOnUiThread(() ->
                    Toast.makeText(CameraActivity.this, "Ошибка при создании файла", Toast.LENGTH_SHORT).show());
            return;
        }

        try {
            OutputStream outputStream = getContentResolver().openOutputStream(imageUri);
            if (outputStream == null) {
                Log.e("TAKEPHOTO", "OutputStream == null");
                return;
            }

            ImageCapture.OutputFileOptions options =
                    new ImageCapture.OutputFileOptions.Builder(outputStream).build();

            imageCapture.takePicture(options, Executors.newSingleThreadExecutor(),
                    new ImageCapture.OnImageSavedCallback() {
                        @Override
                        public void onImageSaved(@NonNull ImageCapture.OutputFileResults output) {
                            Log.d("TAKEPHOTO", "Image saved: " + imageUri);
                            runOnUiThread(() ->
                                    Toast.makeText(CameraActivity.this, "Фото сохранено!", Toast.LENGTH_SHORT).show());
                            try {
                                outputStream.close(); // Закрываем поток после сохранения
                            } catch (IOException e) {
                                Log.e("TAKEPHOTO", "Ошибка при закрытии потока", e);
                            }
                        }

                        @Override
                        public void onError(@NonNull ImageCaptureException exception) {
                            Log.e("TAKEPHOTO", "Ошибка сохранения: " + exception.getMessage());
                            exception.printStackTrace();
                        }
                    });

        } catch (Exception e) {
            Log.e("TAKEPHOTO", "Ошибка при создании OutputStream", e);
        }
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
