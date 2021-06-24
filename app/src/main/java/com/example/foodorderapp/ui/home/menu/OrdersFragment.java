package com.example.foodorderapp.ui.home.menu;

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

import com.example.foodorderapp.MainActivity;
import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.MyOrderAdapter;
import com.example.foodorderapp.databinding.FragmentOrdersBinding;
import com.example.foodorderapp.event.IMyOrder;
import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.presenter.MyOrderPresenter;

import java.util.List;

public class OrdersFragment extends Fragment implements IMyOrder {
    FragmentOrdersBinding binding;
    MyOrderPresenter presenter;
    MyOrderAdapter orderAdapter;

    public static OrdersFragment newInstance() {

        Bundle args = new Bundle();

        OrdersFragment fragment = new OrdersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_orders,container,false);
//        setTitleActionBar();
//        ((MainActivity)getActivity()).setTileActionBar(getString(R.string.action_bar_my_order));
        presenter = new MyOrderPresenter(this,getContext());
        presenter.showMyOrder();
        return binding.getRoot();
    }

    public void setTitleActionBar(){
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getContext().getString(R.string.action_bar_my_order));
    }

    @Override
    public void onResume() {
        presenter.showMyOrder();
        super.onResume();
    }

    @Override
    public void onShowListOrder(List<Cart> cartList) {
        orderAdapter = new MyOrderAdapter(cartList,getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
        binding.rvMyOrder.setAdapter(orderAdapter);
        binding.rvMyOrder.setLayoutManager(layoutManager);
    }

    @Override
    public void onEmptyListOrder() {

    }
}
