package com.example.foodorderapp.model;

public class UserAccount {
    private String phone,username,password, avatar, address;
    private long money;

    public UserAccount(String phone, String username, String password, String avatar, String address, long money) {
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.avatar = avatar;
        this.address = address;
        this.money = money;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }
}
