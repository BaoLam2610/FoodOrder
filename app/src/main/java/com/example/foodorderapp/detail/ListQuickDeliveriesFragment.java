package com.example.foodorderapp.detail;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.ListRestaurantAdapter;
import com.example.foodorderapp.databinding.FragmentListQuickDeliveriesBinding;
import com.example.foodorderapp.event.ICartDatabase;
import com.example.foodorderapp.event.IListRestaurant;
import com.example.foodorderapp.event.IOnClickItemRestaurant;
import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.model.Restaurant;
import com.example.foodorderapp.presenter.CartDatabasePresenter;
import com.example.foodorderapp.presenter.DetailPresenter;
import com.example.foodorderapp.sql.CartDatabaseHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class ListQuickDeliveriesFragment extends Fragment implements IListRestaurant, ICartDatabase {
    private static final String TAG = "ListQuickDeliveriesFrag";
    FragmentListQuickDeliveriesBinding binding;
    List<Restaurant> restaurantList;
    ListRestaurantAdapter restaurantAdapter;
    CartDatabasePresenter cartPresenter;
    DetailPresenter presenter;



    public static ListQuickDeliveriesFragment newInstance() {

        Bundle args = new Bundle();

        ListQuickDeliveriesFragment fragment = new ListQuickDeliveriesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_quick_deliveries, container, false);
        presenter = new DetailPresenter(this,getContext());
        presenter.showListRestaurant(); // from API
//        helper.dropAllTable();
        return binding.getRoot();
    }

    public void getFragment(Fragment fragment){
        getParentFragmentManager().beginTransaction()
                .replace(R.id.flDetailFragment,fragment)
                .addToBackStack(TAG)
                .commit();
    }


    @Override
    public void onShowListRestaurant(List<Restaurant> restaurantList) {
        restaurantAdapter = new ListRestaurantAdapter(restaurantList,getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.tvResFound.setText(restaurantList.size() + " " + getContext().getResources().getString(R.string.tv_restaurant_found));
        binding.rvListRestaurant.setAdapter(restaurantAdapter);
        binding.rvListRestaurant.setLayoutManager(layoutManager);
        cartPresenter = new CartDatabasePresenter(this,getContext());
        restaurantAdapter.setClickItemRestaurant(new IOnClickItemRestaurant() {
            @Override
            public void onClickItem(Restaurant restaurant) {




                EventBus.getDefault().postSticky(restaurant); // dùng luôn sql
                cartPresenter.saveRestaurantOnCart(restaurant.getId(),restaurant.getName(),restaurant.getProvideType(),
                        restaurant.getImage(),restaurant.getAddress(),
                        restaurant.getPhone(),restaurant.getEmail(),
                        restaurant.getRate());
                Cart currentCart = new Cart("001",restaurant.getId());
                Toast.makeText(getContext(), restaurant.getId(), Toast.LENGTH_SHORT).show();
                EventBus.getDefault().postSticky(currentCart);

                getFragment(DetailRestaurantFragment.newInstance());
                // edge://inspect/#devices
            }
        });
    }

    @Override
    public void onShowCart(Cart cart) {

    }
}
