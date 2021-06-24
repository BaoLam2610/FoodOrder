package com.example.foodorderapp.model;

public class DetailFavorite {
    private Restaurant restaurant;
    private UserAccount userAccount;
    private int status;

    public DetailFavorite(Restaurant restaurant, UserAccount userAccount) {
        this.restaurant = restaurant;
        this.userAccount = userAccount;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
