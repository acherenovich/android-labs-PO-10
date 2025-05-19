package com.example.lab3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class UpdateFragment extends Fragment {

    private DatabaseAdapter databaseAdapter;
    private EditText updateEditId, updateEditDescription;
    private Note updatedNote;

//    public static UpdateFragment newInstance(DatabaseAdapter adapter) {
//        UpdateFragment fragment = new UpdateFragment();
//        fragment.databaseAdapter = adapter;
//        return fragment;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result=inflater.inflate(R.layout.fragment_update, container, false);
        return result;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        updateEditId = view.findViewById(R.id.updateIdEdit);
        updateEditDescription = view.findViewById(R.id.updateNoteEdit);

        NotesViewModel notesViewModel = new ViewModelProvider(requireActivity(), new NotesViewModelFactory(requireContext())).get(NotesViewModel.class);
        databaseAdapter = notesViewModel.getDatabaseAdapter();

        Button findButton = view.findViewById(R.id.findNoteButton);
        Button updateButton = view.findViewById(R.id.updateButton);

        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
    }

    public void find(){
        String input = updateEditId.getText().toString();

        // Проверяем, пустой ли ввод
        if (input.isEmpty()) {
            Toast.makeText(getContext(), "Введите номер заметки!", Toast.LENGTH_SHORT).show();
            return;
        }

        int noteId;
        try {
            noteId = Integer.parseInt(input); // Парсим ID
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Некорректный номер! Введите целое число.", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseAdapter.open();
        Note note = databaseAdapter.getNote(noteId);
        databaseAdapter.close();

        if (note == null) {
            Toast.makeText(getContext(), "Заметка не найдена!", Toast.LENGTH_SHORT).show();
            return;
        }

        updatedNote = note;
        updateEditDescription.setText(note.getDescription());
    }

    public void update(){
        if (updatedNote == null) {
            Toast.makeText(getContext(), "Сначала выберите заметку!", Toast.LENGTH_SHORT).show();
            return;
        }

        String description = updateEditDescription.getText().toString();

        databaseAdapter.open();
        updatedNote.setDescription(description);
        long rowsUpdated = databaseAdapter.update(updatedNote);
        databaseAdapter.close();

        if (rowsUpdated > 0) {
            Toast.makeText(getContext(), "Заметка обновлена!", Toast.LENGTH_SHORT).show();
            updateEditId.setText(null);
            updateEditDescription.setText(null);
            ((MainActivity) requireActivity()).switchToPage(PageAdapter.SHOW_PAGE);
        } else {
            Toast.makeText(getContext(), "Ошибка обновления", Toast.LENGTH_SHORT).show();
        }
    }
}
