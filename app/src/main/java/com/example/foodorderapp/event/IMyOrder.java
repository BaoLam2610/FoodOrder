package com.example.foodorderapp.event;

import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.Food;

import java.util.List;

public interface IMyOrder {
//    void onShowListOrder(List<Cart> cartList, List<Food> foodList);
    void onShowListOrder(List<Cart> cartList);
    void onEmptyListOrder();
}
