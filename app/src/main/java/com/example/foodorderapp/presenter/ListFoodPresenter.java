package com.example.foodorderapp.presenter;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.foodorderapp.event.EBus;
import com.example.foodorderapp.event.IListFood;
import com.example.foodorderapp.event.IListRestaurant;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.model.Restaurant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class ListFoodPresenter {
    IListFood iListFood;
    Context context;
    List<Food> foodList;
    Restaurant res;

    public ListFoodPresenter(IListFood iListFood, Context context) {
        this.iListFood = iListFood;
        this.context = context;
    }

    public void showListFood(){
        EventBus.getDefault().register(this);
        foodList = res.getFoodList();
//        iListFood.onListFood(foodList);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    // get list from list restaurant fragment
    public void getRestaurant(Restaurant restaurant) {
        res = restaurant;
    }
}
