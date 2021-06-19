package com.example.foodorderapp.presenter;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.foodorderapp.adapter.MyOrderListFoodAdapter;
import com.example.foodorderapp.adapter.OrderCartAdapter;
import com.example.foodorderapp.event.ICartDatabase;
import com.example.foodorderapp.event.IOnListFood;
import com.example.foodorderapp.event.IOnShowCart;
import com.example.foodorderapp.event.IOnShowDetailCart;
import com.example.foodorderapp.event.IOrderCart;
import com.example.foodorderapp.helper.FormatHelper;
import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.DetailCart;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.model.FoodCart;
import com.example.foodorderapp.model.Restaurant;
import com.example.foodorderapp.model.Voucher;
import com.example.foodorderapp.sql.CartDatabaseHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class CartDatabasePresenter {

    IOrderCart iOrderCart;
    IOnShowCart iOnShowCart;
    IOnListFood iOnListFood;
    ICartDatabase iCartDatabase;
    Context context;
    CartDatabaseHelper helper;
    String idRes;
    Cart currentCart;
    Restaurant tempRestaurant;
    IOnShowDetailCart iOnShowDetailCart;

    public CartDatabasePresenter(IOrderCart iOrderCart, ICartDatabase iCartDatabase, IOnListFood iOnListFood, Context context) {
        this.iOrderCart = iOrderCart;
        this.iCartDatabase = iCartDatabase;
        this.iOnListFood = iOnListFood;
        this.context = context;
    }

    public CartDatabasePresenter(IOnShowDetailCart iOnShowDetailCart,Context context) {
        this.context = context;
        this.iOnShowDetailCart = iOnShowDetailCart;
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
    DetailCart tempDc;
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void getDetailCart(DetailCart detailCart){
        tempDc = detailCart;
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
//    Cart currentCart;
//    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
//    public void getStatusCart(Cart cart){
//
//    }
    public void setCart(){
        EventBus.getDefault().register(this);
        try {
            if (helper == null)
                helper = new CartDatabaseHelper(context);
            currentCart.setStatus(1);
            String date = FormatHelper.getCurrentDate();
            System.out.println(date);
            currentCart.setDate(date);

            helper.setStatusDetailCart(tempDc);
            helper.setStatusRestaurant(tempRestaurant);
            helper.setStatusCart(currentCart);
            helper.setDateCart(currentCart);
        } catch (Exception e) {
            e.printStackTrace();
        }
        EventBus.getDefault().unregister(this);
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

    public void showDetailCart(Food food, Cart cart, OrderCartAdapter.OrderCartViewHolder holder){
        if(helper == null)
            helper = new CartDatabaseHelper(context);
        DetailCart detailCart = helper.findDetailCart(food,cart);
        iOnShowDetailCart.onShowDetailCart(detailCart,holder);
    }

    public void showDetailCart(Food food, Cart cart, MyOrderListFoodAdapter.ListFoodOrderViewHolder holder){
        if(helper == null)
            helper = new CartDatabaseHelper(context);
        DetailCart detailCart = helper.findDetailCart(food,cart);
        iOnShowDetailCart.onShowDetailCart(detailCart,holder);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showListOrder() {
        EventBus.getDefault().register(this);
        if (helper == null)
            helper = new CartDatabaseHelper(context);

        Voucher voucher = helper.getVoucher(currentCart);
        if(voucher !=null)
            iOrderCart.onShowVoucher(voucher,currentCart);

        List<Food> foodList = helper.getFood(currentCart);
        if(foodList.size() != 0) {
            foodList.sort((f1, f2) -> f1.getCategory().compareTo(f2.getCategory()));

            iOrderCart.onShowListFoodOrder(currentCart, foodList);
        } else
            iOrderCart.onEmptyListFoodOrder();

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

    public void destroyAllData() {
        try {
            if (helper == null)
                helper = new CartDatabaseHelper(context);
            helper.deleteAllRestaurant();
            helper.deleteAllDetailCart();
//            helper.deleteAllFood();
            helper.deleteAllCart();
            helper.deleteAllCartEmpty();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
