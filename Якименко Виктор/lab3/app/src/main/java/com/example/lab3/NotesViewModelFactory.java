package com.example.lab3;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class NotesViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public NotesViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NotesViewModel.class)) {
            return (T) new NotesViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

