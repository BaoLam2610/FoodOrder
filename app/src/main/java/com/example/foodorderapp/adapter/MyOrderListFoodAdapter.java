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
import com.example.foodorderapp.databinding.ItemMyOrderFoodBinding;
import com.example.foodorderapp.event.IOnShowDetailCart;
import com.example.foodorderapp.helper.FormatHelper;
import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.DetailCart;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.presenter.CartDatabasePresenter;

import java.util.List;

public class MyOrderListFoodAdapter extends RecyclerView.Adapter<MyOrderListFoodAdapter.ListFoodOrderViewHolder>
implements IOnShowDetailCart {

    List<Food> foodList;
    Context context;
    CartDatabasePresenter presenter;
    Cart cart;
    String type;

    public MyOrderListFoodAdapter(List<Food> foodList,Cart cart, Context context,String type) {
        this.foodList = foodList;
        this.context = context;
        this.cart = cart;
        this.type = type;
    }



    @NonNull
    @Override
    public ListFoodOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemMyOrderFoodBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_my_order_food,parent,false);
        ListFoodOrderViewHolder viewHolder = new ListFoodOrderViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListFoodOrderViewHolder holder, int position) {
        Food food = foodList.get(position);

        if(!type.equals("my_order")) {
            holder.binding.cardView4.setVisibility(View.VISIBLE);
            Glide.with(context).load(food.getImage())
                    .centerCrop()   // căn ảnh
//                    .placeholder(R.drawable.ic_baseline_image_24)  // đợi load ảnh
//                    .error(R.drawable.ic_baseline_error_24)        // load ảnh bị lỗi
                    .into(holder.binding.ivFoodImage);
        }
        holder.binding.tvFoodName.setText(food.getName());
        presenter = new CartDatabasePresenter(this,context);
        presenter.showDetailCart(food,cart,holder);
//        holder.binding.tvFoodCount.setText("x"+food.getCount());
        holder.binding.tvFoodPrice.setText(FormatHelper.formatPrice(food.getPrice()));
    }

    @Override
    public int getItemCount() {
        return foodList.isEmpty() ? 0 : foodList.size();
    }

    @Override
    public void onShowDetailCart(DetailCart detailCart, OrderCartAdapter.OrderCartViewHolder holder) {

    }

    @Override
    public void onShowDetailCart(DetailCart detailCart, ListFoodOrderViewHolder holder) {
        holder.binding.tvFoodCount.setText("x"+detailCart.getCount());
    }


    public class ListFoodOrderViewHolder extends RecyclerView.ViewHolder {
        ItemMyOrderFoodBinding binding;
        public ListFoodOrderViewHolder(@NonNull ItemMyOrderFoodBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
