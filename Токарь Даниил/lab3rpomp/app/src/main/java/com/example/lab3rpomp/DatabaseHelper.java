package com.example.lab3rpomp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dblab3rpomp.db"; // Имя файла БД в assets и в системе
    private static final int DATABASE_VERSION = 1;
    private static String DATABASE_PATH;
    private final Context context;
    private SQLiteDatabase database;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        DATABASE_PATH = context.getApplicationInfo().dataDir + "/databases/"; // Путь к папке databases
    }

    // Создание базы данных (не вызывается, если БД уже есть)
    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    // Обновление базы данных (не вызывается в нашей задаче, т.к. версия БД не меняется)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // При обновлении версии БД здесь можно было бы выполнить миграцию данных.
    }

    // Открытие базы данных
    public void openDatabase() throws IOException {
        String dbFullPath = DATABASE_PATH + DATABASE_NAME;
        if (!checkDatabase()) { // Если БД не существует в папке приложения, копируем из assets
            copyDatabase();
        }
        database = SQLiteDatabase.openDatabase(dbFullPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    // Закрытие базы данных
    @Override
    public synchronized void close() {
        if (database != null)
            database.close();
        super.close();
    }

    // Проверка, существует ли база данных в папке приложения
    private boolean checkDatabase() {
        File dbFile = new File(DATABASE_PATH + DATABASE_NAME);
        return dbFile.exists();
    }

    public boolean addNote(String description, double price) { // Метод теперь принимает цену
        SQLiteDatabase db = this.getWritableDatabase();
        android.content.ContentValues values = new android.content.ContentValues();
        values.put("description", description);
        values.put("price", price); // Добавляем цену в ContentValues

        long result = 0;
        try {
            result = db.insert("notes", null, values);
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Ошибка при вставке заметки в БД", e);
            return false;
        }
        // db.close();  Не закрываем здесь db

        if (result != -1) {
            Log.d("DatabaseHelper", "Заметка успешно добавлена, ID = " + result + ", description = " + description + ", price = " + price); // Лог с ценой
            return true;
        } else {
            Log.e("DatabaseHelper", "Ошибка при добавлении заметки, insert() вернул -1");
            return false;
        }
    }

    public boolean deleteNote(int noteId) {
        SQLiteDatabase db = this.getWritableDatabase(); // Получаем доступную для записи БД
        int deletedRows = db.delete("notes", "_id = ?", new String[]{String.valueOf(noteId)}); // Удаляем запись из таблицы "notes" по условию _id = noteId
        // db.close(); Не закрываем здесь db, управление жизненным циклом БД в FragmentDel

        return deletedRows > 0; // delete() возвращает количество удаленных строк
    }

    // Копирование базы данных из assets в папку приложения
    private void copyDatabase() throws IOException {
        try {
            // Открываем файл БД в assets как входящий поток
            InputStream inputStream = context.getAssets().open(DATABASE_NAME);

            // Создаем папку databases, если ее нет
            File databasesDir = new File(DATABASE_PATH);
            if (!databasesDir.exists()) {
                databasesDir.mkdirs();
            }

            // Полный путь к БД в папке приложения
            String dbFullPath = DATABASE_PATH + DATABASE_NAME;

            // Открываем файл для записи в папке приложения
            OutputStream outputStream = new FileOutputStream(dbFullPath);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();

        } catch (IOException e) {
            Log.e("DatabaseHelper", "Ошибка копирования БД", e);
            throw new IOException("Ошибка копирования базы данных из assets", e);
        }
    }

    public boolean updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        android.content.ContentValues values = new android.content.ContentValues();
        values.put("description", note.getDescription());
        values.put("price", note.getPrice());

        int updatedRows = db.update("notes", values, "_id = ?", new String[]{String.valueOf(note.getId())});
        // db.close();  Do not close db here, lifecycle management in FragmentUpdate

        return updatedRows > 0; // update() returns the number of updated rows
    }

    // Получение списка всех заметок из базы данных
    public List<Note> getAllNotes() {
        List<Note> noteList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase(); // Получаем доступную для чтения БД
        Cursor cursor = null;
        try {
            // Запрос теперь выбирает _id, description и price
            cursor = db.query("notes", new String[]{"_id", "description", "price"},
                    null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    // Получаем значения из курсора для _id (индекс 0), description (индекс 1), price (индекс 2)
                    int id = cursor.getInt(0);
                    String description = cursor.getString(1);
                    double price = cursor.getDouble(2);

                    // Создаем объект Note, передавая id, description и price
                    Note note = new Note(id, description, price);
                    noteList.add(note);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            // db.close();  Не закрываем здесь db
        }
        return noteList;
    }
}