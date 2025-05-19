package com.example.lab17;

import android.database.Cursor;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.VideoView;

public class MediaActivity extends AppCompatActivity {

    private static final int PICK_VIDEO_REQUEST = 1;
    private static final int PICK_AUDIO_REQUEST = 2;

    private VideoView videoView;
    private MediaPlayer mediaPlayer;
    private Uri selectedAudioUri;
    private Uri selectedVideoUri;
    private CheckBox checkboxLoop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        videoView = findViewById(R.id.videoView);
        Button buttonSelectVideo = findViewById(R.id.buttonSelectVideo);
        Button buttonSelectAudio = findViewById(R.id.buttonSelectAudio);
        Button buttonPlayMedia = findViewById(R.id.buttonPlayAudio); // Используем ту же кнопку
        Button buttonPauseAudio = findViewById(R.id.buttonPauseAudio);
        Button buttonResumeAudio = findViewById(R.id.buttonResumeAudio);
        Button buttonStopAudio = findViewById(R.id.buttonStopAudio);
        Button buttonReturnToMainMenu = findViewById(R.id.buttonReturnToMainMenu);
        checkboxLoop = findViewById(R.id.checkboxLoop); // Инициализация CheckBox

        // Выбор видео
        buttonSelectVideo.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("video/*");
            startActivityForResult(intent, PICK_VIDEO_REQUEST);
        });

        // Выбор аудио
        buttonSelectAudio.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("audio/*");
            startActivityForResult(intent, PICK_AUDIO_REQUEST);
        });

        // Воспроизведение медиа (аудио или видео)
        buttonPlayMedia.setOnClickListener(v -> {
            if (selectedAudioUri != null) {
                stopVideoPlayback(); // Останавливаем видео, если оно играет
                startAudioPlayback();
            } else if (selectedVideoUri != null) {
                stopAudioPlayback(); // Останавливаем аудио, если оно играет
                videoView.setVideoURI(selectedVideoUri);
                videoView.start();
            } else {
                Toast.makeText(this, "Media not selected", Toast.LENGTH_SHORT).show();
            }
        });

        // Приостановка воспроизведения
        buttonPauseAudio.setOnClickListener(v -> {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
            if (videoView.isPlaying()) {
                videoView.pause();
            }
        });

        // Возобновление воспроизведения
        buttonResumeAudio.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                mediaPlayer.start();
            }
            if (!videoView.isPlaying()) {
                videoView.start();
            }
        });

        // Остановка воспроизведения
        buttonStopAudio.setOnClickListener(v -> {
            stopAudioPlayback(); // Остановка аудио
            stopVideoPlayback(); // Остановка видео
        });

        // Обработка нажатия на кнопку возврата в главное меню
        buttonReturnToMainMenu.setOnClickListener(v -> {
            finish(); // Закрывает текущую Activity и возвращает в предыдущее меню
        });
    }

    private void startAudioPlayback() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, selectedAudioUri);
        mediaPlayer.setOnCompletionListener(mp -> {
            if (checkboxLoop.isChecked()) {
                mp.seekTo(0);
                mp.start();
            }
        });
        mediaPlayer.start();
    }

    private void stopAudioPlayback() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void stopVideoPlayback() {
        videoView.stopPlayback(); // Остановка видео
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Uri selectedFileUri = data.getData();

            if (requestCode == PICK_VIDEO_REQUEST) {
                // Сохранение выбранного видео для воспроизведения
                selectedVideoUri = selectedFileUri;
                videoView.setVideoURI(selectedFileUri);
            } else if (requestCode == PICK_AUDIO_REQUEST) {
                // Сохранение выбранного аудио для воспроизведения
                selectedAudioUri = selectedFileUri;
                Toast.makeText(this, "Audio selected: " + getFileName(selectedFileUri), Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Получение имени файла из Uri
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAudioPlayback(); // Остановка аудио при уничтожении активности
        stopVideoPlayback(); // Остановка видео при уничтожении активности
    }
}