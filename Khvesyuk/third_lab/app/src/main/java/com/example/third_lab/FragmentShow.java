package com.example.third_lab;

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
    private DatabaseHelper databaseHelper;
    private NoteAdapter adapter;
    private ListView listViewNotes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show, container, false);
        databaseHelper = new DatabaseHelper(getContext());

        listViewNotes = view.findViewById(R.id.listViewNotes);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadNotes();
    }

    private void loadNotes() {
        List<Note> notes = databaseHelper.getAllNotes();
        adapter = new NoteAdapter(getContext(), notes);
        listViewNotes.setAdapter(adapter);
    }
}
