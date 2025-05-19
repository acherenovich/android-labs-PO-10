package com.example.lab4;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FileViewModel extends ViewModel {
    private MutableLiveData<Boolean> fileExists = new MutableLiveData<>(false);
    private MutableLiveData<Integer> progress = new MutableLiveData<>(0);
    private MutableLiveData<Boolean> isDownloading = new MutableLiveData<>(false);

    public LiveData<Boolean> getFileExists() {
        return fileExists;
    }

    public void setFileExists(Boolean b) {
        fileExists.postValue(b);
    }

    public LiveData<Integer> getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress.postValue(progress);
    }

    public LiveData<Boolean> getIsDownloading() {
        return isDownloading;
    }

    public void setIsDownloading(Boolean b) {
        isDownloading.postValue(b);
    }
}
