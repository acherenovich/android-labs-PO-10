package com.example.myapplication3;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import java.io.File;



public class MainActivity extends AppCompatActivity {
    private Button downloadButton, viewButton, deleteButton;
    private EditText idEdit;
    private ProgressBar progressBar;
    private FileViewModel model;

    public boolean fileExists(String fileId) {
        Uri uri = MediaStore.Files.getContentUri("external");
        String[] projection = {MediaStore.MediaColumns._ID};


        String selection = MediaStore.MediaColumns.DISPLAY_NAME + " = ? AND " +
                MediaStore.MediaColumns.RELATIVE_PATH + " = ?";
        String[] selectionArgs = {fileId, "Documents/my_pdfs/"};

        Log.d("FILEEXISTS", selection);

        boolean fileExists = false;

        // Выполняем запрос в MediaStore
        try (Cursor cursor = getContentResolver().query(uri, projection, selection, selectionArgs, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                fileExists = true;
            }
        } catch (Exception e) {
            Log.e("FileCheck", "Error accessing MediaStore", e);
        }
        finally {
            return fileExists;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Выполнила Прокопеня Карина");

        progressBar = findViewById(R.id.progressBar);

        idEdit = findViewById(R.id.idEdit);
        downloadButton = findViewById(R.id.downloadBtn);
        viewButton = findViewById(R.id.openBtn);
        deleteButton = findViewById(R.id.deleteBtn);


        model = new ViewModelProvider(this).get(FileViewModel.class);
        model.getFileExists().observe(this, isExists -> {
            viewButton.setEnabled(isExists);
            deleteButton.setEnabled(isExists);
        });

        model.getProgress().observe(this, prValue -> {
            progressBar.setProgress(prValue);
        });

        model.getIsDownloading().observe(this, isDnld -> {
            if(isDnld) {
                progressBar.setVisibility(View.VISIBLE);
            }
            else {
                progressBar.setVisibility(View.GONE);
            }
        });

        idEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // Проверка, что была нажата клавиша Enter
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

                    String fileId = idEdit.getText().toString() + ".pdf";
                    if (!fileId.isEmpty()) {

                        Log.d("FILEEXISTS", fileId);
                        boolean ind = fileExists(fileId);
                        Log.d("FILEEXISTS", String.valueOf(ind));
                        // Включаем/выключаем кнопки в зависимости от того, найден ли файл
                        model.setFileExists(ind);

                    } else {
                        Toast.makeText(MainActivity.this, "Введите ID файла", Toast.LENGTH_SHORT).show();
                    }
                    return true; // Возвращаем true, чтобы предотвратить обработку события по умолчанию
                }
                return false; // Если не Enter, то возвращаем false, чтобы другие действия могли быть выполнены
            }
        });

        if (!NetworkUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, "Нет подключения", Toast.LENGTH_LONG).show();
        }



        // Загрузка файла
        downloadButton.setOnClickListener(v -> {
            String idFile = idEdit.getText().toString() + ".pdf";
            progressBar.setVisibility(View.VISIBLE);
            new FileDownloader(this).execute(idFile);
        });


        viewButton.setOnClickListener(v -> {
            String idFile = idEdit.getText().toString() + ".pdf";

            // Для Android 10 и выше используем MediaStore для получения URI
            Uri uri = MediaStore.Files.getContentUri("external");

            String[] projection = {MediaStore.MediaColumns._ID};
            String selection = MediaStore.MediaColumns.DISPLAY_NAME + " = ?";
            String[] selectionArgs = {idFile};

            // Запрос к MediaStore для получения URI файла (SELECT _ID FROM files WHERE DISPLAY_NAME = "1.pdf")
            try (Cursor cursor = getContentResolver().query(uri, projection, selection, selectionArgs, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    long fileId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID));
                    uri = ContentUris.withAppendedId(uri, fileId);
                }
            } catch (Exception e) {
                Log.e("FilePicker", "Error accessing MediaStore", e);
            }

            // Если uri найдено, открываем PDF
            if (uri != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "application/pdf");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                startActivity(Intent.createChooser(intent, "Open PDF with"));
            } else {
                Log.e("FilePicker", "File not found");
            }
        });


        deleteButton.setOnClickListener(v -> {
            String idFile = idEdit.getText().toString() + ".pdf";

            // Получаем путь для запроса в MediaStore
            Uri uri = MediaStore.Files.getContentUri("external");
            String[] projection = {MediaStore.MediaColumns._ID};
            String selection = MediaStore.MediaColumns.DISPLAY_NAME + " = ?";
            String[] selectionArgs = {idFile};

            try (Cursor cursor = getContentResolver().query(uri, projection, selection, selectionArgs, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    long fileId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID));
                    Uri fileUri = ContentUris.withAppendedId(uri, fileId);

                    // Удаляем файл из MediaStore
                    int rowsDeleted = getContentResolver().delete(fileUri, null, null);
                    if (rowsDeleted > 0) {
                        viewButton.setEnabled(false);
                        deleteButton.setEnabled(false);
                        idEdit.setText(null);
                        Toast.makeText(this, "Файл удален", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Не удалось удалить файл", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Файл не найден", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e("FileDelete", "Error accessing MediaStore", e);
            }
        });


        PopupHelper.showPopup(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu); // Загружаем меню
        return true;  // Возвращаем true для отображения меню
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_student) {
            Intent intent = new Intent(this, StudentInfoActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.menu_lab) {
            Intent intent = new Intent(this, LabInfoActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


}
