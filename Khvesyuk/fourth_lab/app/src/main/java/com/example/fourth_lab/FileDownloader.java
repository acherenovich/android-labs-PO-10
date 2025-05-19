package com.example.fourth_lab;

import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileDownloader extends AsyncTask<String, Integer, Boolean> {
    private static final String PATH = "https://ntv.ifmo.ru/file/journal/";
    private final Context context;
    private final ProgressBar progressBar;

    public FileDownloader(Context context, ProgressBar progressBar) {
        this.context = context;
        this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        progressBar.setVisibility(ProgressBar.VISIBLE);
    }

    @Override
    protected Boolean doInBackground(String... id) {
        String fileId = id[0];
        String fileUrl = PATH + fileId;

        if (!isNetworkAvailable()) {
            Log.e("FileDownloader", "Нет подключения к интернету");
            return false;
        }

        try {
            Log.d("FileDownloader", "Скачивание: " + fileUrl);
            URL url = new URL(fileUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.e("FileDownloader", "Ошибка: " + conn.getResponseMessage());
                return false;
            }

            try (InputStream input = conn.getInputStream();
                 ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

                int totalSize = conn.getContentLength();
                int downloadedSize = 0;
                byte[] buffer = new byte[1024];
                int bytesRead;

                while ((bytesRead = input.read(buffer)) != -1) {
                    baos.write(buffer, 0, bytesRead);
                    downloadedSize += bytesRead;
                    if (totalSize > 0) {
                        publishProgress((int) ((downloadedSize * 100) / totalSize));
                    }
                }

                savePdfFile(fileId, baos.toByteArray());
            }

            return true;

        } catch (IOException e) {
            Log.e("FileDownloader", "Ошибка загрузки файла с id: " + fileId, e);
            return false;
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        progressBar.setProgress(values[0]);
    }

    private void savePdfFile(String fileId, byte[] pdfData) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileId);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "Documents/my_pdfs/");

        Uri uri = context.getContentResolver().insert(MediaStore.Files.getContentUri("external"), contentValues);
        if (uri != null) {
            try (OutputStream outputStream = context.getContentResolver().openOutputStream(uri)) {
                if (outputStream != null) {
                    outputStream.write(pdfData);
                } else {
                    Log.e("SavePdf", "Не удалось получить OutputStream");
                }
            } catch (IOException e) {
                Log.e("SavePdf", "Ошибка сохранения файла", e);
            }
        } else {
            Log.e("SavePdf", "Не удалось получить Uri для сохранения файла");
        }
    }


    @Override
    protected void onPostExecute(Boolean success) {
        progressBar.setVisibility(ProgressBar.GONE);
        if (success) {
            Toast.makeText(context, "Файл успешно загружен", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Ошибка при загрузке", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
}

