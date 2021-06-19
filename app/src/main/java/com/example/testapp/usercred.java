package com.example.testapp;

public class usercred {
    public String email, type, name,phone,adress,gender,pic;

    public usercred() {
    }

    public usercred(String email, String type, String name, String phone, String adress, String gender, String pic) {
        this.email = email;
        this.type = type;
        this.name = name;
        this.phone = phone;
        this.adress = adress;
        this.gender = gender;
        this.pic = pic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
