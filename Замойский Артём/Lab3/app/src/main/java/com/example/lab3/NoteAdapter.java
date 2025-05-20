package com.example.lab3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class NoteAdapter extends BaseAdapter {
    private Context context;
    private List<Note> notes;
    private LayoutInflater inflater;

    public NoteAdapter(Context context, List<Note> notes) {
        this.context = context;
        this.notes = notes;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Object getItem(int position) {
        return notes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return notes.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.note_item, parent, false);
            holder = new ViewHolder();
            holder.txtId = convertView.findViewById(R.id.txtId);
            holder.txtDescription = convertView.findViewById(R.id.txtDescription);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Заполнение данных
        Note note = notes.get(position);
        holder.txtId.setText(String.valueOf(note.getId()));
        holder.txtDescription.setText(note.getDescription());

        return convertView;
    }

    private static class ViewHolder {
        TextView txtId;
        TextView txtDescription;
    }
}
