package com.example.foodorderapp.presenter;

import android.content.Context;
import android.os.AsyncTask;

import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.ListGroupAdapter;
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

public class HomeListPresenter {
    final String LIST_RESTAURANT_API = "https://demo9455117.mockable.io/ListRestaurant";
    final String LIST_BANNER_API = "https://demo9455117.mockable.io/ListBanner";
    final String LIST_FOOD_CATEGORY_API = "https://demo9455117.mockable.io/ListFoodCategory";
    final int LIST_BANNER = 0;
    final int LIST_FOOD_CATEGORY = 1;
    final int LIST_RESTAURANT = 2;
    IHomeListHelper iHomeListHelper;
    List<Banner> bannerList;
    List<GroupList> groupList;
    List<Restaurant> restaurantList;
    List<FoodBanner> foodBannerList;
    List<Food> foodList;
    List<FoodCategory> categoryList;
    List<String> listApi;
    Context context;

    public HomeListPresenter(Context context, IHomeListHelper iHomeListHelper) {
        this.context = context;
        this.iHomeListHelper = iHomeListHelper;
    }

    public void showGroupList() {
        iHomeListHelper.onShowGroupList(getGroupList());
    }

    public List<FoodCategory> readApiFoodCategory(String s) throws Exception {
        String id, name, image;
        categoryList = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(s);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
//                id = jsonObject.getString("cate_id");
            name = jsonObject.getString("cate_name");
            image = jsonObject.getString("cate_image");

            categoryList.add(new FoodCategory(name, image));
        }
        return categoryList;
    }

    public List<FoodBanner> readApiFoodBanner(String s) throws Exception {
        String id, bannerImg;
        foodBannerList = new ArrayList<>();
        bannerList = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(s);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
//                id = jsonObject.getString("id");
            bannerImg = jsonObject.getString("banner");

            bannerList.add(new Banner(bannerImg));
        }
        foodBannerList.add(new FoodBanner(bannerList));
        return foodBannerList;
    }

    public List<Restaurant> readApiRestaurant(String s) throws Exception {
        String resId, resName, resImage, resAddress, resPhone, resEmail, resProvide, resListFood;
        double resRate;
        String foodID, foodName, foodImage, foodCategory, foodType;
        long foodPrice;
        restaurantList = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(s);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            resId = jsonObject.getString("res_id");
            resName = jsonObject.getString("res_name");
            resImage = jsonObject.getString("res_img");
            resAddress = jsonObject.getString("res_addr");
            resPhone = jsonObject.getString("res_phone");
            resEmail = jsonObject.getString("res_email");
            resRate = jsonObject.getDouble("res_rate");
            resProvide = jsonObject.getString("res_provide");

            resListFood = jsonObject.getString("res_list_food");
            JSONArray jsonArray1 = new JSONArray(resListFood);
            foodList = new ArrayList<>();
            for (int j = 0; j < jsonArray1.length(); j++) {

                JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                foodID = jsonObject1.getString("food_id");
                foodName = jsonObject1.getString("food_name");
                foodImage = jsonObject1.getString("food_img");
                foodPrice = jsonObject1.getLong("food_price");
                foodCategory = jsonObject1.getString("food_category");
//                foodType = jsonObject1.getString("food_type");
                Food food = new Food(foodID,resId, foodName, foodImage, foodCategory,0,foodPrice);
//                String id, String idRes, String name, String image, String category, int count, long price
                foodList.add(food);
            }
            Restaurant restaurant = new Restaurant(resId, resName, resProvide, resImage, resAddress,
                    resPhone, resEmail, resRate, (ArrayList<Food>) foodList);
            restaurantList.add(restaurant);

        }
        return restaurantList;
    }

    public List<GroupList> getGroupList() {
        try {
            groupList = new ArrayList<>();
            listApi = new GetListRestaurant().execute().get();// size = 3
            foodBannerList = readApiFoodBanner(listApi.get(LIST_BANNER));
            groupList.add(new GroupList(
                    ListGroupAdapter.TYPE_FOOD_BANNER,
                    context.getResources().getString(R.string.home_list_title_banner),
                    null, foodBannerList, null
            ));

            categoryList = readApiFoodCategory(listApi.get(LIST_FOOD_CATEGORY));
            groupList.add(new GroupList(
                    ListGroupAdapter.TYPE_FOOD_CATEGORY,
                    context.getResources().getString(R.string.home_list_title_category),
                    categoryList, null, null
            ));

            restaurantList = readApiRestaurant(listApi.get(LIST_RESTAURANT));
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return groupList;
    }
// rì trô phít
    class GetListRestaurant extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<String> doInBackground(Void... voids) {
            List<String> listApi = new ArrayList<>();
            try {
                String bannerApi = getStringApi(LIST_BANNER_API);
                String categoryApi = getStringApi(LIST_FOOD_CATEGORY_API);
                String restaurantApi = getStringApi(LIST_RESTAURANT_API);

                listApi.add(bannerApi);
                listApi.add(categoryApi);
                listApi.add(restaurantApi);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listApi;
        }

        public String getStringApi(String urlApi) throws Exception {
            String result = "";
            URL url = new URL(urlApi);
            URLConnection connection = url.openConnection();
            InputStream is = connection.getInputStream();

            int byteCharacter;
            while ((byteCharacter = is.read()) != -1) {
                result += (char) byteCharacter;
            }
            return result;
        }
    }
}
