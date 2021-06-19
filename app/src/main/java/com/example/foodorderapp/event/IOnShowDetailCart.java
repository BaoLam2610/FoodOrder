package com.example.foodorderapp.event;


import com.example.foodorderapp.adapter.MyOrderListFoodAdapter;
import com.example.foodorderapp.adapter.OrderCartAdapter;
import com.example.foodorderapp.model.DetailCart;

public interface IOnShowDetailCart {
    void onShowDetailCart(DetailCart detailCart, OrderCartAdapter.OrderCartViewHolder holder);
    void onShowDetailCart(DetailCart detailCart, MyOrderListFoodAdapter.ListFoodOrderViewHolder holder);
}
