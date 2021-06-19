package com.example.testapp;

public class stores {
    private String name;
    private String picurl;
    private String rating;
    private String adress;

    public stores(String name,String url,String rating,String adress) {
        this.name = name;
        this.picurl=url;
        this.rating=rating;
        this.adress=adress;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "stores{" +
                "name='" + name + '\'' +
                ", picurl='" + picurl + '\'' +
                ", rating='" + rating + '\'' +
                ", adress='" + adress + '\'' +
                '}';
    }
}
