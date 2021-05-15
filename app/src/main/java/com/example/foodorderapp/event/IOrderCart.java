package com.example.foodorderapp.event;

import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.Food;

import java.util.List;

public interface IOrderCart {
    void onShowListFoodOrder(Cart cart, List<Food> foodList);
}
