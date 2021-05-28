package com.example.foodorderapp.presenter;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.foodorderapp.event.ICartDatabase;
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

import java.util.List;

public class CartDatabasePresenter {

    IOrderCart iOrderCart;
    IOnShowCart iOnShowCart;
    ICartDatabase iCartDatabase;
    IOnListFood iOnListFood;
    Context context;
    CartDatabaseHelper helper;
    String idRes;
    Cart currentCart;
    Restaurant tempRestaurant;

    public CartDatabasePresenter(IOrderCart iOrderCart, ICartDatabase iCartDatabase, IOnListFood iOnListFood, Context context) {
        this.iOrderCart = iOrderCart;
        this.iCartDatabase = iCartDatabase;
        this.iOnListFood = iOnListFood;
        this.context = context;
    }

    public CartDatabasePresenter(ICartDatabase iCartDatabase, Context context) {
        this.iCartDatabase = iCartDatabase;
        this.context = context;
    }

    public CartDatabasePresenter(IOnListFood iOnListFood, Context context) {
        this.iOnListFood = iOnListFood;
        this.context = context;
    }

    public CartDatabasePresenter(ICartDatabase iCartDatabase, IOnListFood iOnListFood, Context context) {
        this.iCartDatabase = iCartDatabase;
        this.iOnListFood = iOnListFood;
        this.context = context;
    }

    public void saveRestaurantOnCart(Restaurant restaurant) {
        try {
            if (helper == null)
                helper = new CartDatabaseHelper(context);
            if (!helper.findRestaurant(restaurant))
                helper.insertRestaurant(restaurant);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void saveFoodOnCart(Food food) {
        EventBus.getDefault().register(this);
        try {
            if (helper == null)
                helper = new CartDatabaseHelper(context);
            food.setRestaurant(tempRestaurant);
            Log.e("Loi",food.getRestaurant().getId());
            helper.insertFood(food);
//            String id, String name, String image, int count, long price, String category, String idRes
        } catch (Exception e) {
            e.printStackTrace();
        }
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void getRestaurant(Restaurant restaurant){
        tempRestaurant = restaurant;
    }

    public void saveCart(Cart cart) {
        try {
            if (helper == null)
                helper = new CartDatabaseHelper(context);
            helper.insertCart(cart);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editFood(Food food) {
        try {
            if (helper == null)
                helper = new CartDatabaseHelper(context);
            helper.updateFood(food);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editCart(Cart cart) {
        try {
            if (helper == null)
                helper = new CartDatabaseHelper(context);
            helper.updateCart(cart);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroyRestaurant(Restaurant restaurant) {
        try {
            if (helper == null)
                helper = new CartDatabaseHelper(context);
            helper.deleteRestaurant(restaurant);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroyCart(Cart cart) {
        try {
            if (helper == null)
                helper = new CartDatabaseHelper(context);
            helper.deleteCart(cart);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showCart() {
        if (helper == null)
            helper = new CartDatabaseHelper(context);
        if (currentCart == null) {
            currentCart = helper.getCart();
//        EventBus.getDefault().register(this);

        }
        iOnShowCart.onShowCart(currentCart);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showListOrder() {
        if (helper == null)
            helper = new CartDatabaseHelper(context);
        List<Food> foodList = helper.getFood();
        foodList.sort((f1, f2) -> f1.getCategory().compareTo(f2.getCategory()));
        EventBus.getDefault().register(this);
        iOrderCart.onShowListFoodOrder(currentCart, foodList);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getCurrentCart(Cart cart) {

        currentCart = cart;
    }

    public void destroyFood(Food food, Cart cart) {
        try {
            if (helper == null)
                helper = new CartDatabaseHelper(context);
            helper.deleteFood(food, cart);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean checkCart(Cart cart) {

        if (helper == null)
            helper = new CartDatabaseHelper(context);

        return helper.findCart(cart);
    }

    public void destroyAllData(Restaurant restaurant, Cart cart) {
        try {
            if (helper == null)
                helper = new CartDatabaseHelper(context);
            if (helper.findRestaurant(restaurant))
                helper.deleteRestaurant(restaurant);
            helper.deleteAllFood();
            if (helper.findCart(cart))
                helper.deleteCart(cart);
            helper.deleteAllCart();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
