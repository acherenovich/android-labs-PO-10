package com.example.lab4;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private Button downloadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        downloadButton = findViewById(R.id.downloadButton);

        // Выполнение сетевого запроса
        fetchDataAsync("https://www.google.com");

        // Кнопка для скачивания файла
        downloadButton.setOnClickListener(v ->
                downloadFile("https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf", "dummy.pdf")
        );

    }

    // Метод для скачивания файла
    private void downloadFile(String url, String fileName) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle("Скачивание файла");
        request.setDescription("Загрузка...");
        request.setMimeType("application/pdf"); // Указываем тип файла
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (manager != null) {
            manager.enqueue(request);
        }
    }


    // Выполнение сетевого запроса в другом потоке
    private void fetchDataAsync(String url) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            String result = fetchData(url);
            runOnUiThread(() -> textView.setText(result));
        });
    }

    // Метод для получения данных с сервера
    private static String fetchData(String requestUrl) {
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            return "Ошибка подключения!";
        }
        return result.toString();
    }
}
