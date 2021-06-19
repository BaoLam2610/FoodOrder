package com.example.foodorderapp.detail;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.ListFoodAdapter;
import com.example.foodorderapp.databinding.FragmentListFoodBinding;
import com.example.foodorderapp.event.ICartDatabase;
import com.example.foodorderapp.event.IListFood;
import com.example.foodorderapp.event.IOnClickAddCart;
import com.example.foodorderapp.event.IOnListFood;
import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.DetailCart;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.model.Restaurant;
import com.example.foodorderapp.presenter.CartDatabasePresenter;
import com.example.foodorderapp.presenter.DetailPresenter;
import com.example.foodorderapp.presenter.ListFoodPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ListFoodFragment extends Fragment implements IOnListFood, IListFood, ICartDatabase {

    FragmentListFoodBinding binding;
    DetailPresenter presenter;
    List<Food> foods;
    ListFoodAdapter foodAdapter;
    ListFoodPresenter foodPresenter;
    CartDatabasePresenter cartPresenter;
    List<Restaurant> restaurants;

    Random rd = new Random();
    Cart currentCart;

    public static ListFoodFragment newInstance(String title) {

        Bundle args = new Bundle();
        args.putString("title", title);
//        args.putString("type", type);
//        args.putParcelableArrayList("list_food", (ArrayList<? extends Parcelable>) foodList);
        ListFoodFragment fragment = new ListFoodFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public String getTitle() {
        Bundle args = getArguments();
        return args.getString("title", "NO TITLE FOUND");
    }
    public String getType(){
        Bundle args = getArguments();
        return args.getString("type", "NO TITLE FOUND");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_food, container, false);
//        cartPresenter = new CartDatabasePresenter((ICartDatabase) this, getContext());
//        presenter = new DetailPresenter(this, getContext(), cartPresenter);

        foodPresenter = new ListFoodPresenter(this,getContext());
//        foodPresenter.showListFood();
        presenter = new DetailPresenter(this,this,this, getContext());
//        foodList = presenter.getListFood();
//        presenter.showListFood(getFoodList(),getTitle());
        String str = getType();
        System.out.println(str);
        presenter.showListFood(getTitle(),getType()); // title : stater food , fast food,...
            // type : default, favorite
//        presenter.showListFood();

//        presenter.showListFood(foodList, getTitle());


        return binding.getRoot();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.POSTING)
    public void getCart(Cart cart) {
        currentCart = cart;
    }

    @Override
    public void onShowListFood(List<Food> foods) {
        foodAdapter = new ListFoodAdapter(foods, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.rvListFood.setAdapter(foodAdapter);
        binding.rvListFood.setLayoutManager(layoutManager);
        foodAdapter.notifyDataSetChanged();

        foodAdapter.setIOnClickAddCart(new IOnClickAddCart() {
            @Override
            public void onClickAddCart(Food food) {
                presenter.updateFoodOrder(food, currentCart, "add");
//                EventBus.getDefault().postSticky(currentCart);
            }

            @Override
            public void onClickPlus(Food food) {
                presenter.updateFoodOrder(food, currentCart, "plus");
//                EventBus.getDefault().postSticky(currentCart);
            }

            @Override
            public void onClickMinus(Food food) {
                presenter.updateFoodOrder(food, currentCart, "minus");
//                EventBus.getDefault().postSticky(currentCart);
            }
        });

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getListFoodUpdate(List<Food> listDelete) {
//        foods = getFoodList();
        boolean check;
        for (int i = 0; i < listDelete.size(); i++) {
            check = foods.contains(listDelete.get(i));
            if (check) {
                int a = foods.indexOf(listDelete.get(i));
                foods.get(a).setCount(0);
            }
        }
        foodAdapter.notifyDataSetChanged();
        EventBus.getDefault().removeStickyEvent(listDelete);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onShowCart(Cart cart) {
//        EventBus.getDefault().postSticky(foodList);
    }


    @Override
    public void onListFood(Restaurant restaurant,List<Restaurant> restaurantList) {

//        restaurants = restaurantList;
        foods = restaurant.getFoodList();
//        foods = foodList;
//        EventBus.getDefault().postSticky(restaurantList);
    }
}
