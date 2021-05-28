package com.example.foodorderapp.event;

public interface ILogin {
    void onSuccessful(int type);
    void onFailure(String mes);
}
