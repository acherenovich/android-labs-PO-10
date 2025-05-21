package com.example.myapplication5;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class NotesActivity extends AppCompatActivity {
    private NotesDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        getSupportActionBar().setTitle("Выполнила Черенович Анна");

        dbHelper = new NotesDatabaseHelper(this);
        List<String> notes = dbHelper.getAllNotes();

        ListView listView = findViewById(R.id.notes_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_note, notes);
        listView.setAdapter(adapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu); // Загружаем меню
        return true;  // Возвращаем true для отображения меню
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_student) {
            // Переход к активности с информацией о студенте
            Intent intent = new Intent(this, StudentInfoActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.menu_lab) {
            // Переход к активности с информацией о лабораторной
            Intent intent = new Intent(this, LabInfoActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
