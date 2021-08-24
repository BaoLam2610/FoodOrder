package com.example.foodorderapp.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

public class Cart implements Serializable {
    private String idCart;
    private Restaurant restaurant;
    private List<Food> foodOrder;
    private String date;
    private int amount;
    private long totalPrice;
    private long deliveryFee;
    private Voucher voucher;
    private String note;
    private int status = 0;
    private static int id = 0;
    private UserAccount user;
    Random rd = new Random();
    DecimalFormat df = new DecimalFormat("0");

    public Cart(String idCart, Restaurant restaurant, int amount, long totalPrice,int status) {
        this.idCart = idCart;
        this.restaurant = restaurant;
        this.amount = amount;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public Cart(Restaurant restaurant, int amount, long totalPrice) {

        idCart = df.format(rd.nextInt(10000));
        this.restaurant = restaurant;

        this.amount = amount;
        this.totalPrice = totalPrice;
        status = 0;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Voucher getVoucher() {
        return voucher;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(long deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public UserAccount getUser() {
        return user;
    }

    public void setUser(UserAccount user) {
        this.user = user;
    }
}
