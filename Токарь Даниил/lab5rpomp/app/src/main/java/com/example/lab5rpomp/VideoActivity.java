package com.example.lab5rpomp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.MediaController;

public class VideoActivity extends AppCompatActivity {

    private static final int PICK_VIDEO_REQUEST = 3;
    private VideoView videoView;
    private MediaController mediaController;
    private Uri videoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        videoView = findViewById(R.id.videoView);
        Button selectVideoButton = findViewById(R.id.selectVideoButton);

        // Инициализация MediaController для управления воспроизведением видео
        mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView); // Привязка контроллера к VideoView

        selectVideoButton.setOnClickListener(v -> openFileChooser());
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("video/*");
        startActivityForResult(intent, PICK_VIDEO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            videoUri = data.getData();
            videoView.setVideoURI(videoUri); // Устанавливаем URI видео в VideoView
            videoView.start(); // Начинаем воспроизведение видео
        }
    }
}