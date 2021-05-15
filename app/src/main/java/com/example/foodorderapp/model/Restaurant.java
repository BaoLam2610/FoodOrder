package com.example.foodorderapp.model;

import java.util.ArrayList;

public class Restaurant {
    private String id, name, provideType, image, address, phone, email;
    private double rate;
    private int cart;
    private ArrayList<Food> foodList;

    public Restaurant(String id, String name, String provideType, String image, String address, String phone, String email, double rate, ArrayList<Food> foodList) {
        this.id = id;
        this.name = name;
        this.provideType = provideType;
        this.image = image;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.rate = rate;
        this.foodList = foodList;
        cart = 0;
    }

    public Restaurant(String id, String name, String provideType, String image, String address, String phone, String email, double rate) {
        this.id = id;
        this.name = name;
        this.provideType = provideType;
        this.image = image;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.rate = rate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCart() {
        return cart;
    }

    public void setCart(int cart) {
        this.cart = cart;
    }

    public Restaurant(String name, String image, String address, double rate) {
        this.name = name;
        this.image = image;
        this.address = address;
        this.rate = rate;
    }

    public Restaurant(String name, String provideType, String address) {
        this.name = name;
        this.provideType = provideType;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvideType() {
        return provideType;
    }

    public void setProvideType(String provideType) {
        this.provideType = provideType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public ArrayList<Food> getFoodList() {
        return foodList;
    }

    public void setFoodList(ArrayList<Food> foodList) {
        this.foodList = foodList;
    }
}
