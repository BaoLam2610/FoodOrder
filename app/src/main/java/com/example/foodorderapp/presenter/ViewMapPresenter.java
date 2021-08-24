package com.example.foodorderapp.presenter;

import android.content.Context;
import android.util.Log;

import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.ListGroupAdapter;
import com.example.foodorderapp.api.ApiRestaurant;
import com.example.foodorderapp.event.IViewMap;
import com.example.foodorderapp.model.Address;
import com.example.foodorderapp.model.GroupList;
import com.example.foodorderapp.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewMapPresenter {

    IViewMap iViewMap;
    Context context;

    public ViewMapPresenter(IViewMap iViewMap, Context context) {
        this.iViewMap = iViewMap;
        this.context = context;
    }

    public void showRestaurantLocation(Restaurant restaurant){
        Address address = restaurant.getAddress();
        double latitude = address.getX();
        double longitude = address.getY();
        iViewMap.onShowRestaurantLocation(latitude,longitude,restaurant);
    }

    public void showAllRestaurant(){
        ApiRestaurant.apiRestaurant.getListRestaurant().enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {

                if (response.body() != null) {
                    List<Restaurant> restaurantList = response.body();
                    iViewMap.onShowAllRestaurantLocation(restaurantList);
                }
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }

    public void showUserLocation() {
    }
}
