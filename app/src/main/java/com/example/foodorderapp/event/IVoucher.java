package com.example.foodorderapp.event;

import com.example.foodorderapp.adapter.MyOrderAdapter;
import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.Voucher;

import java.util.List;

public interface IVoucher {
    void onShowListVoucher(List<Voucher> voucherList, Cart cart);
    void onShowVoucherOrder(MyOrderAdapter.MyOrderViewHolder holder, Voucher voucher, Cart cart);
    void onEmptyVoucherOrder(MyOrderAdapter.MyOrderViewHolder holder,Cart cart);
}
