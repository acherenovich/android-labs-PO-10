package com.example.myapplication7;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import android.Manifest;


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


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION = 1;
    private MapView mapView;
    private Intent serviceIntent;
    private Marker locationMarker;
    private Polyline routeLine;
    private List<GeoPoint> routePoints;
    private List<Polyline> oldRoutes;
    private Marker startMarker;
    private Marker endMarker;

    private final BroadcastReceiver locationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            double latitude = intent.getDoubleExtra("latitude", 0);
            double longitude = intent.getDoubleExtra("longitude", 0);
            updateLocation(new GeoPoint(latitude, longitude));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Выполнила Прокопеня Карина");
        Configuration.getInstance().load(getApplicationContext(),
                getSharedPreferences("osmdroid", MODE_PRIVATE));


        mapView = findViewById(R.id.mapView);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.getController().setZoom(15);
        mapView.setMultiTouchControls(true);

        routePoints = new ArrayList<>();
        oldRoutes = new ArrayList<>();
        routeLine = new Polyline();
        routeLine.setWidth(5f);
        mapView.getOverlays().add(routeLine);

        locationMarker = new Marker(mapView);
        locationMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(locationMarker);



        if (!hasLocationPermission()) {
            requestLocationPermission();
        }

        Button startButton = findViewById(R.id.startTrackingButton);
        Button stopButton = findViewById(R.id.stopTrackingButton);
//        Button zoomInButton = findViewById(R.id.zoomInButton);
//        Button zoomOutButton = findViewById(R.id.zoomOutButton);
        Button clearOldRoutesButton = findViewById(R.id.clearOldRoutesButton);
        clearOldRoutesButton.setOnClickListener(v -> clearOldRoutes());


        Button newRouteButton = findViewById(R.id.resetButton);
        newRouteButton.setOnClickListener(v -> startNewRoute());

        startButton.setOnClickListener(v -> startTrackingService());
        stopButton.setOnClickListener(v -> stopTrackingService());
//        zoomInButton.setOnClickListener(v -> mapView.getController().zoomIn());
//        zoomOutButton.setOnClickListener(v -> mapView.getController().zoomOut());

        loadRoutes();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu); // Загружаем меню
        return true;  // Возвращаем true для отображения меню
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_student) {
            Intent intent = new Intent(this, StudentInfoActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.menu_lab) {
            Intent intent = new Intent(this, LabInfoActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void clearOldRoutes() {
        // Удаляем маршруты с карты
        for (Polyline oldRoute : oldRoutes) {
            mapView.getOverlays().remove(oldRoute);
        }
        oldRoutes.clear();

        // Очищаем сохранённые маршруты
        SharedPreferences preferences = getSharedPreferences("routes", MODE_PRIVATE);
        preferences.edit().clear().apply();

        // Перерисовываем карту
        mapView.invalidate();
    }


    private void loadRoutes() {
        SharedPreferences prefs = getSharedPreferences("routes", MODE_PRIVATE);
        String routesString = prefs.getString("saved_routes", null);
        if (routesString == null) return;

        try {
            JSONArray jsonArray = new JSONArray(routesString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONArray routeArray = jsonArray.getJSONArray(i);
                List<GeoPoint> points = new ArrayList<>();
                for (int j = 0; j < routeArray.length(); j++) {
                    JSONObject jsonPoint = routeArray.getJSONObject(j);
                    double lat = jsonPoint.getDouble("lat");
                    double lon = jsonPoint.getDouble("lon");
                    points.add(new GeoPoint(lat, lon));
                }

                // Создание линии маршрута и добавление в карту
                Polyline oldRoute = new Polyline();
                oldRoute.setPoints(points);
                oldRoute.setWidth(20f);
                oldRoute.setColor(0xFFFF0000);
                mapView.getOverlays().add(oldRoute);
                oldRoutes.add(oldRoute);
            }
            mapView.invalidate(); // Обновляем карту
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private void saveRoutes() {
        SharedPreferences prefs = getSharedPreferences("routes", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray jsonArray = new JSONArray();

        for (Polyline route : oldRoutes) {
            JSONArray routeArray = new JSONArray();
            for (GeoPoint point : route.getActualPoints()) {
                JSONObject jsonPoint = new JSONObject();
                try {
                    jsonPoint.put("lat", point.getLatitude());
                    jsonPoint.put("lon", point.getLongitude());
                    routeArray.put(jsonPoint);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            jsonArray.put(routeArray);
        }

        editor.putString("saved_routes", jsonArray.toString());
        editor.apply();
    }



    private void startNewRoute() {
        if (!routePoints.isEmpty()) {
            Polyline oldRoute = new Polyline();
            oldRoute.setPoints(new ArrayList<>(routePoints));
            oldRoute.setWidth(20f);
            oldRoute.setColor(0xFFFF0000); // Розовый цвет
            mapView.getOverlays().add(oldRoute);
            oldRoutes.add(oldRoute);
            saveRoutes(); // Сохранение маршрута после завершения
        }

        // Очистка списка точек перед созданием нового маршрута
        routePoints.clear();

        // Создание новой линии маршрута
        mapView.getOverlays().remove(routeLine);
        routeLine = new Polyline();
        routeLine.setWidth(5f);
        mapView.getOverlays().add(routeLine);

        // Удаление старых маркеров
        if (startMarker != null) {
            mapView.getOverlays().remove(startMarker);
            startMarker = null;
        }
        if (endMarker != null) {
            mapView.getOverlays().remove(endMarker);
            endMarker = null;
        }

        mapView.invalidate();
        Toast.makeText(this, "Новый маршрут начат", Toast.LENGTH_SHORT).show();
    }





    private void updateLocation(GeoPoint point) {
        locationMarker.setPosition(point);
        mapView.getController().animateTo(point);

        // Добавление начальной точки, если маршрут пустой
        if (routePoints.isEmpty()) {
            Marker startMarker = new Marker(mapView);
            startMarker.setPosition(point);
            startMarker.setIcon(getDrawable(R.drawable.ic_start)); // Используем значок старта
            startMarker.setTitle("Начало пути");
            mapView.getOverlays().add(startMarker);
        }

        // Добавление текущей точки
        routePoints.add(point);
        routeLine.setPoints(routePoints);

        // Обновление карты
        mapView.invalidate();
    }


    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private void startTrackingService() {
        // Если уже был маршрут — сохраняем его в "старые маршруты"
        if (!routePoints.isEmpty()) {
            Polyline oldRoute = new Polyline();
            oldRoute.setPoints(new ArrayList<>(routePoints));
            oldRoute.setWidth(5f);
            oldRoute.setColor(0xFFFF69B4); // Розовый цвет
            mapView.getOverlays().add(oldRoute);
            oldRoutes.add(oldRoute);
        }

        // Очищаем текущий маршрут
        routePoints.clear();
        routeLine.setPoints(routePoints);

        // Удаляем предыдущие маркеры
        if (startMarker != null) mapView.getOverlays().remove(startMarker);
        if (endMarker != null) mapView.getOverlays().remove(endMarker);

        serviceIntent = new Intent(this, LocationService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
        registerReceiver(locationReceiver, new IntentFilter("LOCATION_UPDATE"));
        Toast.makeText(this, "Сервис запущен", Toast.LENGTH_SHORT).show();
    }

    private void stopTrackingService() {
        if (serviceIntent != null) {
            stopService(serviceIntent);
            unregisterReceiver(locationReceiver);
            serviceIntent = null;

            // Добавление значка конца маршрута
            if (!routePoints.isEmpty()) {
                Marker endMarker = new Marker(mapView);
                endMarker.setPosition(routePoints.get(routePoints.size() - 1));
                endMarker.setIcon(getDrawable(R.drawable.ic_end)); // Значок конца пути
                endMarker.setTitle("Конец пути");
                mapView.getOverlays().add(endMarker);
                mapView.invalidate();
            }

            Toast.makeText(this, "Сервис остановлен", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startTrackingService();
            } else {
                Toast.makeText(this, "Разрешение не предоставлено.", Toast.LENGTH_LONG).show();
            }
        }
    }
}