package com.example.lab5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openVideoPlayer(View view) {
        startActivity(new Intent(this, VideoPlayerActivity.class));
    }

    public void openAudioPlayer(View view) {
        startActivity(new Intent(this, AudioPlayerActivity.class));
    }

    public void openImageGallery(View view) {
        startActivity(new Intent(this, ImageGalleryActivity.class));
    }
}
