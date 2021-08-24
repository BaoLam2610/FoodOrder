package com.example.foodorderapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Address implements Serializable {

    private int id;
    @SerializedName("x")
    private double x;
    @SerializedName("y")
    private double y;
    @SerializedName("address")
    private String address;

    public Address(int id, double x, double y, String address) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
