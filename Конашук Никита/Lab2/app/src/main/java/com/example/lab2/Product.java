package com.example.lab2;

import java.io.Serializable;

public class Product implements Serializable {
    private int id;
    private String name;
    private int price;
    private boolean checked;

    public Product(int id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.checked = false;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getPrice() { return price; }
    public boolean isChecked() { return checked; }
    public void setChecked(boolean checked) { this.checked = checked; }
}
