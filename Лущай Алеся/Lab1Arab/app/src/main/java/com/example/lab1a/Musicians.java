package com.example.lab1a;

public class Musicians {
    private String name; // название
    private String frontman;  // фронтмен
    private int logoResource; // ресурс флага

    public Musicians(String name, String frontman, int logo){

        this.name=name;
        this.frontman=frontman;
        this.logoResource=logo;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFrontman() {
        return this.frontman;
    }

    public void setFrontman(String frontman) {
        this.frontman = frontman;
    }

    public int getLogoResource() {
        return this.logoResource;
    }

    public void setLogoResource(int logoResource) {
        this.logoResource = logoResource;
    }
}
