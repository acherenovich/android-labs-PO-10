package com.example.laba5;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button selectFileButton = findViewById(R.id.selectFileButton);
        selectFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker();
            }
        });
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            if (fileUri != null) {
                openCorrectActivity(fileUri);
            }
        }
    }

    private void openCorrectActivity(Uri fileUri) {
        DocumentFile file = DocumentFile.fromSingleUri(this, fileUri);
        if (file != null && file.getName() != null) {
            String fileName = file.getName().toLowerCase();

            Intent intent = null;
            if (fileName.endsWith(".jpg") || fileName.endsWith(".png") || fileName.endsWith(".jpeg")) {
                intent = new Intent(this, ImageActivity.class);
            } else if (fileName.endsWith(".mp3") || fileName.endsWith(".wav") || fileName.endsWith(".ogg")) {
                intent = new Intent(this, AudioActivity.class);

            } else if (fileName.endsWith(".mp4") || fileName.endsWith(".avi") || fileName.endsWith(".mkv")) {
                intent = new Intent(this, VideoActivity.class);
            } else {
                Toast.makeText(this, "Error format file", Toast.LENGTH_SHORT).show();
            }

            if (intent != null) {
                intent.putExtra("fileUri", fileUri.toString());
                startActivity(intent);
            }
        }
    }
}
