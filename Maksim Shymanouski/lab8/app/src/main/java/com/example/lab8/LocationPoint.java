package com.example.lab8;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.function.Consumer;

public class LocationPoint {
    private final Context context;
    private LocationManager locationManager;

    public LocationPoint(Context context) {
        this.context = context;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    @SuppressLint("MissingPermission")
    public void getCurrentLocation(Consumer<Location> callback) {
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            callback.accept(location);
        }
    }

}
