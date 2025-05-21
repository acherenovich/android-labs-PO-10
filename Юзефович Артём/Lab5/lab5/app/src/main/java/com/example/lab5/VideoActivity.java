package com.example.lab5;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class VideoActivity extends AppCompatActivity {
    private VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        mVideoView = findViewById(R.id.videoView);
        Uri videoUri = Uri.parse(getIntent().getStringExtra("videoUri"));
        //Log.d("VIDEOURI", videoUri.toString());

        mVideoView.setVideoURI(videoUri);
        mVideoView.start();

        MediaController mediaController = new MediaController(this);
        mVideoView.setMediaController(mediaController);
        mediaController.setMediaPlayer(mVideoView);
    }
}
