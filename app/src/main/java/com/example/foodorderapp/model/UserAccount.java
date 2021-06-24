package com.example.foodorderapp.model;

import java.io.Serializable;

public class UserAccount implements Serializable {
    private String phone, username, password, address;
    private byte[] avatar;
    private int status;

    public UserAccount(String phone, String username, String password, byte[] avatar, String address, int status) {
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.avatar = avatar;
        this.address = address;
        this.status = status;
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

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
