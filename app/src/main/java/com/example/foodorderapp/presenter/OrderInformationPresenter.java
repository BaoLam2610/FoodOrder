package com.example.foodorderapp.presenter;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.foodorderapp.R;
import com.example.foodorderapp.event.IBackFragment;
import com.example.foodorderapp.event.ICheckCartInf;
import com.example.foodorderapp.event.IDetailRestaurant;
import com.example.foodorderapp.event.IOrderCart;
import com.example.foodorderapp.event.IShowAccountInf;
import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.model.Restaurant;
import com.example.foodorderapp.model.UserAccount;
import com.example.foodorderapp.model.Voucher;
import com.example.foodorderapp.sql.CartDatabaseHelper;

import java.util.List;

public class OrderInformationPresenter {

    IBackFragment iBackFragment;
    IOrderCart iOrderCart;
    IShowAccountInf iShowAccountInf;
    IDetailRestaurant iDetailRestaurant;
    ICheckCartInf iCheckCartInf;
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

    public OrderInformationPresenter(IOrderCart iOrderCart, IShowAccountInf iShowAccountInf,
                                     IDetailRestaurant iDetailRestaurant, ICheckCartInf iCheckCartInf,
                                     IBackFragment iBackFragment, Context context) {
        helper = new CartDatabaseHelper(context);
        this.iCheckCartInf = iCheckCartInf;
        this.iOrderCart = iOrderCart;
        this.iShowAccountInf = iShowAccountInf;
        this.iDetailRestaurant = iDetailRestaurant;
        this.iBackFragment = iBackFragment;
        this.context = context;
    }

    public void saveUserAddress(UserAccount userAccount, String address) {
        userAccount.setAddress(address);
        helper.updateAccount(userAccount);
    }

    public void saveDeliveryFee(Cart cart, double range) {
        long deliveryFee;

        if (range <= 1.0)
            deliveryFee = 0;
        else
            deliveryFee = (long) (10_000 + range * 3000);

        if (deliveryFee > 50_000)
            deliveryFee = 50_000;
        cart.setDeliveryFee(deliveryFee);
        helper.updateCart(cart);
    }

    public void backFragment(int index) {

        if (index == 1)
            iBackFragment.onBackOne();
        else // when login -> still have 2 fragment login, sign up, -> need to remove both fragment
            iBackFragment.onBackTwo(index);

    }

    public void showRestaurantAddress(Cart cart) {
        Restaurant restaurant = helper.getRestaurant(cart);
        iDetailRestaurant.onShowDetailRestaurant(restaurant, "show_address");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showDetailOrder(Cart cart, UserAccount userAccount) {

        if (helper == null)
            helper = new CartDatabaseHelper(context);

        iShowAccountInf.onExistsAccount(userAccount);
        // voucher
        Voucher voucher = helper.getVoucher(cart);
        if (voucher != null)
            iOrderCart.onShowVoucher(voucher, cart);
        // restaurant
        Restaurant restaurant = helper.getRestaurant(cart);
        iDetailRestaurant.onShowDetailRestaurant(restaurant, "show_name");
        // list food
        List<Food> foodList = helper.getFood(cart);
        if (foodList.size() != 0) {
            foodList.sort((f1, f2) -> f1.getCategory().compareTo(f2.getCategory()));
            iOrderCart.onShowListFoodOrder(cart, foodList);
        } else
            iOrderCart.onEmptyListFoodOrder();

    }

    public void checkInformation(String user, String address) {
        if (user.isEmpty() || address.isEmpty() || (user.isEmpty() && address.isEmpty()))
            iCheckCartInf.onIncorrect(context.getResources().getString(R.string.order_inf_error));
        else
            iCheckCartInf.onCorrectly();
    }
}
