package com.example.lab3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

public class FragmentUpdate extends Fragment {
    private EditText editId, editDescription;
    private Button btnUpdate;
    private NotesDAO notesDAO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update, container, false);
        editId = view.findViewById(R.id.editId);
        editDescription = view.findViewById(R.id.editDescription);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        notesDAO = new NotesDAO(getContext());

        btnUpdate.setOnClickListener(v -> {
            String idStr = editId.getText().toString().trim();
            String description = editDescription.getText().toString().trim();
            if (!idStr.isEmpty() && !description.isEmpty()) {
                int id = Integer.parseInt(idStr);
                int rowsUpdated = notesDAO.updateNote(id, description);
                if (rowsUpdated > 0) {
                    Toast.makeText(getContext(), "Заметка обновлена", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Заметка не найдена", Toast.LENGTH_SHORT).show();
                }
                editId.setText("");
                editDescription.setText("");
            }
        });

        return view;
    }
}
