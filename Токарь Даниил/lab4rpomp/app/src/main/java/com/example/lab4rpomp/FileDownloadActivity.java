package com.example.lab4rpomp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileDownloadActivity extends AppCompatActivity {

    private EditText editTextFileUrl;
    private Button buttonDownloadFile;
    private ProgressBar progressBarDownload;
    private TextView textViewDownloadStatus;

    private static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("FileDownload", "FileDownloadActivity.onCreate() started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_download);

        editTextFileUrl = findViewById(R.id.editTextFileUrl);
        buttonDownloadFile = findViewById(R.id.buttonDownloadFile);
        progressBarDownload = findViewById(R.id.progressBarDownload);
        textViewDownloadStatus = findViewById(R.id.textViewDownloadStatus);

        buttonDownloadFile.setOnClickListener(v -> {
            String fileUrl = editTextFileUrl.getText().toString();
            if (!fileUrl.isEmpty()) {
                new DownloadFileTask().execute(fileUrl);
            } else {
                textViewDownloadStatus.setText("Введите URL файла");
            }
        });
    }

    private void checkPermissionsAndDownload(String fileUrl) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            startDownload(fileUrl);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startDownload(editTextFileUrl.getText().toString());
            } else {
                Toast.makeText(this, "Разрешение на запись в память не предоставлено", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void startDownload(String fileUrl) {
        new DownloadFileTask().execute(fileUrl);
    }

    private class DownloadFileTask extends AsyncTask<String, Integer, String> {

        private String fileName;

        @Override
        protected void onPreExecute() {
            Log.d("FileDownload", "DownloadFileTask.onPreExecute() started");
            super.onPreExecute();
            progressBarDownload.setVisibility(ProgressBar.VISIBLE);
            textViewDownloadStatus.setText("Начало загрузки...");
        }

        @Override
        protected String doInBackground(String... fileUrls) {
            Log.d("FileDownload", "DownloadFileTask.doInBackground() started");
            String fileUrl = fileUrls[0];
            fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
            android.net.Uri fileUri = null; // Объявим fileUri здесь, чтобы использовать в catch

            try {
                URL url = new URL(fileUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                int fileLength = urlConnection.getContentLength();
                InputStream inputStream = urlConnection.getInputStream();

                android.content.ContentValues values = new android.content.ContentValues();
                values.put(android.provider.MediaStore.Downloads.DISPLAY_NAME, fileName);
                values.put(android.provider.MediaStore.Downloads.MIME_TYPE, "application/pdf");
                values.put(android.provider.MediaStore.Downloads.IS_PENDING, 1);

                android.net.Uri contentUri = android.provider.MediaStore.Downloads.EXTERNAL_CONTENT_URI;
                Log.d("FileDownload", "Content URI for Downloads: " + contentUri.toString()); // Лог Content URI

                try {
                    fileUri = getContentResolver().insert(contentUri, values);
                    Log.d("FileDownload", "MediaStore.insert() returned Uri: " + fileUri); // Лог Uri после insert
                    if (fileUri == null) {
                        Log.e("FileDownload", "MediaStore.insert() returned null Uri!"); // Лог ошибки insert null
                        return "Ошибка при создании файла в MediaStore (insert returned null)";
                    }
                } catch (Exception insertException) {
                    Log.e("FileDownload", "Exception during MediaStore.insert(): ", insertException); // Лог исключения insert
                    return "Ошибка при создании файла в MediaStore (Exception on insert): " + insertException.getMessage();
                }


                FileOutputStream fileOutput = null;
                try {
                    fileOutput = (FileOutputStream) getContentResolver().openOutputStream(fileUri);
                    Log.d("FileDownload", "getContentResolver().openOutputStream() successful"); // Лог успешного открытия OutputStream
                    if (fileOutput == null) {
                        Log.e("FileDownload", "getContentResolver().openOutputStream() returned null OutputStream!"); // Лог ошибки OutputStream null
                        return "Ошибка при открытии OutputStream для MediaStore (openOutputStream returned null)";
                    }
                } catch (Exception outputStreamException) {
                    Log.e("FileDownload", "Exception during getContentResolver().openOutputStream(): ", outputStreamException); // Лог исключения OutputStream
                    return "Ошибка при открытии OutputStream для MediaStore (Exception on openOutputStream): " + outputStreamException.getMessage();
                }


                byte[] buffer = new byte[4096];
                int bufferLength;
                long totalDownloaded = 0;

                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fileOutput.write(buffer, 0, bufferLength);
                    totalDownloaded += bufferLength;
                    publishProgress((int) ((totalDownloaded * 100) / fileLength));
                }

                fileOutput.close();
                inputStream.close();
                urlConnection.disconnect();

                values.clear();
                values.put(android.provider.MediaStore.Downloads.IS_PENDING, 0);
                getContentResolver().update(fileUri, values, null, null);
                Log.d("FileDownload", "File download successful, updated IS_PENDING=0"); // Лог успеха

                return "Файл успешно сохранен в Downloads";

            } catch (IOException e) {
                Log.e("FileDownload", "IOException during download: ", e); // Лог IOException
                if (fileUri != null) { // Попробуем удалить неудавшийся файл из MediaStore в случае ошибки
                    try {
                        getContentResolver().delete(fileUri, null, null);
                        Log.d("FileDownload", "Deleted partially downloaded file from MediaStore due to error.");
                    } catch (Exception deleteException) {
                        Log.e("FileDownload", "Error deleting partially downloaded file: ", deleteException);
                    }
                }
                return "Ошибка загрузки: " + e.getMessage();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            progressBarDownload.setProgress(progress[0]); // Обновляем ProgressBar
            textViewDownloadStatus.setText("Загружено: " + progress[0] + "%");
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressBarDownload.setVisibility(ProgressBar.INVISIBLE);
            textViewDownloadStatus.setText(result);
            Toast.makeText(FileDownloadActivity.this, result, Toast.LENGTH_LONG).show();
        }
    }
}
