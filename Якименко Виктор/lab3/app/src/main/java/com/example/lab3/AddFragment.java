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

public class AddFragment extends Fragment {

    private EditText noteEdit;
    private DatabaseAdapter databaseAdapter;

//    public static AddFragment newInstance(DatabaseAdapter adapter) {
//        AddFragment fragment = new AddFragment();
//        fragment.databaseAdapter = adapter;
//        return fragment;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result=inflater.inflate(R.layout.fragment_add, container, false);
        return result;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        noteEdit = view.findViewById(R.id.noteEdit);

        NotesViewModel notesViewModel = new ViewModelProvider(requireActivity(), new NotesViewModelFactory(requireContext())).get(NotesViewModel.class);
        databaseAdapter = notesViewModel.getDatabaseAdapter();

        Button saveButton = view.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }

    public void save(){
        String description = noteEdit.getText().toString();

        databaseAdapter.open();
        long rowsInserted = databaseAdapter.insert(description);
        databaseAdapter.close();

        if (rowsInserted > 0) {
            Toast.makeText(getContext(), "Заметка добавлена!", Toast.LENGTH_SHORT).show();
            noteEdit.setText(null);
            ((MainActivity) requireActivity()).switchToPage(PageAdapter.SHOW_PAGE);
        } else {
            Toast.makeText(getContext(), "Ошибка добавления", Toast.LENGTH_SHORT).show();
        }

    }
}