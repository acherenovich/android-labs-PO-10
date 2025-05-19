package com.example.lab3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab3.Note;
import com.example.lab3.R;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private LayoutInflater inflater;

    private Context context;
    private List<Note> notes_list;

    public NotesAdapter(Context context, List<Note> notes_list){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.notes_list = notes_list;
    }


    @NonNull
    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.note_layout,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.ViewHolder holder, int position) {
        Note note = notes_list.get(position);
        holder.id_view.setText(String.valueOf(note.getId()));
        holder.body_view.setText(note.getNote_body());
        holder.date_view.setText(note.getDate());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return notes_list.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        final TextView id_view, body_view, date_view;

        public ViewHolder( View itemView) {
            super(itemView);
            id_view = itemView.findViewById(R.id.note_id);
            body_view = itemView.findViewById(R.id.note_body);
            date_view = itemView.findViewById(R.id.note_date);
        }
    }

}
