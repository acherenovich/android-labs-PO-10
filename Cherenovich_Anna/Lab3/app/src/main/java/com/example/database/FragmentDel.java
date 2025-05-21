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

public class FragmentDel extends Fragment {
    private EditText editText;
    private Button delButton;
    private NotesDatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_del, container, false);

        editText = view.findViewById(R.id.editText);
        delButton = view.findViewById(R.id.delButton);
        databaseHelper = new NotesDatabaseHelper(getActivity());

        delButton.setOnClickListener(v -> {
            String idText = editText.getText().toString().trim();
            if (!idText.isEmpty()) {
                int id = Integer.parseInt(idText);
                databaseHelper.deleteNote(id);
                Toast.makeText(getActivity(), "Note deleted", Toast.LENGTH_SHORT).show();
                editText.setText("");
            } else {
                Toast.makeText(getActivity(), "Enter ID", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}

