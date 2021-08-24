package com.example.foodorderapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.foodorderapp.R;
import com.example.foodorderapp.databinding.ItemBannerBinding;
import com.example.foodorderapp.model.Banner;

import java.util.List;

public class BannerAdapter extends PagerAdapter {

    ItemBannerBinding binding;
    Context context;
    List<Banner> bannerList;

    public BannerAdapter(Context context, List<Banner> bannerList) {
        this.context = context;
        this.bannerList = bannerList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(container.getContext());
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_banner, container, false);

        Banner banner = bannerList.get(position);
        if (banner != null) {
            //Glide.with(mContext).load(image.getId()).into(ivImage);
            Glide.with(context).load(banner.getImage())
                    //.centerCrop()   // căn ảnh
//                    .placeholder(R.drawable.ic_baseline_image_24)  // đợi load ảnh
//                    .error(R.drawable.ic_baseline_error_24)        // load ảnh bị lỗi
                    .into(binding.ivFoodBanner);
        }
        container.addView(binding.getRoot());

        return binding.getRoot();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //remove view
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        //if (bannerList != null)
        return bannerList.size();
        //return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
