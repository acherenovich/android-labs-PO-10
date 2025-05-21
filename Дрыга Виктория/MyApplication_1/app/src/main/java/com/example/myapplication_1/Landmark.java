package com.example.myapplication_1;

public class Landmark {
    private String name;
    private String description;
    private String location;
    private String imageUrl;

    public Landmark(String name, String description, String location, String imageUrl) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Название: " + name + "\n"
                + "Описание: " + description + "\n"
                + "Местоположение: " + location + "\n"
                + "Изображение: " + imageUrl;
    }
}
