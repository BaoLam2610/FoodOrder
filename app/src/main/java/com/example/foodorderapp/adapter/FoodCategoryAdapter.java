package com.example.foodorderapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodorderapp.R;
import com.example.foodorderapp.databinding.ItemFoodCategoryBinding;
import com.example.foodorderapp.model.FoodCategory;

import java.util.List;

public class FoodCategoryAdapter extends RecyclerView.Adapter<FoodCategoryAdapter.FoodCategoryViewHolder> {

    List<FoodCategory> categoryList;
    Context context;

    public FoodCategoryAdapter(List<FoodCategory> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public FoodCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemFoodCategoryBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_food_category, parent, false);
        FoodCategoryViewHolder viewHolder = new FoodCategoryViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodCategoryViewHolder holder, int position) {
        FoodCategory foodCategory = categoryList.get(position);
        holder.binding.tvNameFoodCate.setText(foodCategory.getName());
        //Glide.with(mContext).load(image.getId()).into(ivImage);
//        Glide.with(context).load(foodCategory.getImage())
//                //.centerCrop()   // căn ảnh
////                    .placeholder(R.drawable.ic_baseline_image_24)  // đợi load ảnh
////                    .error(R.drawable.ic_baseline_error_24)        // load ảnh bị lỗi
//                .into(holder.binding.ivFoodCategory);
        holder.binding.ivFoodCategory.setImageResource(Integer.parseInt(foodCategory.getImage()));
    }

    @Override
    public int getItemCount() {

        return categoryList == null ? 0 : categoryList.size();
    }

    public class FoodCategoryViewHolder extends RecyclerView.ViewHolder {
        ItemFoodCategoryBinding binding;

        public FoodCategoryViewHolder(@NonNull ItemFoodCategoryBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
