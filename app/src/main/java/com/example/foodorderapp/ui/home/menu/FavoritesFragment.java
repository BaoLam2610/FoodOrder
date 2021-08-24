package com.example.foodorderapp.ui.home.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.DetailActivity;
import com.example.foodorderapp.MainActivity;
import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.ListRestaurantAdapter;
import com.example.foodorderapp.databinding.FragmentFavoritesBinding;
import com.example.foodorderapp.databinding.FragmentOrdersBinding;
import com.example.foodorderapp.event.ICartDatabase;
import com.example.foodorderapp.event.IOnClickItemRestaurant;
import com.example.foodorderapp.event.IOnShowListFavoriteRestaurant;
import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.Restaurant;
import com.example.foodorderapp.presenter.CartDatabasePresenter;
import com.example.foodorderapp.presenter.FavoriteRestaurantPresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class FavoritesFragment extends Fragment implements IOnShowListFavoriteRestaurant, ICartDatabase {
    FragmentFavoritesBinding binding;
    FavoriteRestaurantPresenter favoritePresenter;
    ListRestaurantAdapter restaurantAdapter;
    CartDatabasePresenter cartPresenter;

    public static FavoritesFragment newInstance() {
        
        Bundle args = new Bundle();
        
        FavoritesFragment fragment = new FavoritesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites,container,false);
//        setTitleActionBar();
//        ((MainActivity)getActivity()).setTileActionBar(getString(R.string.action_bar_favorites));
        cartPresenter = new CartDatabasePresenter(this, getContext());
        favoritePresenter = new FavoriteRestaurantPresenter(this,getContext());
        favoritePresenter.showListFavoriteRestaurant();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        favoritePresenter.showListFavoriteRestaurant();
    }

    @Override
    public void onShowListFavorite(List<Restaurant> restaurantList) {
        binding.rvFavorRestaurant.setVisibility(View.VISIBLE);
        binding.layoutEmptyFavorite.setVisibility(View.INVISIBLE);

        restaurantAdapter = new ListRestaurantAdapter(restaurantList,getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
        binding.rvFavorRestaurant.setAdapter(restaurantAdapter);
        binding.rvFavorRestaurant.setLayoutManager(layoutManager);
        restaurantAdapter.setClickItemRestaurant(new IOnClickItemRestaurant() {
            @Override
            public void onClickItem(Restaurant restaurant) {
                Intent it = new Intent(getContext(), DetailActivity.class);
                it.putExtra("favorite", "item");
                EventBus.getDefault().postSticky(restaurant); // dùng luôn sql
                cartPresenter.saveRestaurantOnCart(restaurant);
                Cart currentCart = new Cart(restaurant, 0, 0);
                cartPresenter.saveCart(currentCart);
                EventBus.getDefault().postSticky(currentCart);
                getContext().startActivity(it);
            }
        });
    }

    public void setTitleActionBar(){
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getContext().getString(R.string.action_bar_favorites));
    }

    @Override
    public void onEmptyListFavorite() {
        binding.rvFavorRestaurant.setVisibility(View.INVISIBLE);
        binding.layoutEmptyFavorite.setVisibility(View.VISIBLE);
    }

    @Override
    public void onShowCart(Cart cart) {

    }
}
