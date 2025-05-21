package com.example.lab2rpomp;

import java.io.Serializable;

public class Product implements Serializable {

    private int id;
    private String name;
    private double price;
    private int quantity;
    private int imageResourceId; // Добавлено поле для ID ресурса изображения

    public Product(int id, String name, double price, int imageResourceId) { // Обновлен конструктор
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = 0;
        this.imageResourceId = imageResourceId; // Инициализация imageResourceId
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getImageResourceId() { // Геттер для imageResourceId
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) { // Сеттер для imageResourceId (если понадобится менять изображение)
        this.imageResourceId = imageResourceId;
    }
}