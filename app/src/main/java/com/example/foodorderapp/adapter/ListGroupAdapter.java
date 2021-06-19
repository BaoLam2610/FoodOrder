package com.example.foodorderapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.DetailActivity;
import com.example.foodorderapp.R;
import com.example.foodorderapp.databinding.ItemListGroupBinding;
import com.example.foodorderapp.event.ICartDatabase;
import com.example.foodorderapp.event.IOnClickItem;
import com.example.foodorderapp.event.IOnClickItemRestaurant;
import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.FoodBanner;
import com.example.foodorderapp.model.FoodCategory;
import com.example.foodorderapp.model.GroupList;
import com.example.foodorderapp.model.Restaurant;
import com.example.foodorderapp.presenter.CartDatabasePresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class ListGroupAdapter extends RecyclerView.Adapter<ListGroupAdapter.ListGroupViewHolder> implements ICartDatabase {

    public static final int TYPE_FOOD_BANNER = 1;
    public static final int TYPE_FOOD_CATEGORY = 2;
    public static final int TYPE_FOOD_QUICK_DELIVERIES = 3;
    public static final int TYPE_FOOD_BEST_RATED = 4;
    List<GroupList> groupList;
    Context context;
    IOnClickItem iOnClickItem;
    CartDatabasePresenter cartPresenter;

    public ListGroupAdapter(List<GroupList> groupList, Context context) {
        this.groupList = groupList;
        this.context = context;
    }

    public void setIOnClickItem(IOnClickItem iOnClickItem) {
        this.iOnClickItem = iOnClickItem;
    }

    @NonNull
    @Override
    public ListGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemListGroupBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_list_group, parent, false);
        ListGroupViewHolder viewHolder = new ListGroupViewHolder(binding);

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return groupList.get(position).getType();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ListGroupViewHolder holder, int position) {
        GroupList list = groupList.get(position);
        holder.binding.tvHeaderTitle.setText(list.getHeaderTitle());

        LinearLayoutManager horLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        LinearLayoutManager verLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2, RecyclerView.HORIZONTAL, false);

        List<FoodBanner> foodBannerList = list.getFoodBanner();
        List<FoodCategory> categoryList = list.getFoodCategory();
        List<Restaurant> restaurantList = list.getRestaurants();

        switch (holder.getItemViewType()) {
            case TYPE_FOOD_BANNER:
                FoodBannerAdapter foodBannerAdapter = new FoodBannerAdapter(foodBannerList, context);
                holder.binding.rvListItem.setHasFixedSize(true);
                holder.binding.rvListItem.setAdapter(foodBannerAdapter);
                holder.binding.rvListItem.setLayoutManager(horLayoutManager);


                break;
            case TYPE_FOOD_CATEGORY:
                FoodCategoryAdapter categoryAdapter = new FoodCategoryAdapter(categoryList, context);
                holder.binding.rvListItem.setHasFixedSize(true);
                holder.binding.rvListItem.setAdapter(categoryAdapter);
                holder.binding.rvListItem.setLayoutManager(horLayoutManager);
                holder.binding.rvListItem.setNestedScrollingEnabled(false);

                break;
            case TYPE_FOOD_QUICK_DELIVERIES:
                QuickDeliveriesAdapter quickDeliveriesAdapter = new QuickDeliveriesAdapter(restaurantList, context);
                holder.binding.rvListItem.setHasFixedSize(true);
                holder.binding.rvListItem.setAdapter(quickDeliveriesAdapter);
                holder.binding.rvListItem.setLayoutManager(gridLayoutManager);
                cartPresenter = new CartDatabasePresenter(this, context);
                quickDeliveriesAdapter.setIOnClickItemRestaurant(new IOnClickItemRestaurant() {
                    @Override
                    public void onClickItem(Restaurant restaurant) {
                        Intent it = new Intent(context, DetailActivity.class);
                        it.putExtra("quick_deliveries", "item");
                        EventBus.getDefault().postSticky(restaurant); // dùng luôn sql
                        cartPresenter.saveRestaurantOnCart(restaurant);
                        Cart currentCart = new Cart(restaurant, 0, 0);
                        cartPresenter.saveCart(currentCart);
                        EventBus.getDefault().postSticky(currentCart);
                        context.startActivity(it);
                        // edge://inspect/#devices
                    }
                });
                break;
            case TYPE_FOOD_BEST_RATED:
                List<Restaurant> bestRatedList = new ArrayList<>();
                for (int i = 0; i < restaurantList.size(); i++) {
                    if (restaurantList.get(i).getRate() >= 4.0)
                        bestRatedList.add(restaurantList.get(i));
                }
//
//

                BestRatedAdapter bestRatedAdapter = new BestRatedAdapter(bestRatedList, context);
                holder.binding.rvListItem.setHasFixedSize(true);
                holder.binding.rvListItem.setAdapter(bestRatedAdapter);
                holder.binding.rvListItem.setLayoutManager(gridLayoutManager);
                bestRatedAdapter.setIOnClickItemRestaurant(new IOnClickItemRestaurant() {
                    @Override
                    public void onClickItem(Restaurant restaurant) {
                        Intent it = new Intent(context, DetailActivity.class);
                        it.putExtra("best_rated", "item");
                        EventBus.getDefault().postSticky(restaurant); // dùng luôn sql
                        cartPresenter.saveRestaurantOnCart(restaurant);
                        Cart currentCart = new Cart(restaurant, 0, 0);
                        cartPresenter.saveCart(currentCart);
                        EventBus.getDefault().postSticky(currentCart);
                        context.startActivity(it);
                    }
                });
                break;
        }
        holder.binding.tvSeeMore.setOnClickListener(v -> {
            iOnClickItem.onClickShowMore(holder.getItemViewType(), position);
        });
    }

    @Override
    public int getItemCount() {
        return groupList != null ? groupList.size() : 0;
    }

    @Override
    public void onShowCart(Cart cart) {

    }

    public class ListGroupViewHolder extends RecyclerView.ViewHolder {
        ItemListGroupBinding binding;

        public ListGroupViewHolder(@NonNull ItemListGroupBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
