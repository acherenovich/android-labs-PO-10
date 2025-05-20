package com.example.lab8;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.osmdroid.util.GeoPoint;
import java.util.ArrayList;
import java.util.List;

public class LocationDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "locations.db";
    private static final int DATABASE_VERSION = 1;
    // Таблица маршрутов
    private static final String TABLE_TRACKS = "tracks";
    private static final String COLUMN_TRACK_ID = "id";
    private static final String COLUMN_TRACK_NAME = "name";

    // Таблица точек
    private static final String TABLE_LOCATIONS = "locations";
    private static final String COLUMN_LAT = "latitude";
    private static final String COLUMN_LON = "longitude";
    private static final String COLUMN_TRACK_FK = "track_id"; // Внешний ключ

    public LocationDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Создаем таблицу маршрутов
        String createTracksTable = "CREATE TABLE " + TABLE_TRACKS + " (" +
                COLUMN_TRACK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TRACK_NAME + " TEXT)";
        db.execSQL(createTracksTable);

        // Создаем таблицу точек с привязкой к маршруту
        String createLocationsTable = "CREATE TABLE " + TABLE_LOCATIONS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LAT + " REAL, " +
                COLUMN_LON + " REAL, " +
                COLUMN_TRACK_FK + " INTEGER, " +
                "FOREIGN KEY (" + COLUMN_TRACK_FK + ") REFERENCES " + TABLE_TRACKS + "(" + COLUMN_TRACK_ID + "))";
        db.execSQL(createLocationsTable);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRACKS);
        onCreate(db);
    }

    // Создать новый маршрут и вернуть его ID
    public long createTrack(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TRACK_NAME, name);
        long trackId = db.insert(TABLE_TRACKS, null, values);
        db.close();
        return trackId;
    }

    // Сохранить точку с привязкой к маршруту
    public void saveLocation(double lat, double lon, long trackId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LAT, lat);
        values.put(COLUMN_LON, lon);
        values.put(COLUMN_TRACK_FK, trackId);
        db.insert(TABLE_LOCATIONS, null, values);
        db.close();
    }

    // Получить все точки маршрута
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

    // Получить все маршруты
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

//    public void saveLocation(double lat, double lon) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_LAT, lat);
//        values.put(COLUMN_LON, lon);
//        db.insert(TABLE_NAME, null, values);
//        db.close();
//    }
//
//    public List<GeoPoint> getLocations() {
//        List<GeoPoint> points = new ArrayList<>();
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
//        if (cursor.moveToFirst()) {
//            do {
//                points.add(new GeoPoint(cursor.getDouble(1), cursor.getDouble(2)));
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        db.close();
//        return points;
//    }
}
