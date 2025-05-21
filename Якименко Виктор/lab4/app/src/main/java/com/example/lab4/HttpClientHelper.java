package com.example.lab4;//package com.example.lab4;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//public class HttpClientHelper {
//    public static String sendGetRequest(String urlStr) throws IOException {
//        URL url = new URL(urlStr);
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//        conn.setReadTimeout(10000);
//        conn.setConnectTimeout(15000);
//        conn.connect();
//
//        int responseCode = conn.getResponseCode();
//        if (responseCode == HttpURLConnection.HTTP_OK) {
//            return readStream(conn.getInputStream());
//        } else {
//            return "Ошибка: " + responseCode;
//        }
//    }
//
//    private static String readStream(InputStream is) throws IOException {
//        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//        StringBuilder result = new StringBuilder();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            result.append(line).append("\n");
//        }
//        reader.close();
//        return result.toString();
//    }
//}
