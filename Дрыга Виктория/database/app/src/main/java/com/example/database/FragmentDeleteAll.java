package com.example.database;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentDeleteAll extends Fragment {
    private Button delAllButton;
    private NotesDatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_all, container, false);

        delAllButton = view.findViewById(R.id.delAllButton);
        databaseHelper = new NotesDatabaseHelper(getActivity());

        delAllButton.setOnClickListener(v -> {
            databaseHelper.deleteAllNotes();
            Toast.makeText(getActivity(), "All notes deleted", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}
