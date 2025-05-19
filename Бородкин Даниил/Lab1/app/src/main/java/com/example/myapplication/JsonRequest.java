package com.example.myapplication;
import okhttp3.*;

import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class JsonRequest {
    public static void send_request(Context context, RequestCallback callback) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String url = "https://server-for-labs-production.up.railway.app/get-cars";
        String savePath = "cars.json";

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Ошибка запроса: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {

                    byte[] jsonData = response.body().bytes();

                    File file = new File(context.getFilesDir(), savePath);
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        fos.write(jsonData);
                        System.out.println("Файл сохранен: " + file.getAbsolutePath());

                        callback.onDataLoaded(file);
                    }
                } else {
                    System.out.println("Ошибка: " + response.code());
                }
            }
        });
    }

    public interface RequestCallback {
        void onDataLoaded(File file);
    }
}



