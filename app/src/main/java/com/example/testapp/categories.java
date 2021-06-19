package com.example.testapp;

public class categories {
    public String name;
    public String url;

    public categories() {
    }

    public categories(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "categories{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
