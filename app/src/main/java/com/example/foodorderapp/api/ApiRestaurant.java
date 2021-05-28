package com.example.foodorderapp.api;

import com.example.foodorderapp.model.Restaurant;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface ApiRestaurant {
    //    LIST_RESTAURANT_API = "https://demo9455117.mockable.io/ListRestaurant";
//    LIST_BANNER_API = "https://demo9455117.mockable.io/ListBanner";
//    LIST_FOOD_CATEGORY_API = "https://demo9455117.mockable.io/ListFoodCategory";
    ApiRestaurant apiRestaurant = new Retrofit.Builder()
            .baseUrl("https://demo9455117.mockable.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiRestaurant.class);

    @GET("ListRestaurant")
    Call<List<Restaurant>> getListRestaurant();
}
