package com.example.lab8;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.*;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MapView mapView;
    private IMapController mapController;
    private FusedLocationProviderClient fusedLocationClient;
    private List<GeoPoint> routePoints = new ArrayList<>();
    private Polyline routeLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(getApplicationContext(), getSharedPreferences("osmdroid", MODE_PRIVATE));
        setContentView(R.layout.activity_main);

        mapView = findViewById(R.id.mapView);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapController = mapView.getController();
        mapController.setZoom(15.0);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        routeLine = new Polyline();
        mapView.getOverlayManager().add(routeLine);

        loadRoute();
        requestLocationUpdates();

        // ✅ Запуск фонового сервиса
        Intent serviceIntent = new Intent(this, LocationService.class);
        startService(serviceIntent);

        // Кнопка очистки маршрута
        Button clearRouteButton = findViewById(R.id.clearRouteButton);
        clearRouteButton.setOnClickListener(v -> clearRoute());
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
                            routePoints.add(geoPoint);
                            saveRoute();
                            addMarker(geoPoint);
                            updateRoute();
                            mapController.setCenter(geoPoint);
                        }
                    }
                },
                null
        );
    }

    private void addMarker(GeoPoint point) {
        Marker marker = new Marker(mapView);
        marker.setPosition(point);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setIcon(getResources().getDrawable(android.R.drawable.ic_menu_compass));
        marker.setTitle("Посещенное место");
        mapView.getOverlays().add(marker);
        mapView.invalidate();
    }

    private void updateRoute() {
        routeLine.setPoints(routePoints);
        mapView.invalidate();
    }

    private void saveRoute() {
        SharedPreferences prefs = getSharedPreferences("RouteData", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        StringBuilder sb = new StringBuilder();

        for (GeoPoint point : routePoints) {
            sb.append(point.getLatitude()).append(",").append(point.getLongitude()).append(";");
        }

        editor.putString("route", sb.toString());
        editor.apply();
    }

    private void loadRoute() {
        SharedPreferences prefs = getSharedPreferences("RouteData", MODE_PRIVATE);
        String routeData = prefs.getString("route", "");

        if (!routeData.isEmpty()) {
            String[] points = routeData.split(";");
            for (String point : points) {
                String[] latLng = point.split(",");
                if (latLng.length == 2) {
                    GeoPoint geoPoint = new GeoPoint(Double.parseDouble(latLng[0]), Double.parseDouble(latLng[1]));
                    routePoints.add(geoPoint);
                }
            }
            updateRoute();
        }
    }

    private void clearRoute() {
        routePoints.clear();
        mapView.getOverlays().clear();
        routeLine.setPoints(routePoints); // ✅ Фикс проблемы с линиями
        mapView.getOverlays().add(routeLine);
        mapView.invalidate();

        SharedPreferences prefs = getSharedPreferences("RouteData", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("route");
        editor.apply();

        Toast.makeText(this, "Маршрут очищен!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
}
