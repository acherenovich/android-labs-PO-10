package com.example.lab14;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class AudioActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private TextView timeText;
    private ImageButton playButton, pauseButton, rewindButton, forwardButton, startButton, endButton;
    private Handler handler = new Handler();

    private final int REWIND_TIME = 5000;
    private final int FORWARD_TIME = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        timeText = findViewById(R.id.timeText);
        seekBar = findViewById(R.id.seekBar);
        playButton = findViewById(R.id.playButton);
        pauseButton = findViewById(R.id.pauseButton);
        rewindButton = findViewById(R.id.rewindButton);
        forwardButton = findViewById(R.id.forwardButton);
        startButton = findViewById(R.id.startButton);
        endButton = findViewById(R.id.endButton);

        String audioUriString = getIntent().getStringExtra("audioUri");
        if (audioUriString != null) {
            Uri audioUri = Uri.parse(audioUriString);
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(this, audioUri);
                mediaPlayer.prepare();
                seekBar.setMax(mediaPlayer.getDuration());
            } catch (IOException e) {
                Toast.makeText(this, "Ошибка при воспроизведении аудиофайла", Toast.LENGTH_SHORT).show();
            }

            playButton.setOnClickListener(v -> startAudio());
            pauseButton.setOnClickListener(v -> pauseAudio());
            rewindButton.setOnClickListener(v -> seekAudio(-REWIND_TIME));
            forwardButton.setOnClickListener(v -> seekAudio(FORWARD_TIME));
            startButton.setOnClickListener(v -> seekAudio(-mediaPlayer.getCurrentPosition()));
            endButton.setOnClickListener(v -> seekAudio(mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition()));

            handler.postDelayed(updateTimeRunnable, 1000);

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        mediaPlayer.seekTo(progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    handler.removeCallbacks(updateTimeRunnable);
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    if (mediaPlayer.isPlaying()) {
                        handler.postDelayed(updateTimeRunnable, 1000);
                    }
                }
            });

        } else {
            Toast.makeText(this, "Ошибка: аудиофайл не найден", Toast.LENGTH_SHORT).show();
        }
    }

    private void startAudio() {
        mediaPlayer.start();
        playButton.setVisibility(ImageButton.GONE);
        pauseButton.setVisibility(ImageButton.VISIBLE);
        handler.postDelayed(updateTimeRunnable, 1000);
    }

    private void pauseAudio() {
        mediaPlayer.pause();
        playButton.setVisibility(ImageButton.VISIBLE);
        pauseButton.setVisibility(ImageButton.GONE);
        handler.removeCallbacks(updateTimeRunnable);
    }

    private void seekAudio(int time) {
        int newPosition = mediaPlayer.getCurrentPosition() + time;
        mediaPlayer.seekTo(Math.max(0, Math.min(newPosition, mediaPlayer.getDuration())));
    }

    private Runnable updateTimeRunnable = new Runnable() {
        @Override
        public void run() {
            int currentPosition = mediaPlayer.getCurrentPosition();
            int totalDuration = mediaPlayer.getDuration();
            seekBar.setProgress(currentPosition);

            String currentTime = formatTime(currentPosition);
            String totalTime = formatTime(totalDuration);
            timeText.setText(currentTime + " / " + totalTime);

            if (mediaPlayer.isPlaying()) {
                handler.postDelayed(this, 1000);
            }
        }
    };

    private String formatTime(int time) {
        int minutes = time / 60000;
        int seconds = (time % 60000) / 1000;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        handler.removeCallbacks(updateTimeRunnable);
    }
}
