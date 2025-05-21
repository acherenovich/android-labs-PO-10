package com.example.myapplication;
import android.graphics.Bitmap;
import android.util.Base64;
import android.graphics.BitmapFactory;

public class ImageConverter {

    public static Bitmap getImageFromBase64(String base64Image) {
        // Декодируем строку Base64 в массив байтов
        byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
        // Преобразуем байты в Bitmap
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
