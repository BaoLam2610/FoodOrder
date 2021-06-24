package com.example.foodorderapp.event;

import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.model.FoodCart;
import com.example.foodorderapp.model.Voucher;

import java.util.List;

public interface IOrderCart {
    void onShowListFoodOrder(Cart cart, List<Food> foodList);
    void onEmptyListFoodOrder();
    void onShowVoucher(Voucher voucher, Cart cart);
    void onEmptyVoucher(Cart cart);
    void onCalculationPrice(long price,long discountPercent, long discount,long deliveryFee, long totalPrice);
}
