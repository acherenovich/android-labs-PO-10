package com.example.lab7;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class VideoActivity extends AppCompatActivity {
    private VideoView mVideoView;
    private EditText videoUrlEdit;
    private Button btnVideoUrl, btnVideoGallery, btnResumePause, btnStop, btnBack;
    private SeekBar videoScrolling;
    private CheckBox checkboxLoop;

    private Handler handler = new Handler();
    private Runnable updateSeekBar;
    private boolean isPlaying = false;
    private boolean isLooping = false;
    private static final int PICKFILE_RESULT_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        mVideoView = findViewById(R.id.videoView);
        videoUrlEdit = findViewById(R.id.videoUrl);
        btnVideoUrl = findViewById(R.id.btnVideoUrl);
        btnVideoGallery = findViewById(R.id.btnVideoGallery);
        checkboxLoop = findViewById(R.id.checkboxLoop);
        videoScrolling = findViewById(R.id.videoScrolling);
        btnResumePause = findViewById(R.id.videoResumePauseBtn);
        btnStop = findViewById(R.id.videoStopBtn);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        btnResumePause.setOnClickListener(v -> {
            if (isPlaying) {
                pauseVideo();
            } else {
                startPlay();
            }
        });

        btnStop.setOnClickListener(v -> {
            stopPlay();
        });

        btnVideoUrl.setOnClickListener(v -> {
            if(!videoUrlEdit.getText().toString().isEmpty()) {
                Uri videoUri = Uri.parse(videoUrlEdit.getText().toString());
                mVideoView.setVideoURI(videoUri);
                startPlay();
            }
            else {
                Toast.makeText(this, "Введите url", Toast.LENGTH_SHORT).show();
            }
        });

        btnVideoGallery.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
                requestMediaPermissions();
                return; // Выходим из метода, чтобы дождаться результата запроса
            }

            openVideoPicker();
        });


        // Устанавливаем слушатель окончания видео
        mVideoView.setOnCompletionListener(mp -> handleCompletion());

        // Слушатель для CheckBox
        checkboxLoop.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isLooping = isChecked;
        });

        //videoScrolling.setMax(mVideoView.getDuration());
        videoScrolling.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mVideoView.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Обновление SeekBar
        updateSeekBar();
    }

    private void handleCompletion() {
        if (isLooping) {
            mVideoView.start(); // Повтор
        } else {
            isPlaying = false;
            btnResumePause.setText("Resume");
            mVideoView.seekTo(0); // Сброс видео в начало
            videoScrolling.setProgress(0); // Сброс SeekBar
        }
    }

    private void requestMediaPermissions() {
        // Проверяем разрешения для каждого типа медиа


            // Если разрешение не предоставлено, запрашиваем его
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            android.Manifest.permission.READ_MEDIA_VIDEO
                    },
                    100); // requestCode можно выбрать любой
    }

    // Обработка результатов запроса разрешений
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openVideoPicker(); // Если разрешение получено — открываем галерею
            } else {
                Toast.makeText(this, "Разрешение не предоставлено!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void openVideoPicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        startActivityForResult(intent, PICKFILE_RESULT_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICKFILE_RESULT_CODE && resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            mVideoView.setVideoURI(fileUri);
            startPlay();
        }
    }

    private void pauseVideo() {
        mVideoView.pause();
        isPlaying = false;
        btnResumePause.setText("Resume");
    }

    private void stopPlay() {
        if (mVideoView.isPlaying()) {
            isPlaying = false;
            btnResumePause.setText("Resume");
            mVideoView.pause();
            mVideoView.seekTo(0); // Сброс видео в начало
            videoScrolling.setProgress(0); // Сброс SeekBar
        }
    }

    private void updateSeekBar() {
        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (mVideoView != null && mVideoView.isPlaying()) {
                    videoScrolling.setProgress(mVideoView.getCurrentPosition());
                    videoScrolling.setMax(mVideoView.getDuration());
                }
                handler.postDelayed(this, 500);
            }
        };
        handler.post(updateSeekBar);
    }

    private void startPlay() {
        isPlaying = true;
        btnResumePause.setText("Pause");
        mVideoView.start();

//        MediaController mediaController = new MediaController(this);
//        mVideoView.setMediaController(mediaController);
//        mediaController.setMediaPlayer(mVideoView);



        // Проверяем состояние чекбокса при старте видео
//        if (checkboxLoop.isChecked()) {
//            mVideoView.setOnCompletionListener(mp -> mVideoView.start()); // Повтор
//        } else {
//            mVideoView.setOnCompletionListener(null);
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mVideoView.isPlaying()) {
            stopPlay();
        }
        handler.removeCallbacks(updateSeekBar);
    }
}
