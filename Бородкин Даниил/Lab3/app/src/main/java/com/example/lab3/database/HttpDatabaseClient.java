package com.example.lab3.database;

import android.util.Log;

import okhttp3.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpDatabaseClient {
    private static final String BASE_URL = "https://server-for-labs-production.up.railway.app";
    private static final String TAG = "DatabaseClient";
    private static final OkHttpClient client = new OkHttpClient();
    private static final ExecutorService executor = Executors.newSingleThreadExecutor(); // Используем ExecutorService для фона

    // Метод для скачивания базы данных
    public static void downloadDatabase(final String savePath) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(BASE_URL + "/get-database")
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        File downloadedFile = new File(savePath);
                        downloadedFile.getParentFile().mkdirs(); // Создаем директорию, если она не существует
                        response.body().source().readAll(okio.Okio.sink(downloadedFile));
                        Log.d(TAG, "База данных успешно скачана: " + savePath);
                    } else {
                        Log.e(TAG, "Не удалось скачать базу данных: " + response.message());
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Ошибка при скачивании базы данных: ", e);
                }
            }
        });
    }

    // Метод для отправки базы данных
    public static void uploadDatabase(final String filePath) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                File file = new File(filePath);

                if (!file.exists()) {
                    Log.e(TAG, "Файл не существует: " + filePath);
                    return;
                }

                RequestBody requestBody = RequestBody.create(file, MediaType.parse("application/octet-stream"));
                MultipartBody requestBodyMultipart = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("file", file.getName(), requestBody)
                        .build();

                Request request = new Request.Builder()
                        .url(BASE_URL + "/upload-database")
                        .post(requestBodyMultipart)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "База данных успешно загружена.");
                    } else {
                        Log.e(TAG, "Не удалось загрузить базу данных: " + response.message());
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Ошибка при загрузке базы данных: ", e);
                }
            }
        });
    }
}
