package com.example.foodorderapp.detail;

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
import com.example.foodorderapp.adapter.OrderCartAdapter;
import com.example.foodorderapp.databinding.FragmentOrderCartBinding;
import com.example.foodorderapp.event.ICartDatabase;
import com.example.foodorderapp.event.IOnClickDeleteOrder;
import com.example.foodorderapp.event.IOnListFood;
import com.example.foodorderapp.event.IOnShowCart;
import com.example.foodorderapp.event.IOrderCart;
import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.presenter.CartDatabasePresenter;
import com.example.foodorderapp.presenter.DetailPresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class ListOrderCartFragment extends Fragment implements IOrderCart {

    FragmentOrderCartBinding binding;
    OrderCartAdapter orderCartAdapter;
    CartDatabasePresenter presenter;

    public static ListOrderCartFragment newInstance() {

        Bundle args = new Bundle();

        ListOrderCartFragment fragment = new ListOrderCartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_cart, container, false);
        presenter = new CartDatabasePresenter(this,null,null,getContext());
//        presenter.showCart();
        presenter.showListOrder();
        return binding.getRoot();
    }


//    List<Food> foodList;


    @Override
    public void onShowListFoodOrder(Cart cart, List<Food> foodList) {
        orderCartAdapter = new OrderCartAdapter(foodList,getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.rvFoodOrderCart.setAdapter(orderCartAdapter);
        binding.rvFoodOrderCart.setLayoutManager(layoutManager);
        orderCartAdapter.setIOnClickDeleteOrder(new IOnClickDeleteOrder() {
            @Override
            public void onClickDeleteOrder(Food food) {
                foodList.remove(food);
                int amount = cart.getAmount()-1;
                long totalPrice = cart.getTotalPrice()- (food.getCount()*food.getPrice());
//
                presenter.destroyFood(food,cart);

                cart.setAmount(amount);
                cart.setTotalPrice(totalPrice);
                Toast.makeText(getContext(), food.getIdRes(), Toast.LENGTH_SHORT).show();
//                presenter.editCart(cart);


//                food.setCount(0);
                EventBus.getDefault().postSticky(food);
                EventBus.getDefault().postSticky(cart);
                orderCartAdapter.notifyDataSetChanged();


            }
        });
        binding.tvSubTotal.setText(cart.getTotalPrice()+"đ");
//        binding.tvCoupon.setText(cart.getTotalPrice()+"");
        binding.tvAmountToPay.setText(cart.getTotalPrice()+"đ");
    }
}
