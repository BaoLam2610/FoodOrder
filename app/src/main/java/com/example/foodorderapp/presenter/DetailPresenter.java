package com.example.foodorderapp.presenter;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.foodorderapp.event.ICartDatabase;
import com.example.foodorderapp.event.ICheckLogin;
import com.example.foodorderapp.event.IDetailRestaurant;
import com.example.foodorderapp.event.IListFood;
import com.example.foodorderapp.event.IListRestaurant;
import com.example.foodorderapp.event.IOnListFood;
import com.example.foodorderapp.event.IOnShowCart;
import com.example.foodorderapp.event.IOrderCart;
import com.example.foodorderapp.event.IRestoreList;
import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.DetailCart;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.model.Restaurant;
import com.example.foodorderapp.sql.CartDatabaseHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class DetailPresenter {

    IListFood iListFood;
    IRestoreList iRestoreList;
    ICartDatabase iCartDatabase;
    CartDatabasePresenter cartPresenter;
    IListRestaurant iListRestaurant;
    IDetailRestaurant iDetailRestaurant;
    ICheckLogin iCheckLogin;
    IOnShowCart iOnShowCart;
    IOrderCart iOrderCart;
    CartDatabaseHelper helper;
    IOnListFood iOnListFood;
    List<Restaurant> restaurantList;
    Restaurant res;
    Context context;
    List<Food> fastFoodList;
    List<Food> starterFoodList;
    List<Food> mainCourseList;
    List<Food> desertList;
    List<Food> drinkList;
    List<Food> listOrder;
    List<Food> listDelete = new ArrayList<>();
    String typeCheck;
    String tempType;
    //    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
//    public void getListType(String type){
//        if(type.equals(IHelper.TYPE_QUICK_DELIVERIES) || type.equals(IHelper.TYPE_BEST_RATED))
//            tempType = type;
//    }
    List<Restaurant> bestRestaurantList;

    public DetailPresenter(ICheckLogin iCheckLogin, Context context) {
        this.iCheckLogin = iCheckLogin;
        this.context = context;
    }


    public DetailPresenter(IDetailRestaurant iDetailRestaurant, IOnShowCart iOnShowCart) {
        this.iDetailRestaurant = iDetailRestaurant;
        this.iOnShowCart = iOnShowCart;

    }

    public DetailPresenter(IOnListFood iOnListFood, Context context, CartDatabasePresenter cartPresenter) {
        this.iOnListFood = iOnListFood;
        this.context = context;
        this.cartPresenter = cartPresenter;
    }

    public DetailPresenter(IDetailRestaurant iDetailRestaurant, Context context, IRestoreList iRestoreList) {
        this.iDetailRestaurant = iDetailRestaurant;
        this.context = context;
        this.iRestoreList = iRestoreList;
    }

    public DetailPresenter(IOrderCart iOrderCart, IOnShowCart iOnShowCart, Context context, CartDatabasePresenter cartPresenter) {
        this.iOrderCart = iOrderCart;
        this.context = context;
        this.cartPresenter = cartPresenter;
        this.iOnShowCart = iOnShowCart;
    }


    public DetailPresenter(IListRestaurant iListRestaurant, Context context) {
        this.iListRestaurant = iListRestaurant;
        this.context = context;
    }

    public DetailPresenter(ICartDatabase iCartDatabase, IListFood iListFood, IOnListFood iOnListFood, Context context) {
        this.iCartDatabase = iCartDatabase;
        this.iOnListFood = iOnListFood;
        this.iListFood = iListFood;
        this.context = context;
    }

    public DetailPresenter(IDetailRestaurant iDetailRestaurant, Context context) {
        this.iDetailRestaurant = iDetailRestaurant;
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showListRestaurant(String type) {// quick delivery or best rated
        EventBus.getDefault().register(this);
//        type = tempType;
        System.out.println(type);
//        System.out.println(type);
//        switch (type) {
//            case "0":
        iListRestaurant.onShowListRestaurant(restaurantList);
//                break;
//            case "1":
//                bestRestaurantList = new ArrayList<>();
//                if(bestRestaurantList != null) {
////                    bestRestaurantList.removeIf(r -> r.getRate() <= 4.0);
//                    for (int i = 0; i < restaurantList.size(); i++) {
//                        if(restaurantList.get(i).getRate()>= 4.0)
//                            bestRestaurantList.add(restaurantList.get(i));
//                    }
//
//                }
//                iListRestaurant.onShowListRestaurant(bestRestaurantList);
//                break;
//        }

        EventBus.getDefault().unregister(this);
    }

    public void saveRestaurantOnCart(Restaurant restaurant) {
        try {
            if (helper == null)
                helper = new CartDatabaseHelper(context);
            if (!helper.findRestaurant(restaurant))
                helper.insertRestaurant(restaurant);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveOnDatabase() {
        helper = new CartDatabaseHelper(context);
        EventBus.getDefault().register(this);
        for (int i = 0; i < restaurantList.size(); i++) {
            if (!helper.findRestaurant(restaurantList.get(i)))
                saveRestaurantOnCart(restaurantList.get(i));
        }
        EventBus.getDefault().unregister(this);
    }

    public void showDetailRestaurant() {

        EventBus.getDefault().register(this);
        iDetailRestaurant.onShowDetailRestaurant(res, null);
        EventBus.getDefault().unregister(this);

    }

    public List<Food> getListFood() {
        List<Food> foodList;
//        EventBus.getDefault().register(this);
        foodList = res.getFoodList();
        iListFood.onListFood(res, restaurantList);
//        EventBus.getDefault().unregister(this);
        return foodList;
    }

//    public void showListFood(){
//        iListFood.onListFood(getListFood());
//    }

    public List<Food> getListFood(List<Food> foodList, String type) {
        fastFoodList = new ArrayList<>();
        starterFoodList = new ArrayList<>();
        mainCourseList = new ArrayList<>();
        desertList = new ArrayList<>();
        drinkList = new ArrayList<>();

        for (int i = 0; i < foodList.size(); i++) {
            switch (foodList.get(i).getCategory()) {
                case "Fast food":
                    fastFoodList.add(foodList.get(i));
                    break;
                case "Starter food":
                    starterFoodList.add(foodList.get(i));
                    break;
                case "Main course food":
                    mainCourseList.add(foodList.get(i));
                    break;
                case "Desert food":
                    desertList.add(foodList.get(i));
                    break;
                case "Drink":
                    drinkList.add(foodList.get(i));
                    break;
                default:
                    break;
            }
        }
        switch (type) {
            case "Fast Food":
            case "Đồ ăn nhanh":
                return fastFoodList;
            case "Starter":
            case "Khai vị":
                return starterFoodList;
            case "Main Course":
            case "Món chính":
                return mainCourseList;
            case "Desert":
            case "Tráng miệng":
                return desertList;
            case "Drink":
            case "Đồ uống":
                return drinkList;
        }
        return null;
    }

    public List<Food> getListFavoriteFood() {
//        EventBus.getDefault().register(this);
        List<Food> foodList;
        if (helper == null)
            helper = new CartDatabaseHelper(context);
        foodList = helper.getListFavoriteFood(res);

//        EventBus.getDefault().unregister(this);
        return foodList;
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getCheck(String type) {
        typeCheck = type;
    }

    public void showListFood(String type, String listType) {
//        public void showListFood(List<Food> foodList,String type) {
//        iListFood.onListFood(getListFood());
        EventBus.getDefault().register(this);
        switch (typeCheck) {
            case "default":
                iOnListFood.onShowListFood(getListFood(getListFood(), type));
                break;
            case "favorite":
                iOnListFood.onShowListFood(getListFood(getListFavoriteFood(), type));
                break;
        }
        EventBus.getDefault().unregister(this);
//        iOnListFood.onShowListFood(getListFood(foodList, type));
//        iListFood.onListFood(res, restaurantList);

//        List<Food> foodList = getListFood();

    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getListRestaurant(List<Restaurant> list) {

        restaurantList = list;

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN) // get detail from restaurant fragment
    public void getDetailRestaurant(Restaurant restaurant) {

        res = restaurant;
    }

    public void saveFood(Food food) {
//        EventBus.getDefault().register(this);
        try {
            if (helper == null)
                helper = new CartDatabaseHelper(context);
            if (helper.findFood(food))
                return;
            food.setRestaurant(res);
            helper.insertFood(food);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        EventBus.getDefault().unregister(this);
    }

    public void saveDetailCart(DetailCart detailCart) {
        try {
            if (helper == null)
                helper = new CartDatabaseHelper(context);
            helper.insertDetailCart(detailCart);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editFood(Food food) {
        try {
            if (helper == null)
                helper = new CartDatabaseHelper(context);

            helper.updateFood(food);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editCart(Cart cart) {
        try {
            if (helper == null)
                helper = new CartDatabaseHelper(context);
            helper.updateCart(cart);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editDetailCart(DetailCart detailCart) {
        try {
            if (helper == null)
                helper = new CartDatabaseHelper(context);
            helper.updateDetailCart(detailCart);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroyDetailCart(DetailCart detailCart) {
        try {
            if (helper == null)
                helper = new CartDatabaseHelper(context);
            helper.deleteDetailCart(detailCart);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroyFood(Food food, Cart cart) {
        try {
            if (helper == null)
                helper = new CartDatabaseHelper(context);
            helper.deleteFood(food, cart);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addFoodOrder(Food food, Cart cart) {

    }

    public void updateFoodOrder(Food food, Cart cart, String type) {
        DetailCart detailCart = new DetailCart(food, cart);
        switch (type) {
            case "add":
                saveFood(food);
                detailCart.setCount(1);
                saveDetailCart(detailCart); // create relationship food - cart

                cart.setAmount(cart.getAmount() + 1);
                cart.setTotalPrice(cart.getTotalPrice() + food.getPrice());


                editCart(cart);
//                iCartDatabase.onShowCart(cart);
                EventBus.getDefault().postSticky(cart);
                break;
            case "plus":
                cart.setTotalPrice(cart.getTotalPrice() + food.getPrice());

//                editFood(food);
                detailCart.setCount(food.getCount());
                editDetailCart(detailCart);
                editCart(cart);
//                iCartDatabase.onShowCart(cart);
                EventBus.getDefault().postSticky(cart);
                break;
            case "minus":
                if (food.getCount() == 0) {
//                    cartPresenter.destroyFood(food, cart);
                    destroyDetailCart(detailCart);
                    destroyFood(food, cart);
                    cart.setAmount(cart.getAmount() - 1);
                }
                cart.setTotalPrice(cart.getTotalPrice() - food.getPrice());

//                cartPresenter.editFood(food);
//                cartPresenter.editCart(cart);
//                editFood(food);
                detailCart.setCount(food.getCount());
                editDetailCart(detailCart);
                editCart(cart);
//                iCartDatabase.onShowCart(cart);
                EventBus.getDefault().postSticky(cart);
        }
        EventBus.getDefault().postSticky(detailCart);
    }

    public void deleteOrder(Food food, Cart cart) {
        listDelete.add(food);

        if (helper == null)
            helper = new CartDatabaseHelper(context);
        DetailCart detailCart = helper.findDetailCart(food, cart);
        int amount = cart.getAmount() - 1;
        long totalPrice = cart.getTotalPrice() - (detailCart.getCount() * food.getPrice());
        destroyDetailCart(detailCart);
        destroyFood(food, cart);

        cart.setAmount(amount);
        cart.setTotalPrice(totalPrice);

        editCart(cart);
        food.setCount(0);
        EventBus.getDefault().postSticky(listDelete);
        EventBus.getDefault().postSticky(cart);
        iOnShowCart.onShowCart(cart.getAmount(), cart.getTotalPrice());
    }
}
