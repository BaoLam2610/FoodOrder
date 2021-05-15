package com.example.foodorderapp.model;

import java.util.List;

public class FoodBanner {
    private List<Banner> bannerList;

    public FoodBanner(List<Banner> bannerList) {
        this.bannerList = bannerList;
    }

    public List<Banner> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<Banner> bannerList) {
        this.bannerList = bannerList;
    }
}
