package com.example.database;

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

public class FragmentUpdate extends Fragment {
    private EditText editTextId, editTextNewText;
    private Button btnUpdate;
    private NotesDatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update, container, false);
        editTextId = view.findViewById(R.id.editNoteId);
        editTextNewText = view.findViewById(R.id.editNoteDescription);
        btnUpdate = view.findViewById(R.id.btn_update);
        databaseHelper = new NotesDatabaseHelper(getContext());

        btnUpdate.setOnClickListener(v -> {
            String idStr = editTextId.getText().toString().trim();
            String newText = editTextNewText.getText().toString().trim();

            if (!idStr.isEmpty() && !newText.isEmpty()) {
                int id = Integer.parseInt(idStr);
                databaseHelper.updateNote(id, newText);
                Toast.makeText(getContext(), "Заметка обновлена", Toast.LENGTH_SHORT).show();
                editTextId.setText("");
                editTextNewText.setText("");
            } else {
                Toast.makeText(getContext(), "Введите номер и новый текст", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
