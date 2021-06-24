package com.example.foodorderapp.presenter;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.foodorderapp.event.IDetailRestaurant;
import com.example.foodorderapp.event.IOrderCart;
import com.example.foodorderapp.event.IShowAccountInf;
import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.model.Restaurant;
import com.example.foodorderapp.model.UserAccount;
import com.example.foodorderapp.model.Voucher;
import com.example.foodorderapp.sql.CartDatabaseHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class OrderInformationPresenter {

    IOrderCart iOrderCart;
    IShowAccountInf iShowAccountInf;
    IDetailRestaurant iDetailRestaurant;
    Context context;
    CartDatabaseHelper helper;

    public OrderInformationPresenter(IOrderCart iOrderCart, Context context) {
        this.iOrderCart = iOrderCart;
        this.context = context;
    }

    public OrderInformationPresenter(IOrderCart iOrderCart, IDetailRestaurant iDetailRestaurant, Context context) {
        this.iOrderCart = iOrderCart;
        this.iDetailRestaurant = iDetailRestaurant;
        this.context = context;
    }

    public OrderInformationPresenter(IOrderCart iOrderCart, IShowAccountInf iShowAccountInf, IDetailRestaurant iDetailRestaurant, Context context) {
        this.iOrderCart = iOrderCart;
        this.iShowAccountInf = iShowAccountInf;
        this.iDetailRestaurant = iDetailRestaurant;
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showDetailOrder(Cart cart, UserAccount userAccount) {

        if (helper == null)
            helper = new CartDatabaseHelper(context);

        iShowAccountInf.onExistsAccount(userAccount);
        // voucher
        Voucher voucher = helper.getVoucher(cart);
        if(voucher !=null)
            iOrderCart.onShowVoucher(voucher,cart);
        // restaurant
        Restaurant restaurant = helper.getRestaurant(cart);
        iDetailRestaurant.onShowDetailRestaurant(restaurant);
        // list food
        List<Food> foodList = helper.getFood(cart);
        if(foodList.size() != 0) {
            foodList.sort((f1, f2) -> f1.getCategory().compareTo(f2.getCategory()));
            iOrderCart.onShowListFoodOrder(cart, foodList);
        } else
            iOrderCart.onEmptyListFoodOrder();


    }
}
