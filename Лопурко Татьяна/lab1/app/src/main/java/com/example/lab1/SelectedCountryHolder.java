package com.example.lab1;

public class SelectedCountryHolder {
    private static final SelectedCountryHolder instance = new SelectedCountryHolder();
    private Country selectedCountry;

    private SelectedCountryHolder() {}

    public static SelectedCountryHolder getInstance() {
        return instance;
    }

    public Country getSelectedCountry() {
        return selectedCountry;
    }

    public void setSelectedCountry(Country country) {
        this.selectedCountry = country;
    }
}
