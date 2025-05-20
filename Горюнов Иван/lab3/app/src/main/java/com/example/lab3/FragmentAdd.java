package com.example.lab3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

public class FragmentAdd extends Fragment {

    private DatabaseHelper databaseHelper;

    public FragmentAdd() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        EditText editText = view.findViewById(R.id.editText);
        Button button = view.findViewById(R.id.buttonAdd);

        databaseHelper = new DatabaseHelper(getContext());

        button.setOnClickListener(v -> {
            String note = editText.getText().toString();
            if (!note.isEmpty()) {
                databaseHelper.addNote(note);
                Toast.makeText(getContext(), "Заметка добавлена!", Toast.LENGTH_SHORT).show();
                editText.setText("");
            } else {
                Toast.makeText(getContext(), "Введите текст заметки", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
