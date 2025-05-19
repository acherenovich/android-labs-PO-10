package com.example.lab14;

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
    private ImageButton playPauseButton, rewindButton, forwardButton, startButton, endButton;

    private final int REWIND_TIME = 5000;
    private final int FORWARD_TIME = 10000;
    private final Handler handler = new Handler();
    private Runnable updateSeekBarTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        videoView = findViewById(R.id.videoView);
        seekBar = findViewById(R.id.seekBar);
        playPauseButton = findViewById(R.id.playPauseButton);
        rewindButton = findViewById(R.id.rewindButton);
        forwardButton = findViewById(R.id.forwardButton);
        startButton = findViewById(R.id.startButton);
        endButton = findViewById(R.id.endButton);

        String videoUriString = getIntent().getStringExtra("videoUri");
        if (videoUriString != null) {
            Uri videoUri = Uri.parse(videoUriString);
            videoView.setVideoURI(videoUri);

            videoView.setOnPreparedListener(mediaPlayer -> {
                seekBar.setMax(videoView.getDuration());
                updateSeekBar();
            });

            videoView.setOnCompletionListener(mp -> {
                playPauseButton.setImageResource(R.drawable.play_button);
            });
        } else {
            Toast.makeText(this, "Ошибка: видео не найдено", Toast.LENGTH_SHORT).show();
        }

        playPauseButton.setOnClickListener(view -> {
            if (videoView.isPlaying()) {
                videoView.pause();
                playPauseButton.setImageResource(R.drawable.play_button);
                handler.removeCallbacks(updateSeekBarTask);
            } else {
                videoView.start();
                playPauseButton.setImageResource(R.drawable.pause_button);
                updateSeekBar();
            }
        });

        rewindButton.setOnClickListener(view -> seekVideo(-REWIND_TIME));
        forwardButton.setOnClickListener(view -> seekVideo(FORWARD_TIME));
        startButton.setOnClickListener(view -> videoView.seekTo(0));
        endButton.setOnClickListener(view -> videoView.seekTo(videoView.getDuration()));

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

    private void seekVideo(int milliseconds) {
        int newPosition = videoView.getCurrentPosition() + milliseconds;
        videoView.seekTo(Math.max(0, Math.min(newPosition, videoView.getDuration())));
    }

    private void updateSeekBar() {
        updateSeekBarTask = new Runnable() {
            @Override
            public void run() {
                if (videoView.isPlaying()) {
                    seekBar.setProgress(videoView.getCurrentPosition());
                    handler.postDelayed(this, 500);
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
