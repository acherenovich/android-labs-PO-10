package com.example.lab7;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build; // <-- *** ДОБАВИТЬ ЭТОТ ИМПОРТ ***
import android.os.Bundle;
import android.os.Handler;
import android.util.Log; // Можете оставить или убрать, если не нужен
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
// import android.widget.MediaController; // Не используется в вашем коде
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull; // <-- *** ДОБАВИТЬ ЭТОТ ИМПОРТ ***
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class VideoActivity extends AppCompatActivity {
    private VideoView mVideoView;
    private EditText videoUrlEdit;
    private Button btnVideoUrl, btnVideoGallery, btnResumePause, btnStop, btnBack; // btnBack может быть неактуален
    private SeekBar videoScrolling;
    private CheckBox checkboxLoop;

    private Handler handler = new Handler();
    private Runnable updateSeekBar;
    private boolean isPlaying = false;
    private boolean isLooping = false;
    private static final int PICKFILE_RESULT_CODE = 1;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 100; // Константа для запроса разрешений

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
        // btnBack = findViewById(R.id.btnBack); // Если эта кнопка есть в layout

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
                // Лучше запускать в onPrepared, но оставляем как было
                startPlay();
            }
            else {
                Toast.makeText(this, "Введите url", Toast.LENGTH_SHORT).show();
            }
        });

        // --- НАЧАЛО ИЗМЕНЕНИЙ ---
        btnVideoGallery.setOnClickListener(v -> {
            checkAndRequestPermissionOrOpenPicker();
        });
        // --- КОНЕЦ ИЗМЕНЕНИЙ ---


        mVideoView.setOnCompletionListener(mp -> handleCompletion());

        checkboxLoop.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isLooping = isChecked;
            // Применить сразу, если видео уже играет (опционально)
            // if (mVideoView.isPlaying()) {
            //     mVideoView.setLooping(isChecked);
            // }
        });

        // Устанавливать Max лучше в onPrepared, но оставляем как было
        // videoScrolling.setMax(mVideoView.getDuration());
        videoScrolling.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mVideoView.canSeekForward()) { // Добавлена проверка
                    mVideoView.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Можно добавить handler.removeCallbacks(updateSeekBar);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Можно добавить handler.postDelayed(updateSeekBar, 100);
            }
        });

        // Обновление SeekBar
        updateSeekBar(); // Запускается сразу, может вызвать проблемы до загрузки видео
    }

    private void handleCompletion() {
        if (isLooping) {
            // Если используем setLooping(true), этот код не нужен
            // Если управляем вручную:
            mVideoView.seekTo(0);
            mVideoView.start(); // Повтор
        } else {
            isPlaying = false;
            btnResumePause.setText("Resume");
            mVideoView.seekTo(0); // Сброс видео в начало
            videoScrolling.setProgress(0); // Сброс SeekBar
            // Стоит остановить updateSeekBar здесь:
            // handler.removeCallbacks(updateSeekBar);
        }
    }

    // --- НАЧАЛО ИЗМЕНЕНИЙ ---

    /**
     * Определяет необходимое разрешение в зависимости от версии Android.
     */
    private String getRequiredPermission() {
        // Начиная с Android 13 (API 33)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return Manifest.permission.READ_MEDIA_VIDEO;
        }
        // Для Android 11, 12 (API 30, 31, 32) и ниже
        else {
            return Manifest.permission.READ_EXTERNAL_STORAGE;
        }
    }

    /**
     * Проверяет наличие разрешения. Если нет - запрашивает. Если есть - открывает галерею.
     */
    private void checkAndRequestPermissionOrOpenPicker() {
        String permission = getRequiredPermission();
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            // Разрешение не предоставлено, запрашиваем его
            ActivityCompat.requestPermissions(this,
                    new String[]{permission},
                    STORAGE_PERMISSION_REQUEST_CODE); // Используем константу
        } else {
            // Разрешение уже предоставлено, открываем галерею
            openVideoPicker();
        }
    }

    // Удаляем старый метод requestMediaPermissions, так как его логика теперь в checkAndRequestPermissionOrOpenPicker
    /*
    private void requestMediaPermissions() { ... }
    */

    /**
     * Обработка результатов запроса разрешений.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) { // Проверяем наш код запроса
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Разрешение получено
                openVideoPicker(); // Открываем галерею
            } else {
                // Разрешение не получено
                Toast.makeText(this, "Разрешение на доступ к хранилищу не предоставлено!", Toast.LENGTH_SHORT).show();
                // Здесь можно объяснить пользователю, почему разрешение важно,
                // или предложить перейти в настройки приложения.
            }
        }
    }
    // --- КОНЕЦ ИЗМЕНЕНИЙ ---


    private void openVideoPicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        // Рекомендуется использовать новый ActivityResultLauncher, но оставляем как было
        try {
            startActivityForResult(Intent.createChooser(intent, "Выберите видео"), PICKFILE_RESULT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Установите файловый менеджер.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICKFILE_RESULT_CODE && resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            if (fileUri != null) {
                mVideoView.setVideoURI(fileUri);
                // Лучше запускать в onPrepared, но оставляем как было
                startPlay();
            } else {
                Toast.makeText(this, "Не удалось получить URI видео", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void pauseVideo() {
        if (mVideoView.isPlaying()) { // Добавлена проверка
            mVideoView.pause();
            isPlaying = false;
            btnResumePause.setText("Resume");
            // Стоит остановить updateSeekBar здесь:
            // handler.removeCallbacks(updateSeekBar);
        }
    }

    private void stopPlay() {
        // Проверяем не только isPlaying, но и сам VideoView
        if (mVideoView.isPlaying() || mVideoView.getCurrentPosition() > 0) {
            isPlaying = false;
            btnResumePause.setText("Resume");
            mVideoView.stopPlayback(); // Полностью останавливает и сбрасывает
            // После stopPlayback() часто нужно заново устанавливать URI
            // Если нужно просто пауза + перемотка:
            // mVideoView.pause();
            // mVideoView.seekTo(0);
            videoScrolling.setProgress(0); // Сброс SeekBar
            // Обязательно остановить updateSeekBar здесь:
            handler.removeCallbacks(updateSeekBar); // <-- Добавлено
        }
    }

    private void updateSeekBar() {
        // Этот Runnable лучше инициализировать один раз в onCreate
        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                // Добавлена проверка mVideoView != null и isPlaying
                if (mVideoView != null && isPlaying) {
                    try {
                        int currentPosition = mVideoView.getCurrentPosition();
                        int duration = mVideoView.getDuration();
                        // Устанавливать max лучше один раз в onPrepared
                        if (duration > 0) { // Проверка, что длительность известна
                            // Установка max здесь может быть неэффективной
                            if (videoScrolling.getMax() != duration) {
                                videoScrolling.setMax(duration);
                            }
                            videoScrolling.setProgress(currentPosition);
                        }
                        // Повторяем только если все еще играем
                        handler.postDelayed(this, 500);
                    } catch (IllegalStateException e) {
                        Log.e("VideoActivity", "Error updating seekbar", e);
                        // Может произойти, если VideoView в неправильном состоянии
                        // Остановить Runnable в этом случае
                        handler.removeCallbacks(this);
                    }
                } else if (mVideoView != null && !isPlaying && mVideoView.getCurrentPosition() == 0){
                    // Если видео остановлено и в начале, можно прекратить обновления
                    handler.removeCallbacks(this);
                } else {
                    // Если не играем, но не в начале (пауза), можно продолжить через паузу
                    handler.postDelayed(this, 500);
                }
            }
        };
        // Запуск сразу может быть преждевременным
        handler.post(updateSeekBar);
    }

    private void startPlay() {
        // Нет проверки, готов ли VideoView, может вызвать ошибку
        isPlaying = true;
        btnResumePause.setText("Pause");
        mVideoView.start();

        // Запускаем/возобновляем обновление SeekBar
        handler.removeCallbacks(updateSeekBar); // Убираем старые вызовы
        handler.post(updateSeekBar); // Запускаем новый
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Останавливаем видео и освобождаем ресурсы
        if (mVideoView != null) { // Добавлена проверка на null
            mVideoView.stopPlayback();
        }
        // Обязательно убираем сообщения из Handler
        if (handler != null && updateSeekBar != null) { // Добавлена проверка на null
            handler.removeCallbacks(updateSeekBar);
        }
    }
}