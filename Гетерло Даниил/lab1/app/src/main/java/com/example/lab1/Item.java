package com.example.lab1;


import java.io.Serializable;

public class Item implements Serializable {
    private static final long serialVersionUID = 1L; // рекомендуется для Serializable

    private int id;
    private String title;

    private String description;

    private String flagUrl;




    public Item(int id, String title, String description,String flagUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.flagUrl = flagUrl;
    }

    public int getId() { return id; }
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
    public String getFlagUrl() {
        return flagUrl;
    }
}


