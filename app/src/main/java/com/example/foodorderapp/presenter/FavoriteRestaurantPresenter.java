package com.example.foodorderapp.presenter;

import android.content.Context;
import android.widget.ImageView;

import com.example.foodorderapp.event.ICheckFavorite;
import com.example.foodorderapp.event.IOnModifyFavorite;
import com.example.foodorderapp.event.IOnShowListFavoriteRestaurant;
import com.example.foodorderapp.model.DetailFavorite;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.model.Restaurant;
import com.example.foodorderapp.model.UserAccount;
import com.example.foodorderapp.sql.CartDatabaseHelper;

import java.util.List;

public class FavoriteRestaurantPresenter {
    IOnShowListFavoriteRestaurant iOnShowList;
    IOnModifyFavorite iOnModify;
    ICheckFavorite iCheckFavorite;
    CartDatabaseHelper helper;
    Context context;

    public FavoriteRestaurantPresenter(IOnModifyFavorite iOnModify, ICheckFavorite iCheckFavorite, Context context) {
        this.iOnModify = iOnModify;
        this.iCheckFavorite = iCheckFavorite;
        this.context = context;
    }

    public FavoriteRestaurantPresenter(IOnShowListFavoriteRestaurant iOnShowList, Context context) {
        this.iOnShowList = iOnShowList;
        this.context = context;
    }

    public void checkFavoriteRestaurant(Restaurant restaurant, ImageView checked, ImageView unchecked) {
        if (helper == null)
            helper = new CartDatabaseHelper(context);
        UserAccount user = helper.checkStatusAccount();
        if (user != null) {
            DetailFavorite detailFavorite = new DetailFavorite(restaurant, user);
            if (helper.findDetailFavorite(detailFavorite))
                iCheckFavorite.onChecked(checked, unchecked);
            else
                iCheckFavorite.onUnchecked(checked, unchecked);
        } else {
            if (helper.findFavoriteRestaurant(restaurant))
                iCheckFavorite.onChecked(checked, unchecked);
            else
                iCheckFavorite.onUnchecked(checked, unchecked);
        }
    }


    public void saveFavoriteRestaurant(Restaurant restaurant) {
        if (helper == null)
            helper = new CartDatabaseHelper(context);
//        if(helper.findRestaurant(restaurant))

        UserAccount user = helper.checkStatusAccount();
        if (user != null) {
            DetailFavorite detailFavorite = new DetailFavorite(restaurant, user);
            helper.setDetailFavorite(detailFavorite);
        } else
            helper.setFavoriteRestaurant(restaurant);
        List<Food> foodList = restaurant.getFoodList();
        for (int i = 0; i < foodList.size(); i++) {
            Food food = foodList.get(i);
            if (!helper.findFood(food)) {
                food.setRestaurant(restaurant);
                helper.insertFood(food);
            }
        }
        iOnModify.onSaveFavorite();
    }

    public void destroyFavoriteRestaurant(Restaurant restaurant) {
        if (helper == null)
            helper = new CartDatabaseHelper(context);
        UserAccount user = helper.checkStatusAccount();
        if (user != null) {
            DetailFavorite detailFavorite = new DetailFavorite(restaurant, user);
            helper.deleteDetailFavorite(detailFavorite);
        } else
            helper.deleteFavoriteRestaurant(restaurant);
        helper.deleteFavoriteFood(restaurant);
        iOnModify.onDestroyFavorite();
    }

    public void showListFavoriteRestaurant() {
        if (helper == null)
            helper = new CartDatabaseHelper(context);
        List<Restaurant> restaurantList;
        UserAccount user = helper.checkStatusAccount();
        if (user != null) {
            DetailFavorite detailFavorite = new DetailFavorite(null, user);
            restaurantList = helper.getListDetailFavorite(detailFavorite);
        } else
            restaurantList = helper.getListFavoriteRestaurant();

        if (restaurantList.size() != 0)
            iOnShowList.onShowListFavorite(restaurantList);
        else
            iOnShowList.onEmptyListFavorite();
    }
}
