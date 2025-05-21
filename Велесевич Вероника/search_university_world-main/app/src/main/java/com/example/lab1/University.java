package com.example.lab1;

public class University {
    private String name;
    private String country;
    private String webPage;
    private String domain;

    public University(String name, String country, String webPage, String domain) {
        this.name = name;
        this.country = country;
        this.webPage = webPage;
        this.domain = domain;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getWebPage() {
        return webPage;
    }

    public String getDomain() {
        return domain;
    }
}