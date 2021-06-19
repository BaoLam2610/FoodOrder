package com.example.foodorderapp.event;

import com.example.foodorderapp.model.Food;

import java.util.List;

public interface IOnListFood {
//    void onShowListFood(List<Food> fastFoodList,List<Food> starterFoodList,List<Food> mainCourseList,
//                        List<Food> desertList,List<Food> drinkList);
    void onShowListFood(List<Food> foodList);

}
