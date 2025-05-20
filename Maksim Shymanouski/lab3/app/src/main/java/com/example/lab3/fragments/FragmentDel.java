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


public class FragmentDel extends Fragment {
    private EditText editText;
    private Button delButton;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_del, container, false);
        editText = view.findViewById(R.id.editText);
        delButton = view.findViewById(R.id.delButton);
        dbHelper = new DatabaseHelper(getContext());

        delButton.setOnClickListener(v -> {
            int id = Integer.parseInt(editText.getText().toString());
            dbHelper.deleteNote(id);
        });

        return view;
    }
}
