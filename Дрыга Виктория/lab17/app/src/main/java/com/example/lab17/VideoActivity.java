package com.example.lab17;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class VideoActivity extends AppCompatActivity {
    private VideoView videoView;
    private SeekBar seekBar;
    private ImageButton playButton, pauseButton, rewindButton, forwardButton, startButton, endButton;

    private final int REWIND_TIME = 5000; // Перемотка назад на 5 секунд
    private final int FORWARD_TIME = 10000; // Перемотка вперед на 10 секунд
    private final Handler handler = new Handler();
    private Runnable updateSeekBarTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        videoView = findViewById(R.id.videoView);
        seekBar = findViewById(R.id.seekBar);
        playButton = findViewById(R.id.playButton);
        pauseButton = findViewById(R.id.pauseButton);
        rewindButton = findViewById(R.id.rewindButton);
        forwardButton = findViewById(R.id.forwardButton);
        startButton = findViewById(R.id.startButton); // Кнопка перемотки в начало
        endButton = findViewById(R.id.endButton); // Кнопка перемотки в конец

        // Получаем URI видеофайла
        String videoUriString = getIntent().getStringExtra("videoUri");
        if (videoUriString != null) {
            Uri videoUri = Uri.parse(videoUriString);
            videoView.setVideoURI(videoUri);

            videoView.setOnPreparedListener(mediaPlayer -> {
                seekBar.setMax(videoView.getDuration());
                updateSeekBar();
            });

            // Автоматический переход в начало после окончания видео
            videoView.setOnCompletionListener(mp -> {
                playButton.setVisibility(ImageButton.VISIBLE);
                pauseButton.setVisibility(ImageButton.INVISIBLE);
            });
        } else {
            Toast.makeText(this, "Ошибка: видео не найдено", Toast.LENGTH_SHORT).show();
        }

        // Обработчики кнопок
        playButton.setOnClickListener(view -> {
            videoView.start();
            playButton.setVisibility(ImageButton.INVISIBLE);
            pauseButton.setVisibility(ImageButton.VISIBLE);
            updateSeekBar();  // Перезапуск обновления SeekBar
        });

        pauseButton.setOnClickListener(view -> {
            videoView.pause();
            playButton.setVisibility(ImageButton.VISIBLE);
            pauseButton.setVisibility(ImageButton.INVISIBLE);
            handler.removeCallbacks(updateSeekBarTask);  // Остановить обновление SeekBar
        });

        // Перемотка назад на 5 секунд и вперед на 10 секунд
        rewindButton.setOnClickListener(view -> seekVideo(-REWIND_TIME)); // Перемотка назад на 5 секунд
        forwardButton.setOnClickListener(view -> seekVideo(FORWARD_TIME)); // Перемотка вперед на 10 секунд

        startButton.setOnClickListener(view -> videoView.seekTo(0)); // Перемотка в начало
        endButton.setOnClickListener(view -> videoView.seekTo(videoView.getDuration())); // Перемотка в конец

        // Обновление seekBar при изменении позиции видео
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

    // Метод перемотки видео
    private void seekVideo(int milliseconds) {
        int newPosition = videoView.getCurrentPosition() + milliseconds;
        videoView.seekTo(Math.max(0, Math.min(newPosition, videoView.getDuration())));
    }

    // Обновление ползунка
    private void updateSeekBar() {
        updateSeekBarTask = new Runnable() {
            @Override
            public void run() {
                if (videoView.isPlaying()) {
                    seekBar.setProgress(videoView.getCurrentPosition());
                    handler.postDelayed(this, 500);  // обновление каждые 500ms
                }
            }
        };
        handler.post(updateSeekBarTask);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateSeekBarTask);
    }
}
