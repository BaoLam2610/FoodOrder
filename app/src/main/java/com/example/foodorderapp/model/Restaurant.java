package com.example.foodorderapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Restaurant implements Serializable {
    @SerializedName("res_id")
    private String id;
    @SerializedName("res_name")
    private String name;
    @SerializedName("res_provide")
    private String provideType;
    @SerializedName("res_img")
    private String image;
    @SerializedName("res_addr")
    private Address address;
    @SerializedName("res_phone")
    private String phone;
    @SerializedName("res_email")
    private String email;
    @SerializedName("res_rate")
    private double rate;
    @SerializedName("res_banner")
    private Banner banner;
    @SerializedName("res_list_food")
    private ArrayList<Food> foodList;
    private int cart;
    private int status;

    //    String id, String name, String provideType, String image, String address, String phone, String email, double rate


    public Restaurant(String id, String name, String provideType, String image, Address address, String phone, String email, double rate) {
        this.id = id;
        this.name = name;
        this.provideType = provideType;
        this.image = image;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.rate = rate;
        status = 0;
    }


    public Banner getBanner() {
        return banner;
    }

    public void setBanner(Banner banner) {
        this.banner = banner;
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
