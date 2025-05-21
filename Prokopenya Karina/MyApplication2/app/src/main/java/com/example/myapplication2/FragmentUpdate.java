package com.example.myapplication2;

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
    private Button buttonUpdate;
    private DatabaseHelper dbHelper;

    public FragmentUpdate() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update, container, false);

        editTextId = view.findViewById(R.id.editTextId);
        editTextNewText = view.findViewById(R.id.editTextNewText);
        buttonUpdate = view.findViewById(R.id.buttonUpdate);
        dbHelper = new DatabaseHelper(getContext());

        buttonUpdate.setOnClickListener(v -> {
            String idText = editTextId.getText().toString().trim();
            String newText = editTextNewText.getText().toString().trim();

            if (!idText.isEmpty() && !newText.isEmpty()) {
                int id = Integer.parseInt(idText);
                dbHelper.updateNote(id, newText);
                Toast.makeText(getContext(), "Заметка обновлена", Toast.LENGTH_SHORT).show();
                editTextId.setText("");
                editTextNewText.setText("");
            } // Обновляем список заметок в FragmentShow
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).updateNotesList();
            }
            else {
                Toast.makeText(getContext(), "Заполните оба поля", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
