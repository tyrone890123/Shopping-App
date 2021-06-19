package com.example.testapp;

public class ShipItem {
    public String name;
    public String category;
    public String url;
    public String rating;
    public String price;
    public String stock;
    public String brand;
    public String description;
    public String sellerUUID;
    public int quantitySold;
    public String quantity;
    public String status;

    public ShipItem() {
    }

    public ShipItem(String name, String category, String url, String rating, String price, String stock, String brand, String description, String sellerUUID, int quantitySold, String quantity, String status) {
        this.name = name;
        this.category = category;
        this.url = url;
        this.rating = rating;
        this.price = price;
        this.stock = stock;
        this.brand = brand;
        this.description = description;
        this.sellerUUID = sellerUUID;
        this.quantitySold = quantitySold;
        this.quantity = quantity;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSellerUUID() {
        return sellerUUID;
    }

    public void setSellerUUID(String sellerUUID) {
        this.sellerUUID = sellerUUID;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ShipItem{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", url='" + url + '\'' +
                ", rating='" + rating + '\'' +
                ", price='" + price + '\'' +
                ", stock='" + stock + '\'' +
                ", brand='" + brand + '\'' +
                ", description='" + description + '\'' +
                ", sellerUUID='" + sellerUUID + '\'' +
                ", quantitySold=" + quantitySold +
                ", quantity='" + quantity + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
