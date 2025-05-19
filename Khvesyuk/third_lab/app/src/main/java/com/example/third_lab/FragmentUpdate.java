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

public class FragmentUpdate extends Fragment {
    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update, container, false);

        databaseHelper = new DatabaseHelper(getContext());
        EditText editTextId = view.findViewById(R.id.editTextUpdateId);
        EditText editTextDescription = view.findViewById(R.id.editTextUpdateDescription);
        Button buttonUpdate = view.findViewById(R.id.buttonUpdate);
        TextView textViewSuccess = view.findViewById(R.id.textViewSuccess);

        buttonUpdate.setOnClickListener(v -> {
            String idText = editTextId.getText().toString().trim();
            String description = editTextDescription.getText().toString().trim();

            if (!idText.isEmpty() && !description.isEmpty()) {
                int id = Integer.parseInt(idText);
                databaseHelper.updateNote(id, description);
                editTextId.setText("");
                editTextDescription.setText("");
                textViewSuccess.setText("Успешно");

                new Handler(Looper.getMainLooper()).postDelayed(() -> textViewSuccess.setText(""), 2000);
            }
        });

        return view;
    }
}
