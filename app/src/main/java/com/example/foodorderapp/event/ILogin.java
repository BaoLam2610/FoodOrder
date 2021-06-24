package com.example.foodorderapp.event;

import com.example.foodorderapp.model.UserAccount;

public interface ILogin {
    void onSuccessful(int type, UserAccount userAccount);
    void onFailure(String mes);
}
