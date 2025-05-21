package com.example.lab3.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.lab3.DatabaseHelper;
import com.example.lab3.R;
import com.example.lab3.adapters.MyNotesAdapter;

public class FragmentShow extends Fragment {
    private ListView listView;
    private MyNotesAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show, container, false);
        listView = view.findViewById(R.id.listView);
        dbHelper = new DatabaseHelper(getContext());

        // Загружаем данные из БД при создании фрагмента
        Cursor cursor = dbHelper.getAllNotes();
        adapter = new MyNotesAdapter(getContext(), cursor);
        listView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateNotesList();
    }

    public void updateNotesList() {
        Cursor cursor = dbHelper.getAllNotes();
        adapter.changeCursor(cursor); // обновляем адаптер с новыми данными
    }
}
