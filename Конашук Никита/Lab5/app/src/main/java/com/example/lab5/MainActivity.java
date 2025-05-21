package com.example.lab5;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private VideoView videoView;
    private MediaPlayer mediaPlayer;
    private Button selectImageButton, selectAudioButton, selectVideoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        videoView = findViewById(R.id.videoView);
        selectImageButton = findViewById(R.id.selectImageButton);
        selectAudioButton = findViewById(R.id.selectAudioButton);
        selectVideoButton = findViewById(R.id.selectVideoButton);

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser("image/*");
            }
        });

        selectAudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser("audio/*");
            }
        });

        selectVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser("video/*");
            }
        });
    }

    private void openFileChooser(String type) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(type);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Выберите файл"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            if (fileUri != null) {
                handleFile(fileUri);
            }
        }
    }

    private void handleFile(Uri fileUri) {
        String fileType = getContentResolver().getType(fileUri);

        if (fileType != null) {
            if (fileType.startsWith("image")) {
                showImage(fileUri);
            } else if (fileType.startsWith("audio")) {
                playAudio(fileUri);
            } else if (fileType.startsWith("video")) {
                playVideo(fileUri);
            } else {
                Toast.makeText(this, "Формат файла не поддерживается", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showImage(Uri imageUri) {
        imageView.setVisibility(View.VISIBLE);
        videoView.setVisibility(View.GONE);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        imageView.setImageURI(imageUri);
    }

    private void playAudio(Uri audioUri) {
        imageView.setVisibility(View.GONE);
        videoView.setVisibility(View.GONE);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, audioUri);
        mediaPlayer.start();
    }

    private void playVideo(Uri videoUri) {
        imageView.setVisibility(View.GONE);
        videoView.setVisibility(View.VISIBLE);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        videoView.setVideoURI(videoUri);
        videoView.start();
    }
}
