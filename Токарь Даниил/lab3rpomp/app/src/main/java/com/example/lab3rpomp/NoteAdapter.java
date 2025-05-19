package com.example.lab3rpomp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NoteAdapter extends ArrayAdapter<Note> {

    private final Context context;
    private final List<Note> notes; // Список заметок для отображения

    public NoteAdapter(Context context, List<Note> notes) {
        super(context, R.layout.list_item_note, notes); // Используем наш макет list_item_note
        this.context = context;
        this.notes = notes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listItemView = inflater.inflate(R.layout.list_item_note, parent, false);

        TextView noteNumberTextView = listItemView.findViewById(R.id.note_number_text_view);
        TextView noteDescriptionTextView = listItemView.findViewById(R.id.note_description_text_view);
        TextView notePriceTextView = listItemView.findViewById(R.id.note_price_text_view); // Находим TextView для цены

        Note currentNote = notes.get(position);

        noteNumberTextView.setText(String.valueOf(currentNote.getId()) + ".");
        noteDescriptionTextView.setText(currentNote.getDescription());
        notePriceTextView.setText("Цена: " + String.format("%.2f", currentNote.getPrice())); // Форматируем цену и устанавливаем текст

        return listItemView;
    }

}