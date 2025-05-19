package com.example.lab17;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;


public class MainActivity extends AppCompatActivity {
    private static final int PICKFILE_RESULT_CODE = 1;
    private String setType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestMediaPermissions();
        updateButtonStates();

        Button audioBtn = findViewById(R.id.audioBtn);
        Button videoBtn = findViewById(R.id.videoBtn);
        Button imageBtn = findViewById(R.id.imageBtn);
        Button cameraBtn = findViewById(R.id.cameraBtn);
        Button infoButton = findViewById(R.id.info_button);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });

        audioBtn.setOnClickListener(view -> {
            setType = "audio/*";
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("audio/*");
            startActivityForResult(intent, PICKFILE_RESULT_CODE);
        });

        videoBtn.setOnClickListener(view -> {
            setType = "video/*";
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("video/*");
            startActivityForResult(intent, PICKFILE_RESULT_CODE);
        });

        imageBtn.setOnClickListener(view -> {
            setType = "image/*";
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, PICKFILE_RESULT_CODE);
        });

        cameraBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CameraActivity.class);
            startActivity(intent);
        });

    }

    private void requestMediaPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_MEDIA_IMAGES,
                            Manifest.permission.READ_MEDIA_VIDEO,
                            Manifest.permission.READ_MEDIA_AUDIO
                    },
                    100); // requestCode можно выбрать любой
        } else {
            Toast.makeText(this, "Разрешения уже предоставлены", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Разрешение не предоставлено: " + permissions[i], Toast.LENGTH_SHORT).show();
                }
            }
            updateButtonStates();
        }
    }

    private void updateButtonStates() {
        Button audioBtn = findViewById(R.id.audioBtn);
        Button videoBtn = findViewById(R.id.videoBtn);
        Button imageBtn = findViewById(R.id.imageBtn);

        boolean hasAudioPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED;
        boolean hasVideoPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED;
        boolean hasImagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;

        audioBtn.setEnabled(hasAudioPermission);
        videoBtn.setEnabled(hasVideoPermission);
        imageBtn.setEnabled(hasImagePermission);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICKFILE_RESULT_CODE && resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData();

            if (setType.equals("audio/*")) {
                openAudio(fileUri);
            } else if (setType.equals("video/*")) {
                openVideo(fileUri);
            } else if (setType.equals("image/*")) {
                openImage(fileUri);
            } else {
                Toast.makeText(this, "Неподдерживаемый формат файла", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openImage(Uri imageUri) {
        Intent intent = new Intent(this, ImageActivity.class);
        intent.putExtra("imageUri", imageUri.toString());
        startActivity(intent);
    }

    private void openAudio(Uri audioUri) {
        Intent intent = new Intent(this, AudioActivity.class);
        intent.putExtra("audioUri", audioUri.toString());
        startActivity(intent);
    }

    private void openVideo(Uri videoUri) {
        Intent intent = new Intent(this, VideoActivity.class);
        intent.putExtra("videoUri", videoUri.toString());
        startActivity(intent);
    }

}
