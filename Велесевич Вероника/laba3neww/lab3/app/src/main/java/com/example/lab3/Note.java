package com.example.lab3;

public class Note {
    private int id; // Номер заметки (уникальный идентификатор)
    private String description; // Описание заметки

    // Конструктор
    public Note(int id, String description) {
        this.id = id;
        this.description = description;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return id + ". " + description;
    }
}
