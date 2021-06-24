package com.example.foodorderapp.event;

import com.example.foodorderapp.model.UserAccount;

public interface ISignUp {
    void onSuccessful(UserAccount userAccount);
    void onFailure(String mes);
}
