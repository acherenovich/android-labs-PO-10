package com.example.lab3;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class NotesConverter {
    private List<Note> notes_list =  new ArrayList<>();;
    private Cursor notesCursor;
    public NotesConverter(Cursor cursor){
        this.notesCursor = cursor;

    }

    public List<Note> generate_notes_list(){
        int count = notesCursor.getCount();
        for (int i = 0; i < count; i++){
            int id  = notesCursor.getInt(0);
            String body = notesCursor.getString(1);
            String date = notesCursor.getString(2);
            notes_list.add(new Note(id,body,date));
            notesCursor.moveToNext();
        }
        return notes_list;
    }
}
