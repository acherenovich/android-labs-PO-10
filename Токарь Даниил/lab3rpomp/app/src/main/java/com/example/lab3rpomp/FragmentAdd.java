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

public class FragmentAdd extends Fragment {

    private EditText editTextNoteDescription;
    private EditText editTextNotePrice; // Добавляем EditText для цены
    private Button buttonAddNote;
    private DatabaseHelper databaseHelper;

    public FragmentAdd() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        editTextNoteDescription = view.findViewById(R.id.edit_text_note_description);
        editTextNotePrice = view.findViewById(R.id.edit_text_note_price); // Находим EditText для цены
        buttonAddNote = view.findViewById(R.id.button_add_note);

        databaseHelper = new DatabaseHelper(getContext());

        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noteDescription = editTextNoteDescription.getText().toString().trim();
                String notePriceStr = editTextNotePrice.getText().toString().trim(); // Получаем цену как строку
                double notePrice = 0.0; // Цена по умолчанию

                if (!noteDescription.isEmpty()) {
                    try {
                        if (!notePriceStr.isEmpty()) {
                            notePrice = Double.parseDouble(notePriceStr); // Преобразуем цену в double, если введено значение
                        }

                        databaseHelper.openDatabase();
                        boolean isAdded = databaseHelper.addNote(noteDescription, notePrice); // Передаем цену в addNote()
                        if (isAdded) {
                            Toast.makeText(getContext(), "Заметка добавлена", Toast.LENGTH_SHORT).show();
                            editTextNoteDescription.getText().clear();
                            editTextNotePrice.getText().clear(); // Очищаем поле ввода цены
                        } else {
                            Toast.makeText(getContext(), "Ошибка добавления заметки", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Неверный формат цены", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Ошибка базы данных", Toast.LENGTH_SHORT).show();
                    } finally {
                        databaseHelper.close();
                    }
                } else {
                    Toast.makeText(getContext(), "Введите описание заметки", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}