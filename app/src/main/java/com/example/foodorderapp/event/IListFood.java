package com.example.foodorderapp.event;

import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.model.Restaurant;

import java.util.List;

public interface IListFood {
    void onListFood(Restaurant restaurant, List<Restaurant> restaurantList);
}
