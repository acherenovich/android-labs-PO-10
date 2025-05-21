package com.example.lab3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

public class FragmentUpdate extends Fragment {

    private DatabaseHelper databaseHelper;

    public FragmentUpdate() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update, container, false);
        EditText idField = view.findViewById(R.id.editTextId);
        EditText noteField = view.findViewById(R.id.editTextNewNote);
        Button button = view.findViewById(R.id.buttonUpdate);

        databaseHelper = new DatabaseHelper(getContext());

        button.setOnClickListener(v -> {
            int id = Integer.parseInt(idField.getText().toString());
            String newNote = noteField.getText().toString();
            if (databaseHelper.updateNote(id, newNote)) {
                Toast.makeText(getContext(), "Заметка обновлена!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Ошибка обновления", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
