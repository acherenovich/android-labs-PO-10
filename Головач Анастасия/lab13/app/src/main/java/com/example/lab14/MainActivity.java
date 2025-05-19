package com.example.lab14;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_MUSIC_REQUEST = 1;
    private static final int PICK_VIDEO_REQUEST = 2;
    private static final int PICK_IMAGE_REQUEST = 3;
    private static final int REQUEST_CODE_PERMISSIONS = 100;

    private ImageView imageView;
    private VideoView videoView;
    private Button btnPlay, btnPause, btnStop;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        videoView = findViewById(R.id.videoView);
        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        btnStop = findViewById(R.id.btnStop);

        Button btnChooseMusic = findViewById(R.id.btnChooseMusic);
        Button btnChooseVideo = findViewById(R.id.btnChooseVideo);
        Button btnChooseImage = findViewById(R.id.btnChooseImage);

        btnChooseMusic.setOnClickListener(v -> openFileChooser("audio/*", PICK_MUSIC_REQUEST));
        btnChooseVideo.setOnClickListener(v -> openFileChooser("video/*", PICK_VIDEO_REQUEST));
        btnChooseImage.setOnClickListener(v -> openFileChooser("image/*", PICK_IMAGE_REQUEST));

        // Check for permissions
        checkAndRequestPermissions();
    }

    private void checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_AUDIO
            }, REQUEST_CODE_PERMISSIONS);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Handle permissions results if needed
    }

    private void openFileChooser(String mimeType, int requestCode) {
        if (requestCode == PICK_MUSIC_REQUEST && !checkAudioPermission()) {
            Toast.makeText(this, "Audio permission not granted. Please allow access to audio.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (requestCode == PICK_VIDEO_REQUEST && !checkVideoPermission()) {
            Toast.makeText(this, "Video permission not granted. Please allow access to video.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (requestCode == PICK_IMAGE_REQUEST && !checkImagePermission()) {
            Toast.makeText(this, "Image permission not granted. Please allow access to images.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(mimeType);
        startActivityForResult(intent, requestCode);
    }

    private boolean checkAudioPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED;
        }
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkVideoPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED;
        }
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkImagePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;
        }
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (requestCode == PICK_MUSIC_REQUEST) {
                playAudio(uri);
            } else if (requestCode == PICK_VIDEO_REQUEST) {
                playVideo(uri);
            } else if (requestCode == PICK_IMAGE_REQUEST) {
                displayImage(uri);
            }
        }
    }

    private void displayImage(Uri uri) {
        imageView.setVisibility(View.VISIBLE);
        videoView.setVisibility(View.GONE);
        btnPlay.setVisibility(View.VISIBLE);
        btnPause.setVisibility(View.VISIBLE);
        btnStop.setVisibility(View.VISIBLE);

        Intent intent = new Intent(MainActivity.this, ImageDisplayActivity.class);
        intent.putExtra("imageUri", uri);
        startActivity(intent);
    }

    private void playAudio(Uri uri) {
        imageView.setVisibility(View.GONE);
        videoView.setVisibility(View.GONE);
        btnPlay.setVisibility(View.VISIBLE);
        btnPause.setVisibility(View.VISIBLE);
        btnStop.setVisibility(View.VISIBLE);

        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, uri);

        btnPlay.setOnClickListener(v -> mediaPlayer.start());
        btnPause.setOnClickListener(v -> mediaPlayer.pause());
        btnStop.setOnClickListener(v -> {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        });
    }

    private void playVideo(Uri uri) {
        imageView.setVisibility(View.GONE);
        videoView.setVisibility(View.VISIBLE);
        btnPlay.setVisibility(View.VISIBLE);
        btnPause.setVisibility(View.VISIBLE);
        btnStop.setVisibility(View.VISIBLE);

        videoView.setVideoURI(uri);

        btnPlay.setOnClickListener(v -> videoView.start());
        btnPause.setOnClickListener(v -> videoView.pause());
        btnStop.setOnClickListener(v -> videoView.stopPlayback());
    }
}