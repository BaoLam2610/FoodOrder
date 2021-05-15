package com.example.foodorderapp.presenter;

import android.content.Context;

import com.example.foodorderapp.event.ICheckLogin;

public class DrawerPresenter {
    ICheckLogin iCheckLogin;
    Context context;

    public DrawerPresenter(ICheckLogin iCheckLogin, Context context) {
        this.iCheckLogin = iCheckLogin;
        this.context = context;
    }

//    public void updateAccountInf(){
//        iCheckLogin.onExists();
//    }
//    public void
}
