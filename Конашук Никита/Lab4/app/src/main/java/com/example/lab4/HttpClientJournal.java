package com.example.lab4;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClientJournal {

    private static final String BASE_URL = "https://ntv.ifmo.ru/file/journal/";
    private static final String TAG = "PdfDownloader";

    public static void downloadPdf(Context context, String page) {
        new DownloadPdfTask(context, page).execute();
    }

    // Асинхронная задача для загрузки PDF
    private static class DownloadPdfTask extends AsyncTask<Void, Void, Void> {

        private Context context;
        private String page;

        public DownloadPdfTask(Context context, String page) {
            this.context = context;
            this.page = page;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String fileUrl = BASE_URL + page + ".pdf";
            String fileName = page + ".pdf";

            File directory = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "pdf_files");

            if (!directory.exists() && !directory.mkdirs()) {
                Log.e(TAG, "Не удалось создать каталог");
                return null;
            }

            File pdfFile = new File(directory, fileName);

            if (pdfFile.exists()) {
                Log.d(TAG, "Файл уже существует: " + pdfFile.getAbsolutePath());
                return null;
            }

            try {
                URL url = new URL(fileUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(15000);
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "Ошибка загрузки: " + connection.getResponseMessage());
                    return null;
                }

                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                FileOutputStream outputStream = new FileOutputStream(pdfFile);

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();
                inputStream.close();
                Log.d(TAG, "Файл загружен: " + pdfFile.getAbsolutePath());


            } catch (Exception e) {
                Log.e(TAG, "Ошибка загрузки PDF", e);
            }
            return null;
        }
    }
}