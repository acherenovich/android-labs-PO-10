package com.example.lab1;
public class Item {
    private int id;
    private String title;
    private String description;
    private String image; // URL изображения (при необходимости
    // Конструктор
    public Item(int id, String title, String description, String image) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
    }

    // Геттеры
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getImage() { return image; }
}
