package com.example.foodorderapp.detail;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.MyOrderListFoodAdapter;
import com.example.foodorderapp.databinding.FragmentOrderInformationBinding;
import com.example.foodorderapp.event.IDetailRestaurant;
import com.example.foodorderapp.event.IOrderCart;
import com.example.foodorderapp.helper.FormatHelper;
import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.model.Restaurant;
import com.example.foodorderapp.model.Voucher;
import com.example.foodorderapp.presenter.OrderInformationPresenter;

import java.util.List;

public class OrderInformationFragment extends Fragment implements IOrderCart, IDetailRestaurant {
    FragmentOrderInformationBinding binding;
    OrderInformationPresenter presenter;
    MyOrderListFoodAdapter foodAdapter;

    public static OrderInformationFragment newInstance(Cart cart) {

        Bundle args = new Bundle();
        args.putSerializable("cart", cart);
        OrderInformationFragment fragment = new OrderInformationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public Cart getCart() {
        Bundle args = getArguments();
        return (Cart) args.getSerializable("cart");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_information, container, false);
        setHasOptionsMenu(true);
        presenter = new OrderInformationPresenter(this, this, getContext());
        presenter.showDetailOrder(getCart());


        return binding.getRoot();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            default:
                getParentFragmentManager().popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onShowListFoodOrder(Cart cart, List<Food> foodList) {
        foodAdapter = new MyOrderListFoodAdapter(foodList, cart, getContext(), "order_inf");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.rvFoodOrder.setAdapter(foodAdapter);
        binding.rvFoodOrder.setLayoutManager(layoutManager);

        // note
        binding.tvOrderNote.setText(cart.getNote());


        // price
        if (cart.getVoucher() == null)
            binding.tvAmountToPay.setText(FormatHelper.formatPrice(cart.getTotalPrice()));
        binding.tvSubTotal.setText(FormatHelper.formatPrice(cart.getTotalPrice()));
    }

    @Override
    public void onEmptyListFoodOrder() {

    }

    @Override
    public void onShowVoucher(Voucher voucher, Cart cart) {
        binding.tvSubTotal.setText(FormatHelper.formatPrice(cart.getTotalPrice()));
        long price = cart.getTotalPrice();
        long discount = (long) ((voucher.getDiscount() / 100.0f) * price);
        binding.tvDiscount.setText("-" + FormatHelper.formatPrice(discount));
        binding.tvAmountToPay.setText(FormatHelper.formatPrice((long) (price - discount)));
    }

    @Override
    public void onShowDetailRestaurant(Restaurant restaurant) {
        binding.tvResName.setText("Restaurant's name: " + restaurant.getName());
    }
}
