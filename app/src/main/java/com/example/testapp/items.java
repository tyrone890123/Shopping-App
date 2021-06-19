package com.example.testapp;

import android.os.Parcel;
import android.os.Parcelable;

public class items implements Parcelable {
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

    public items(){

    }

    public items(String name, String category, String url, String rating, String price, String stock, String brand, String description, String sellerUUID, int quantitySold) {
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
    }

    protected items(Parcel in) {
        name = in.readString();
        category = in.readString();
        url = in.readString();
        rating = in.readString();
        price = in.readString();
        stock = in.readString();
        brand = in.readString();
        description = in.readString();
        sellerUUID = in.readString();
        quantitySold = in.readInt();
    }

    public static final Creator<items> CREATOR = new Creator<items>() {
        @Override
        public items createFromParcel(Parcel in) {
            return new items(in);
        }

        @Override
        public items[] newArray(int size) {
            return new items[size];
        }
    };

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

    @Override
    public String toString() {
        return "items{" +
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
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(category);
        dest.writeString(url);
        dest.writeString(rating);
        dest.writeString(price);
        dest.writeString(stock);
        dest.writeString(brand);
        dest.writeString(description);
        dest.writeString(sellerUUID);
        dest.writeInt(quantitySold);
    }
}
