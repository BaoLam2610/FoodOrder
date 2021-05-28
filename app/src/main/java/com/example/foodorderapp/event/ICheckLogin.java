package com.example.foodorderapp.event;

import com.example.foodorderapp.model.UserAccount;

public interface ICheckLogin {
    void onExists(String mes);
    void onNotExists(String mes);
}
