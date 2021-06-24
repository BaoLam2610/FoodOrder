package com.example.foodorderapp.presenter;

import android.content.Context;

import com.example.foodorderapp.R;
import com.example.foodorderapp.event.ICheck;
import com.example.foodorderapp.event.ICheckLogin;
import com.example.foodorderapp.event.ILogin;
import com.example.foodorderapp.event.IShowAccountInf;
import com.example.foodorderapp.event.ISignUp;
import com.example.foodorderapp.login.SignUpFragment;
import com.example.foodorderapp.model.UserAccount;
import com.example.foodorderapp.sql.CartDatabaseHelper;

public class LoginSignUpPresenter {
    final int EXISTS_PHONE = 1;
    final int NOT_EXISTS_PHONE = 0;
    ILogin iLogin;
    ISignUp iSignUp;
    ICheckLogin iCheckLogin;
    IShowAccountInf iShowAccountInf;
    ICheck iCheck;
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

    public LoginSignUpPresenter(IShowAccountInf iShowAccountInf, Context context) {
        this.iShowAccountInf = iShowAccountInf;
        this.context = context;
    }

    public LoginSignUpPresenter(ISignUp iSignUp, ICheck iCheck,Context context) {
        this.iSignUp = iSignUp;
        this.iCheck = iCheck;
        this.context = context;
    }

    public void checkLogin(String phone) {
        if (phone.isEmpty()) {
            iLogin.onFailure(context.getResources().getString(R.string.error_phone_empty));
            return;
        }
        if (phone.length() < 10 || phone.length() > 11) {
            iLogin.onFailure(context.getResources().getString(R.string.error_phone_length));
            return;
        }
        if (helper == null)
            helper = new CartDatabaseHelper(context);
        if (helper.findAccount(phone)) { // if exists -> true -> login form, else false -> sign up
            UserAccount user = helper.getAccount(phone);
            iLogin.onSuccessful(EXISTS_PHONE,user);
        } else {
            iLogin.onSuccessful(NOT_EXISTS_PHONE,null);
        }
    }

    public void checkInput(int type, String phone, String name, String pass1, String pass2){
        if (helper == null)
            helper = new CartDatabaseHelper(context);
        switch (type){
            case 0:
                if (phone.isEmpty()) {
                    iCheck.onFailureCheck(SignUpFragment.TYPE_PHONE,context.getResources().getString(R.string.error_phone_empty));
                    return;
                }
                if (phone.length() < 10 || phone.length() > 11) {
                    iCheck.onFailureCheck(SignUpFragment.TYPE_PHONE,context.getResources().getString(R.string.error_phone_length));
                    return;
                }
                if(helper.findAccount(phone)){
                    iCheck.onFailureCheck(SignUpFragment.TYPE_PHONE,context.getResources().getString(R.string.error_phone_exists));
                    return;
                }
                iCheck.onCorrectCheck(SignUpFragment.TYPE_PHONE);
                break;
            case 1:
                if (name.isEmpty()) {
                    iCheck.onFailureCheck(SignUpFragment.TYPE_FULL_NAME,context.getResources().getString(R.string.error_username_empty));
                    return;
                }
                iCheck.onCorrectCheck(SignUpFragment.TYPE_FULL_NAME);
                break;
            case 2:
                if (pass1.isEmpty()) {
                    iCheck.onFailureCheck(SignUpFragment.TYPE_PASSWORD,context.getResources().getString(R.string.error_password_empty));
                    return;
                }
                iCheck.onCorrectCheck(SignUpFragment.TYPE_PASSWORD);
            case 3:
                if (!pass1.equals(pass2)) {
                    iCheck.onFailureCheck(SignUpFragment.TYPE_CONFIRM_PASSWORD,context.getResources().getString(R.string.error_confirm_pass));
                    return;
                }
                if(pass1.isEmpty())
                    iCheck.onCorrectCheck(SignUpFragment.TYPE_CONFIRM_PASSWORD);
                iCheck.onCorrectCheck(SignUpFragment.TYPE_CONFIRM_PASSWORD);
        }

    }

    public void checkFullName(String name){
        if (name.isEmpty()) {
            iSignUp.onFailure(context.getResources().getString(R.string.error_username_empty));
            return;
        }
    }

    public void checkSignUp(String phone, String username, String pass1, String pass2) {
        if (helper == null)
            helper = new CartDatabaseHelper(context);
        if (phone.isEmpty()) {
            iSignUp.onFailure(context.getResources().getString(R.string.error_phone_empty));
            return;
        }
        if (phone.length() < 10 || phone.length() > 11) {
            iSignUp.onFailure(context.getResources().getString(R.string.error_phone_length));
            return;
        }
        if(helper.findAccount(phone)){
            iSignUp.onFailure(context.getResources().getString(R.string.error_phone_exists));
            return;
        }
        if (username.isEmpty()) {
            iSignUp.onFailure(context.getResources().getString(R.string.error_username_empty));
            return;
        }
        if (pass1.isEmpty()) {
            iSignUp.onFailure(context.getResources().getString(R.string.error_password_empty));
            return;
        }
        if (!pass1.equals(pass2)) {
            iSignUp.onFailure(context.getResources().getString(R.string.error_confirm_pass));
            return;
        }
        UserAccount userAccount = new UserAccount(phone, username, pass1, null, null, 1);
        helper.insertAccount(userAccount);
        iSignUp.onSuccessful(userAccount);
    }

    public void checkLogin(String phone, String password){
        if (helper == null)
            helper = new CartDatabaseHelper(context);
        if(helper.checkAccount(phone, password)) {
            helper.setStatusAccount(phone);
            UserAccount user = helper.getAccount(phone);
            iLogin.onSuccessful(0,user);
        }else
            iLogin.onFailure(context.getResources().getString(R.string.error_login));
    }

    public void logoutAccount(){
        if(helper==null)
            helper = new CartDatabaseHelper(context);
        UserAccount user = helper.checkStatusAccount();
        if(user != null) {
            helper.logoutAccount(user);
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
        } else {
            iShowAccountInf.onNotExistsAccount();
        }

    }
}
