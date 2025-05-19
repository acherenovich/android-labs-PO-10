package com.example.lab3rpomp;

public class Note {
    private int id; // Добавляем поле id
    private String description;
    private double price; // Добавляем поле price

    public Note(int id, String description, double price) { // Изменяем конструктор
        this.id = id;
        this.description = description;
        this.price = price;
    }

    public int getId() { // Геттер для id
        return id;
    }

    public void setId(int id) { // Сеттер для id
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() { // Геттер для price
        return price;
    }

    public void setPrice(double price) { // Сеттер для price
        this.price = price;
    }
}
