package com.example.lab7;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AudioActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private TextView fileInfo;
    private Button playButton, pauseButton, stopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        fileInfo = findViewById(R.id.fileInfo);
        playButton = findViewById(R.id.btnPlay);
        pauseButton = findViewById(R.id.btnPause);
        stopButton = findViewById(R.id.btnStop);

        Uri uri = getIntent().getData();
        if (uri != null) {
            fileInfo.setText("Открыт аудиофайл: " + uri.getLastPathSegment());
            playAudio(uri);
        }

        playButton.setOnClickListener(v -> {
            if (mediaPlayer != null) mediaPlayer.start();
        });

        pauseButton.setOnClickListener(v -> {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) mediaPlayer.pause();
        });

        stopButton.setOnClickListener(v -> stopAudio());
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

    private void stopAudio() {
        if (mediaPlayer != null) {
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
