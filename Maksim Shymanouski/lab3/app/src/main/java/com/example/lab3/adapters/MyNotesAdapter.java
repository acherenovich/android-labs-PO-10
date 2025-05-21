package com.example.lab3.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.cursoradapter.widget.CursorAdapter;
import android.view.ViewGroup;

import com.example.lab3.R;


public class MyNotesAdapter extends CursorAdapter {
    public MyNotesAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_note, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView idText = view.findViewById(R.id.idText);
        TextView descriptionText = view.findViewById(R.id.descriptionText);

        int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));

        idText.setText(String.valueOf(id));
        descriptionText.setText(description);
    }

}
