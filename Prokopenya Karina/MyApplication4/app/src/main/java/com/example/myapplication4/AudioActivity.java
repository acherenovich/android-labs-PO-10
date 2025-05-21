package com.example.myapplication4;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class AudioActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private SeekBar seekBar, volumeSeekBar;
    private AudioManager audioManager;
    private Timer timer;
    private Button btnPlay, btnPause, btnStop, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        getSupportActionBar().setTitle("Выполнила Прокопеня Карина");

        // Инициализация элементов
        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        btnStop = findViewById(R.id.btnStop);
        btnBack = findViewById(R.id.btnBack);
        seekBar = findViewById(R.id.seekBar);
        volumeSeekBar = findViewById(R.id.volumeSeekBar);
        TextView tvAudioTitle = findViewById(R.id.tvAudioTitle);

        // Получаем строковый путь к файлу из Intent
        // Получаем строковый путь к файлу из Intent
        String audioPath = getIntent().getStringExtra("fileUri");

        if (audioPath == null || audioPath.isEmpty()) {
            tvAudioTitle.setText("Ошибка: Неверный путь к файлу");
            return;
        }

// Преобразуем строковый путь обратно в Uri
        Uri audioUri = Uri.parse(audioPath);

// Создание MediaPlayer
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(this, audioUri);  // Используем Uri вместо строки
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            tvAudioTitle.setText("Ошибка при загрузке аудио файла");
            return;
        }


        // Устанавливаем максимальное значение seekBar
        seekBar.setMax(mediaPlayer.getDuration());

        // Управление воспроизведением
        btnPlay.setOnClickListener(v -> {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                updateSeekBar();  // Обновляем seekBar после начала воспроизведения
            }
        });

        btnPause.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
        });

        btnStop.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                try {
                    mediaPlayer.prepare(); // Подготовить для повторного воспроизведения
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // Обновление seekBar в реальном времени
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (mediaPlayer.isPlaying()) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                }
            }
        }, 0, 500);

        // Перемотка аудио
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Управление громкостью
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        volumeSeekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volumeSeekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Кнопка назад
        btnBack.setOnClickListener(v -> {
            mediaPlayer.stop();
            timer.cancel();
            finish();
        });
    }

    // Обновление seekBar
    private void updateSeekBar() {
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (timer != null) {
            timer.cancel();
        }
    }
}
