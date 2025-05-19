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

public class FragmentAdd extends Fragment {
    private EditText editText;
    private Button addButton;
    private NotesDatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        editText = view.findViewById(R.id.editText);
        addButton = view.findViewById(R.id.addButton);
        databaseHelper = new NotesDatabaseHelper(getActivity());

        addButton.setOnClickListener(v -> {
            String text = editText.getText().toString();
            if (!text.isEmpty()) {
                databaseHelper.addNote(text);
                Toast.makeText(getActivity(), "Note added", Toast.LENGTH_SHORT).show();
                editText.setText("");
            } else {
                Toast.makeText(getActivity(), "Enter text", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
