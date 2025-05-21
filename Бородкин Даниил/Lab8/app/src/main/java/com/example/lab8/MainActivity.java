package com.example.lab8;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS = 1001;
    private static final String PREFS_NAME = "geo_prefs";
    private static final String KEY_POINTS = "saved_points";

    private MapView mapView;
    private Button btnAddPoint, btnLocate;
    private FusedLocationProviderClient fusedLocationClient;
    private ArrayList<LocationData> locationList = new ArrayList<>();
    private Polyline routeLine;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Configuration.getInstance().load(getApplicationContext(), getSharedPreferences("osmdroid", MODE_PRIVATE));
            setContentView(R.layout.activity_main);

            mapView = findViewById(R.id.map);
            btnAddPoint = findViewById(R.id.btn_add_point);
            btnLocate = findViewById(R.id.btn_locate);

            mapView.setTileSource(TileSourceFactory.MAPNIK);
            mapView.getController().setZoom(15.0);
            mapView.setMultiTouchControls(true);

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            requestPermissions();
            requestLocationUpdates();

            btnAddPoint.setOnClickListener(v -> addCurrentLocation());
            btnLocate.setOnClickListener(v -> locateUser());

            loadSavedPoints();
        }

        private void requestLocationUpdates() {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return;
            }

            LocationRequest locationRequest = LocationRequest.create()
                    .setInterval(25000)
                    .setFastestInterval(25000)
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            for (Location location : locationResult.getLocations()) {
                                GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                                locationList.add(new LocationData(geoPoint, getCurrentTime()));
                                savePoints();
                                updateMap(geoPoint, getCurrentTime());
                                mapView.getController().setCenter(geoPoint);
                            }
                        }
                    },
                    null
            );
        }
    

    private void requestPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS);
        }
    }

    private void addCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Нет разрешения на геолокацию!", Toast.LENGTH_SHORT).show();
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                String timestamp = getCurrentTime();
                GeoPoint point = new GeoPoint(location.getLatitude(), location.getLongitude());

                locationList.add(new LocationData(point, timestamp));
                savePoints();

                updateMap(point, timestamp);
            } else {
                Toast.makeText(this, "Не удалось получить местоположение", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void locateUser() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Нет разрешения на геолокацию!", Toast.LENGTH_SHORT).show();
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                GeoPoint point = new GeoPoint(location.getLatitude(), location.getLongitude());
                mapView.getController().setCenter(point);
                Toast.makeText(this, "Местоположение обновлено", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Не удалось определить местоположение", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void updateMap(GeoPoint point, String timestamp) {
        runOnUiThread(() -> {
            Marker marker = new Marker(mapView);
            marker.setIcon(getDrawable(R.drawable.ic_launcher_foreground));
            marker.setPosition(point);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setTitle("Посещено: " + timestamp);
            marker.setOnMarkerClickListener((m, mapView) -> {
                Toast.makeText(MainActivity.this, m.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            });

            mapView.getOverlays().add(marker);

            drawRoute();
            mapView.invalidate();
        });
    }

    private void drawRoute() {
        if (locationList.size() > 1) {
            if (routeLine != null) {
                mapView.getOverlays().remove(routeLine);
            }
            routeLine = new Polyline();
            for (LocationData loc : locationList) {
                routeLine.addPoint(loc.getGeoPoint());
            }
            mapView.getOverlays().add(routeLine);
        }
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void savePoints() {
        JSONArray jsonArray = new JSONArray();
        for (LocationData loc : locationList) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("lat", loc.getGeoPoint().getLatitude());
                obj.put("lon", loc.getGeoPoint().getLongitude());
                obj.put("timestamp", loc.getTimestamp());
                jsonArray.put(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .edit()
                .putString(KEY_POINTS, jsonArray.toString())
                .apply();
    }

    private void loadSavedPoints() {
        String jsonString = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).getString(KEY_POINTS, "");
        if (!jsonString.isEmpty()) {
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    double lat = obj.getDouble("lat");
                    double lon = obj.getDouble("lon");
                    String timestamp = obj.getString("timestamp");

                    GeoPoint point = new GeoPoint(lat, lon);
                    locationList.add(new LocationData(point, timestamp));
                    updateMap(point, timestamp);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Разрешение на геолокацию получено", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Разрешение на геолокацию НЕ получено!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private static class LocationData {
        private final GeoPoint geoPoint;
        private final String timestamp;

        public LocationData(GeoPoint geoPoint, String timestamp) {
            this.geoPoint = geoPoint;
            this.timestamp = timestamp;
        }

        public GeoPoint getGeoPoint() {
            return geoPoint;
        }

        public String getTimestamp() {
            return timestamp;
        }
    }
}
