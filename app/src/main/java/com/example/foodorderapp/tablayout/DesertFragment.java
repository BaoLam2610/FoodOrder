package com.example.foodorderapp.tablayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.example.foodorderapp.event.IOnClickAddCart;
import com.example.foodorderapp.event.IOnListFood;
import com.example.foodorderapp.helper.IHelper;
import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.presenter.CartDatabasePresenter;
import com.example.foodorderapp.presenter.DetailPresenter;
import com.example.foodorderapp.sql.CartDatabaseHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Random;

public class DesertFragment extends Fragment implements IOnListFood,ICartDatabase {

    FragmentListFoodBinding binding;
    DetailPresenter presenter;
    List<Food> foodList;
    ListFoodAdapter foodAdapter;
    CartDatabasePresenter cartPresenter;
    CartDatabaseHelper helper;
    Random rd = new Random();

    Cart currentCart;

    public static DesertFragment newInstance() {

        Bundle args = new Bundle();

        DesertFragment fragment = new DesertFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //    public String getTitle() {
//        Bundle args = getArguments();
//        return (String) args.getCharSequence("title", "NO TITLE FOUND");
//    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_food, container, false);
//

        presenter = new DetailPresenter(this, getContext());
        presenter.showListFood(IHelper.TAB_DESERT);
        cartPresenter = new CartDatabasePresenter((ICartDatabase) this, getContext());

        return binding.getRoot();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.POSTING)
    public void getCart(Cart cart) {
        currentCart = cart;
    }

    @Override
    public void onShowListFood(List<Food> desertList) {
        foodList = desertList;
        foodAdapter = new ListFoodAdapter(foodList, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.rvListFood.setAdapter(foodAdapter);
        binding.rvListFood.setLayoutManager(layoutManager);
        foodAdapter.notifyDataSetChanged();


        foodAdapter.setIOnClickAddCart(new IOnClickAddCart() {
            @Override
            public void onClickAddCart(Food food) {
                cartPresenter.saveFoodOnCart(food);
                currentCart.setAmount(currentCart.getAmount()+1);
                currentCart.setTotalPrice(currentCart.getTotalPrice()+food.getPrice());
                EventBus.getDefault().postSticky(currentCart);
            }

            @Override
            public void onClickPlus(Food food) {

                currentCart.setTotalPrice(currentCart.getTotalPrice()+food.getPrice());
                cartPresenter.editFood(food);
                cartPresenter.editCart(currentCart);
                EventBus.getDefault().postSticky(currentCart);

            }

            @Override
            public void onClickMinus(Food food) {
                if (food.getCount() == 0) {
                    cartPresenter.destroyFood(food, currentCart);
                    currentCart.setAmount(currentCart.getAmount()-1);
                }
                currentCart.setTotalPrice(currentCart.getTotalPrice()-food.getPrice());

                cartPresenter.editFood(food);
                cartPresenter.editCart(currentCart);
                EventBus.getDefault().postSticky(currentCart);

            }
        });

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getListFoodUpdate(Food food) {
        boolean check = foodList.contains(food);
        if (check) {
            int i = foodList.indexOf(food);
            foodList.get(i).setCount(0);
            foodAdapter.notifyDataSetChanged();
        }

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

    }
}
