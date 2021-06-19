package com.example.foodorderapp.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Food implements Parcelable {
    @SerializedName("food_id")
    private String id;
    @SerializedName("food_name")
    private String name;
    @SerializedName("food_img")
    private String image;
    @SerializedName("food_price")
    private long price;
    @SerializedName("food_category")
    private String category;
    private int count;
    private Restaurant restaurant;
    private String cart;

    public Food(String id, String name, String image, long price, String category, Restaurant restaurant) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
        this.category = category;
        this.restaurant = restaurant;
    }

//    public Food(String id, String name, String image, long price, String category, int count, Restaurant restaurant, Cart cart) {
//        this.id = id;
//        this.name = name;
//        this.image = image;
//        this.price = price;
//        this.category = category;
//        this.count = count;
//        this.restaurant = restaurant;
//        this.cart = cart;
//    }

    protected Food(Parcel in) {
        id = in.readString();
        name = in.readString();
        image = in.readString();
        price = in.readLong();
        category = in.readString();
        count = in.readInt();
        restaurant = in.readParcelable(Restaurant.class.getClassLoader());
        cart = in.readString();
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public String getCart() {
        return cart;
    }

    public void setCart(String cart) {
        this.cart = cart;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Food food = (Food) o;
        return id.equals(food.id);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(image);
        dest.writeLong(price);
        dest.writeString(category);
        dest.writeInt(count);
        dest.writeParcelable(restaurant, flags);
        dest.writeString(cart);
    }
}
