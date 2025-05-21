package com.example.lab_4;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileDownloader extends AsyncTask<String, Integer, Boolean> {
    final static public String PATH = "https://ntv.ifmo.ru/file/journal/";
    Context context;

    private FileViewModel model;

    FileDownloader(Context context) {
        this.context = context;
        this.model = new ViewModelProvider((AppCompatActivity) context).get(FileViewModel.class);
    }

    @Override
    protected void onPreExecute() {
        model.setIsDownloading(true);
    }

    @Override
    protected Boolean doInBackground(String... id) {
        try {
            URL url = new URL(PATH + id[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (!"application/pdf".equals(conn.getContentType())) {
                return false;
            }



            InputStream input = conn.getInputStream();
            int totalSize = conn.getContentLength();
            int downloadedSize = 0;

            // Чтение байтов из потока
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
                downloadedSize += bytesRead;

                // Вычисление прогресса
                if (totalSize > 0) {
                    int progress = (int) ((downloadedSize * 100) / totalSize);
                    publishProgress(progress); // Обновление прогресса
                }
            }
            byte[] pdfData = baos.toByteArray();

            // Сохраняем файл через MediaStore
            savePdfFile(context, id[0], pdfData);

            input.close();
            return true;
        } catch (Exception e) {
            Log.d("MyEx", e.getMessage());
            return false;
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        model.setProgress(values[0]);
    }

    private void savePdfFile(Context context, String fileId, byte[] pdfData) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileId);
        Log.d("SAVEPDFFILE", fileId);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "Documents/files/");

        Uri uri = context.getContentResolver().insert(MediaStore.Files.getContentUri("external"), contentValues);

        if (uri != null) {
            try (OutputStream outputStream = context.getContentResolver().openOutputStream(uri)) {
                if (outputStream != null) {
                    outputStream.write(pdfData);
                }
            } catch (IOException e) {
                Log.e("SavePdf", "Error writing to file", e);
            }
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        Log.d("ON POST EXECUTE", String.valueOf(success));
        if (success) {
            model.setFileExists(true);
        } else {
            model.setFileExists(false);
            Toast.makeText(context, "Данные не загружены", Toast.LENGTH_SHORT).show();
        }
        model.setProgress(0);
        model.setIsDownloading(false);
    }
}
