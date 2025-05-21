package com.example.lab4;//package com.example.lab4;
//
//import android.util.Log;
//
//import java.io.InputStreamReader;
//import java.net.Socket;
//
//public class SocketClient extends Thread {
//    @Override
//    public void run() {
//        try {
//            Socket socket = new Socket("remote.servername.com", 13); //порт 13
//            InputStreamReader isr = new InputStreamReader(socket.getInputStream(), "ISO-8859-1");
//            StringBuilder response = new StringBuilder();
//            int ch;
//            while ((ch = isr.read()) != -1) {
//                response.append((char) ch);
//            }
//            Log.d("Socket", "Ответ сервера: " + response.toString());
//            socket.close();
//        } catch (Exception e) {
//            Log.e("Socket", "Ошибка подключения", e);
//        }
//    }
//}
