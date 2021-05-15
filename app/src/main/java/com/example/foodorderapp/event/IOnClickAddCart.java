package com.example.foodorderapp.event;

import com.example.foodorderapp.model.Food;

import java.util.List;

public interface IOnClickAddCart {
    void onClickAddCart(Food food);
    void onClickPlus(Food food);
    void onClickMinus(Food food);
}
