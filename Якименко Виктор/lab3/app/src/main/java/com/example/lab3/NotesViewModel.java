package com.example.lab3;

import android.content.Context;

import androidx.lifecycle.ViewModel;

public class NotesViewModel extends ViewModel {
    private DatabaseAdapter databaseAdapter;

    public NotesViewModel(Context context) {
        databaseAdapter = new DatabaseAdapter(context);
    }

    public DatabaseAdapter getDatabaseAdapter() {
        return databaseAdapter;
    }
}

