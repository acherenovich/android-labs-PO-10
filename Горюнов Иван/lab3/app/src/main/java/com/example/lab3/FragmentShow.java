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

    private DatabaseHelper databaseHelper;
    private NoteAdapter adapter;
    private ArrayList<Note> notesList;

    public FragmentShow() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show, container, false);
        ListView listView = view.findViewById(R.id.listView);

        databaseHelper = new DatabaseHelper(getContext());
        notesList = new ArrayList<>();
        loadNotes();

        adapter = new NoteAdapter(getContext(), notesList);
        listView.setAdapter(adapter);
        return view;
    }

    private void loadNotes() {
        Cursor cursor = databaseHelper.getAllNotes();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String note = cursor.getString(1);
            notesList.add(new Note(id, note));
        }
        cursor.close();
    }
}
