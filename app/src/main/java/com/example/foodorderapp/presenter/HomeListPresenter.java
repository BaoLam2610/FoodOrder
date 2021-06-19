package com.example.foodorderapp.presenter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.ListGroupAdapter;
import com.example.foodorderapp.api.ApiRestaurant;
import com.example.foodorderapp.event.IHomeListHelper;
import com.example.foodorderapp.model.Banner;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.model.FoodBanner;
import com.example.foodorderapp.model.FoodCategory;
import com.example.foodorderapp.model.GroupList;
import com.example.foodorderapp.model.Restaurant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeListPresenter {

    IHomeListHelper iHomeListHelper;
    List<GroupList> groupList;
    Context context;

    public HomeListPresenter(Context context, IHomeListHelper iHomeListHelper) {
        this.context = context;
        this.iHomeListHelper = iHomeListHelper;
    }

    public void showGroupList() {
        ApiRestaurant.apiRestaurant.getListRestaurant().enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {

                if (response.body() != null) {
                    List<Restaurant> restaurantList = response.body();
//                    restaurantList.isEmpty();
//                    restaurantList.size();
                    groupList = new ArrayList<>();
                    groupList.add(new GroupList(
                            ListGroupAdapter.TYPE_FOOD_BANNER,
                            context.getResources().getString(R.string.home_list_title_banner),
                            null, getBannerList(restaurantList), null
                    ));


                    groupList.add(new GroupList(
                            ListGroupAdapter.TYPE_FOOD_CATEGORY,
                            context.getResources().getString(R.string.home_list_title_category),
                            getFoodCategory(), null, null
                    ));

//            restaurantList = readApiRestaurant(listApi.get(LIST_RESTAURANT));
                    groupList.add(new GroupList(
                            ListGroupAdapter.TYPE_FOOD_QUICK_DELIVERIES,
                            context.getResources().getString(R.string.home_list_title_quick_delivery),
                            null, null,
                            restaurantList
                    ));
                    groupList.add(new GroupList(ListGroupAdapter.TYPE_FOOD_BEST_RATED,
                            context.getResources().getString(R.string.home_list_title_best_rated),
                            null, null,
                            restaurantList));


                }
                iHomeListHelper.onShowGroupList(groupList);
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });

    }


    public List<FoodBanner> getBannerList(List<Restaurant> list){
        List<Banner> bannerList = new ArrayList<>();
        List<FoodBanner> foodBannerList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            bannerList.add(list.get(i).getBanner());
        }
        FoodBanner foodBanner = new FoodBanner(bannerList);
        foodBannerList.add(foodBanner);
        return foodBannerList;
    }
    public List<FoodCategory> getFoodCategory(){
        List<FoodCategory> foodCategoryList = new ArrayList<>();
        foodCategoryList.add(new FoodCategory("Fast Food",R.drawable.ic_fast_food+""));
        foodCategoryList.add(new FoodCategory("Starter",R.drawable.ic_starter_food+""));
        foodCategoryList.add(new FoodCategory("Main Course",R.drawable.ic_main_course+""));
        foodCategoryList.add(new FoodCategory("Dessert",R.drawable.ic_desert+""));
        foodCategoryList.add(new FoodCategory("Drink",R.drawable.ic_drink+""));
        return foodCategoryList;
    }
}
