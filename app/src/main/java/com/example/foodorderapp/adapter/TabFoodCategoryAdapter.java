package com.example.foodorderapp.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.foodorderapp.R;
import com.example.foodorderapp.detail.ListFoodFragment;
import com.example.foodorderapp.model.Food;

import java.util.List;

public class TabFoodCategoryAdapter extends FragmentStatePagerAdapter {

    Context context;
    List<Food> foodList;
    String type;

    public TabFoodCategoryAdapter(@NonNull FragmentManager fm, int behavior, Context context) {
        super(fm, behavior);
        this.context = context;
        this.type = type;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ListFoodFragment.newInstance(context.getResources().getString(R.string.fast_food));
            case 1:
                return ListFoodFragment.newInstance(context.getResources().getString(R.string.starter));
            case 2:
                return ListFoodFragment.newInstance(context.getResources().getString(R.string.main_course));
            case 3:
                return ListFoodFragment.newInstance(context.getResources().getString(R.string.desert));
            case 4:
                return ListFoodFragment.newInstance(context.getResources().getString(R.string.drink));
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
                title = context.getResources().getString(R.string.fast_food);
                break;
            case 1:
                title = context.getResources().getString(R.string.starter);
                break;
            case 2:
                title = context.getResources().getString(R.string.main_course);
                break;
            case 3:
                title = context.getResources().getString(R.string.desert);
                break;
            case 4:
                title = context.getResources().getString(R.string.drink);
                break;
        }
        return title;
    }
}
