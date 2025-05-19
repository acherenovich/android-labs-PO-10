package com.example.lab8;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.*;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import java.util.ArrayList;
import java.util.List;
import android.graphics.drawable.Drawable;
import android.graphics.Color;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION = 1;
    private MapView mapView;
    private Polyline polyline;
    private List<GeoPoint> trackPoints = new ArrayList<>();
    private boolean isFirstLocationUpdate = true;
    private boolean firstPointSet = false;
    private LocationDatabase db;
    private LocationReceiver locationReceiver;
    private Intent serviceIntent;
    Button deleteTracksButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(getApplicationContext(),
                getSharedPreferences("osmdroid", MODE_PRIVATE));
        setContentView(R.layout.activity_main);
        deleteTracksButton = findViewById(R.id.deleteTracksButton);
        deleteTracksButton.setOnClickListener(v -> deleteAllRoutes());
        locationReceiver = new LocationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.lab8.LOCATION_UPDATED");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(locationReceiver, filter, Context.RECEIVER_EXPORTED);
        }
        db = new LocationDatabase(this);
        mapView = findViewById(R.id.map);
        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        mapView.getController().setZoom(15);
        // Запуск службы геолокации при запуске приложения
        if (!hasLocationPermission()) {
            requestLocationPermission();
        } else {
            //startTrackingService();
        }

        Button startButton = findViewById(R.id.startTrackingButton);
        Button stopButton = findViewById(R.id.stopTrackingButton);
        startButton.setOnClickListener(v -> startTrackingService());
        stopButton.setOnClickListener(v -> stopTrackingService());

        // Загружаем маршруты из БД сразу при запуске
        loadSavedLocations();
        //mapView.invalidate();
    }
    private void deleteAllRoutes() {
        db.deleteAllTracksAndLocations();
        Toast.makeText(this, "Все маршруты удалены", Toast.LENGTH_SHORT).show();
        mapView.getOverlays().clear();
        trackPoints.clear();
        polyline = null;
        firstPointSet = false;
        isFirstLocationUpdate = true;
        mapView.invalidate();
        loadSavedLocations();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationReceiver != null) {
            unregisterReceiver(locationReceiver);
        }
        stopTrackingService();
    }


    private void startTrackingService() {
        serviceIntent = new Intent(this, LocationService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Toast.makeText(this, "Запуск сервиса", Toast.LENGTH_SHORT).show();
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }

    private void stopTrackingService() {
        if (serviceIntent != null) {
            Marker marker = new Marker(mapView);
            marker.setPosition(trackPoints.get(trackPoints.size() - 1));
            marker.setTitle("Конец");
            mapView.getOverlays().add(marker);

            stopService(serviceIntent);
            serviceIntent = null;
        }
        Log.d("STOPSERVICE", "Остановка сервиса");
    }

    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
    }


    void addLocationToMap(double lat, double lon) {
        GeoPoint point = new GeoPoint(lat, lon);
        trackPoints.add(point);
        if (isFirstLocationUpdate) {
            mapView.getController().setCenter(point);
            isFirstLocationUpdate = false;

            Marker marker = new Marker(mapView);
            marker.setPosition(trackPoints.get(0));
            marker.setTitle("Начало");
            mapView.getOverlays().add(marker);
        }

        updatePolyline();
        mapView.invalidate();
    }

    private void updatePolyline() {
        if (trackPoints.size() >= 2) {
            if (polyline != null) {
                mapView.getOverlays().remove(polyline);
            }
            polyline = new Polyline();
            polyline.setPoints(trackPoints);
            mapView.getOverlays().add(polyline);
        }
    }

    private void drawPolyline(List<GeoPoint> points) {
        if (points.size() < 2) return;

        Log.d("LOADSAVEDLOCATIONS", "DrawPolyline");

        polyline = new Polyline();
        polyline.setPoints(points);

        mapView.getOverlayManager().add(polyline);
        mapView.invalidate();
    }

    private void loadSavedLocations() {
        List<Track> tracks = db.getAllTracks();

        Log.d("LOADSAVEDLOCATIONS", tracks.toString());

        for (Track track : tracks) {
            List<GeoPoint> savedPoints = db.getLocations(track.getId());

            Log.d("LOADSAVEDLOCATIONS", String.valueOf(track.getId()));
            Log.d("LOADSAVEDLOCATIONS", savedPoints.toString());


            if (!savedPoints.isEmpty()) {
                // Добавляем линию на карту
                drawPolyline(savedPoints);


                Marker marker = new Marker(mapView);
                marker.setPosition(savedPoints.get(0));
                marker.setTitle("Начало " + track.getId());
                mapView.getOverlays().add(marker);

                marker = new Marker(mapView);
                marker.setPosition(savedPoints.get(savedPoints.size() - 1));
                marker.setTitle("Конец " + track.getId());
                mapView.getOverlays().add(marker);

                if (!firstPointSet) {
                    mapView.getController().setCenter(savedPoints.get(0));
                    firstPointSet = true;
                }
            }
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(this, "Разрешение не предоставлено.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
