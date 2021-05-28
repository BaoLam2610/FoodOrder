package com.example.foodorderapp.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Food {
    @SerializedName("food_id")
    private String id;
    @SerializedName("food_name")
    private String name;
    @SerializedName("food_img")
    private String image;
    @SerializedName("food_price")
    private long price;
    @SerializedName("food_category")
    private String category;
    private int count;
    private Restaurant restaurant;


    public Food(String id, String name, String image, long price, String category, int count, Restaurant restaurant) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
        this.category = category;
        this.count = count;
        this.restaurant = restaurant;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Food food = (Food) o;
        return id.equals(food.id);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
