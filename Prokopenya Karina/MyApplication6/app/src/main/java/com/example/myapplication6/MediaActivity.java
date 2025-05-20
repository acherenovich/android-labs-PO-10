package com.example.myapplication6;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;

public class MediaActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private MediaPlayer mediaPlayer;
    private EditText etMediaPath;
    private CheckBox chbLoop;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private SeekBar seekBarVolume;
    private AudioManager audioManager;

    private static final int REQUEST_PERMISSION_CODE = 100;
    private static final int REQUEST_CODE_PICK_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        // Инициализация элементов интерфейса
        etMediaPath = findViewById(R.id.etMediaPath);
        chbLoop = findViewById(R.id.chbLoop);
        surfaceView = findViewById(R.id.surfaceView);
        seekBarVolume = findViewById(R.id.seekBarVolume);

        // Настройка AudioManager для регулировки громкости
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // Настройка SeekBar для регулировки громкости
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        seekBarVolume.setMax(maxVolume);
        seekBarVolume.setProgress(currentVolume);

        // Обработчик изменения громкости через SeekBar
        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Обработчик изменения состояния чекбокса "Зациклить воспроизведение"
        chbLoop.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (mediaPlayer != null) {
                mediaPlayer.setLooping(isChecked);
            }
        });

        // Инициализация кнопки для выбора файла
        Button btnChooseFile = findViewById(R.id.btnChooseFile);
        btnChooseFile.setOnClickListener(v -> chooseFile());

        // Инициализация SurfaceHolder для SurfaceView
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                // Подключаем Surface к MediaPlayer
                if (mediaPlayer != null) {
                    mediaPlayer.setDisplay(holder);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {}
        });
    }

    private void playFile(Uri uri) {
        try {
            releaseMP(); // Освобождаем старый MediaPlayer перед созданием нового
            mediaPlayer = new MediaPlayer();

            // Привязка к SurfaceHolder
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            if (surfaceHolder.getSurface().isValid()) {
                mediaPlayer.setDisplay(surfaceHolder);
            }

            mediaPlayer.setDataSource(this, uri);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> mediaPlayer.start());

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка при воспроизведении файла", Toast.LENGTH_SHORT).show();
        }
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_media);
//        getSupportActionBar().setTitle("Выполнила Прокопеня Карина");
//
//        // Инициализация элементов интерфейса
//        etMediaPath = findViewById(R.id.etMediaPath);
//        chbLoop = findViewById(R.id.chbLoop);
//        surfaceView = findViewById(R.id.surfaceView);
//        seekBarVolume = findViewById(R.id.seekBarVolume);
//
//        // Настройка AudioManager для регулировки громкости
//        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//
//        // Настройка SeekBar для регулировки громкости
//        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//        seekBarVolume.setMax(maxVolume);
//        seekBarVolume.setProgress(currentVolume);
//
//        // Обработчик изменения громкости через SeekBar
//        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {}
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {}
//        });
//
//        // Обработчик изменения состояния чекбокса "Зациклить воспроизведение"
//        chbLoop.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if (mediaPlayer != null) {
//                mediaPlayer.setLooping(isChecked);
//            }
//        });
//
//        // Инициализация кнопки для выбора файла
//        Button btnChooseFile = findViewById(R.id.btnChooseFile);
//        btnChooseFile.setOnClickListener(v -> chooseFile());
//
//        SurfaceHolder surfaceHolder = surfaceView.getHolder();
//        if (surfaceHolder.getSurface().isValid()) {
//            mediaPlayer.setDisplay(surfaceHolder);
//        }
//
//
//        // Инициализация SurfaceHolder для SurfaceView
//        surfaceHolder = surfaceView.getHolder();
//        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
//            @Override
//            public void surfaceCreated(SurfaceHolder holder) {
//                // Подключаем Surface к MediaPlayer
//                if (mediaPlayer != null) {
//                    mediaPlayer.setDisplay(holder);
//                }
//            }
//
//            @Override
//            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}
//
//            @Override
//            public void surfaceDestroyed(SurfaceHolder holder) {}
//        });
//    }

    public void onClickStart(View view) {
        releaseMP(); // Освобождаем старый MediaPlayer, если он был

        try {
            String dataSource = etMediaPath.getText().toString().trim();

            if (dataSource.startsWith("http") || dataSource.startsWith("rtsp")) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(this, Uri.parse(dataSource));
                mediaPlayer.setDisplay(surfaceHolder);
                mediaPlayer.setOnPreparedListener(this);
                mediaPlayer.prepareAsync();
                mediaPlayer.setLooping(chbLoop.isChecked());
                mediaPlayer.setOnCompletionListener(this);
            } else {
                // Проверка разрешений
                if (!checkPermissions()) {
                    requestPermissions();
                    return;
                }

                // Путь к файлу
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), dataSource);
                if (file.exists()) {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(file.getAbsolutePath());
                    mediaPlayer.setDisplay(surfaceHolder);
                    mediaPlayer.setOnPreparedListener(this);
                    mediaPlayer.prepareAsync();
                    mediaPlayer.setLooping(chbLoop.isChecked());
                    mediaPlayer.setOnCompletionListener(this);
                } else {
                    throw new Exception("Файл не найден");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка воспроизведения: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onClick(View view) {
        if (mediaPlayer == null) return;

        int id = view.getId();

        if (id == R.id.btnPause) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.start();
            }
        } else if (id == R.id.btnStop) {
            mediaPlayer.stop();
            mediaPlayer.reset();  // Ресет плеера для нового воспроизведения
        }
    }


    private void releaseMP() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // Действия при завершении воспроизведения
        if (chbLoop.isChecked()) {
            mp.start();  // Зацикливание при необходимости
        } else {
            Toast.makeText(this, "Видео завершено", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMP();
    }

    // Проверка разрешений
    private boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Права предоставлены, продолжаем выполнение
                chooseFile();
            } else {
                // Права не предоставлены
                Toast.makeText(this, "Необходимо разрешение для доступа к файлам", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Метод для выбора файла
    private void chooseFile() {
        Log.d("MediaActivity", "Выбор файла запущен"); // Логирование
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"audio/*", "video/*"});
        startActivityForResult(intent, REQUEST_CODE_PICK_FILE);
    }

    // Обработка выбранного файла
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Uri selectedUri = data.getData();  // Получаем URI выбранного файла
            playFile(selectedUri);  // Воспроизводим файл
        }
    }


    // Воспроизведение выбранного файла
//    private void playFile(Uri uri) {
//        try {
//            if (mediaPlayer == null) {
//                mediaPlayer = new MediaPlayer();
//            }
//
//            // Получаем SurfaceHolder и проверяем, не освобождена ли поверхность
////            SurfaceHolder surfaceHolder = surfaceView.getHolder();
//            if (surfaceHolder.getSurface().isValid()) {
//                mediaPlayer.setDisplay(surfaceHolder); // Устанавливаем поверхность
//            }
//
//            mediaPlayer.setDataSource(this, uri); // Устанавливаем источник данных
//            mediaPlayer.prepareAsync(); // Асинхронно подготавливаем плеер
//            mediaPlayer.setOnPreparedListener(mp -> mediaPlayer.start()); // Начинаем воспроизведение после подготовки
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


    // Получение пути файла из URI
    private String getFilePathFromUri(Uri uri) {
        String filePath = null;
        if (uri != null) {
            // Для URI типа content используем ContentResolver
            if ("content".equalsIgnoreCase(uri.getScheme())) {
                try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                    if (cursor != null && cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        if (columnIndex != -1) {
                            filePath = cursor.getString(columnIndex);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                // Для URI типа file, просто берем путь
                filePath = uri.getPath();
            }
        }
        return filePath;
    }
}