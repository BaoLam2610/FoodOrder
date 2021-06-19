package com.example.foodorderapp.event;

import android.view.Menu;
import android.widget.ImageView;

public interface ICheckFavorite {
    void onChecked(ImageView ivChecked, ImageView ivUnchecked);
    void onUnchecked(ImageView ivChecked, ImageView ivUnchecked);
}
