package com.example.lab3.fragments;

import static com.example.lab3.database.HttpDatabaseClient.uploadDatabase;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.lab3.MainActivity;
import com.example.lab3.R;
import com.example.lab3.database.SharedViewModel;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;
import com.example.lab3.database.DatabaseHelper;

public class FragmentUpdate extends Fragment {
    private SharedViewModel viewModel;
    private DatabaseHelper sqlHelper;
    private EditText noteIdInput, noteBodyInput;

    private static final String TAG = "FragmentUpdate";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sqlHelper = new DatabaseHelper(requireContext(), MainActivity.DB_FILE_PATH);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_content, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        noteIdInput = view.findViewById(R.id.id_text_edit_update);
        noteBodyInput = view.findViewById(R.id.note_body_update);
        Button updateButton = view.findViewById(R.id.button_update);

        updateButton.setOnClickListener(v -> updateNote());

        return view;
    }

    private void updateNote() {
        String idText = noteIdInput.getText().toString().trim();
        String newText = noteBodyInput.getText().toString().trim();

        if (idText.isEmpty() || newText.isEmpty()) {
            Toast.makeText(requireContext(), "Заполните все поля!", Toast.LENGTH_SHORT).show();
            return;
        }

        try (SQLiteDatabase db = sqlHelper.openDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put(DatabaseHelper.COLUMN_BODY, newText);

            int rowsAffected = db.update(DatabaseHelper.TABLE, cv, "_id = ?", new String[]{idText});

            if (rowsAffected > 0) {
                Toast.makeText(requireContext(), "Заметка обновлена!", Toast.LENGTH_SHORT).show();
                viewModel.triggerEvent(); // Уведомляем об изменениях
                noteIdInput.setText("");
                noteBodyInput.setText("");
                uploadDatabase(MainActivity.DB_FILE_PATH);
            } else {
                Toast.makeText(requireContext(), "Заметка не найдена!", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.e(TAG, "Ошибка обновления заметки", e);
            Toast.makeText(requireContext(), "Ошибка обновления!", Toast.LENGTH_SHORT).show();
        }
    }
}

