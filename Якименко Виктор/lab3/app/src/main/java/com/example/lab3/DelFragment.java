package com.example.lab3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class DelFragment extends Fragment {

    private DatabaseAdapter databaseAdapter;
    EditText delNoteId;

//    public static DelFragment newInstance(DatabaseAdapter adapter) {
//        DelFragment fragment = new DelFragment();
//        fragment.databaseAdapter = adapter;
//        return fragment;
//    }

   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result=inflater.inflate(R.layout.fragment_del, container, false);

        return result;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        delNoteId = view.findViewById(R.id.delEdit);

        NotesViewModel notesViewModel = new ViewModelProvider(requireActivity(), new NotesViewModelFactory(requireContext())).get(NotesViewModel.class);
        databaseAdapter = notesViewModel.getDatabaseAdapter();

        Button delButton = view.findViewById(R.id.delButton);

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
    }

    public void delete(){
        String input = delNoteId.getText().toString();

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
        long rowsDeleted = databaseAdapter.delete(noteId);
        databaseAdapter.close();

        if (rowsDeleted > 0) {
            Toast.makeText(getContext(), "Заметка удалена!", Toast.LENGTH_SHORT).show();
            delNoteId.setText(null);
            ((MainActivity) requireActivity()).switchToPage(PageAdapter.SHOW_PAGE);
        } else {
            Toast.makeText(getContext(), "Заметка не найдена!", Toast.LENGTH_SHORT).show();
        }

    }
}
