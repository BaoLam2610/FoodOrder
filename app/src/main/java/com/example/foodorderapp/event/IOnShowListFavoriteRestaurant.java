package com.example.foodorderapp.event;

import com.example.foodorderapp.model.Restaurant;

import java.util.List;

public interface IOnShowListFavoriteRestaurant {
    void onShowListFavorite(List<Restaurant> restaurantList);
    void onEmptyListFavorite();
}
