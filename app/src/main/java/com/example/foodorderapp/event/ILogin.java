package com.example.foodorderapp.event;

public interface ILogin {
    void onSuccessful(int type,String username);
    void onFailure(String mes);
}
