package com.example.lab7;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.VideoView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MediaActivity extends AppCompatActivity {
    Button btnAudio, btnVideo, btnPlay, btnPause, btnStop;
    VideoView videoView;
    MediaPlayer mediaPlayer;
    static final int REQUEST_AUDIO = 101;
    static final int REQUEST_VIDEO = 102;
    boolean isAudio = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        btnAudio = findViewById(R.id.btnAudio);
        btnVideo = findViewById(R.id.btnVideo);
        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        btnStop = findViewById(R.id.btnStop);
        videoView = findViewById(R.id.videoView);

        btnAudio.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("audio/*");
            startActivityForResult(intent, REQUEST_AUDIO);
        });

        btnVideo.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("video/*");
            startActivityForResult(intent, REQUEST_VIDEO);
        });

        btnPlay.setOnClickListener(v -> {
            if (isAudio && mediaPlayer != null) mediaPlayer.start();
            else videoView.start();
        });

        btnPause.setOnClickListener(v -> {
            if (isAudio && mediaPlayer != null) mediaPlayer.pause();
            else videoView.pause();
        });

        btnStop.setOnClickListener(v -> {
            if (isAudio && mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            } else {
                videoView.stopPlayback();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (requestCode == REQUEST_AUDIO) {
                isAudio = true;
                if (mediaPlayer != null) mediaPlayer.release();
                mediaPlayer = MediaPlayer.create(this, uri);
            } else if (requestCode == REQUEST_VIDEO) {
                isAudio = false;
                videoView.setVideoURI(uri);
                videoView.start();
            }
        }
    }
}