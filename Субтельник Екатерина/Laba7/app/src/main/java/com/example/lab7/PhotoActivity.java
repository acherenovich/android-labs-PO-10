package com.example.lab7;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        ImageView imageView = findViewById(R.id.imageView);

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.Media._ID};
        String selection = MediaStore.Images.Media.RELATIVE_PATH + " LIKE ?";
        String[] selectionArgs = new String[]{"DCIM/Camera/%"};
        String orderBy = MediaStore.Images.Media.DATE_TAKEN + " DESC";

        Cursor cursor = getContentResolver().query(uri, projection, selection, selectionArgs, orderBy);

        if(cursor != null && cursor.moveToFirst()) {
            long imageId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
            cursor.close();

            Uri imageUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, String.valueOf(imageId));
            imageView.setImageURI(imageUri);
        }
        else {
            Toast.makeText(this, "Ошибка просмотра фото", Toast.LENGTH_SHORT).show();
            finish();
        }

    }
}