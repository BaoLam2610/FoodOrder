package com.example.foodorderapp.model;

import java.util.List;
import java.util.Objects;

public class GroupList {
    private final int type;
    private String headerTitle;
    private List<FoodCategory> foodCategory;
    private List<FoodBanner> foodBanner;
    private List<Restaurant> restaurants;

    public GroupList(int type, String headerTitle,
                     List<FoodCategory> foodCategory,
                     List<FoodBanner> foodBanner,
                     List<Restaurant> restaurants) {
        this.type = type;
        this.headerTitle = headerTitle;
        this.foodCategory = foodCategory;
        this.foodBanner = foodBanner;
        this.restaurants = restaurants;
    }

    public GroupList(int type, String headerTitle, List<Restaurant> restaurants) {
        this.type = type;
        this.headerTitle = headerTitle;
        this.restaurants = restaurants;
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public int getType() {
        return type;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public List<FoodCategory> getFoodCategory() {
        return foodCategory;
    }

    public void setFoodCategory(List<FoodCategory> foodCategory) {
        this.foodCategory = foodCategory;
    }

    public List<FoodBanner> getFoodBanner() {
        return foodBanner;
    }

    public void setFoodBanner(List<FoodBanner> foodBanner) {
        this.foodBanner = foodBanner;
    }


}
