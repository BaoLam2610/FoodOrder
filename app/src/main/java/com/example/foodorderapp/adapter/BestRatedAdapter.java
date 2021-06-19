package com.example.foodorderapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodorderapp.R;
import com.example.foodorderapp.databinding.ItemBestRatedBinding;
import com.example.foodorderapp.event.IOnClickItemRestaurant;
import com.example.foodorderapp.model.Restaurant;

import java.util.List;

public class BestRatedAdapter extends RecyclerView.Adapter<BestRatedAdapter.BestRatedViewHolder> {

    List<Restaurant> restaurantList;
    Context context;
    IOnClickItemRestaurant iOnClickItemRestaurant;

    public BestRatedAdapter(List<Restaurant> restaurantList, Context context) {
        this.restaurantList = restaurantList;
        this.context = context;
    }

    public void setIOnClickItemRestaurant(IOnClickItemRestaurant iOnClickItemRestaurant) {
        this.iOnClickItemRestaurant = iOnClickItemRestaurant;
    }

    @NonNull
    @Override
    public BestRatedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemBestRatedBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_best_rated, parent, false);
        BestRatedViewHolder viewHolder = new BestRatedViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BestRatedViewHolder holder, int position) {
        Restaurant restaurant = restaurantList.get(position);
//        if (restaurant.getRate() >= 4.0) {
        holder.binding.ivResImage.setImageResource(R.drawable.ic_launcher_background);// change this
        Glide.with(context).load(restaurant.getImage())
                .centerCrop()   // căn ảnh
//                    .placeholder(R.drawable.ic_baseline_image_24)  // đợi load ảnh
//                    .error(R.drawable.ic_baseline_error_24)        // load ảnh bị lỗi
                .into(holder.binding.ivResImage);
        holder.binding.tvResName.setText(restaurant.getName());
        holder.binding.tvResAddress.setText(restaurant.getAddress());
        holder.binding.tvResRate.setText(restaurant.getRate() + " ");
        holder.binding.rbResRate.setRating((float) restaurant.getRate());
        holder.binding.itemRestaurant.setOnClickListener(v -> iOnClickItemRestaurant.onClickItem(restaurant));
//        }
    }

    @Override
    public int getItemCount() {

//        int count = 0;
//        for (int i = 0; i < restaurantList.size(); i++) {
//            if (restaurantList.get(i).getRate() < 4.0)
//                count++;
//        }
        return restaurantList.isEmpty() ? 0 : restaurantList.size();// - count;
    }

    public class BestRatedViewHolder extends RecyclerView.ViewHolder {
        ItemBestRatedBinding binding;

        public BestRatedViewHolder(@NonNull ItemBestRatedBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
