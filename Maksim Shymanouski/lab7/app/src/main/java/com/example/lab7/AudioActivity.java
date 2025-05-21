package com.example.lab7;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;

public class AudioActivity extends AppCompatActivity {

    private Button chooseAudioBtn, btnResumePause, btnStop, btnBack;
    private SeekBar audioScrolling;
    private CheckBox checkboxLoop;
    private MediaPlayer mMediaPlayer;
    private boolean isPlaying = false;
    private boolean isLooping = false;
    private Handler handler = new Handler();
    private Runnable updateSeekBar;
    private static final int PICKFILE_RESULT_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        chooseAudioBtn = findViewById(R.id.chooseAudioBtn);
        btnResumePause = findViewById(R.id.videoResumePauseBtn);
        btnStop = findViewById(R.id.videoStopBtn);
        audioScrolling = findViewById(R.id.audioScrolling);
        checkboxLoop = findViewById(R.id.checkboxLoop);
        btnBack = findViewById(R.id.btnBack);

        mMediaPlayer = new MediaPlayer();

        chooseAudioBtn.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                requestMediaPermissions();
                return;
            }

            openAudioPicker();
        });

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

        btnBack.setOnClickListener(v -> finish());

        mMediaPlayer.setOnCompletionListener(mp -> handleCompletion());

        checkboxLoop.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isLooping = isChecked;
        });

        audioScrolling.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mMediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        updateSeekBar();
    }


    private void handleCompletion() {
        if (isLooping) {
            mMediaPlayer.start(); // Повтор
        } else {
            isPlaying = false;
            btnResumePause.setText("Resume");
            mMediaPlayer.seekTo(0);
            audioScrolling.setProgress(0);
        }
    }


    private void requestMediaPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.READ_MEDIA_AUDIO
                },
                100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openAudioPicker(); // Если разрешение получено — открываем галерею
            } else {
                Toast.makeText(this, "Разрешение не предоставлено!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void openAudioPicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        startActivityForResult(intent, PICKFILE_RESULT_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICKFILE_RESULT_CODE && resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            try {
                mMediaPlayer.reset();
                mMediaPlayer.setDataSource(this, fileUri);
                mMediaPlayer.prepare();
                startPlay();
            } catch (IOException e) {
                Log.d("STARTAUDIO", "Error");
            }

        }
    }


    private void pauseVideo() {
        mMediaPlayer.pause();
        isPlaying = false;
        btnResumePause.setText("Resume");
    }

    private void stopPlay() {
        if (mMediaPlayer.isPlaying()) {
            isPlaying = false;
            btnResumePause.setText("Resume");
            mMediaPlayer.pause();
            mMediaPlayer.seekTo(0);
            audioScrolling.setProgress(0);
        }
    }

    private void updateSeekBar() {
        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                    audioScrolling.setProgress(mMediaPlayer.getCurrentPosition());
                    audioScrolling.setMax(mMediaPlayer.getDuration());
                }
                handler.postDelayed(this, 500);
            }
        };
        handler.post(updateSeekBar);
    }

    private void startPlay() {
        if (!isPlaying) {
            isPlaying = true;
            btnResumePause.setText("Pause");

            if (!mMediaPlayer.isPlaying()) {
                try {
                    mMediaPlayer.start();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer.isPlaying()) {
            stopPlay();
        }
        handler.removeCallbacks(updateSeekBar);
    }
}