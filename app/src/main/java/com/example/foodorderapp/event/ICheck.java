package com.example.foodorderapp.event;

public interface ICheck {
    void onCorrectCheck(int type);
    void onFailureCheck(int type, String mes);
}
