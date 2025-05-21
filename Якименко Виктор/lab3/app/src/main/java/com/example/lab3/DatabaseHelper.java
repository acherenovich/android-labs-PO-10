package com.example.lab3;

import android.content.ContentValues;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.database.sqlite.SQLiteStatement;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "notesinfo.db"; // название бд
    private static final int SCHEMA = 1; // версия базы данных
    static final String TABLE = "notes"; // название таблицы в бд
    // названия столбцов
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DESCRIPTION = "description";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_DESCRIPTION + " TEXT);");

        // Добавление начальных данных
        for (int i = 1; i <= 100; i++) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_DESCRIPTION, String.format("Текст замечательной заметки %d", i));

            db.insert(TABLE, null, values);
        }

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
        onCreate(db);
    }
}
