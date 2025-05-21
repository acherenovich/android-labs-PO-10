package com.example.lab3.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import com.example.lab3.Note;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "notes_app.db"; // Имя базы данных
    private static final int SCHEMA = 1;
    public static final String TABLE = "notes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_BODY = "body";
    public static final String COLUMN_DATE = "date";

    private String dbPath; // Путь к базе данных

    // Конструктор для использования пользовательского пути к БД
    public DatabaseHelper(@Nullable Context context, String dbPath) {
        super(context, DATABASE_NAME, null, SCHEMA);
        this.dbPath = dbPath;  // Сохраняем путь к базе данных
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("CREATE TABLE notes (" + COLUMN_ID
//                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_BODY
//                + " TEXT, " + COLUMN_DATE + " TEXT);");
//        List<String> realNotes = new ArrayList<>(Arrays.asList(
//                "Купить молоко и хлеб",
//                "Позвонить врачу и записаться на прием",
//                "Закончить отчет по проекту",
//                "Посмотреть новый фильм, который советовали",
//                "Записать идею для статьи",
//                "Почитать 20 страниц книги",
//                "Погулять вечером 30 минут",
//                "Написать другу, с которым давно не общался",
//                "Проверить расписание на завтра",
//                "Заняться уборкой в комнате",
//                "Запланировать поездку на выходные",
//                "Изучить новую главу по программированию",
//                "Купить подарок на день рождения",
//                "Посмотреть интересный курс онлайн",
//                "Сделать бэкап важных файлов",
//                "Оплатить счета за интернет и коммуналку",
//                "Разобрать почту и удалить ненужные письма",
//                "Составить список целей на месяц",
//                "Приготовить новое блюдо",
//                "Выучить 5 новых слов на иностранном языке"
//        ));
//
//        ArrayList<Note> notes = new ArrayList<>();
//        for (int i = 0; i < realNotes.size(); i++) {
//            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
//            notes.add(new Note(i + 1, realNotes.get(i), date ));
//        }
//
//        for (Note note : notes) {
//            String insertQuery = "INSERT INTO notes (body, date) VALUES ('" +
//                    note.getNote_body() + "', '" + note.getDate() + "');";
//            db.execSQL(insertQuery);
//        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
        onCreate(db);
    }

    // Метод для открытия базы данных по пути
    public SQLiteDatabase openDatabase() {
        return SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }
}

