package com.example.foodorderapp.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.foodorderapp.R;
import com.example.foodorderapp.databinding.ItemFoodBannerBinding;
import com.example.foodorderapp.model.Banner;
import com.example.foodorderapp.model.FoodBanner;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FoodBannerAdapter extends RecyclerView.Adapter<FoodBannerAdapter.FoodBannerViewHolder> {

    List<FoodBanner> foodBannerList;
    BannerAdapter bannerAdapter;
    Context context;
    Timer timer;

    public FoodBannerAdapter(List<FoodBanner> foodBannerList, Context context) {
        this.foodBannerList = foodBannerList;
        this.context = context;
    }

    @NonNull
    @Override
    public FoodBannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemFoodBannerBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_food_banner, parent, false);

        FoodBannerViewHolder viewHolder = new FoodBannerViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodBannerViewHolder holder, int position) {
        FoodBanner foodBanner = foodBannerList.get(position);
        List<Banner> bannerList = foodBanner.getBannerList();

        bannerAdapter = new BannerAdapter(context, bannerList);
        holder.binding.vpBanner.setAdapter(bannerAdapter);
        holder.binding.ciImage.setViewPager(holder.binding.vpBanner);

        bannerAdapter.registerDataSetObserver(holder.binding.ciImage.getDataSetObserver());
        autoSlideImage(bannerList, holder.binding.vpBanner);
    }

    @Override
    public int getItemCount() {
        return foodBannerList != null ? foodBannerList.size() : 0;
    }

    private void autoSlideImage(List<Banner> bannerList, ViewPager viewPager) {
        if (bannerList.isEmpty() || viewPager == null)
            return;

        // init timer
        if (timer == null)
            timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = viewPager.getCurrentItem();
                        int totalItem = bannerList.size() - 1;
                        if (currentItem < totalItem) {
                            currentItem++;
                            viewPager.setCurrentItem(currentItem);
                        } else {
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }
        }, 2000, 2000);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public class FoodBannerViewHolder extends RecyclerView.ViewHolder {
        ItemFoodBannerBinding binding;
        public FoodBannerViewHolder(@NonNull ItemFoodBannerBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    //    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (timer != null) {
//            timer.cancel();
//            timer = null;
//        }
//    }
}
