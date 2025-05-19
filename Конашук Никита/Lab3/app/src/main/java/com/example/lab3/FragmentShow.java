package com.example.lab3;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;

public class FragmentShow extends Fragment {
    private ListView listView;
    private NotesDatabaseHelper dbHelper;
    private NoteAdapter adapter;
    private ArrayList<Note> noteList;

    public FragmentShow() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show, container, false);

        listView = view.findViewById(R.id.listView);
        dbHelper = new NotesDatabaseHelper(getContext());

        // Загружаем данные из БД
        loadNotes();

        return view;
    }

    public void loadNotes() {
        noteList = new ArrayList<>();
        Cursor cursor = dbHelper.getReadableDatabase().query(
                NotesDatabaseHelper.TABLE_NAME,
                null, null, null, null, null, null
        );

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(NotesDatabaseHelper.COLUMN_ID));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(NotesDatabaseHelper.COLUMN_DESCRIPTION));

                noteList.add(new Note(id, description));
            } while (cursor.moveToNext());
        }
        cursor.close();

        // Устанавливаем адаптер
        adapter = new NoteAdapter(getContext(), noteList);
        listView.setAdapter(adapter);
    }
}
