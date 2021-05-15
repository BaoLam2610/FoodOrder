package com.example.foodorderapp.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.foodorderapp.tablayout.DesertFragment;
import com.example.foodorderapp.tablayout.DrinkFragment;
import com.example.foodorderapp.tablayout.FastFoodFragment;
import com.example.foodorderapp.tablayout.MainCourseFoodFragment;
import com.example.foodorderapp.tablayout.StarterFoodFragment;

public class TabFoodCategoryAdapter extends FragmentStatePagerAdapter {

    Context context;


    public TabFoodCategoryAdapter(@NonNull FragmentManager fm, int behavior, Context context) {
        super(fm, behavior);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FastFoodFragment fastFoodFragment = null;
                if (fastFoodFragment == null)
                    fastFoodFragment = FastFoodFragment.newInstance();
                return fastFoodFragment;
            case 1:
                StarterFoodFragment starterFoodFragment = null;
                if (starterFoodFragment == null)
                    starterFoodFragment = StarterFoodFragment.newInstance();
                return starterFoodFragment;
            case 2:
                MainCourseFoodFragment mainCourseFoodFragment = null;
                if (mainCourseFoodFragment == null)
                    mainCourseFoodFragment = MainCourseFoodFragment.newInstance();
                return mainCourseFoodFragment;
            case 3:
                DesertFragment desertFragment = null;
                if (desertFragment == null)
                    desertFragment = DesertFragment.newInstance();
                return desertFragment;
            case 4:
                DrinkFragment drinkFragment = null;
                if (drinkFragment == null)
                    drinkFragment = DrinkFragment.newInstance();
                return drinkFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "Fast Food";
                break;
            case 1:
                title = "Starter";
                break;
            case 2:
                title = "Main Course";
                break;
            case 3:
                title = "Desert";
                break;
            case 4:
                title = "Drink";
                break;
        }
        return title;
    }
}
