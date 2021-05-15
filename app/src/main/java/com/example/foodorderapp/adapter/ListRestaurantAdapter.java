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
import com.example.foodorderapp.databinding.ItemRestaurantBinding;
import com.example.foodorderapp.event.IOnClickItemRestaurant;
import com.example.foodorderapp.model.Restaurant;

import java.util.List;

public class ListRestaurantAdapter extends RecyclerView.Adapter<ListRestaurantAdapter.RestaurantViewHolder> {

    List<Restaurant> restaurantList;
    Context context;
    IOnClickItemRestaurant clickItemRestaurant;

    public void setClickItemRestaurant(IOnClickItemRestaurant clickItemRestaurant) {
        this.clickItemRestaurant = clickItemRestaurant;
    }

    public ListRestaurantAdapter(List<Restaurant> restaurantList, Context context) {
        this.restaurantList = restaurantList;
        this.context = context;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemRestaurantBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_restaurant, parent,false);
        RestaurantViewHolder viewHolder = new RestaurantViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        Restaurant restaurant = restaurantList.get(position);
        holder.binding.ivResImage.setImageResource(R.drawable.ic_launcher_background);// change this
        Glide.with(context).load(restaurant.getImage())
                .centerCrop()   // căn ảnh
//                    .placeholder(R.drawable.ic_baseline_image_24)  // đợi load ảnh
//                    .error(R.drawable.ic_baseline_error_24)        // load ảnh bị lỗi
                .into(holder.binding.ivResImage);
        holder.binding.tvResName.setText(restaurant.getName());
        holder.binding.tvResProvide.setText(restaurant.getProvideType());
        holder.binding.tvResAddress.setText(restaurant.getAddress());
        holder.binding.rbResRate.setRating((float) restaurant.getRate());
        holder.binding.llItemRes.setOnClickListener( v->{
            clickItemRestaurant.onClickItem(restaurant);
        });
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public class RestaurantViewHolder extends RecyclerView.ViewHolder {
        ItemRestaurantBinding binding;
        public RestaurantViewHolder(@NonNull ItemRestaurantBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}