package com.example.lab33.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lab33.R;
import com.example.lab33.adapters.NotesAdapter;
import com.example.lab33.database.NotesDatabaseHelper;

public class FragmentShow extends Fragment {

    private ListView listView;
    private TextView emptyView;
    private NotesDatabaseHelper dbHelper;
    private NotesAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show, container, false);

        listView = view.findViewById(R.id.listView);
        emptyView = view.findViewById(R.id.emptyView);
        dbHelper = new NotesDatabaseHelper(getContext());

        loadNotes();

        return view;
    }

    private void loadNotes() {
        Cursor cursor = dbHelper.getAllNotes();
        if (cursor.getCount() < 20) {
            emptyView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            adapter = new NotesAdapter(getContext(), cursor);
            listView.setAdapter(adapter);
        }
    }
}
