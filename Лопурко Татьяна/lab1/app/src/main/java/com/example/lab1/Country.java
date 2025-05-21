package com.example.lab1;

import java.io.Serializable;

public class Country implements Serializable {
    private String name;
    private String capital;
    private float population;
    private String language;
    private String currency;
    private String flagUrl;

    public Country(String name, String capital, float population, String language, String currency, String flagUrl) {
        this.name = name;
        this.capital = capital;
        this.population = population;
        this.language = language;
        this.currency = currency;
        this.flagUrl = flagUrl;
    }

    public String getName() {return name;}
    public String getCapital() { return capital; }
    public float getPopulation() { return population; }
    public String getLanguage() { return language; }
    public String getCurrency() { return currency; }
    public String getFlagUrl() { return flagUrl; }
}
