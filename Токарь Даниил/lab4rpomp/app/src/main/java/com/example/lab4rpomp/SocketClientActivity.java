package com.example.lab4rpomp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClientActivity extends AppCompatActivity {

    private EditText editTextServerIp;
    private EditText editTextServerPort;
    private EditText editTextMessage;
    private Button buttonSendMessage;
    private TextView textViewSocketResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_client);

        editTextServerIp = findViewById(R.id.editTextServerIp);
        editTextServerPort = findViewById(R.id.editTextServerPort);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSendMessage = findViewById(R.id.buttonSendMessage);
        textViewSocketResponse = findViewById(R.id.textViewSocketResponse);

        buttonSendMessage.setOnClickListener(v -> {
            String serverIp = editTextServerIp.getText().toString();
            String portStr = editTextServerPort.getText().toString();
            String message = editTextMessage.getText().toString();

            if (!serverIp.isEmpty() && !portStr.isEmpty() && !message.isEmpty()) {
                int port = Integer.parseInt(portStr);
                new SocketClientTask().execute(serverIp, port, message);
            } else {
                textViewSocketResponse.setText("Заполните все поля");
            }
        });
    }

    private class SocketClientTask extends AsyncTask<Object, Void, String> {

        @Override
        protected String doInBackground(Object... params) {
            String serverIp = (String) params[0];
            int port = (int) params[1];
            String message = (String) params[2];
            String response = "";

            try (Socket socket = new Socket(serverIp, port);
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                out.println(message); // Отправляем сообщение серверу
                response = in.readLine(); // Читаем ответ от сервера

            } catch (IOException e) {
                return "Ошибка: " + e.getMessage();
            }
            return response != null ? response : "Нет ответа от сервера";
        }

        @Override
        protected void onPostExecute(String result) {
            textViewSocketResponse.setText(result);
        }
    }
}
