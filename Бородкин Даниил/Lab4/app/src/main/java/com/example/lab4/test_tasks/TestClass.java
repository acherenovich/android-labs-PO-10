//package com.example.lab4.test_tasks;
//
//import static androidx.core.content.ContextCompat.getSystemService;
//
//import android.content.Context;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.view.View;
//import android.widget.Toast;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.Reader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//public class TestClass {
//    public void myClickHandler(View view) {
//        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//        if (networkInfo != null && networkInfo.isConnected()) {
//            // Данные доступны
//            Toast.makeText(this, "Сеть доступна", Toast.LENGTH_SHORT).show();
//        } else {
//            // Сеть не доступна
//            Toast.makeText(this, "Нет подключения к сети", Toast.LENGTH_SHORT).show();
//        }
//    }
//    private String downloadUrl(String myurl) throws IOException {
//        InputStream is = null;
//        int len = 500; // Ограничение на длину ответа
//        try {
//            URL url = new URL(myurl);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setReadTimeout(10000);
//            conn.setConnectTimeout(15000);
//            conn.setRequestMethod("GET");
//            conn.setDoInput(true);
//            conn.connect();
//            int response = conn.getResponseCode();
//            is = conn.getInputStream();
//
//            return readIt(is, len);
//        } finally {
//            if (is != null) {
//                is.close();
//            }
//        }
//    }
//
//    private String readIt(InputStream stream, int len) throws IOException {
//        Reader reader = new InputStreamReader(stream, "UTF-8");
//        char[] buffer = new char[len];
//        reader.read(buffer);
//        return new String(buffer);
//    }
//
//}
