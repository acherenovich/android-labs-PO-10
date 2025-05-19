package com.example.lab4;



import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;

public class MainActivity extends Activity {

    private static final int PICK_FILE_REQUEST = 1;
    private Button btnChooseFile, btnPlay, btnPause, btnClose;
    private FrameLayout mediaContainer;
    private ImageView imageView;
    private VideoView videoView;
    private TextView audioInfo;
    private Uri selectedFileUri;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false; // флаг для отслеживания состояния воспроизведения
    private int mediaPosition = 0; // хранение текущей позиции для аудио или видео



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnChooseFile = findViewById(R.id.btnChooseFile);
        mediaContainer = findViewById(R.id.mediaContainer);
        imageView = findViewById(R.id.imageView);
        videoView = findViewById(R.id.videoView);
        audioInfo = findViewById(R.id.audioInfo);
        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        btnClose = findViewById(R.id.btnClose);

        btnChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker();
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedFileUri != null) {
                    String fileType = getContentResolver().getType(selectedFileUri);
                    if (fileType != null) {
                        if (fileType.startsWith("audio/")) {
                            playAudio(selectedFileUri);
                        } else if (fileType.startsWith("video/")) {
                            playVideo(selectedFileUri);
                        }
                    }
                }
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseMedia();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMedia();
            }
        });
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Выберите файл"), PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                selectedFileUri = data.getData();
                String fileType = getContentResolver().getType(selectedFileUri);

                // Скрываем все элементы по умолчанию
                imageView.setVisibility(View.GONE);
                videoView.setVisibility(View.GONE);
                audioInfo.setVisibility(View.GONE);
                btnPlay.setVisibility(View.GONE);
                btnPause.setVisibility(View.GONE);
                btnClose.setVisibility(View.GONE);

                if (fileType != null) {
                    if (fileType.startsWith("image/")) {
                        imageView.setVisibility(View.VISIBLE);
                        imageView.setImageURI(selectedFileUri);
                        btnClose.setVisibility(View.VISIBLE);
                    } else if (fileType.startsWith("video/")) {
                        videoView.setVisibility(View.VISIBLE);
                        videoView.setVideoURI(selectedFileUri);
                        btnPlay.setVisibility(View.VISIBLE);
                        btnPause.setVisibility(View.VISIBLE);
                        btnClose.setVisibility(View.VISIBLE);
                    } else if (fileType.startsWith("audio/")) {
                        audioInfo.setVisibility(View.VISIBLE);
                        String fileName = selectedFileUri.getLastPathSegment();
                        audioInfo.setText("Аудиофайл: " + fileName);
                        btnPlay.setVisibility(View.VISIBLE);
                        btnPause.setVisibility(View.VISIBLE);
                        btnClose.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    private void playAudio(Uri audioUri) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(this, audioUri);
                mediaPlayer.prepare();
                mediaPosition = 0; // Сбрасываем позицию при новом файле
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mediaPlayer.seekTo(mediaPosition); // Восстанавливаем позицию, если файл не сменился
        }

        mediaPlayer.start();
        isPlaying = true;

        mediaPlayer.setOnCompletionListener(mp -> isPlaying = false);
    }


    private void playVideo(Uri videoUri) {
        if (!videoView.isPlaying()) {
            if (mediaPosition == 0) {
                videoView.setVideoURI(videoUri); // Только если это новый запуск
            }
            videoView.seekTo(mediaPosition);
            videoView.start();
        }
    }



    private void pauseMedia() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPosition = mediaPlayer.getCurrentPosition(); // Сохраняем текущую позицию
            mediaPlayer.pause();
            isPlaying = false;
        }
        if (videoView.isPlaying()) {
            mediaPosition = videoView.getCurrentPosition(); // Сохраняем текущую позицию
            videoView.pause();
            isPlaying = false;
        }
    }


    private void closeMedia() {
        imageView.setVisibility(View.GONE);
        videoView.setVisibility(View.GONE);
        audioInfo.setVisibility(View.GONE);
        btnPlay.setVisibility(View.GONE);
        btnPause.setVisibility(View.GONE);
        btnClose.setVisibility(View.GONE);
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        videoView.stopPlayback();
    }
}

