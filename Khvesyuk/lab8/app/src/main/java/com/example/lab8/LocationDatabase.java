package com.example.lab8;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Pair;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class LocationDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "locations.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_TRACKS = "tracks";
    private static final String COLUMN_TRACK_ID = "id";
    private static final String COLUMN_TRACK_NAME = "name";

    private static final String TABLE_LOCATIONS = "locations";
    private static final String COLUMN_LAT = "latitude";
    private static final String COLUMN_LON = "longitude";
    private static final String COLUMN_TRACK_FK = "track_id";

    private static final String TABLE_POINTS = "points";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_TIME = "timestamp";


    public LocationDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTracksTable = "CREATE TABLE " + TABLE_TRACKS + " (" +
                COLUMN_TRACK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TRACK_NAME + " TEXT)";
        db.execSQL(createTracksTable);

        String createLocationsTable = "CREATE TABLE " + TABLE_LOCATIONS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LAT + " REAL, " +
                COLUMN_LON + " REAL, " +
                COLUMN_TRACK_FK + " INTEGER, " +
                "FOREIGN KEY (" + COLUMN_TRACK_FK + ") REFERENCES " + TABLE_TRACKS + "(" + COLUMN_TRACK_ID + "))";
        db.execSQL(createLocationsTable);

        String createPointsTable = "CREATE TABLE " + TABLE_POINTS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LAT + " REAL, " +
                COLUMN_LON + " REAL, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_TIME + " INTEGER)";
        db.execSQL(createPointsTable);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRACKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POINTS);

        onCreate(db);
    }

    public long createTrack(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TRACK_NAME, name);
        long trackId = db.insert(TABLE_TRACKS, null, values);
        db.close();
        return trackId;
    }

    public void saveLocation(double lat, double lon, long trackId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LAT, lat);
        values.put(COLUMN_LON, lon);
        values.put(COLUMN_TRACK_FK, trackId);
        db.insert(TABLE_LOCATIONS, null, values);
        db.close();
    }

    public List<GeoPoint> getLocations(long trackId) {
        List<GeoPoint> points = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT latitude, longitude FROM " + TABLE_LOCATIONS +
                " WHERE " + COLUMN_TRACK_FK + " = ?", new String[]{String.valueOf(trackId)});
        if (cursor.moveToFirst()) {
            do {
                points.add(new GeoPoint(cursor.getDouble(0), cursor.getDouble(1)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return points;
    }

    public List<Track> getAllTracks() {
        List<Track> tracks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TRACKS, null);
        if (cursor.moveToFirst()) {
            do {
                tracks.add(new Track(cursor.getLong(0), cursor.getString(1)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return tracks;
    }

    public void insertPoint(double latitude, double longitude, String name, long timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("latitude", latitude);
        values.put("longitude", longitude);
        values.put("name", name);
        values.put("timestamp", timestamp);
        db.insert("points", null, values);
    }


    public List<Pair<GeoPoint, Pair<String, Long>>> getSavedPoints() {
        List<Pair<GeoPoint, Pair<String, Long>>> points = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("points", new String[]{"latitude", "longitude", "name", "timestamp"}, null, null, null, null, null);

        if (cursor != null) {
            int latitudeColumnIndex = cursor.getColumnIndex("latitude");
            int longitudeColumnIndex = cursor.getColumnIndex("longitude");
            int nameColumnIndex = cursor.getColumnIndex("name");
            int timestampColumnIndex = cursor.getColumnIndex("timestamp");

            if (latitudeColumnIndex != -1 && longitudeColumnIndex != -1 && nameColumnIndex != -1 && timestampColumnIndex != -1) {
                while (cursor.moveToNext()) {
                    double latitude = cursor.getDouble(latitudeColumnIndex);
                    double longitude = cursor.getDouble(longitudeColumnIndex);
                    String name = cursor.getString(nameColumnIndex);
                    long timestamp = cursor.getLong(timestampColumnIndex);

                    GeoPoint point = new GeoPoint(latitude, longitude);
                    points.add(new Pair<>(point, new Pair<>(name, timestamp)));
                }
            } else {
                Log.e("Database", "One or more columns not found in the database.");
            }

            cursor.close();
        }
        db.close();
        return points;
    }
}
