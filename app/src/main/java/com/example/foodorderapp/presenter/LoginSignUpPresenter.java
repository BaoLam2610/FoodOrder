package com.example.foodorderapp.presenter;

import android.content.Context;

import com.example.foodorderapp.event.ICheckLogin;
import com.example.foodorderapp.event.ILogin;
import com.example.foodorderapp.event.IShowAccountInf;
import com.example.foodorderapp.event.ISignUp;
import com.example.foodorderapp.model.UserAccount;
import com.example.foodorderapp.sql.CartDatabaseHelper;

public class LoginSignUpPresenter {
    final int EXISTS_PHONE = 1;
    final int NOT_EXISTS_PHONE = 0;
    ILogin iLogin;
    ISignUp iSignUp;
    ICheckLogin iCheckLogin;
    IShowAccountInf iShowAccountInf;
    Context context;
    CartDatabaseHelper helper;

    public LoginSignUpPresenter(ILogin iLogin, Context context) {
        this.iLogin = iLogin;
        this.context = context;
    }

    public LoginSignUpPresenter(ISignUp iSignUp, Context context) {
        this.iSignUp = iSignUp;
        this.context = context;
    }

    public LoginSignUpPresenter(ICheckLogin iCheckLogin, IShowAccountInf iShowAccountInf, Context context) {
        this.iCheckLogin = iCheckLogin;
        this.iShowAccountInf = iShowAccountInf;
        this.context = context;
    }

    public void checkLogin(String phone) {
        if (phone.isEmpty()) {
            iLogin.onFailure("Phone can not be empty");
            return;
        }
        if (phone.length() < 10 || phone.length() > 11) {
            iLogin.onFailure("Length of Phone must be 10 or 11 number");
            return;
        }
        if (helper == null)
            helper = new CartDatabaseHelper(context);
        if (helper.findAccount(phone)) { // if exists -> true -> login form, else false -> sign up
            iLogin.onSuccessful(EXISTS_PHONE);
        } else {
            iLogin.onSuccessful(NOT_EXISTS_PHONE);
        }
    }

    public void checkSignUp(String phone, String username, String pass1, String pass2) {
        if (username.isEmpty()) {
            iSignUp.onFailure("Username can not be empty");
            return;
        }
        if (pass1.isEmpty()) {
            iSignUp.onFailure("Password can not be empty");
            return;
        }
        if (!pass1.equals(pass2)) {
            iSignUp.onFailure("Confirm password not same password");
            return;
        }
        if (helper == null)
            helper = new CartDatabaseHelper(context);
        UserAccount userAccount = new UserAccount(phone, username, pass1, null, null, 1);
        helper.insertAccount(userAccount);
        iSignUp.onSuccessful();
    }

    public void checkLogin(String phone, String password){
        if (helper == null)
            helper = new CartDatabaseHelper(context);
        if(helper.checkAccount(phone, password)) {
            helper.setStatusAccount(phone);
            iLogin.onSuccessful(0); // type in here is not thing
        }else
            iLogin.onFailure("Phone or password is wrong. Or account is not exists");
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
        if(user != null){
            iShowAccountInf.onExistsAccount(user);
        } else
            iShowAccountInf.onNotExistsAccount();

    }
}
