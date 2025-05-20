package com.example.myapplication2;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
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

public class FragmentAdd extends Fragment {

    private EditText editTextNote;
    private Button buttonAdd;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        editTextNote = view.findViewById(R.id.editTextNote);
        buttonAdd = view.findViewById(R.id.buttonAdd);
        dbHelper = new DatabaseHelper(requireContext());

        buttonAdd.setOnClickListener(v -> addNoteToDatabase());

        return view;
    }

    private void addNoteToDatabase() {
        String noteText = editTextNote.getText().toString().trim();

        if (noteText.isEmpty()) {
            Toast.makeText(requireContext(), "Введите текст заметки!", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("description", noteText);
        long newRowId = db.insert("notes", null, values);
        db.close();

        if (newRowId != -1) {
            Toast.makeText(requireContext(), "Заметка добавлена!", Toast.LENGTH_SHORT).show();
            editTextNote.setText("");

            // Обновляем список заметок в FragmentShow
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).updateNotesList();
            }

        } else {
            Toast.makeText(requireContext(), "Ошибка добавления!", Toast.LENGTH_SHORT).show();
        }
    }
}
