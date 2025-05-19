package com.example.database;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.List;

public class FragmentShow extends Fragment {
    private ListView listView;
    private NotesAdapter adapter;
    private NotesDatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show, container, false);
        listView = view.findViewById(R.id.listView);
        databaseHelper = new NotesDatabaseHelper(getContext());

        loadNotes();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        loadNotes();
    }
    private void loadNotes() {
        List<Note> notes = databaseHelper.getAllNotes();
        adapter = new NotesAdapter(getContext(), notes);
        listView.setAdapter(adapter);
    }
}
