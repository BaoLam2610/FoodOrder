package com.example.foodorderapp.ui.home.menu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

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
//                return HomeListFragment.newInstance();
                return OrdersFragment.newInstance();
            case 2:
//                return OrdersFragment.newInstance();
                return FavoritesFragment.newInstance();
            default:
                return HomeListFragment.newInstance();
        }
//        if(position == 0){
//            return HomeListFragment.newInstance();
//        } else if(position == 1){
//            return OrdersFragment.newInstance();
//        } else
//            return FavoritesFragment.newInstance();
    }

    @Override
    public int getCount() {
        return 3;
    }


}
