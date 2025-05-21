package com.example.lab3;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

public class ShowFragment extends Fragment {
    private DatabaseAdapter databaseAdapter;

    private ListView notesList;
    ArrayAdapter<Note> arrayAdapter;



//    public static ShowFragment newInstance(DatabaseAdapter adapter) {
//        ShowFragment fragment = new ShowFragment();
//        fragment.databaseAdapter = adapter;
//        return fragment;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result=inflater.inflate(R.layout.fragment_show, container, false);

        return result;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        notesList = view.findViewById(R.id.notesList);

        // Получаем ViewModel и DatabaseAdapter
        // Используем фабрику для передачи DatabaseAdapter в ViewModel
        NotesViewModel notesViewModel = new ViewModelProvider(requireActivity(), new NotesViewModelFactory(requireContext())).get(NotesViewModel.class);
        databaseAdapter = notesViewModel.getDatabaseAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();

        databaseAdapter.open();
        List<Note> notes = databaseAdapter.getNotes();
        arrayAdapter = new NotesAdapter(requireContext(), R.layout.list_item, notes);
        notesList.setAdapter(arrayAdapter);
        databaseAdapter.close();
    }
}
