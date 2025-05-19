package com.example.lab8;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class LocationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        double latitude = intent.getDoubleExtra("latitude", 0);
        double longitude = intent.getDoubleExtra("longitude", 0);

        Log.d("LocationReceiver", "Получены координаты: " + latitude + ", " + longitude);
        // Здесь можно обновить UI или передать данные в активность
        // Например, обновить карту:
        if (context instanceof MainActivity) {
            ((MainActivity) context).addLocationToMap(latitude, longitude);
        }
    }
}
