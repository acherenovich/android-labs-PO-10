package com.example.lab33.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lab33.R;
import com.example.lab33.database.NotesDatabaseHelper;

public class FragmentDel extends Fragment {

    private EditText editTextId;
    private NotesDatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_del, container, false);

        editTextId = view.findViewById(R.id.editTextId);
        Button btnDelete = view.findViewById(R.id.btnDelete);
        dbHelper = new NotesDatabaseHelper(getContext());

        btnDelete.setOnClickListener(v -> {
            String id = editTextId.getText().toString().trim();
            if (!id.isEmpty()) {
                dbHelper.deleteNote(Integer.parseInt(id));
                Toast.makeText(getContext(), "Note deleted", Toast.LENGTH_SHORT).show();
                editTextId.setText("");
            } else {
                Toast.makeText(getContext(), "Enter note ID", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
