package com.example.lab3.database;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Boolean> event = new MutableLiveData<>();

    public void triggerEvent() {
        event.setValue(true);
    }

    public LiveData<Boolean> getEvent() {
        return event;
    }
}
