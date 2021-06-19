package com.example.foodorderapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodorderapp.R;
import com.example.foodorderapp.databinding.ItemMyOrderBinding;
import com.example.foodorderapp.helper.FormatHelper;
import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.Food;

import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyOrderViewHolder> {

    List<Cart> cartList;
    List<Food> foodList;
    Context context;
    MyOrderListFoodAdapter listFoodAdapter;

    public MyOrderAdapter(List<Cart> cartList,List<Food> foodList, Context context) {
        this.cartList = cartList;
        this.foodList = foodList;
        this.context = context;
        notifyDataSetChanged();
    }

    public MyOrderAdapter(List<Cart> cartList, Context context) {
        this.cartList = cartList;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemMyOrderBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_my_order,parent,false);
        MyOrderViewHolder viewHolder = new MyOrderViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderViewHolder holder, int position) {
        Cart cart = cartList.get(position);

        Glide.with(context).load(cart.getRestaurant().getImage())
                .centerCrop()   // căn ảnh
//                    .placeholder(R.drawable.ic_baseline_image_24)  // đợi load ảnh
//                    .error(R.drawable.ic_baseline_error_24)        // load ảnh bị lỗi
                .into(holder.binding.ivResImage);
        holder.binding.tvResName.setText(cart.getRestaurant().getName());
        holder.binding.tvResAddress.setText(cart.getRestaurant().getAddress());

        System.out.println(cart.getDate());
        holder.binding.tvDateOrder.setText(cart.getDate());
        holder.binding.tvTotalPrice.setText(context.getResources().getString(R.string.my_order_total_price) + " " + FormatHelper.formatPrice(cart.getTotalPrice()));

        List<Food> foodList = cart.getRestaurant().getFoodList();
        listFoodAdapter = new MyOrderListFoodAdapter(foodList,cart,context,"my_order");
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
        holder.binding.rvFoodOrder.setAdapter(listFoodAdapter);
        holder.binding.rvFoodOrder.setLayoutManager(layoutManager);
    }

    @Override
    public int getItemCount() {
        return cartList.isEmpty() ? 0 : cartList.size();
    }

    public class MyOrderViewHolder extends RecyclerView.ViewHolder {
        ItemMyOrderBinding binding;
        public MyOrderViewHolder(@NonNull ItemMyOrderBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
