package com.example.foodorderapp.ui.home.menu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.foodorderapp.map.ViewAllRestaurantFragment;
import com.example.foodorderapp.map.ViewMapFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        int pos = position - 1;
        switch (position) {
            case 0:
                return HomeListFragment.newInstance();
            case 1:
                return OrdersFragment.newInstance();
            case 2:
                return FavoritesFragment.newInstance();
            case 3:
                return ViewAllRestaurantFragment.newInstance();
            default:
                return HomeListFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }


}
