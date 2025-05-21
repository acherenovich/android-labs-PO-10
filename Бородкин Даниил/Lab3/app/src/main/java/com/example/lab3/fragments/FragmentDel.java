package com.example.lab3.fragments;

import static com.example.lab3.database.HttpDatabaseClient.uploadDatabase;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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


public class FragmentDel extends Fragment {
    private DatabaseHelper sqlHelper;

    private SQLiteDatabase db;
    private SharedViewModel viewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sqlHelper = new DatabaseHelper(requireContext(), MainActivity.DB_FILE_PATH);
        db = sqlHelper.openDatabase();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_del_content, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        EditText note_id = view.findViewById(R.id.id_text_edit_del); //

        Button button = view.findViewById(R.id.button_del);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String text = note_id.getText().toString(); //
                Log.d("MyApp", text);
                delete_note(text);
                note_id.setText("");
            }
        });

        return view;
    }
    private void delete_note(String id) {
        if (id.isEmpty() || !id.matches("\\d+")) {
            Toast.makeText(getContext(), "Некорректный ID!", Toast.LENGTH_SHORT).show();
            return;
        }
        try (SQLiteDatabase db = sqlHelper.openDatabase()) {
            int deletedRows = db.delete(DatabaseHelper.TABLE, "_id = ?", new String[]{id});
            if (deletedRows > 0) {
                Toast.makeText(getContext(), "Заметка удалена!", Toast.LENGTH_SHORT).show();
                uploadDatabase(MainActivity.DB_FILE_PATH);
            } else {
                Toast.makeText(getContext(), "Заметка не найдена!", Toast.LENGTH_SHORT).show();
            }
        }
        viewModel.triggerEvent();
    }

}

