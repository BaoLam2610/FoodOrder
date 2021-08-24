package com.example.foodorderapp.presenter;

import android.content.Context;

import com.example.foodorderapp.event.IBanner;
import com.example.foodorderapp.model.Banner;
import com.example.foodorderapp.model.Restaurant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class BannerPresenter {
    IBanner iBanner;
    Context context;

    public BannerPresenter(IBanner iBanner, Context context) {
        this.iBanner = iBanner;
        this.context = context;
    }

    public void showListBanner(){
        EventBus.getDefault().register(this);
        iBanner.onShowListBanner(restaurantList);
        EventBus.getDefault().unregister(this);
    }

    List<Restaurant> restaurantList;
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getBannerList(List<Restaurant> restaurantBanner){
        restaurantList = restaurantBanner;
    }
}
