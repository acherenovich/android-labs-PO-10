package com.example.lab8;

public class Track {
    private long id;
    private String name;

    public Track(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

