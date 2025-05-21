package com.example.lab33.fragments;

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

import com.example.lab33.R;
import com.example.lab33.database.NotesDatabaseHelper;

public class FragmentUpdate extends Fragment {

    private EditText editTextId, editTextNewDescription;
    private NotesDatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update, container, false);

        editTextId = view.findViewById(R.id.editTextId);
        editTextNewDescription = view.findViewById(R.id.editTextNewDescription);
        Button btnUpdate = view.findViewById(R.id.btnUpdate);
        dbHelper = new NotesDatabaseHelper(getContext());

        btnUpdate.setOnClickListener(v -> {
            String id = editTextId.getText().toString().trim();
            String newDesc = editTextNewDescription.getText().toString().trim();
            if (!id.isEmpty() && !newDesc.isEmpty()) {
                dbHelper.updateNote(Integer.parseInt(id), newDesc);
                Toast.makeText(getContext(), "Note updated", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
