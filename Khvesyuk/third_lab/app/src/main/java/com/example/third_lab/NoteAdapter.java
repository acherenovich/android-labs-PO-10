package com.example.third_lab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ArrayAdapter;
import java.util.List;

public class NoteAdapter extends ArrayAdapter<Note> {

    public NoteAdapter(Context context, List<Note> notes) {
        super(context, 0, notes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_note, parent, false);
        }

        Note note = getItem(position);
        TextView textViewId = convertView.findViewById(R.id.textViewNoteId);
        TextView textViewDescription = convertView.findViewById(R.id.textViewNoteDescription);

        if (note != null) {
            textViewId.setText("ID: " + note.getId());
            textViewDescription.setText(note.getDescription());
        }

        return convertView;
    }
}
