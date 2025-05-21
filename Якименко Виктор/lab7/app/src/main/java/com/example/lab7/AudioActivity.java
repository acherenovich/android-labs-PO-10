package com.example.lab7;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build; // <-- *** ДОБАВИТЬ ИМПОРТ ***
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull; // <-- *** ДОБАВИТЬ ИМПОРТ ***
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;

public class AudioActivity extends AppCompatActivity {

    private Button chooseAudioBtn, btnResumePause, btnStop; // btnBack убран, если нет в layout
    private SeekBar audioScrolling;
    private CheckBox checkboxLoop;
    private MediaPlayer mMediaPlayer;
    private boolean isPlaying = false;
    private boolean isLooping = false;
    private Handler handler = new Handler();
    private Runnable updateSeekBar;

    private static final int PICKFILE_RESULT_CODE = 1;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 101; // Используем другой или тот же код, главное - консистентно

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        chooseAudioBtn = findViewById(R.id.chooseAudioBtn);
        btnResumePause = findViewById(R.id.videoResumePauseBtn); // Убедитесь, что ID верный для AudioActivity
        btnStop = findViewById(R.id.videoStopBtn); // Убедитесь, что ID верный для AudioActivity
        audioScrolling = findViewById(R.id.audioScrolling);
        checkboxLoop = findViewById(R.id.checkboxLoop);
        // btnBack = findViewById(R.id.btnBack); // Если есть

        mMediaPlayer = new MediaPlayer();

        // --- НАЧАЛО ИЗМЕНЕНИЙ ---
        chooseAudioBtn.setOnClickListener(v -> {
            checkAndRequestPermissionOrOpenPicker();
        });
        // --- КОНЕЦ ИЗМЕНЕНИЙ ---

        btnResumePause.setOnClickListener(v -> {
            // Имя метода pauseVideo() может сбивать с толку, но оставляем как есть
            if (isPlaying) {
                pauseAudio(); // Логичнее назвать pauseAudio()
            } else {
                startPlay();
            }
        });

        btnStop.setOnClickListener(v -> {
            stopPlay();
        });

        // Слушатель готовности (рекомендуется добавить)
        mMediaPlayer.setOnPreparedListener(mp -> {
            Log.d("AudioActivity", "MediaPlayer prepared. Duration: " + mp.getDuration());
            audioScrolling.setMax(mp.getDuration());
            // Можно начать воспроизведение здесь, если нужно
            startPlay();
            // Запускаем обновление SeekBar после готовности
            handler.post(updateSeekBar);
        });

        // Устанавливаем слушатель окончания
        mMediaPlayer.setOnCompletionListener(mp -> handleCompletion());

        // Слушатель ошибок (рекомендуется добавить)
        mMediaPlayer.setOnErrorListener((mp, what, extra) -> {
            Log.e("AudioActivity", "MediaPlayer Error - what: " + what + " extra: " + extra);
            Toast.makeText(AudioActivity.this, "Ошибка воспроизведения аудио", Toast.LENGTH_SHORT).show();
            stopPlay(); // Останавливаем при ошибке
            return true; // Сообщаем, что ошибка обработана
        });


        // Слушатель для CheckBox
        checkboxLoop.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isLooping = isChecked;
            // Применяем сразу к плееру, если он уже создан и готов
            if (mMediaPlayer != null) {
                try {
                    mMediaPlayer.setLooping(isLooping);
                } catch (IllegalStateException e) {
                    Log.w("AudioActivity", "Cannot set looping state: " + e.getMessage());
                }
            }
        });

        // Устанавливать Max лучше в onPrepared
        // audioScrolling.setMax(mMediaPlayer.getDuration());
        audioScrolling.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Проверяем, готов ли плеер к перемотке
                if (fromUser && mMediaPlayer != null && isPlaying) { // Проверяем isPlaying, чтобы не перематывать в паузе, если не хотим
                    try {
                        mMediaPlayer.seekTo(progress);
                    } catch (IllegalStateException e) {
                        Log.w("AudioActivity", "Cannot seek: " + e.getMessage());
                    }
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

        // Обновление SeekBar - лучше инициализировать Runnable здесь, а запускать в onPrepared
        initializeSeekBarUpdater();
        // updateSeekBar(); // Не запускаем сразу
    }


    private void handleCompletion() {
        Log.d("AudioActivity", "Audio completed. isLooping: " + isLooping);
        // setLooping(true) обрабатывает зацикливание автоматически
        if (!isLooping) {
            isPlaying = false;
            btnResumePause.setText("Resume"); // Или "Воспр."
            // seekTo(0) может вызвать IllegalStateException, если плеер не готов
            try {
                if (mMediaPlayer != null) {
                    mMediaPlayer.seekTo(0);
                }
            } catch (IllegalStateException e) {
                Log.w("AudioActivity", "Cannot seek to 0 on completion: " + e.getMessage());
            }
            audioScrolling.setProgress(0); // Сброс SeekBar
            handler.removeCallbacks(updateSeekBar); // Остановить обновление
        }
        // Если isLooping == true и был вызван mMediaPlayer.setLooping(true),
        // MediaPlayer сам начнет воспроизведение заново.
    }


    // --- НАЧАЛО ИЗМЕНЕНИЙ ---

    /**
     * Определяет необходимое разрешение в зависимости от версии Android.
     */
    private String getRequiredPermission() {
        // Начиная с Android 13 (API 33)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return Manifest.permission.READ_MEDIA_AUDIO; // Используем разрешение для АУДИО
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
            openAudioPicker();
        }
    }

    // Удаляем старый метод requestMediaPermissions
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
                openAudioPicker(); // Открываем галерею
            } else {
                // Разрешение не получено
                Toast.makeText(this, "Разрешение на доступ к аудио не предоставлено!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    // --- КОНЕЦ ИЗМЕНЕНИЙ ---


    private void openAudioPicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*"); // Тип для аудио
        try {
            startActivityForResult(Intent.createChooser(intent, "Выберите аудиофайл"), PICKFILE_RESULT_CODE);
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
                Log.d("AudioActivity", "Audio selected: " + fileUri.toString());
                // Останавливаем и сбрасываем плеер перед загрузкой нового файла
                stopPlay();
                if (mMediaPlayer != null) {
                    try {
                        mMediaPlayer.reset();
                        mMediaPlayer.setDataSource(this, fileUri);
                        mMediaPlayer.setLooping(isLooping); // Устанавливаем зацикливание до prepare
                        mMediaPlayer.prepareAsync(); // Используем асинхронную подготовку
                        // Воспроизведение начнется в onPreparedListener
                        // Показываем какой-то индикатор загрузки, если нужно
                    } catch (IOException e) {
                        Log.e("AudioActivity", "IOException setting data source", e);
                        Toast.makeText(this, "Ошибка загрузки аудиофайла", Toast.LENGTH_SHORT).show();
                    } catch (IllegalStateException e) {
                        Log.e("AudioActivity", "IllegalStateException setting data source", e);
                        Toast.makeText(this, "Ошибка плеера", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(this, "Не удалось получить URI аудио", Toast.LENGTH_SHORT).show();
            }
        }
    }


    // Логичнее переименовать в pauseAudio
    private void pauseAudio() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            try {
                mMediaPlayer.pause();
                isPlaying = false;
                btnResumePause.setText("Resume"); // Или "Воспр."
                handler.removeCallbacks(updateSeekBar); // Остановить обновление SeekBar
                Log.d("AudioActivity", "Audio paused at: " + mMediaPlayer.getCurrentPosition());
            } catch (IllegalStateException e) {
                Log.w("AudioActivity", "Cannot pause: " + e.getMessage());
            }
        }
    }

    private void stopPlay() {
        if (mMediaPlayer != null) { // Проверяем плеер на null
            // Проверяем, играет ли или был ли запущен вообще
            boolean wasPlaying = false;
            try {
                wasPlaying = mMediaPlayer.isPlaying();
            } catch (IllegalStateException e) { /*ignore*/ }

            if (wasPlaying || isPlaying || mMediaPlayer.getCurrentPosition() > 0) {
                try {
                    // mMediaPlayer.stop(); // stop() требует prepare() заново
                    mMediaPlayer.pause(); // Ставим на паузу
                    mMediaPlayer.seekTo(0); // Перематываем в начало
                    isPlaying = false;
                    btnResumePause.setText("Resume"); // Или "Воспр."
                    audioScrolling.setProgress(0); // Сброс SeekBar
                    handler.removeCallbacks(updateSeekBar); // Остановить обновление SeekBar
                    Log.d("AudioActivity", "Audio stopped (paused at 0).");
                } catch (IllegalStateException e) {
                    Log.w("AudioActivity", "Cannot stop/seek: " + e.getMessage());
                    // В случае серьезной ошибки можно попробовать reset()
                    // mMediaPlayer.reset();
                    // isPlaying = false; ... и т.д.
                }
            }
        }
    }

    // Выносим инициализацию Runnable
    private void initializeSeekBarUpdater() {
        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (mMediaPlayer != null && isPlaying) { // Обновляем только если играет
                    try {
                        int currentPosition = mMediaPlayer.getCurrentPosition();
                        int duration = mMediaPlayer.getDuration();
                        if (duration > 0) {
                            // Можно проверить и установить max здесь, если в onPrepared не сработало
                            if (audioScrolling.getMax() != duration) {
                                audioScrolling.setMax(duration);
                            }
                            audioScrolling.setProgress(currentPosition);
                        }
                        // Повторяем вызов через 500 мс, только если все еще играем
                        handler.postDelayed(this, 500);
                    } catch (IllegalStateException e) {
                        Log.e("AudioActivity", "Error updating seekbar", e);
                        handler.removeCallbacks(this); // Прекратить попытки при ошибке
                    }
                }
                // Если не играем, Runnable сам себя не перезапустит
            }
        };
    }


    private void startPlay() {
        // Добавляем проверки
        if (mMediaPlayer != null && !isPlaying) { // Не пытаемся стартовать, если уже играет
            try {
                // Убедимся, что плеер готов (проверка состояния не помешает, но prepareAsync и onPrepared надежнее)
                mMediaPlayer.start();
                isPlaying = true;
                btnResumePause.setText("Pause"); // Или "Пауза"

                // Устанавливаем максимальное значение, если вдруг не установилось в onPrepared
                int duration = mMediaPlayer.getDuration();
                if (duration > 0 && audioScrolling.getMax() != duration) {
                    audioScrolling.setMax(duration);
                }

                // Запускаем/возобновляем обновление SeekBar
                handler.removeCallbacks(updateSeekBar);
                handler.post(updateSeekBar);
                Log.d("AudioActivity", "Audio started playing.");

            } catch (IllegalStateException e) {
                Log.e("AudioActivity", "Cannot start playback: " + e.getMessage());
                Toast.makeText(this, "Не удалось начать воспроизведение", Toast.LENGTH_SHORT).show();
                // Сбросить состояние, если старт не удался
                isPlaying = false;
                btnResumePause.setText("Resume");
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        // Ставим на паузу при сворачивании Activity
        pauseAudio();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // Освобождаем ресурсы MediaPlayer и Handler
        if (mMediaPlayer != null) {
            handler.removeCallbacks(updateSeekBar); // Сначала останавливаем Runnable
            try {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop(); // Останавливаем перед освобождением
                }
                mMediaPlayer.release(); // Освобождаем ресурсы плеера
            } catch (IllegalStateException e) {
                Log.e("AudioActivity", "Error releasing MediaPlayer", e);
            }
            mMediaPlayer = null; // Обнуляем ссылку
        }
        Log.d("AudioActivity", "onDestroy called, MediaPlayer released.");
    }
}