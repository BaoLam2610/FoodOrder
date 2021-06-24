package com.example.foodorderapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.foodorderapp.MainActivity;
import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.ListGroupAdapter;
import com.example.foodorderapp.databinding.FragmentHomeBinding;
import com.example.foodorderapp.model.FoodCategory;
import com.example.foodorderapp.model.GroupList;
import com.example.foodorderapp.ui.home.menu.ViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    ViewPagerAdapter viewPagerAdapter;

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);
        getViewPager();
        binding.bottomMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.mnHomeList:
                        binding.vpFragment.setCurrentItem(0);
                        break;
                    case R.id.mnOrders:
                        binding.vpFragment.setCurrentItem(1);
                        break;
                    case R.id.mnFavorites:
                        binding.vpFragment.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });
        return binding.getRoot();
    }

    public void getViewPager(){
        viewPagerAdapter = new ViewPagerAdapter(getParentFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        binding.vpFragment.setAdapter(viewPagerAdapter);
//        binding.vpFragment.setCurrentItem(binding.vpFragment.getCurrentItem()-1);

        int i = binding.vpFragment.getCurrentItem();
        System.out.println(i);
        binding.vpFragment.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getContext().getString(R.string.action_bar_home));
                        binding.bottomMenu.getMenu().findItem(R.id.mnHomeList).setChecked(true);
                        break;
                    case 1:
                        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getContext().getString(R.string.action_bar_my_order));
                        binding.bottomMenu.getMenu().findItem(R.id.mnOrders).setChecked(true);
                        break;
                    case 2:
                        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getContext().getString(R.string.action_bar_favorites));
                        binding.bottomMenu.getMenu().findItem(R.id.mnFavorites).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

}