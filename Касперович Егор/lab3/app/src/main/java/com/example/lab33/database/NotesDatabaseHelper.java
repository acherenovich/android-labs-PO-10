package com.example.lab33.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NotesDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NOTES = "notes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DESCRIPTION = "description";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NOTES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_DESCRIPTION + " TEXT NOT NULL);";

    public NotesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        addInitialNotes(db);
        Log.d("DB_CHECK", "Database created and test notes added");
    }

    private void addInitialNotes(SQLiteDatabase db) {
        for (int i = 1; i <= 20; i++) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_DESCRIPTION, "Test note " + i);
            db.insert(TABLE_NOTES, null, values);
        }
        Log.d("DB_CHECK", "Initial notes inserted");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }

    // Добавление новой заметки
    public void addNote(String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DESCRIPTION, description);
        db.insert(TABLE_NOTES, null, values);
        db.close();
    }

    // Удаление заметки по ID
    public void deleteNote(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Обновление заметки по ID
    public void updateNote(int id, String newDescription) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DESCRIPTION, newDescription);
        db.update(TABLE_NOTES, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Получение всех заметок
    public Cursor getAllNotes() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT _id, description FROM " + TABLE_NOTES, null);
    }


    public boolean isDatabaseEmpty() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NOTES, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            Log.d("DB_CHECK", "Notes count in DB: " + count);
            return count < 20;
        }
        return true;
    }

    public void insertTestData() {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 1; i <= 20; i++) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_DESCRIPTION, "Test Note " + i); // Исправлено!
            db.insert(TABLE_NOTES, null, values);
        }
        db.close();
    }


}
