package com.example.foodorderapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.foodorderapp.databinding.ActivityDetailBinding;
import com.example.foodorderapp.detail.DetailRestaurantFragment;
import com.example.foodorderapp.detail.ListRestaurantFragment;
import com.example.foodorderapp.login.LoginSignUpFragment;
import com.example.foodorderapp.event.IOnBackPressed;
import com.example.foodorderapp.helper.IHelper;

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
        if (it.hasExtra("quick_deliveries")) {
            switch (it.getStringExtra("quick_deliveries")) {
                case "show_more":
                    getFragment(ListRestaurantFragment.newInstance(IHelper.TYPE_QUICK_DELIVERIES));
                    break;
                case "item":
                    bundle.putString("back_pressed", "check");
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
                    DetailRestaurantFragment detailRestaurantFragment = DetailRestaurantFragment.newInstance();
                    detailRestaurantFragment.setArguments(bundle);
                    getFragmentDetail(detailRestaurantFragment);
                    break;
                default:
                    break;
            }
        }


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

    public void setActionBarTitle(String title){
        binding.toolbar.setTitle(title);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.flDetailFragment);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        }
    }


}