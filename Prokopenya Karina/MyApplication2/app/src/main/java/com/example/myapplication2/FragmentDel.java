package com.example.myapplication2;

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

public class FragmentDel extends Fragment {

    private EditText editTextNoteId;
    private Button buttonDel;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_del, container, false);

        editTextNoteId = view.findViewById(R.id.editTextNoteId);
        buttonDel = view.findViewById(R.id.buttonDel);
        dbHelper = new DatabaseHelper(requireContext());

        buttonDel.setOnClickListener(v -> deleteNoteFromDatabase());

        return view;
    }

    private void deleteNoteFromDatabase() {
        String noteIdStr = editTextNoteId.getText().toString().trim();

        if (noteIdStr.isEmpty()) {
            Toast.makeText(requireContext(), "Введите номер заметки!", Toast.LENGTH_SHORT).show();
            return;
        }

        int noteId = Integer.parseInt(noteIdStr);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deletedRows = db.delete("notes", "id=?", new String[]{String.valueOf(noteId)});
        db.close();

        if (deletedRows > 0) {
            Toast.makeText(requireContext(), "Заметка удалена!", Toast.LENGTH_SHORT).show();
            editTextNoteId.setText("");
        }
        // Обновляем список заметок в FragmentShow
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).updateNotesList();
        }
        else {
            Toast.makeText(requireContext(), "Заметка не найдена!", Toast.LENGTH_SHORT).show();
        }
    }
}
