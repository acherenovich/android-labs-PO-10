package com.example.laba3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class NoteAdapter extends ArrayAdapter<Note> {
    public NoteAdapter(Context context, List<Note> notes) {
        super(context, 0, notes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_note, parent, false);
        }

        Note note = getItem(position);
        TextView textId = convertView.findViewById(R.id.textId);
        TextView textDesc = convertView.findViewById(R.id.textDesc);

        textId.setText(String.valueOf(note.getId()));
        textDesc.setText(note.getDescription());

        return convertView;
    }
}
