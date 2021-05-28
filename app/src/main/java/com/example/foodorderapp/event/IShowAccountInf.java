package com.example.foodorderapp.event;

import com.example.foodorderapp.model.UserAccount;

public interface IShowAccountInf {
    void onExistsAccount(UserAccount userAccount);
    void onNotExistsAccount();
}
