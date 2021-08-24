package com.example.foodorderapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.foodorderapp.databinding.ActivityDetailBinding;
import com.example.foodorderapp.detail.BannerFragment;
import com.example.foodorderapp.detail.DetailRestaurantFragment;
import com.example.foodorderapp.detail.ListRestaurantFragment;

import com.example.foodorderapp.event.IOnBackPressed;
import com.example.foodorderapp.helper.IHelper;
import com.example.foodorderapp.login.LoginSignUpFragment;
import com.example.foodorderapp.model.Restaurant;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";
    ActivityDetailBinding binding;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        setSupportActionBar(binding.toolbar);

        Intent it = getIntent();
        Bundle bundle = new Bundle();
        if(it.hasExtra("restaurant_banner")){
            switch (it.getStringExtra("restaurant_banner")) {
                case "show_more":
                    getFragment(BannerFragment.newInstance());
                    break;
            }
        }

        if (it.hasExtra("quick_deliveries")) {
            switch (it.getStringExtra("quick_deliveries")) {
                case "show_more":
                    getFragment(ListRestaurantFragment.newInstance(IHelper.TYPE_QUICK_DELIVERIES));
                    break;
                case "item":
                    bundle.putString("back_pressed", "check");
                    EventBus.getDefault().postSticky("default");
                    DetailRestaurantFragment detailRestaurantFragment = DetailRestaurantFragment.newInstance();
                    detailRestaurantFragment.setArguments(bundle);
                    getFragmentDetail(detailRestaurantFragment);
                    break;
                default:
                    break;
            }

        }
        if (it.hasExtra("best_rated")) {
            switch (it.getStringExtra("best_rated")) {
                case "show_more":
                    getFragment(ListRestaurantFragment.newInstance(IHelper.TYPE_BEST_RATED));
                    break;
                case "item":
                    bundle.putString("back_pressed", "check");
                    EventBus.getDefault().postSticky("default");
                    DetailRestaurantFragment detailRestaurantFragment = DetailRestaurantFragment.newInstance();
                    detailRestaurantFragment.setArguments(bundle);
                    getFragmentDetail(detailRestaurantFragment);
                    break;
                default:
                    break;
            }

        }
        if (it.hasExtra("favorite")) {
            switch (it.getStringExtra("favorite")) {
                case "item":
                    bundle.putString("back_pressed", "check");
                    EventBus.getDefault().postSticky("favorite");
                    DetailRestaurantFragment detailRestaurantFragment = DetailRestaurantFragment.newInstance();
                    detailRestaurantFragment.setArguments(bundle);
                    getFragmentDetail(detailRestaurantFragment);
                    break;
                default:
                    break;
            }
        }
        if(it.hasExtra("login_screen")){
            switch (it.getStringExtra("login_screen")){
                case "login":
                    getFragment(LoginSignUpFragment.newInstance("main",null));
                    break;
            }
        }

    }

    public void setActionBarCustom(int dp){
        float density = getResources().getDisplayMetrics().density;
        float px = dp *density;
        binding.appBar.getLayoutParams().height = (int)px;
        binding.appBar.setBackgroundResource(R.drawable.bgr_cooking);
    }

    public void setActionBarOpacity(){
        binding.appBar.setBackgroundColor(Color.TRANSPARENT);
    }

    public void setActionBarDefault(int dp){
        float density = getResources().getDisplayMetrics().density;
        float px = dp *density;
        binding.appBar.getLayoutParams().height = (int)px;
        binding.appBar.setBackgroundResource(R.color.white);
    }

    public void getFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flDetailFragment, fragment)
                .commit();
    }

    public void getFragmentDetail(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flDetailFragment, fragment)
                .commit();
    }


    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.flDetailFragment);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        getIntent().removeExtra("quick_deliveries");
        getIntent().removeExtra("best_rated");
        getIntent().removeExtra("favorite");
        getIntent().removeExtra("login_screen");
    }
}