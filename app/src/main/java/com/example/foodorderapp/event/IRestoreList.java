package com.example.foodorderapp.event;

import com.example.foodorderapp.model.Restaurant;

import java.util.List;

public interface IRestoreList {
    void onRestore(List<Restaurant> restaurantList);
}
