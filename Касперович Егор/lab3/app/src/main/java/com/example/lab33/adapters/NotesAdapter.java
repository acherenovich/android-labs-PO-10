package com.example.lab33.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.CursorAdapter;

import com.example.lab33.R;

public class NotesAdapter extends CursorAdapter {

    public NotesAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView txtNoteId = view.findViewById(R.id.txtNoteId);
        TextView txtNoteDesc = view.findViewById(R.id.txtNoteDesc);

        int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));

        txtNoteId.setText(String.valueOf(id));
        txtNoteDesc.setText(description);
    }
}
