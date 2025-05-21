package com.example.lab1;

import android.content.Context;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CountryViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public CountryViewModelFactory(Context context) {
        this.context = context.getApplicationContext(); // Сохраняем application context
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CountryViewModel.class)) {
            return (T) new CountryViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}