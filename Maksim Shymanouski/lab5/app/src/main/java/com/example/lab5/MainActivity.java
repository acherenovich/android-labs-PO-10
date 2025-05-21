package com.example.lab5;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;
import android.media.MediaPlayer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private Button selectFileButton, pauseButton, rewindButton, forwardButton;
    private ImageView imageView;
    private VideoView videoView;
    private MediaPlayer mediaPlayer;
    private TextView fileInfoText;

    private boolean isPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectFileButton = findViewById(R.id.selectFileButton);
        imageView = findViewById(R.id.imageView);
        videoView = findViewById(R.id.videoView);
        fileInfoText = findViewById(R.id.fileInfoText);

        pauseButton = findViewById(R.id.pauseButton);
        rewindButton = findViewById(R.id.rewindButton);
        forwardButton = findViewById(R.id.forwardButton);

        selectFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPaused) {
                    if (mediaPlayer != null) {
                        mediaPlayer.start();
                    } else if (videoView != null) {
                        videoView.start();
                    }
                    pauseButton.setText("Pause");
                    isPaused = false;
                } else {
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    } else if (videoView.isPlaying()) {
                        videoView.pause();
                    }
                    pauseButton.setText("Resume");
                    isPaused = true;
                }
            }
        });

        rewindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    int currentPos = mediaPlayer.getCurrentPosition();
                    mediaPlayer.seekTo(currentPos - 5000);
                } else if (videoView.isPlaying()) {
                    int currentPos = videoView.getCurrentPosition();
                    videoView.seekTo(currentPos - 5000);
                }
            }
        });

        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    int currentPos = mediaPlayer.getCurrentPosition();
                    mediaPlayer.seekTo(currentPos + 5000);
                } else if (videoView.isPlaying()) {
                    int currentPos = videoView.getCurrentPosition();
                    videoView.seekTo(currentPos + 5000);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            String fileExtension = getFileExtension(fileUri);

            String fileName = getFileName(fileUri);
            fileInfoText.setText("Chosen file: " + fileName);

            if (fileExtension.equals("jpg") || fileExtension.equals("png") || fileExtension.equals("jpeg")) {
                imageView.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.GONE);
                handleImageFile(fileUri);
            } else if (fileExtension.equals("mp3") || fileExtension.equals("wav")) {
                imageView.setVisibility(View.GONE);
                videoView.setVisibility(View.GONE);
                handleAudioFile(fileUri);
            } else if (fileExtension.equals("mp4") || fileExtension.equals("avi")) {
                imageView.setVisibility(View.GONE);
                videoView.setVisibility(View.VISIBLE);
                handleVideoFile(fileUri);
            }
        }
    }

    private String getFileExtension(Uri uri) {
        String extension = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    String fileName = cursor.getString(columnIndex);
                    extension = fileName.substring(fileName.lastIndexOf('.') + 1);
                }
            }
        }
        return extension;
    }

    private String getFileName(Uri uri) {
        String fileName = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    fileName = cursor.getString(columnIndex);
                }
            }
        }
        return fileName;
    }

    private void handleImageFile(Uri uri) {
        imageView.setImageURI(uri);
    }

    private void handleAudioFile(Uri uri) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, uri);
        mediaPlayer.start();
        pauseButton.setText("Pause");
        isPaused = false;
    }

    private void handleVideoFile(Uri uri) {
        videoView.setVideoURI(uri);
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        videoView.start();
        pauseButton.setText("Pause");
        isPaused = false;
    }
}
