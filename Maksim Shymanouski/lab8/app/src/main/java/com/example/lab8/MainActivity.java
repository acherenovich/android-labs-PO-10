package com.example.lab8;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Pair;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION = 1;
    private MapView mapView;
    private Polyline polyline;
    private List<GeoPoint> trackPoints = new ArrayList<>();
    private boolean isFirstLocationUpdate = true;
    private boolean firstPointSet = false;
    private LocationDatabase db;
    private LocationReceiver locationReceiver;

    private LocationPoint locationPoint;

    private ExecutorService executor;
    private Handler handler;

    private Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(getApplicationContext(),
                getSharedPreferences("osmdroid", MODE_PRIVATE));
        setContentView(R.layout.activity_main);

        locationReceiver = new LocationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.lab8.LOCATION_UPDATED");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(locationReceiver, filter, Context.RECEIVER_EXPORTED);
        }

        db = new LocationDatabase(this);
        locationPoint = new LocationPoint(this);

        mapView = findViewById(R.id.mapView);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        GeoPoint startPoint = new GeoPoint(52.10829, 23.76865);
        mapView.getController().setCenter(startPoint);
        mapView.getController().setZoom(15);



        if (!hasLocationPermission()) {
            requestLocationPermission();
        } else {
            //startTrackingService();
        }
        Log.d("OSM", "Loading map tiles...");
        Button btnMarkPoint = findViewById(R.id.btnMarkPoint);
        Button startButton = findViewById(R.id.btnStartTracking);
        Button stopButton = findViewById(R.id.btnStopTracking);

        btnMarkPoint.setOnClickListener(v -> showAddPointDialog());
        startButton.setOnClickListener(v -> startTrackingService());
        stopButton.setOnClickListener(v -> stopTrackingService());

        executor = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());

        loadSavedLocations();
        loadSavedPoints();
        //mapView.invalidate();
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
            Toast.makeText(this, "Start service", Toast.LENGTH_SHORT).show();
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }

    private void stopTrackingService() {
        if (serviceIntent != null) {
            Marker marker = new Marker(mapView);
            marker.setPosition(trackPoints.get(trackPoints.size() - 1));
            marker.setTitle("End");
            mapView.getOverlays().add(marker);


            stopService(serviceIntent);
            serviceIntent = null; // Убираем Intent после остановки сервиса
        }
        Log.d("STOPSERVICE", "Stop Service");
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

        //!!!!!!!!!!!!!!!!!!!!
        if (isFirstLocationUpdate) {
            mapView.getController().setCenter(point);
            isFirstLocationUpdate = false;

            Marker marker = new Marker(mapView);
            marker.setPosition(trackPoints.get(0));
            marker.setTitle("Beginning");
            mapView.getOverlays().add(marker);
        }

        //Marker marker = new Marker(mapView);
        //marker.setPosition(point);
        //marker.setTitle("Точка: " + trackPoints.size());
        //mapView.getOverlays().add(marker);

        updatePolyline(); //!!!!!!!!!!
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
                drawPolyline(savedPoints);


                Marker marker = new Marker(mapView);
                marker.setPosition(savedPoints.get(0));
                marker.setTitle("Beginning " + track.getId());
                mapView.getOverlays().add(marker);

                marker = new Marker(mapView);
                marker.setPosition(savedPoints.get(savedPoints.size() - 1));
                marker.setTitle("End " + track.getId());
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
                //startTrackingService();
            } else {
                Toast.makeText(this, "Permission is not guaranteed.", Toast.LENGTH_LONG).show();
            }
        }
    }



    private void markPoint(String name) {
        locationPoint.getCurrentLocation(location -> {
            double lat = location.getLatitude();
            double lon = location.getLongitude();
            long timestamp = System.currentTimeMillis();

            executor.execute(() -> {
                db.insertPoint(lat, lon, name, timestamp);
                handler.post(() -> {
                    addMarkerToMap(name, lat, lon, timestamp);
                    Toast.makeText(this, "Point was saved", Toast.LENGTH_SHORT).show();
                });
            });
        });
    }

    private void addMarkerToMap(String name, double lat, double lon, long timestamp) {
        Marker marker = new Marker(mapView);
        marker.setPosition(new GeoPoint(lat, lon));
        marker.setTitle(name);

        String formattedDate = formatTimestamp(timestamp);

        marker.setSnippet("Date: " + formattedDate + "<br>Coordinates: " + lat + ", " + lon);

        marker.setOnMarkerClickListener((m, mapView) -> {
            m.showInfoWindow();
            return true;
        });

        mapView.getOverlays().add(marker);
        mapView.invalidate();
    }


    private void showAddPointDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter the name of point");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Save (Shymanouski)", (dialog, which) -> {
            String name = input.getText().toString();
            if (!name.isEmpty()) {
                markPoint(name);
            } else {
                Toast.makeText(MainActivity.this, "The name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }


    private void loadSavedPoints() {
        List<Pair<GeoPoint, Pair<String, Long>>> savedPoints = db.getSavedPoints();
        for (Pair<GeoPoint, Pair<String, Long>> entry : savedPoints) {
            GeoPoint point = entry.first;
            String name = entry.second.first;
            long timestamp = entry.second.second;

            addMarkerToMap(name, point.getLatitude(), point.getLongitude(), timestamp);
        }
    }


    public String formatTimestamp(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault());

        Date date = new Date(timestamp);

        return sdf.format(date);
    }
}
