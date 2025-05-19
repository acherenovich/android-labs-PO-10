package com.example.myapplication_1;

public class SelectedLandmarkHolder {
    private static SelectedLandmarkHolder instance;
    private Landmark selectedLandmark;

    private SelectedLandmarkHolder() {}

    public static SelectedLandmarkHolder getInstance() {
        if (instance == null) {
            instance = new SelectedLandmarkHolder();
        }
        return instance;
    }

    public Landmark getSelectedLandmark() {
        return selectedLandmark;
    }

    public void setSelectedLandmark(Landmark landmark) {
        this.selectedLandmark = landmark;
    }
}
