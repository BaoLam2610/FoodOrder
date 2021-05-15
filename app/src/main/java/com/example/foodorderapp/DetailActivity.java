package com.example.foodorderapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.example.foodorderapp.databinding.ActivityDetailBinding;
import com.example.foodorderapp.detail.DetailRestaurantFragment;
import com.example.foodorderapp.detail.ListQuickDeliveriesFragment;
import com.example.foodorderapp.detail.LoginSignUpFragment;
import com.example.foodorderapp.event.IOnBackPressed;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";
    ActivityDetailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_detail);

        Intent it = getIntent();
        Bundle bundle = new Bundle();
        switch (it.getStringExtra("quick_deliveries")){
            case "show_more":
                getFragment(ListQuickDeliveriesFragment.newInstance());
                break;
            case "item":
                bundle.putString("quick_deliveries","check");
                DetailRestaurantFragment detailRestaurantFragment = DetailRestaurantFragment.newInstance();
                detailRestaurantFragment.setArguments(bundle);
                getFragmentDetail(detailRestaurantFragment);
                break;
            default:
                break;
        }
        switch (it.getStringExtra("login_screen")){
            case "login":
                getFragment(LoginSignUpFragment.newInstance());
                break;
            default:
                break;
        }

    }
    public void getFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flDetailFragment,fragment)
                .commit();
    }
    public void getFragmentDetail(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flDetailFragment,fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.flDetailFragment);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        }
    }

}