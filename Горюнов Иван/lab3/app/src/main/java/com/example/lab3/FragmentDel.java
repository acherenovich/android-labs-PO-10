package com.example.lab3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

public class FragmentDel extends Fragment {

    private DatabaseHelper databaseHelper;

    public FragmentDel() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_del, container, false);
        EditText editText = view.findViewById(R.id.editTextDelete);
        Button button = view.findViewById(R.id.buttonDel);

        databaseHelper = new DatabaseHelper(getContext());

        button.setOnClickListener(v -> {
            int id = Integer.parseInt(editText.getText().toString());
            if (databaseHelper.deleteNote(id)) {
                Toast.makeText(getContext(), "Заметка удалена!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Ошибка удаления", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
