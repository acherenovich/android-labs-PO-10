package com.example.lab7;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class VideoActivity extends AppCompatActivity {

    private VideoView videoView;
    private TextView fileInfo;
    private Button playButton, pauseButton, stopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        videoView = findViewById(R.id.videoView);
        fileInfo = findViewById(R.id.fileInfo);
        playButton = findViewById(R.id.btnPlay);
        pauseButton = findViewById(R.id.btnPause);
        stopButton = findViewById(R.id.btnStop);

        Uri uri = getIntent().getData();
        if (uri != null) {
            fileInfo.setText("Открыто видео: " + uri.getLastPathSegment());
            playVideo(uri);
        }

        playButton.setOnClickListener(v -> videoView.start());
        pauseButton.setOnClickListener(v -> videoView.pause());
        stopButton.setOnClickListener(v -> stopVideo());
    }

    private void playVideo(Uri uri) {
        videoView.setVideoURI(uri);
        videoView.start();
    }

    private void stopVideo() {
        videoView.stopPlayback();
        videoView.setVisibility(View.GONE);
        fileInfo.setText("Файл не выбран");
    }
}
