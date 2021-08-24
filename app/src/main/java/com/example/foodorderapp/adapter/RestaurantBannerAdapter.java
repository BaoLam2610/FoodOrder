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
import com.example.foodorderapp.databinding.ItemRestaurantBannerBinding;
import com.example.foodorderapp.model.Banner;
import com.example.foodorderapp.model.Restaurant;

import java.util.List;

public class RestaurantBannerAdapter extends RecyclerView.Adapter<RestaurantBannerAdapter.ResBannerViewHolder> {

    List<Restaurant> restaurantList;
    Context context;

    public RestaurantBannerAdapter(List<Restaurant> restaurantList, Context context) {
        this.restaurantList = restaurantList;
        this.context = context;
    }

    @NonNull
    @Override
    public ResBannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemRestaurantBannerBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_restaurant_banner, parent, false);
        ResBannerViewHolder viewHolder = new ResBannerViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ResBannerViewHolder holder, int position) {
        Restaurant restaurant = restaurantList.get(position);
        Banner banner = restaurant.getBanner();
        Glide.with(context).load(banner.getImage())
                .centerCrop()   // căn ảnh
//                    .placeholder(R.drawable.ic_baseline_image_24)  // đợi load ảnh
//                    .error(R.drawable.ic_baseline_error_24)        // load ảnh bị lỗi
                .into(holder.binding.ivBanner);
        holder.binding.tvResName.setText(restaurant.getName());
        holder.binding.tvContentBanner.setText(banner.getContent());

    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public class ResBannerViewHolder extends RecyclerView.ViewHolder {
        ItemRestaurantBannerBinding binding;
        public ResBannerViewHolder(@NonNull ItemRestaurantBannerBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
