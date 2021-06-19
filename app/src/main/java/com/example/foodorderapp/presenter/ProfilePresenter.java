package com.example.foodorderapp.presenter;

import android.content.Context;

import com.example.foodorderapp.R;
import com.example.foodorderapp.event.IOnProfile;
import com.example.foodorderapp.model.UserAccount;
import com.example.foodorderapp.sql.CartDatabaseHelper;

public class ProfilePresenter {
    IOnProfile iOnProfile;
    Context context;
    CartDatabaseHelper helper;

    public ProfilePresenter(IOnProfile iOnProfile, Context context) {
        this.iOnProfile = iOnProfile;
        this.context = context;
    }

    public void editProfile(UserAccount userAccount){
        if(helper == null)
            helper = new CartDatabaseHelper(context);

        if (userAccount.getPhone().isEmpty()) {
            iOnProfile.onFailure(context.getResources().getString(R.string.error_phone_empty));
            return;
        }
        if (userAccount.getPhone().length() < 10 || userAccount.getPhone().length() > 11) {
            iOnProfile.onFailure(context.getResources().getString(R.string.error_phone_length));
            return;
        }

        if (userAccount.getUsername().isEmpty()) {
            iOnProfile.onFailure(context.getResources().getString(R.string.error_username_empty));
            return;
        }
        iOnProfile.onSaveSuccessful();
        helper.updateAccount(userAccount);

    }
}
