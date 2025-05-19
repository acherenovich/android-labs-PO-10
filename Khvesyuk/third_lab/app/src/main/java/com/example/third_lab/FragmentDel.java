package com.example.third_lab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.os.Looper;

public class FragmentDel extends Fragment {
    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_del, container, false);

        databaseHelper = new DatabaseHelper(getContext());
        EditText editText = view.findViewById(R.id.editTextDelete);
        Button button = view.findViewById(R.id.buttonDelete);
        TextView textViewSuccess = view.findViewById(R.id.textViewSuccess);

        button.setOnClickListener(v -> {
            String idText = editText.getText().toString().trim();
            if (!idText.isEmpty()) {
                int id = Integer.parseInt(idText);
                databaseHelper.deleteNote(id);
                editText.setText("");
                textViewSuccess.setText("Успешно");

                new Handler(Looper.getMainLooper()).postDelayed(() -> textViewSuccess.setText(""), 2000);
            }
        });

        return view;
    }
}
