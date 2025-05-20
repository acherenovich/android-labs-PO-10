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
    private EditText editText;
    private Button btnDel;
    private NotesDAO notesDAO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_del, container, false);
        editText = view.findViewById(R.id.editText);
        btnDel = view.findViewById(R.id.btnDel);
        notesDAO = new NotesDAO(getContext());

        btnDel.setOnClickListener(v -> {
            String idStr = editText.getText().toString().trim();
            if (!idStr.isEmpty()) {
                int id = Integer.parseInt(idStr);
                int rowsDeleted = notesDAO.deleteNote(id);
                if (rowsDeleted > 0) {
                    Toast.makeText(getContext(), "Заметка удалена", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Заметка не найдена", Toast.LENGTH_SHORT).show();
                }
                editText.setText("");
            }
        });

        return view;
    }
}
