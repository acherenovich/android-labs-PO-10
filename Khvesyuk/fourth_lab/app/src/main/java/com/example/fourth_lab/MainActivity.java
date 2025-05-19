package com.example.fourth_lab;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity {
    private Button downloadButton, viewButton, deleteButton;
    private EditText idEdit;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        idEdit = findViewById(R.id.idEdit);
        downloadButton = findViewById(R.id.downloadBtn);
        viewButton = findViewById(R.id.openBtn);
        deleteButton = findViewById(R.id.deleteBtn);

        PopupHelper.showPopup(this);

        downloadButton.setOnClickListener(v -> {

            String idFile = idEdit.getText().toString().trim();
            if (idFile.isEmpty()) {
                Toast.makeText(this, "Введите ID файла", Toast.LENGTH_SHORT).show();
                return;
            }

            idFile += ".pdf";
            if (fileExists(idFile)) {
                Toast.makeText(this, "Файл уже загружен", Toast.LENGTH_SHORT).show();
            } else {
                new FileDownloader(this, progressBar).execute(idFile);
            }
        });

        viewButton.setOnClickListener(v -> {
            String idFile = idEdit.getText().toString().trim() + ".pdf";
            Uri uri = getFileUri(idFile);
            if (uri != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "application/pdf");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(intent, "Открыть PDF с помощью"));
            } else {
                Toast.makeText(this, "Файл не найден", Toast.LENGTH_SHORT).show();
            }
        });

        deleteButton.setOnClickListener(v -> {
            String idFile = idEdit.getText().toString().trim() + ".pdf";
            if (deleteFile(idFile)) {
                Toast.makeText(this, "Файл удален", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Не удалось удалить файл", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean fileExists(String fileName) {
        Uri uri = MediaStore.Files.getContentUri("external");
        String selection = MediaStore.MediaColumns.DISPLAY_NAME + " = ?";
        String[] selectionArgs = {fileName};

        try (Cursor cursor = getContentResolver().query(uri, new String[]{MediaStore.MediaColumns._ID}, selection, selectionArgs, null)) {
            return cursor != null && cursor.moveToFirst();
        } catch (Exception e) {
            Log.e("FileCheck", "Ошибка при проверке файла", e);
            return false;
        }
    }

    private Uri getFileUri(String fileName) {
        Uri uri = MediaStore.Files.getContentUri("external");
        String[] projection = {MediaStore.MediaColumns._ID};
        String selection = MediaStore.MediaColumns.DISPLAY_NAME + " = ?";
        String[] selectionArgs = {fileName};

        try (Cursor cursor = getContentResolver().query(uri, projection, selection, selectionArgs, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                long fileId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID));
                return ContentUris.withAppendedId(uri, fileId);
            }
        } catch (Exception e) {
            Log.e("FilePicker", "Ошибка при поиске файла", e);
        }
        return null;
    }


    public boolean deleteFile(String fileName) {
        Uri fileUri = getFileUri(fileName);
        if (fileUri != null) {
            return getContentResolver().delete(fileUri, null, null) > 0;
        }
        return false;
    }
}
