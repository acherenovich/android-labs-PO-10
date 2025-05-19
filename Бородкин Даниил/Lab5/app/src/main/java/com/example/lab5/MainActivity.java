package com.example.lab5;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import java.io.IOException;
public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private VideoView videoView;
    private MediaPlayer mediaPlayer;
    private TextView fileInfo;
    private Button playButton, pauseButton, stopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        videoView = findViewById(R.id.videoView);
        Button btnSelectFile = findViewById(R.id.btnSelectFile);
        fileInfo = findViewById(R.id.fileInfo);
        playButton = findViewById(R.id.btnPlay);
        pauseButton = findViewById(R.id.btnPause);
        stopButton = findViewById(R.id.btnStop);

        requestPermissions();

        ActivityResultLauncher<Intent> filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        handleFile(uri);
                    }
                }
        );

        btnSelectFile.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            filePickerLauncher.launch(intent);
        });

        playButton.setOnClickListener(v -> playMedia());
        pauseButton.setOnClickListener(v -> pauseMedia());
        stopButton.setOnClickListener(v -> stopMedia());
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            }
        }
    }

    private void handleFile(Uri uri) {
        String mimeType = getContentResolver().getType(uri);

        if (mimeType == null) {
            Toast.makeText(this, "Не удалось определить тип файла", Toast.LENGTH_SHORT).show();
            return;
        }

        fileInfo.setText("Открыт файл: " + uri.getLastPathSegment());

        // Скрываем все элементы перед загрузкой нового файла
        imageView.setVisibility(ImageView.GONE);
        videoView.setVisibility(VideoView.GONE);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        // В зависимости от типа файла показываем соответствующий элемент
        if (mimeType.startsWith("image")) {
            displayImage(uri);
        } else if (mimeType.startsWith("audio")) {
            playAudio(uri);
        } else if (mimeType.startsWith("video")) {
            playVideo(uri);
        } else {
            Toast.makeText(this, "Неподдерживаемый формат файла", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayImage(Uri uri) {
        imageView.setVisibility(ImageView.VISIBLE);
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            Toast.makeText(this, "Ошибка загрузки изображения", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void playAudio(Uri uri) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, uri);
        if (mediaPlayer != null) {
            mediaPlayer.start();
        } else {
            Toast.makeText(this, "Ошибка воспроизведения аудио", Toast.LENGTH_SHORT).show();
        }
    }

    private void playVideo(Uri uri) {
        videoView.setVisibility(VideoView.VISIBLE);
        videoView.setVideoURI(uri);
        videoView.start();
    }

    private void playMedia() {
        if (videoView.getVisibility() == VideoView.VISIBLE) {
            videoView.start();
        } else if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    private void pauseMedia() {
        if (videoView.getVisibility() == VideoView.VISIBLE && videoView.isPlaying()) {
            videoView.pause();
        } else if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    private void stopMedia() {
        if (videoView.getVisibility() == VideoView.VISIBLE) {
            videoView.stopPlayback();
            videoView.setVisibility(VideoView.GONE);
        } else if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        fileInfo.setText("Файл не выбран");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
