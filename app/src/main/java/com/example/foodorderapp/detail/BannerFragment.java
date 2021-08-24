package com.example.foodorderapp.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.DetailActivity;
import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.RestaurantBannerAdapter;
import com.example.foodorderapp.databinding.FragmentBannerBinding;
import com.example.foodorderapp.event.IBanner;
import com.example.foodorderapp.model.Restaurant;
import com.example.foodorderapp.presenter.BannerPresenter;

import java.util.List;

public class BannerFragment extends Fragment implements IBanner {
    FragmentBannerBinding binding;
    BannerPresenter presenter;
    RestaurantBannerAdapter bannerAdapter;

    public static BannerFragment newInstance() {

        Bundle args = new Bundle();

        BannerFragment fragment = new BannerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_banner, container, false);
        setTitleActionBar();
        setHasOptionsMenu(true);
        presenter = new BannerPresenter(this, getContext());
        presenter.showListBanner();
        return binding.getRoot();
    }

    public void setTitleActionBar() {
        ((DetailActivity) getActivity()).getSupportActionBar().setTitle(getContext().getResources().getString(R.string.restaurant_banner_title));
        ((DetailActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                getActivity().finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onShowListBanner(List<Restaurant> restaurantBanner) {
        bannerAdapter = new RestaurantBannerAdapter(restaurantBanner, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.rvRestaurantBanner.setAdapter(bannerAdapter);
        binding.rvRestaurantBanner.setLayoutManager(layoutManager);
    }
}
