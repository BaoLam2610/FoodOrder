package com.example.foodorderapp.ui.home.menu;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodorderapp.MainActivity;
import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.MyOrderAdapter;
import com.example.foodorderapp.databinding.FragmentOrdersBinding;
import com.example.foodorderapp.event.IMyOrder;
import com.example.foodorderapp.event.ISeeDetailCart;
import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.Restaurant;
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
        binding.rvMyOrder.setVisibility(View.VISIBLE);
        binding.layoutEmptyOrder.setVisibility(View.INVISIBLE);

        orderAdapter = new MyOrderAdapter(cartList,getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
        binding.rvMyOrder.setAdapter(orderAdapter);
        binding.rvMyOrder.setLayoutManager(layoutManager);

        orderAdapter.setISeeDetailCart(new ISeeDetailCart() {
            @Override
            public void onShowDetailCart(Cart cart) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_detail_cart);

                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                WindowManager.LayoutParams windowAttributes = window.getAttributes();
                windowAttributes.gravity = Gravity.CENTER;
                window.setAttributes(windowAttributes);

                ImageView btnClose = dialog.findViewById(R.id.btnClose);
                TextView tvIdCart = dialog.findViewById(R.id.tvIdCart);
                TextView tvUsername = dialog.findViewById(R.id.tvUsername);
                TextView tvPhone = dialog.findViewById(R.id.tvPhone);
                TextView tvAddress = dialog.findViewById(R.id.tvAddress);
                TextView tvDateOrder = dialog.findViewById(R.id.tvDateOrder);
                TextView tvOrderNote = dialog.findViewById(R.id.tvNote);

                ImageView ivResImage = dialog.findViewById(R.id.ivResImage);
                TextView tvResName = dialog.findViewById(R.id.tvResName);
                TextView tvResRate = dialog.findViewById(R.id.tvResRate);
                AppCompatRatingBar rbResRate = dialog.findViewById(R.id.rbResRate);

                Restaurant restaurant = cart.getRestaurant();

                Glide.with(getContext()).load(restaurant.getImage())
                        .centerCrop()   // căn ảnh
//                    .placeholder(R.drawable.ic_baseline_image_24)  // đợi load ảnh
//                    .error(R.drawable.ic_baseline_error_24)        // load ảnh bị lỗi
                        .into(ivResImage);
                tvResName.setText(restaurant.getName());
                rbResRate.setRating((float) restaurant.getRate());
                tvResRate.setText(restaurant.getRate()+"");

                tvIdCart.setText("ID - " + cart.getIdCart());

                tvUsername.setText(getContext().getString(R.string.my_order_customer) + " " + cart.getUser().getUsername());
                tvPhone.setText(getContext().getString(R.string.my_order_phone) + " "  + cart.getUser().getPhone());
                tvAddress.setText(getContext().getString(R.string.my_order_receiving_address) + " "  + cart.getUser().getAddress());

                tvDateOrder.setText(getContext().getString(R.string.my_order_date) + " "  + cart.getDate());
                if(!cart.getNote().equals(""))
                    tvOrderNote.setText(getContext().getString(R.string.my_order_note) + " " + cart.getNote());
                else
                    tvOrderNote.setText(getContext().getString(R.string.my_order_empty_note));

                btnClose.setOnClickListener(view -> {
                    dialog.dismiss();
                });
                dialog.show();
            }
        });
    }

    @Override
    public void onEmptyListOrder() {
        binding.rvMyOrder.setVisibility(View.INVISIBLE);
        binding.layoutEmptyOrder.setVisibility(View.VISIBLE);
    }
}
