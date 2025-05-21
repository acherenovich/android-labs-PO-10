package com.example.laba5;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class VideoActivity extends AppCompatActivity {

    private VideoView videoView;
    private SeekBar seekBar;
    private Button btnPlayPause;
    private Button btnRewind;
    private Button btnForward;
    private Button btnReplay;
    private Timer timer;
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        videoView = findViewById(R.id.videoView);
        seekBar = findViewById(R.id.seekBar);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        btnRewind = findViewById(R.id.btnRewind);
        btnForward = findViewById(R.id.btnForward);
        btnReplay = findViewById(R.id.btnReplay);

        String fileUriString = getIntent().getStringExtra("fileUri");
        if (fileUriString != null) {
            Uri fileUri = Uri.parse(fileUriString);
            videoView.setVideoURI(fileUri);
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        videoView.setOnPreparedListener(mp -> {
            seekBar.setMax(videoView.getDuration());
            videoView.start();
            isPlaying = true;
            btnPlayPause.setText("Pause");

            updateSeekBar();
        });

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

        btnPlayPause.setOnClickListener(v -> {
            if (isPlaying) {
                videoView.pause();
                isPlaying = false;
                btnPlayPause.setText("Play");
            } else {
                videoView.start();
                isPlaying = true;
                btnPlayPause.setText("Pause");
                updateSeekBar();
            }
        });

        btnRewind.setOnClickListener(v -> {
            int currentPosition = videoView.getCurrentPosition();
            int newPosition = currentPosition - 5000;
            if (newPosition < 0) {
                newPosition = 0;
            }
            videoView.seekTo(newPosition);
        });

        btnForward.setOnClickListener(v -> {
            int currentPosition = videoView.getCurrentPosition();
            int newPosition = currentPosition + 5000;
            if (newPosition > videoView.getDuration()) {
                newPosition = videoView.getDuration();
            }
            videoView.seekTo(newPosition);
        });

        btnReplay.setOnClickListener(v -> {
            videoView.seekTo(0);
            videoView.start();
            isPlaying = true;
            btnPlayPause.setText("Pause");
            updateSeekBar();
        });
    }

    private void updateSeekBar() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> seekBar.setProgress(videoView.getCurrentPosition()));
            }
        }, 0, 500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}
