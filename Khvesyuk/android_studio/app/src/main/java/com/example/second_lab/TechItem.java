package com.example.second_lab;

import java.io.Serializable;

public class TechItem implements Serializable {
    private int id;
    private String name;
    private double price;
    private boolean isChecked;

    public TechItem(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.isChecked = false;
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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
