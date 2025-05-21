package com.example.lab3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;



public class NotesDAO {
    private NotesDatabaseHelper dbHelper;

    public NotesDAO(Context context) {
        dbHelper = new NotesDatabaseHelper(context);
    }

    public long addNote(String description) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NotesDatabaseHelper.COLUMN_DESCRIPTION, description);
        return db.insert(NotesDatabaseHelper.TABLE_NAME, null, values);
    }

    public int deleteNote(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(NotesDatabaseHelper.TABLE_NAME, "id = ?", new String[]{String.valueOf(id)});
    }

    public int updateNote(int id, String newDescription) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NotesDatabaseHelper.COLUMN_DESCRIPTION, newDescription);
        return db.update(NotesDatabaseHelper.TABLE_NAME, values, "id = ?", new String[]{String.valueOf(id)});
    }
}

