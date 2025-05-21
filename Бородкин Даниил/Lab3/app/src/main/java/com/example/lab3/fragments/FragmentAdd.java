package com.example.lab3.fragments;

import static com.example.lab3.database.HttpDatabaseClient.uploadDatabase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.lab3.MainActivity;
import com.example.lab3.R;
import com.example.lab3.database.DatabaseHelper;
import com.example.lab3.database.SharedViewModel;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FragmentAdd extends Fragment {
    private SharedViewModel viewModel;
    private static final String TAG = "MyApp";
    private DatabaseHelper sqlHelper;
    private EditText note_body;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sqlHelper = new DatabaseHelper(requireContext(), MainActivity.DB_FILE_PATH);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_content, container, false);


        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        note_body = view.findViewById(R.id.note_body_add);


        Button button = view.findViewById(R.id.button_add);
        button.setOnClickListener(v -> {
            String text = note_body.getText().toString().trim();
            if (text.isEmpty()) {
                Toast.makeText(requireContext(), "Введите текст заметки", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.d(TAG, "Добавление заметки: " + text);
            addNewNote(text);
            note_body.setText("");
        });

        return view;
    }

    private void addNewNote(String noteText) {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_BODY, noteText);
        cv.put(DatabaseHelper.COLUMN_DATE, date);

        try (SQLiteDatabase db = sqlHelper.openDatabase()) {
            long result = db.insert(DatabaseHelper.TABLE, null, cv);
            if (result == -1) {
                Toast.makeText(requireContext(), "Ошибка добавления заметки!", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Не удалось добавить заметку в базу данных");
            } else {
                Toast.makeText(requireContext(), "Заметка добавлена!", Toast.LENGTH_SHORT).show();
                viewModel.triggerEvent();
                Log.d(TAG, "Заметка успешно добавлена с id: " + result);
                uploadDatabase(MainActivity.DB_FILE_PATH);
            }
        } catch (Exception e) {
            Log.e(TAG, "Ошибка при добавлении заметки: ", e);
            Toast.makeText(requireContext(), "Ошибка при работе с базой данных", Toast.LENGTH_SHORT).show();
        }
    }
}
