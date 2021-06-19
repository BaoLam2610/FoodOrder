package com.example.foodorderapp.event;

import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.Voucher;

import java.util.List;

public interface IVoucher {
    void onShowListVoucher(List<Voucher> voucherList, Cart cart);
}
