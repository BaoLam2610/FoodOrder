package com.example.foodorderapp.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.foodorderapp.tablayout.LoginFragment;
import com.example.foodorderapp.tablayout.SignUpFragment;

public class TabLoginAdapter extends FragmentStatePagerAdapter {

    Context context;

    public TabLoginAdapter(@NonNull FragmentManager fm, int behavior, Context context) {
        super(fm, behavior);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                LoginFragment loginFragment = null;
                if(loginFragment == null)
                    loginFragment = LoginFragment.newInstance();
                return loginFragment;
            case 1:
                SignUpFragment signUpFragment = null;
                if(signUpFragment == null)
                    signUpFragment = SignUpFragment.newInstance();
                return signUpFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";

        switch (position){
            case 0:
                title = "Login";
                break;
            case 1:
                title = "Sign In";
                break;
        }

        return title;
    }
}
