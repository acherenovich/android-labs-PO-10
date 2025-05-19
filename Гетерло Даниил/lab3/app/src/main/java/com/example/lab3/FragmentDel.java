package com.example.lab3;

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

public class FragmentDel extends Fragment {
    private EditText editTextId;
    private Button buttonDel;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_del, container, false);
        dbHelper = new DatabaseHelper(getContext());
        editTextId = view.findViewById(R.id.editTextId);
        buttonDel = view.findViewById(R.id.buttonDel);

        buttonDel.setOnClickListener(v -> {
            int id = Integer.parseInt(editTextId.getText().toString().trim());
            if (dbHelper.deleteNote(id)) {
                Toast.makeText(getContext(), "Note deleted", Toast.LENGTH_SHORT).show();
                editTextId.setText("");
            }
        });

        return view;
    }
}
