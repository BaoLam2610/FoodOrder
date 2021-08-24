package com.example.foodorderapp.presenter;

import android.content.Context;

import com.example.foodorderapp.event.IMyOrder;
import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.sql.CartDatabaseHelper;

import java.util.List;

public class MyOrderPresenter {
    IMyOrder iMyOrder;
    Context context;
    CartDatabaseHelper helper;

    public MyOrderPresenter(IMyOrder iMyOrder, Context context) {
        this.iMyOrder = iMyOrder;
        this.context = context;
    }

//    public List<Food> showMyOrderFood(Cart cart){
//
//        if(helper == null)
//            helper = new CartDatabaseHelper(context);
//        List<Food> foodList = helper.getMyOrderListFood(cart.getIdCart(),cart.getRestaurant().getId());
//        return foodList;
//    }

    public void showMyOrder(){
        if(helper == null)
            helper = new CartDatabaseHelper(context);
        List<Cart> cartList = helper.getMyOrderList();
        if(cartList.size() != 0)
            iMyOrder.onShowListOrder(cartList);
        else
            iMyOrder.onEmptyListOrder();
    }

}
