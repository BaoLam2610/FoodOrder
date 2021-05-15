package com.example.foodorderapp.ui.home.menu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 1:
                return OrdersFragment.newInstance();
            case 2:
                return FavoritesFragment.newInstance();
            default:
                return HomeListFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
