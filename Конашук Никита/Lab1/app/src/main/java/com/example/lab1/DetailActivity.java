package com.example.lab1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {
    private TextView detailTitle, detailBody;
    private Button shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailTitle = findViewById(R.id.detailTitle);
        detailBody = findViewById(R.id.detailBody);
        shareButton = findViewById(R.id.shareButton);

        // Получаем переданные данные
        String title = getIntent().getStringExtra("title");
        String body = getIntent().getStringExtra("body");

        // Устанавливаем данные в TextView
        detailTitle.setText(title);
        detailBody.setText(body);

        // Обработчик нажатия на кнопку "Поделиться"
        shareButton.setOnClickListener(v -> sharePost(title, body));
    }

    private void sharePost(String title, String body) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, title + "\n\n" + body);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, "Поделиться постом через...");
        startActivity(shareIntent);
    }
}
