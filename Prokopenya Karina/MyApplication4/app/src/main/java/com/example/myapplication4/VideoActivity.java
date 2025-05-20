package com.example.myapplication4;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

public class VideoActivity extends AppCompatActivity {

    private VideoView videoView;
    private Button btnPlay, btnPause, btnStop, btnBack;
    private SeekBar seekBar;
    private Uri videoUri;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        getSupportActionBar().setTitle("Выполнила Прокопеня Карина");

        videoView = findViewById(R.id.videoView);
        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        btnStop = findViewById(R.id.btnStop);
        btnBack = findViewById(R.id.btnBack);
        seekBar = findViewById(R.id.seekBar);

        // Получаем Uri видеофайла из Intent
        String videoPath = getIntent().getStringExtra("fileUri");
        if (videoPath != null) {
            videoUri = Uri.parse(videoPath);
            videoView.setVideoURI(videoUri);
        }

        // Обновление ползунка в фоне
        videoView.setOnPreparedListener(mp -> {
            seekBar.setMax(videoView.getDuration());
            updateSeekBar();
        });

        // Обработчик кнопки "Играть"
        btnPlay.setOnClickListener(v -> {
            videoView.start();
            updateSeekBar();
        });

        // Обработчик кнопки "Пауза"
        btnPause.setOnClickListener(v -> videoView.pause());

        // Обработчик кнопки "Стоп"
        btnStop.setOnClickListener(v -> {
            videoView.stopPlayback();
            videoView.setVideoURI(videoUri); // Перезагружаем видео
            seekBar.setProgress(0);
        });

        // Обработчик кнопки "Назад"
        btnBack.setOnClickListener(v -> finish());

        // Обработчик изменения положения ползунка
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    videoView.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    // Метод для обновления ползунка
    private void updateSeekBar() {
        handler.postDelayed(() -> {
            if (videoView.isPlaying()) {
                seekBar.setProgress(videoView.getCurrentPosition());
                updateSeekBar();
            }
        }, 500);
    }
}
