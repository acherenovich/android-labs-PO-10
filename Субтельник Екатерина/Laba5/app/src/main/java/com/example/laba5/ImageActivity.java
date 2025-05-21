package com.example.laba5;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        ImageView imageView = findViewById(R.id.imageView);

        String fileUriString = getIntent().getStringExtra("fileUri");
        if (fileUriString != null) {
            Uri fileUri = Uri.parse(fileUriString);
            imageView.setImageURI(fileUri);
        }
    }
}
