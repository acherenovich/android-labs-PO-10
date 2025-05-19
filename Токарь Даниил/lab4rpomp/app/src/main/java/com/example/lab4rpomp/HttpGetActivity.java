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
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpGetActivity extends AppCompatActivity {

    private EditText editTextUrl;
    private Button buttonSendRequest;
    private TextView textViewResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_get);

        editTextUrl = findViewById(R.id.editTextUrl);
        buttonSendRequest = findViewById(R.id.buttonSendRequest);
        textViewResponse = findViewById(R.id.textViewResponse);

        buttonSendRequest.setOnClickListener(v -> {
            String url = editTextUrl.getText().toString();
            if (!url.isEmpty()) {
                new HttpGetTask().execute(url);
            } else {
                textViewResponse.setText("Введите URL");
            }
        });
    }

    private class HttpGetTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String urlString = urls[0];
            StringBuilder response = new StringBuilder();
            HttpURLConnection urlConnection = null;

            try {
                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line).append("\n");
                }
                reader.close();

            } catch (IOException e) {
                return "Ошибка: " + e.getMessage();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return response.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            textViewResponse.setText(result);
        }
    }
}
