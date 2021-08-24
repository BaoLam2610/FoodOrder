package com.example.foodorderapp.event;

import com.example.foodorderapp.model.Restaurant;

public interface IDetailRestaurant {
    void onShowDetailRestaurant(Restaurant restaurant,String type);
}
