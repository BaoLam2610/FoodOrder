package com.example.foodorderapp.event;

import com.example.foodorderapp.model.Address;
import com.example.foodorderapp.model.Restaurant;

import java.util.List;

public interface IViewMap {
    void onShowRestaurantLocation(double latitude, double longitude, Restaurant restaurant);
    void onShowAllRestaurantLocation(List<Restaurant> addressList);
    void onShowYourLocation(double latitude, double longitude);
}
