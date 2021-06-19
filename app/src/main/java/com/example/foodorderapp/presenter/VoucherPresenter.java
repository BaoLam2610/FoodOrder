package com.example.foodorderapp.presenter;

import android.content.Context;

import com.example.foodorderapp.R;
import com.example.foodorderapp.event.IOrderCart;
import com.example.foodorderapp.event.IVoucher;
import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.Voucher;
import com.example.foodorderapp.sql.CartDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class VoucherPresenter {
    IVoucher iVoucher;
    IOrderCart iOrderCart;
    CartDatabaseHelper helper;
    Context context;
    List<Voucher> voucherList;

    public VoucherPresenter(IVoucher iVoucher, Context context) {
        this.iVoucher = iVoucher;
        this.context = context;
    }


    public void showListVoucher(Cart cart){
        List<Voucher> voucherList = getVoucherList();
        iVoucher.onShowListVoucher(voucherList,cart);
    }

    public List<Voucher> getVoucherList(){
        List<Voucher> voucherList = new ArrayList<>();
        Voucher voucher1 = new Voucher("MH0001","Discount 30%", R.drawable.voucher_1,30);
        Voucher voucher2 = new Voucher("MH0002","Discount 50%", R.drawable.voucher_2,50);
        Voucher voucher3 = new Voucher("MH0003","Discount 30%", R.drawable.voucher_3,30);
        Voucher voucher4 = new Voucher("MH0004","Discount 50%", R.drawable.voucher_4,50);
        voucherList.add(voucher1);
        voucherList.add(voucher2);
        voucherList.add(voucher3);
        voucherList.add(voucher4);
        if(helper == null)
            helper = new CartDatabaseHelper(context);
        for (int i = 0; i < voucherList.size(); i++) {
            if(!helper.findVoucher(voucherList.get(i)))
                helper.insertVoucher(voucherList.get(i));
        }
        return  voucherList;
    }


}
