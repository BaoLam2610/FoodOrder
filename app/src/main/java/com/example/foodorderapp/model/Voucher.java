package com.example.foodorderapp.model;

public class Voucher {
    private String id, name;
    private int image;
    private int discount;

    public Voucher(String id, String name, int discount) {
        this.id = id;
        this.name = name;

        this.discount = discount;
    }

    public Voucher(String id, String name, int image, int discount) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.discount = discount;
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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
