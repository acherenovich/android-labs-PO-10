package com.example.lab3.fragments;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab3.MainActivity;
import com.example.lab3.Note;
import com.example.lab3.NotesConverter;
import com.example.lab3.R;
import com.example.lab3.adapters.NotesAdapter;
import com.example.lab3.database.DatabaseHelper;
import com.example.lab3.database.SharedViewModel;


import java.util.ArrayList;
import java.util.List;

public class FragmentShow extends Fragment {
    private SharedViewModel viewModel;
    private final List<Note> notesList = new ArrayList<>();
    private DatabaseHelper sqlHelper;
    private NotesAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sqlHelper = new DatabaseHelper(requireContext(), MainActivity.DB_FILE_PATH);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_content, container, false);


        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);


        RecyclerView notesListView = view.findViewById(R.id.notes_list_view);
        notesListView.setLayoutManager(new LinearLayoutManager(requireContext()));


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        notesListView.addItemDecoration(dividerItemDecoration);


        adapter = new NotesAdapter(requireContext(), notesList);
        notesListView.setAdapter(adapter);


        viewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
            if (event) {
                updateNotesList();
            }
        });


        updateNotesList();

        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateNotesList() {
        notesList.clear();

        try (SQLiteDatabase db = sqlHelper.openDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE, null)) {

            if (cursor.moveToFirst()) {
                NotesConverter converter = new NotesConverter(cursor);
                notesList.addAll(converter.generate_notes_list());
            }

            adapter.notifyDataSetChanged();

        } catch (Exception e) {
            Log.e("FragmentShow", "Ошибка при загрузке заметок", e);
            Toast.makeText(requireContext(), "Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (sqlHelper != null) sqlHelper.close();
    }
}
