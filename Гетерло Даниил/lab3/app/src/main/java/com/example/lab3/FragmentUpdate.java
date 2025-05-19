package com.example.lab3;

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

public class FragmentUpdate extends Fragment {
    private EditText editTextId, editTextNewNote;
    private Button buttonUpdate;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update, container, false);
        dbHelper = new DatabaseHelper(getContext());
        editTextId = view.findViewById(R.id.editTextId);
        editTextNewNote = view.findViewById(R.id.editTextNewNote);
        buttonUpdate = view.findViewById(R.id.buttonUpdate);

        buttonUpdate.setOnClickListener(v -> {
            int id = Integer.parseInt(editTextId.getText().toString().trim());
            String newText = editTextNewNote.getText().toString().trim();
            if (dbHelper.updateNote(id, newText)) {
                Toast.makeText(getContext(), "Note updated", Toast.LENGTH_SHORT).show();
                editTextId.setText("");
                editTextNewNote.setText("");
            }
        });

        return view;
    }
}
