package com.example.lab5;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.VideoView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private VideoView videoView;
    private LinearLayout audioControls, videoControls;
    private Button btnPlayAudio, btnPauseAudio, btnStopAudio;
    private Button btnPlayVideo, btnPauseVideo, btnStopVideo;
    private Uri selectedFileUri;
    private MediaPlayer mediaPlayer;

    private final ActivityResultLauncher<Intent> filePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedFileUri = result.getData().getData();
                    if (selectedFileUri != null) {
                        processSelectedFile(selectedFileUri);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnChooseFile = findViewById(R.id.btn_choose_file);
        imageView = findViewById(R.id.imageView);
        videoView = findViewById(R.id.videoView);

        // Аудио элементы
        audioControls = findViewById(R.id.audioControls);
        btnPlayAudio = findViewById(R.id.btn_play_audio);
        btnPauseAudio = findViewById(R.id.btn_pause_audio);
        btnStopAudio = findViewById(R.id.btn_stop_audio);

        // Видео элементы
        videoControls = findViewById(R.id.videoControls);
        btnPlayVideo = findViewById(R.id.btn_play_video);
        btnPauseVideo = findViewById(R.id.btn_pause_video);
        btnStopVideo = findViewById(R.id.btn_stop_video);

        btnChooseFile.setOnClickListener(v -> openFileChooser());

        // Управление аудио
        btnPlayAudio.setOnClickListener(v -> playAudio());
        btnPauseAudio.setOnClickListener(v -> pauseAudio());
        btnStopAudio.setOnClickListener(v -> stopAudio());

        // Управление видео
        btnPlayVideo.setOnClickListener(v -> playVideo());
        btnPauseVideo.setOnClickListener(v -> pauseVideo());
        btnStopVideo.setOnClickListener(v -> stopVideo());
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        filePickerLauncher.launch(Intent.createChooser(intent, "Выберите файл"));
    }

    private void processSelectedFile(Uri fileUri) {
        String fileType = getContentResolver().getType(fileUri);

        imageView.setVisibility(View.GONE);
        videoView.setVisibility(View.GONE);
        audioControls.setVisibility(View.GONE);
        videoControls.setVisibility(View.GONE);

        if (fileType != null) {
            if (fileType.startsWith("image/")) {
                imageView.setVisibility(View.VISIBLE);
                Glide.with(this).load(fileUri).into(imageView);
            } else if (fileType.startsWith("video/")) {
                videoView.setVisibility(View.VISIBLE);
                videoView.setVideoURI(fileUri);
                videoControls.setVisibility(View.VISIBLE);
            } else if (fileType.startsWith("audio/")) {
                audioControls.setVisibility(View.VISIBLE);
                prepareMediaPlayer(fileUri);
            }
        }
    }

    private void prepareMediaPlayer(Uri audioUri) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(this, audioUri);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playAudio() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    private void pauseAudio() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    private void stopAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void playVideo() {
        if (videoView != null) {
            videoView.start();
        }
    }

    private void pauseVideo() {
        if (videoView != null && videoView.isPlaying()) {
            videoView.pause();
        }
    }

    private void stopVideo() {
        if (videoView != null) {
            videoView.stopPlayback();
            videoView.setVideoURI(selectedFileUri); // Нужно заново установить видео
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
