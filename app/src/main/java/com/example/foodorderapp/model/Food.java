package com.example.foodorderapp.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Objects;

public class Food {
    private String id,idRes, name;
    private String image;
    private long price;
    private String category;
    private int count;
    private String type;

    public Food(String id) {
        this.id = id;
    }

    public Food(String id, String name, String image, long price, String category) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
        this.category = category;
        count = 0;
    }

    public String getIdRes() {
        return idRes;
    }

    public void setIdRes(String idRes) {
        this.idRes = idRes;
    }

    public Food(String id, String idRes, String name, String image, String category, int count, long price) {
        this.id = id;
        this.idRes = idRes;
        this.name = name;
        this.image = image;
        this.price = price;
        this.category = category;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
