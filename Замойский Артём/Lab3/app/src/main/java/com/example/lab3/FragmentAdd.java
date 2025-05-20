package com.example.lab3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

public class FragmentAdd extends Fragment {
    private EditText editText;
    private Button btnAdd;
    private NotesDAO notesDAO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        editText = view.findViewById(R.id.editText);
        btnAdd = view.findViewById(R.id.btnAdd);
        notesDAO = new NotesDAO(getContext());

        btnAdd.setOnClickListener(v -> {
            String description = editText.getText().toString().trim();
            if (!description.isEmpty()) {
                notesDAO.addNote(description);
                editText.setText("");
                Toast.makeText(getContext(), "Заметка добавлена", Toast.LENGTH_SHORT).show();

                updateListView(); // ОБНОВЛЕНИЕ СПИСКА ПОСЛЕ ДОБАВЛЕНИЯ
            }
        });

        return view;
    }

    private void updateListView() {
        FragmentShow fragmentShow = (FragmentShow) getParentFragmentManager()
                .findFragmentByTag("android:switcher:" + R.id.viewPager + ":0"); // 0 - индекс вкладки FragmentShow
        if (fragmentShow != null) {
            fragmentShow.loadNotes(); // ПЕРЕЗАГРУЖАЕМ ДАННЫЕ
        }
    }
}
