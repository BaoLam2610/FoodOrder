package com.example.foodorderapp.presenter;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.foodorderapp.event.ICartDatabase;
import com.example.foodorderapp.event.ICheckLogin;
import com.example.foodorderapp.event.IDetailRestaurant;
import com.example.foodorderapp.event.IListRestaurant;
import com.example.foodorderapp.event.IOnListFood;
import com.example.foodorderapp.event.IOnShowCart;
import com.example.foodorderapp.event.IOrderCart;
import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.model.Restaurant;
import com.example.foodorderapp.sql.CartDatabaseHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class DetailPresenter {

    ICartDatabase iCartDatabase;
    IListRestaurant iListRestaurant;
    IDetailRestaurant iDetailRestaurant;
    ICheckLogin iCheckLogin;
    IOnShowCart iOnShowCart;
    IOrderCart iOrderCart;
    CartDatabaseHelper helper;
    IOnListFood iOnListFood;
    List<Restaurant> restaurantList;
    Food food;
    Integer totalItemCart;
    Restaurant res;
    Context context;
    List<Food> list;
    List<Food> fastFoodList;
    List<Food> starterFoodList;
    List<Food> mainCourseList;
    List<Food> desertList;
    List<Food> drinkList;
    List<Food> listOrder;

    public DetailPresenter(ICheckLogin iCheckLogin, Context context) {
        this.iCheckLogin = iCheckLogin;
        this.context = context;
    }

    public DetailPresenter(IDetailRestaurant iDetailRestaurant, IOnShowCart iOnShowCart) {
        this.iDetailRestaurant = iDetailRestaurant;
        this.iOnShowCart = iOnShowCart;

    }


    public DetailPresenter(IOnListFood iOnListFood, Context context) {
        this.iOnListFood = iOnListFood;
        this.context = context;
    }
    public DetailPresenter(IOnShowCart iOnShowCart, Context context) {
        this.iOnShowCart = iOnShowCart;
        this.context = context;
    }
    public DetailPresenter(IListRestaurant iListRestaurant, Context context) {
        this.iListRestaurant = iListRestaurant;
        this.context = context;
    }
    public DetailPresenter(IDetailRestaurant iDetailRestaurant, Context context) {
        this.iDetailRestaurant = iDetailRestaurant;
        this.context = context;
    }
    public DetailPresenter(IOrderCart iOrderCart, Context context) {
        this.iOrderCart = iOrderCart;
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showListRestaurant(String type) {

        EventBus.getDefault().register(this);
        switch (type) {
            case "0":
                iListRestaurant.onShowListRestaurant(restaurantList);
                break;
            case "1":
                List<Restaurant> bestRestaurantList = restaurantList;
                bestRestaurantList.removeIf(r -> r.getRate() <= 4.0);
                iListRestaurant.onShowListRestaurant(bestRestaurantList);
                break;
        }

        EventBus.getDefault().unregister(this);
    }

    public void showDetailRestaurant() {
        EventBus.getDefault().register(this);
        iDetailRestaurant.onShowDetailRestaurant(res);
        EventBus.getDefault().unregister(this);
    }

    public List<Food> getListFood(int type) {
        fastFoodList = new ArrayList<>();
        starterFoodList = new ArrayList<>();
        mainCourseList = new ArrayList<>();
        desertList = new ArrayList<>();
        drinkList = new ArrayList<>();

        EventBus.getDefault().register(this);
        list = res.getFoodList();
        for (int i = 0; i < list.size(); i++) {
            switch (list.get(i).getCategory()) {
                case "Fast food":
                    fastFoodList.add(list.get(i));
                    break;
                case "Starter food":
                    starterFoodList.add(list.get(i));
                    break;
                case "Main course food":
                    mainCourseList.add(list.get(i));
                    break;
                case "Desert food":
                    desertList.add(list.get(i));
                    break;
                case "Drink":
                    drinkList.add(list.get(i));
                    break;
                default:
                    break;
            }
        }
        switch (type) {
            case 0:
                return fastFoodList;
            case 1:
                return starterFoodList;
            case 2:
                return mainCourseList;
            case 3:
                return desertList;
            case 4:
                return drinkList;
        }
        return null;
//        iOnListFood.onShowListFood(fastFoodList,starterFoodList,mainCourseList,desertList,drinkList);
    }

    public void showListFood(int type) {
        iOnListFood.onShowListFood(getListFood(type));
    }


    public void showTotalItemCart() {
//        if(totalItemCart == 0)
//            return;
        EventBus.getDefault().register(this);
//        iOnShowTotalItemCart.onShowTotalItemCart();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getListOrderCart(Cart cart) {
        if (cart != null && cart.getRestaurant() != null && cart.getRestaurant().getFoodList() != null)
            listOrder = new ArrayList<>(cart.getRestaurant().getFoodList());
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getListRestaurant(List<Restaurant> list) {
        restaurantList = new ArrayList<>(list);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getDetailRestaurant(Restaurant restaurant) {
        res = restaurant;
    }


}
