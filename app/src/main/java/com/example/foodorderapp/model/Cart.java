package com.example.foodorderapp.model;

import java.util.List;

public class Cart {
    private String idCart;
    private String idRes, idFood;
    private Restaurant restaurant;
    private List<Food> foodOrder;
    private int amount;
    private long totalPrice;

    public Cart() {
    }

    public Cart(String idCart,String idRes) {
        this.idCart = idCart;
        this.idRes = idRes;
        amount = 0;
        totalPrice = 0;
    }

    public Cart(String idCart, String idRes, int amount, long totalPrice) {
        this.idCart = idCart;
        this.idFood = idRes;
        this.amount = amount;
        this.totalPrice = totalPrice;
    }

    public Cart(List<Food> foodOrder) {
        this.foodOrder = foodOrder;
    }


    public String getIdRes() {
        return idRes;
    }

    public void setIdRes(String idRes) {
        this.idRes = idRes;
    }

    public String getIdFood() {
        return idFood;
    }

    public void setIdFood(String idFood) {
        this.idFood = idFood;
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

    public String getIdCart() {
        return idCart;
    }

    public void setIdCart(String idCart) {
        this.idCart = idCart;
    }

    public void setFoodOrder(List<Food> foodOrder) {
        this.foodOrder = foodOrder;
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
