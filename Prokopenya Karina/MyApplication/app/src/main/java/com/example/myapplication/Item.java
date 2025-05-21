package com.example.myapplication;

public class Item {
    private int id;
    private String title;
    private String description;
    private String imageUrl;
    private String fear;  // Новое поле для страха

    // Конструктор
    public Item(int id, String title, String description, String imageUrl, String fear) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.fear = fear;  // Инициализация нового поля
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFear() {
        return fear;  // Геттер для нового поля
    }

    public void setFear(String fear) {
        this.fear = fear;  // Сеттер для нового поля
    }
}
