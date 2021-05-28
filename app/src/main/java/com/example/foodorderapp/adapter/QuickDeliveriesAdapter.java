package com.example.foodorderapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodorderapp.R;
import com.example.foodorderapp.databinding.ItemQuickDeliveriesBinding;
import com.example.foodorderapp.event.IOnClickItemRestaurant;
import com.example.foodorderapp.model.Restaurant;

import java.util.List;

public class QuickDeliveriesAdapter extends RecyclerView.Adapter<QuickDeliveriesAdapter.QuickDeliveriesViewHolder> {

    List<Restaurant> restaurantList;
    IOnClickItemRestaurant iOnClickItemRestaurant;
    Context context;

    public void setIOnClickItemRestaurant(IOnClickItemRestaurant iOnClickItemRestaurant) {
        this.iOnClickItemRestaurant = iOnClickItemRestaurant;
    }

    public QuickDeliveriesAdapter(List<Restaurant> restaurantList, Context context) {
        this.restaurantList = restaurantList;
        this.context = context;
    }

    @NonNull
    @Override
    public QuickDeliveriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemQuickDeliveriesBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_quick_deliveries
                , parent, false);

        QuickDeliveriesViewHolder viewHolder = new QuickDeliveriesViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull QuickDeliveriesViewHolder holder, int position) {
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
        holder.binding.itemRestaurant.setOnClickListener(v -> iOnClickItemRestaurant.onClickItem(restaurant));
    }

    @Override
    public int getItemCount() {
        return restaurantList == null ? 0 : restaurantList.size();
    }

    public class QuickDeliveriesViewHolder extends RecyclerView.ViewHolder {
        ItemQuickDeliveriesBinding binding;

        public QuickDeliveriesViewHolder(@NonNull ItemQuickDeliveriesBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
