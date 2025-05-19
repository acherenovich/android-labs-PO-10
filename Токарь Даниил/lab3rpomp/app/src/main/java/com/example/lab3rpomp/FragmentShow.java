package com.example.lab3rpomp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.List;

public class FragmentShow extends Fragment {

    private ListView notesListView;
    private NoteAdapter noteAdapter;
    private List<Note> notesList;
    private DatabaseHelper databaseHelper; // Добавляем DatabaseHelper

    public FragmentShow() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show, container, false);

        notesListView = view.findViewById(R.id.notes_list_view);

        databaseHelper = new DatabaseHelper(getContext()); // Инициализируем DatabaseHelper

        try {
            databaseHelper.openDatabase(); // Открываем БД
            notesList = databaseHelper.getAllNotes(); // Получаем список заметок из БД
        } catch (IOException e) {
            e.printStackTrace(); // Обработка ошибки открытия БД
            // Можно показать сообщение пользователю об ошибке
        } finally {
            databaseHelper.close(); // Закрываем БД в finally, чтобы гарантировать закрытие
        }


        noteAdapter = new NoteAdapter(getContext(), notesList);
        notesListView.setAdapter(noteAdapter);

        return view;
    }

}