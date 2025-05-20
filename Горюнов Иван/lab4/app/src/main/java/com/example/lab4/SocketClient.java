package com.example.lab4;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.Socket;

public class SocketClient {
    public static String sendMessage(String ip, int port, String message) {
        try (Socket socket = new Socket(ip, port);
             DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
             InputStream inputStream = socket.getInputStream()) {

            outputStream.writeUTF(message);
            outputStream.flush();

            byte[] buffer = new byte[1024];
            int bytesRead = inputStream.read(buffer);
            return new String(buffer, 0, bytesRead);
        } catch (Exception e) {
            e.printStackTrace();
            return "Ошибка соединения!";
        }
    }
}

