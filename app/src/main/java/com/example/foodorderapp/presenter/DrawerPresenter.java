package com.example.foodorderapp.presenter;

import android.content.Context;

import com.example.foodorderapp.event.ICheckLogin;
import com.example.foodorderapp.event.IShowAccountInf;
import com.example.foodorderapp.model.UserAccount;
import com.example.foodorderapp.sql.CartDatabaseHelper;

import java.util.List;

public class DrawerPresenter {
    ICheckLogin iCheckLogin;
    IShowAccountInf iShowAccountInf;
    CartDatabaseHelper helper;
    Context context;

    public DrawerPresenter(ICheckLogin iCheckLogin,IShowAccountInf iShowAccountInf, Context context) {
        this.iCheckLogin = iCheckLogin;
        this.iShowAccountInf = iShowAccountInf;
        this.context = context;
    }

    public DrawerPresenter(ICheckLogin iCheckLogin, Context context) {
        this.iCheckLogin = iCheckLogin;
        this.context = context;
    }

    public void logoutAccount(){
        if(helper==null)
            helper = new CartDatabaseHelper(context);
        UserAccount user = helper.checkStatusAccount();
        if(user != null) {
            user.setStatus(0);
            helper.updateAccount(user);
            iCheckLogin.onExists("Logout success");
        }
        else{
            iCheckLogin.onNotExists("Account not exists");
        }

    }

    public void showAccountInformation(){

        if(helper==null)
            helper = new CartDatabaseHelper(context);
        UserAccount user = helper.checkStatusAccount();
        if(user == null){
           iShowAccountInf.onExistsAccount(user);
        } else
            iShowAccountInf.onNotExistsAccount();

    }
}
