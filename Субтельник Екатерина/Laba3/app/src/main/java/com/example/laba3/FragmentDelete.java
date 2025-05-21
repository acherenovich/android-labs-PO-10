package com.example.laba3;

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

public class FragmentDelete extends Fragment {
    private EditText editTextId;
    private Button btnDelete;
    private SQLiteHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete, container, false);
        editTextId = view.findViewById(R.id.editTextId);
        btnDelete = view.findViewById(R.id.btnDelete);
        dbHelper = new SQLiteHelper(getContext());

        btnDelete.setOnClickListener(v -> {
            String idStr = editTextId.getText().toString().trim();
            if (!idStr.isEmpty()) {
                int id = Integer.parseInt(idStr);
                dbHelper.deleteNote(id);
                Toast.makeText(getContext(), "Note deleted", Toast.LENGTH_SHORT).show();
                editTextId.setText("");
            } else {
                Toast.makeText(getContext(), "Enter correct number", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
