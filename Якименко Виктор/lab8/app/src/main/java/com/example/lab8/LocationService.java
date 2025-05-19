package com.example.lab8;


import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import org.osmdroid.util.GeoPoint;

public class LocationService extends Service {
    private static final String CHANNEL_ID = "LocationChannel";
    private static final int NOTIFICATION_ID = 1;
    private Location lastLocation = null;  // Для хранения предыдущей локации
    private static final float MIN_DISTANCE = 100f;  // Минимальное расстояние для обновления (в метрах)
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    private LocationDatabase db;
    private long currentTrackId; // ID текущего маршрута

    @Override
    public void onCreate() {
        super.onCreate();

        super.onCreate();
        db = new LocationDatabase(this);
        currentTrackId = db.createTrack("Маршрут " + System.currentTimeMillis()); // Создаем новый маршрут

        createNotificationChannel();
        startForeground(NOTIFICATION_ID, createNotification());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest.Builder(10000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    if (lastLocation == null || location.distanceTo(lastLocation) > MIN_DISTANCE) {
                        // Если местоположение изменилось на более чем MIN_DISTANCE метров
                        Log.d("LocationService", "Новая локация: " + location.getLatitude() + ", " + location.getLongitude());
                        db.saveLocation(location.getLatitude(), location.getLongitude(), currentTrackId);

                        // Отправляем broadcast о новой координате
                        Intent intent = new Intent("com.example.lab8.LOCATION_UPDATED");
                        intent.putExtra("latitude", location.getLatitude());
                        intent.putExtra("longitude", location.getLongitude());
                        sendBroadcast(intent);


                        lastLocation = location;  // Обновляем предыдущую локацию
                    }
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.w("LocationService", "Нет разрешения на геолокацию");
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, getMainLooper());
    }

    private Notification createNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Отслеживание местоположения")
                .setContentText("Идёт сбор геоданных в фоне")
                .setSmallIcon(R.drawable.ic_location) // Добавьте иконку в res/drawable
                .build();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Location Tracking",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (fusedLocationClient != null && locationCallback != null) {

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            Log.d("Location", "Широта: " + latitude + ", Долгота: " + longitude);

                            // Отправляем broadcast о новой координате
                            Intent intent = new Intent("com.example.lab8.LOCATION_UPDATED");
                            intent.putExtra("latitude", latitude);
                            intent.putExtra("longitude", longitude);
                            sendBroadcast(intent);
                        } else {
                            Log.d("Location", "Местоположение недоступно");
                        }
                    })
                    .addOnFailureListener(e -> Log.e("Location", "Ошибка получения местоположения", e));


            fusedLocationClient.removeLocationUpdates(locationCallback);
            Log.d("STOPSERVICE1", "Остановка сервиса");
        }
        stopForeground(true);  // Останавливаем foreground-сервис
    }

}