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

public class FragmentAdd extends Fragment {

    private EditText editTextDescription;
    private NotesDatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        editTextDescription = view.findViewById(R.id.editTextDescription);
        Button btnAdd = view.findViewById(R.id.btnAdd);
        dbHelper = new NotesDatabaseHelper(getContext());

        btnAdd.setOnClickListener(v -> {
            String description = editTextDescription.getText().toString().trim();
            if (!description.isEmpty()) {
                dbHelper.addNote(description);
                Toast.makeText(getContext(), "Note added", Toast.LENGTH_SHORT).show();
                editTextDescription.setText("");
            } else {
                Toast.makeText(getContext(), "Enter description", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
