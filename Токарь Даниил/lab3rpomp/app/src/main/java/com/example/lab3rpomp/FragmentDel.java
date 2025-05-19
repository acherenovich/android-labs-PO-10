package com.example.lab3rpomp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.IOException;

public class FragmentDel extends Fragment {

    private EditText editTextNoteNumberDelete;
    private Button buttonDeleteNote;
    private DatabaseHelper databaseHelper;

    public FragmentDel() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_del, container, false);

        editTextNoteNumberDelete = view.findViewById(R.id.edit_text_note_number_delete);
        buttonDeleteNote = view.findViewById(R.id.button_delete_note);
        databaseHelper = new DatabaseHelper(getContext());

        buttonDeleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noteNumberStr = editTextNoteNumberDelete.getText().toString().trim();

                if (!noteNumberStr.isEmpty()) {
                    try {
                        int noteId = Integer.parseInt(noteNumberStr); // Преобразуем введенный номер в int
                        databaseHelper.openDatabase();
                        boolean isDeleted = databaseHelper.deleteNote(noteId);
                        if (isDeleted) {
                            Toast.makeText(getContext(), "Заметка удалена", Toast.LENGTH_SHORT).show();
                            editTextNoteNumberDelete.getText().clear(); // Очищаем поле ввода
                        } else {
                            Toast.makeText(getContext(), "Заметка с таким номером не найдена", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Неверный формат номера", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Ошибка базы данных", Toast.LENGTH_SHORT).show();
                    } finally {
                        databaseHelper.close();
                    }
                } else {
                    Toast.makeText(getContext(), "Введите номер заметки для удаления", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}