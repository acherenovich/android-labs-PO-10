package com.example.lab3;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class NotesAdapter extends ArrayAdapter<Note> {
    private LayoutInflater inflater;
    private int layout;
    private List<Note> notes;

    public NotesAdapter(Context context,int resource, List<Note> notes) {
        super(context, resource, notes);
        this.notes = notes;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (parent instanceof ListView) {
            Log.d("Adapter", "Parent is ListView");
        }
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Note note = notes.get(position);
        viewHolder.noteNumberView.setText(String.valueOf(note.getId()));
        viewHolder.noteDescriptionView.setText(note.getDescription());

        return convertView;
    }


    private class ViewHolder {
        final TextView noteNumberView, noteDescriptionView;

        ViewHolder(View view) {
            noteNumberView = view.findViewById(R.id.noteNumber);
            noteDescriptionView = view.findViewById(R.id.noteDescription);
        }
    }
}
