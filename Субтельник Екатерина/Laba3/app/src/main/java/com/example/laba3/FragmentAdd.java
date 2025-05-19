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

public class FragmentAdd extends Fragment {
    private EditText editText;
    private SQLiteHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        editText = view.findViewById(R.id.editText);
        Button buttonAdd = view.findViewById(R.id.buttonAdd);
        dbHelper = new SQLiteHelper(getContext());

        buttonAdd.setOnClickListener(v -> {
            String desc = editText.getText().toString();
            if (!desc.isEmpty()) {
                dbHelper.addNote(desc);
                editText.setText("");
                Toast.makeText(getContext(), "Note added", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}

