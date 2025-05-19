package com.example.lab3;
import android.os.Build;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Note {

    private int id;
    private String note_body;
    private String date;
    public Note(int id, String note_body, String date){
        this.id = id;
        this.note_body = note_body;
            this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getNote_body() {
        return note_body;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNote_body(String note_body) {
        this.note_body = note_body;
    }
}
