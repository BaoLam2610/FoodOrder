package com.example.foodorderapp.model;

import java.util.List;

public class Cart {
    private String idCart;
    private Restaurant restaurant;
    private List<Food> foodOrder;
    private int amount;
    private long totalPrice;

    public Cart() {
    }

    public Cart(String idCart, Restaurant restaurant, int amount, long totalPrice) {
        this.restaurant = restaurant;
        this.idCart = idCart;
        this.amount = amount;
        this.totalPrice = totalPrice;
    }

    public Cart(List<Food> foodOrder) {
        this.foodOrder = foodOrder;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public List<Food> getFoodOrder() {
        return foodOrder;
    }

    public void setFoodOrder(List<Food> foodOrder) {
        this.foodOrder = foodOrder;
    }

    public String getIdCart() {
        return idCart;
    }

    public void setIdCart(String idCart) {
        this.idCart = idCart;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }
}
