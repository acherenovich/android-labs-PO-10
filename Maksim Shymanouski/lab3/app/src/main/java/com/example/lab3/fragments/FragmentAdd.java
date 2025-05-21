package com.example.lab3.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.lab3.R;
import com.example.lab3.DatabaseHelper;

import java.util.Objects;

public class FragmentAdd extends Fragment {
    private EditText editText;
    private Button addButton;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        editText = view.findViewById(R.id.editText);
        addButton = view.findViewById(R.id.addButton);
        dbHelper = new DatabaseHelper(getContext());

        addButton.setOnClickListener(v -> {
            String description = editText.getText().toString();
            dbHelper.addNote(description);

            FragmentShow fragmentShow = (FragmentShow) getParentFragmentManager()
                    .findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + 0); // Индекс страницы 0 для FragmentShow

            if (fragmentShow != null) {
                // Обновляем список в FragmentShow
                fragmentShow.updateNotesList();
            }
        });


        return view;
    }
}
