package com.example.lab3.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import androidx.fragment.app.Fragment;

import com.example.lab3.DatabaseHelper;
import com.example.lab3.R;


public class FragmentUpdate extends Fragment {
    private EditText idEditText, descriptionEditText;
    private Button updateButton;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update, container, false);
        idEditText = view.findViewById(R.id.idEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        updateButton = view.findViewById(R.id.updateButton);
        dbHelper = new DatabaseHelper(getContext());

        updateButton.setOnClickListener(v -> {
            int id = Integer.parseInt(idEditText.getText().toString());
            String newDescription = descriptionEditText.getText().toString();
            dbHelper.updateNote(id, newDescription);
        });

        return view;
    }
}
