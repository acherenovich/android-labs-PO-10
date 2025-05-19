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

public class FragmentUpdate extends Fragment {

    private EditText editTextNoteNumberUpdate;
    private EditText editTextNoteDescriptionUpdate;
    private EditText editTextNotePriceUpdate;
    private Button buttonUpdateNote;
    private DatabaseHelper databaseHelper;

    public FragmentUpdate() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update, container, false);

        editTextNoteNumberUpdate = view.findViewById(R.id.edit_text_note_number_update);
        editTextNoteDescriptionUpdate = view.findViewById(R.id.edit_text_note_description_update);
        editTextNotePriceUpdate = view.findViewById(R.id.edit_text_note_price_update);
        buttonUpdateNote = view.findViewById(R.id.button_update_note);

        databaseHelper = new DatabaseHelper(getContext());

        buttonUpdateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noteNumberStr = editTextNoteNumberUpdate.getText().toString().trim();
                String noteDescription = editTextNoteDescriptionUpdate.getText().toString().trim();
                String notePriceStr = editTextNotePriceUpdate.getText().toString().trim();
                double notePrice = 0.0;

                if (!noteNumberStr.isEmpty() && !noteDescription.isEmpty()) { // Description is required for update
                    try {
                        int noteId = Integer.parseInt(noteNumberStr);
                        if (!notePriceStr.isEmpty()) {
                            notePrice = Double.parseDouble(notePriceStr);
                        }

                        databaseHelper.openDatabase();
                        Note noteToUpdate = new Note(noteId, noteDescription, notePrice); // Create Note object with updated data
                        boolean isUpdated = databaseHelper.updateNote(noteToUpdate); // Pass Note object to updateNote()

                        if (isUpdated) {
                            Toast.makeText(getContext(), "Заметка обновлена", Toast.LENGTH_SHORT).show();
                            editTextNoteNumberUpdate.getText().clear();
                            editTextNoteDescriptionUpdate.getText().clear();
                            editTextNotePriceUpdate.getText().clear();
                        } else {
                            Toast.makeText(getContext(), "Заметка с таким номером не найдена", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Неверный формат номера или цены", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Ошибка базы данных", Toast.LENGTH_SHORT).show();
                    } finally {
                        databaseHelper.close();
                    }
                } else {
                    Toast.makeText(getContext(), "Введите номер и новое описание заметки", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}