package com.example.foodorderapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodorderapp.R;
import com.example.foodorderapp.databinding.ItemFoodOrderCartBinding;
import com.example.foodorderapp.event.ICartDatabase;
import com.example.foodorderapp.event.IOnClickDeleteOrder;
import com.example.foodorderapp.event.IOnShowDetailCart;
import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.DetailCart;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.presenter.CartDatabasePresenter;
import com.example.foodorderapp.sql.CartDatabaseHelper;

import java.text.DecimalFormat;
import java.util.List;

public class OrderCartAdapter extends RecyclerView.Adapter<OrderCartAdapter.OrderCartViewHolder>
implements IOnShowDetailCart {

    DecimalFormat df = new DecimalFormat("###,###");
    List<Food> foodList;
    Context context;
    CartDatabaseHelper helper;
    IOnClickDeleteOrder iOnClickDeleteOrder;
    CartDatabasePresenter presenter;
    Cart cart;

    public void setIOnClickDeleteOrder(IOnClickDeleteOrder iOnClickDeleteOrder) {
        this.iOnClickDeleteOrder = iOnClickDeleteOrder;
    }

    public OrderCartAdapter(List<Food> foodList,Cart cart, Context context) {
        this.foodList = foodList;
        this.context = context;
        this.cart = cart;
    }

    @NonNull
    @Override
    public OrderCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemFoodOrderCartBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_food_order_cart,parent, false);
        OrderCartViewHolder viewHolder = new OrderCartViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderCartViewHolder holder, int position) {
        Food food = foodList.get(position);
        holder.binding.tvFoodName.setText(food.getName());
        holder.binding.tvFoodCategory.setText(food.getCategory());
        holder.binding.tvFoodPrice.setText(df.format(food.getPrice()));
        presenter = new CartDatabasePresenter(this,context);
        presenter.showDetailCart(food,cart,holder);
        Glide.with(context).load(food.getImage())
                .centerCrop()   // căn ảnh
//                    .placeholder(R.drawable.ic_baseline_image_24)  // đợi load ảnh
//                    .error(R.drawable.ic_baseline_error_24)        // load ảnh bị lỗi
                .into(holder.binding.ivFoodImage);
        holder.binding.btnDeleteOrder.setOnClickListener(v-> iOnClickDeleteOrder.onClickDeleteOrder(food));
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    @Override
    public void onShowDetailCart(DetailCart detailCart, OrderCartViewHolder holder) {
        holder.binding.tvFoodCount.setText(context.getResources().getString(R.string.amount) + " " + detailCart.getCount());
    }

    @Override
    public void onShowDetailCart(DetailCart detailCart, MyOrderListFoodAdapter.ListFoodOrderViewHolder holder) {

    }


    public class OrderCartViewHolder extends RecyclerView.ViewHolder {
        ItemFoodOrderCartBinding binding;
        public OrderCartViewHolder(@NonNull ItemFoodOrderCartBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
