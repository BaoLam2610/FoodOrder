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
import com.example.foodorderapp.databinding.ItemFoodBinding;
import com.example.foodorderapp.event.IOnClickAddCart;
import com.example.foodorderapp.model.Food;

import java.text.DecimalFormat;
import java.util.List;

public class ListFoodAdapter extends RecyclerView.Adapter<ListFoodAdapter.FoodViewHolder> {

    List<Food> foodList;
    Context context;
    IOnClickAddCart iOnClickAddCart;
    int count = 0;

    public ListFoodAdapter(List<Food> foodList, Context context) {
        this.foodList = foodList;
        this.context = context;
    }

    public void setIOnClickAddCart(IOnClickAddCart iOnClickAddCart) {
        this.iOnClickAddCart = iOnClickAddCart;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemFoodBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_food, parent, false);
        FoodViewHolder viewHolder = new FoodViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food = foodList.get(position);
        DecimalFormat df = new DecimalFormat("###,###");
        Glide.with(context).load(food.getImage())
                .centerCrop()   // căn ảnh
//                    .placeholder(R.drawable.ic_baseline_image_24)  // đợi load ảnh
//                    .error(R.drawable.ic_baseline_error_24)        // load ảnh bị lỗi
                .into(holder.binding.ivFoodImage);
        holder.binding.tvFoodName.setText(food.getName());
        holder.binding.tvFoodPrice.setText("Cost: " + df.format(food.getPrice()) + "đ");

        if(food.getCount()==0){
            holder.binding.btnAmount.setVisibility(View.INVISIBLE);
            holder.binding.btnAddToCart.setVisibility(View.VISIBLE);
        } else{
            holder.binding.btnAmount.setVisibility(View.VISIBLE);
            holder.binding.btnAddToCart.setVisibility(View.INVISIBLE);
            holder.binding.tvAmount.setText(food.getCount() + "");
        }



        holder.binding.btnAddToCart.setOnClickListener(v->{
            holder.binding.btnAmount.setVisibility(View.VISIBLE);
            holder.binding.btnAddToCart.setVisibility(View.INVISIBLE);
            food.setCount(1);
            holder.binding.tvAmount.setText(food.getCount() + "");
            iOnClickAddCart.onClickAddCart(food);
        });

        holder.binding.btnPlus.setOnClickListener(v->{
            count = food.getCount();
            count++;
            holder.binding.tvAmount.setText(count + "");
            food.setCount(count);
            iOnClickAddCart.onClickPlus(food);
        });
        holder.binding.btnMinus.setOnClickListener(v->{
            count = food.getCount();
            if(count == 1){
                holder.binding.btnAmount.setVisibility(View.INVISIBLE);
                holder.binding.btnAddToCart.setVisibility(View.VISIBLE);
                count = 0;
                food.setCount(count);
                iOnClickAddCart.onClickMinus(food);
            } else{
                count--;
                holder.binding.tvAmount.setText(count + "");
                food.setCount(count);
                iOnClickAddCart.onClickMinus(food);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {
        ItemFoodBinding binding;

        public FoodViewHolder(@NonNull ItemFoodBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
