package com.example.foodorderapp.event;

import com.example.foodorderapp.model.UserAccount;

public interface ICheckLogin {
    void onExists(UserAccount account);
    void onNotExists();
}
