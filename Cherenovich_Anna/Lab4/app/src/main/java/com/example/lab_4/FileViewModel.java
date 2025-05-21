package com.example.lab_4;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FileViewModel extends ViewModel {
    private final MutableLiveData<Boolean> fileExists = new MutableLiveData<>(false);
    private final MutableLiveData<Integer> progress = new MutableLiveData<>(0);
    private final MutableLiveData<Boolean> isDownloading = new MutableLiveData<>(false);

    public LiveData<Boolean> getFileExists() {
        return fileExists;
    }

    public void setFileExists(boolean exists) {
        fileExists.setValue(exists);
    }

    public LiveData<Integer> getProgress() {
        return progress;
    }

    public void setProgress(int value) {
        progress.setValue(value);
    }

    public LiveData<Boolean> getIsDownloading() {
        return isDownloading;
    }

    public void setIsDownloading(boolean downloading) {
        isDownloading.setValue(downloading);
    }
}