package com.example.lab5rpomp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

public class AudioActivity extends AppCompatActivity {

    private static final int PICK_AUDIO_REQUEST = 2;
    private MediaPlayer mediaPlayer;
    private Button playPauseButton;
    private SeekBar timelineSeekBar;
    private SeekBar volumeSeekBar;
    private Uri audioUri;
    private Handler mHandler = new Handler(); // Handler для обновления SeekBar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        playPauseButton = findViewById(R.id.playPauseButton);
        Button selectAudioButton = findViewById(R.id.selectAudioButton);
        timelineSeekBar = findViewById(R.id.timelineSeekBar);
        volumeSeekBar = findViewById(R.id.volumeSeekBar);

        mediaPlayer = new MediaPlayer();
        playPauseButton.setEnabled(false);
        timelineSeekBar.setEnabled(false); // SeekBar таймлайна изначально неактивен

        selectAudioButton.setOnClickListener(v -> openFileChooser());

        playPauseButton.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                playPauseButton.setText("Воспроизвести");
            } else {
                mediaPlayer.start();
                playPauseButton.setText("Пауза");
                updateSeekBar(); // Запускаем обновление SeekBar при воспроизведении
            }
        });

        // Настройка SeekBar таймлайна
        timelineSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress); // Перематываем аудио при перемещении SeekBar пользователем
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Не требуется действий при начале касания
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Не требуется действий при завершении касания
            }
        });

        // Настройка SeekBar громкости
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volume = (float) progress / 100f; // Преобразуем progress (0-100) в диапазон громкости (0.0-1.0)
                mediaPlayer.setVolume(volume, volume); // Устанавливаем громкость для обоих каналов (левого и правого)
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Не требуется действий при начале касания
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Не требуется действий при завершении касания
            }
        });
        volumeSeekBar.setProgress(50); // Устанавливаем начальную громкость на 50%
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("audio/*");
        startActivityForResult(intent, PICK_AUDIO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_AUDIO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            audioUri = data.getData();
            prepareMediaPlayer();
        }
    }

    private void prepareMediaPlayer() {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(this, audioUri);
            mediaPlayer.prepareAsync();

            mediaPlayer.setOnPreparedListener(mp -> {
                playPauseButton.setEnabled(true);
                playPauseButton.setText("Воспроизвести");
                timelineSeekBar.setEnabled(true); // Активируем SeekBar таймлайна
                timelineSeekBar.setMax(mediaPlayer.getDuration()); // Устанавливаем максимальное значение SeekBar равным длительности аудио
                Toast.makeText(AudioActivity.this, "Аудиофайл готов к воспроизведению", Toast.LENGTH_SHORT).show();
            });

            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                Toast.makeText(AudioActivity.this, "Ошибка воспроизведения аудио", Toast.LENGTH_SHORT).show();
                playPauseButton.setEnabled(false);
                timelineSeekBar.setEnabled(false); // Деактивируем SeekBar таймлайна в случае ошибки
                return true;
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(AudioActivity.this, "Ошибка при подготовке аудиофайла", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateSeekBar() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            timelineSeekBar.setProgress(mediaPlayer.getCurrentPosition()); // Обновляем положение SeekBar
            Runnable runnable = this::updateSeekBar; // Создаем Runnable для повторного обновления
            mHandler.postDelayed(runnable, 100); // Запускаем Runnable с задержкой 100 мс (для плавного обновления)
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mHandler.removeCallbacksAndMessages(null); // Останавливаем Handler и все Runnable при уничтожении Activity
    }
}
