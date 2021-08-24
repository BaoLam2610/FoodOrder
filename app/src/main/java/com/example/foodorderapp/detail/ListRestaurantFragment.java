package com.example.foodorderapp.detail;

import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.DetailActivity;
import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.ListRestaurantAdapter;
import com.example.foodorderapp.databinding.FragmentListQuickDeliveriesBinding;
import com.example.foodorderapp.event.ICartDatabase;
import com.example.foodorderapp.event.IListRestaurant;
import com.example.foodorderapp.event.IOnClickItemRestaurant;
import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.Restaurant;
import com.example.foodorderapp.presenter.CartDatabasePresenter;
import com.example.foodorderapp.presenter.DetailPresenter;
import com.example.foodorderapp.presenter.ListFoodPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class ListRestaurantFragment extends Fragment implements IListRestaurant, ICartDatabase {
    public static final String TAG = "ListQuickDeliveriesFrag";
    FragmentListQuickDeliveriesBinding binding;
    List<Restaurant> restaurants;
    ListRestaurantAdapter restaurantAdapter;
    ListFoodPresenter restaurantPresenter;
    CartDatabasePresenter cartPresenter;
    DetailPresenter presenter;
    SearchView searchView;
    String type;
    int countRes;

    public static ListRestaurantFragment newInstance(String type) {

        Bundle args = new Bundle();
        args.putString("list_type_check", type);
//        args.putParcelableArrayList("restaurant_list", (ArrayList<? extends Parcelable>) restaurantList);
        ListRestaurantFragment fragment = new ListRestaurantFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public String getListType() {
        Bundle args = getArguments();
        return args.getString("list_type_check", "NO TITLE FOUND");
    }
//    public List<Restaurant> getRestaurantList(){
//        Bundle args = getArguments();
//        return args.getParcelableArrayList("restaurant_list");
//    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_quick_deliveries, container, false);

        String type = getListType();
        System.out.println(type);


        presenter = new DetailPresenter(this, getContext());
//        presenter.showListRestaurant(getRestaurantList(),getListType()); // bundle
//        presenter.saveOnDatabase();
        presenter.showListRestaurant(getListType()); // from API
//        if (getListType() != null)
//            presenter.showListRestaurant(getListType()); // from API
//        restaurantPresenter = new ListRestaurantPresenter(this,getContext());
//        restaurantPresenter.showListRestaurant(getListType());
        setHasOptionsMenu(true);
        setTitleActionBar();

        return binding.getRoot();
    }
//
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    @Override
//    public void onResume() {
//        super.onResume();
//        presenter.showListRestaurant(getListType()); // from API
//    }

    public void setTitleActionBar() {
        String title = "";
        switch (getListType()) {
            case "0":
                title = getContext().getResources().getString(R.string.home_list_title_quick_delivery);
                break;
            case "1":
                title = getContext().getResources().getString(R.string.home_list_title_best_rated);
                break;
        }
        ((DetailActivity) getActivity()).getSupportActionBar().setTitle(title);
        ((DetailActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void getFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.flDetailFragment, fragment)
                .addToBackStack(TAG)
                .commit();
    }


    @Override
    public void onShowListRestaurant(List<Restaurant> restaurantList) {
        if(restaurantList!=null) {
            restaurants = restaurantList;
        }
        restaurantAdapter = new ListRestaurantAdapter(restaurants, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        restaurantAdapter.notifyDataSetChanged();
        countRes = restaurants.size();
        binding.tvResFound.setText(countRes + " " + getContext().getResources().getString(R.string.tv_restaurant_found));
        binding.rvListRestaurant.setAdapter(restaurantAdapter);
        binding.rvListRestaurant.setLayoutManager(layoutManager);

        cartPresenter = new CartDatabasePresenter(this, getContext());
        restaurantAdapter.setClickItemRestaurant(new IOnClickItemRestaurant() {
            @Override
            public void onClickItem(Restaurant restaurant) {
                EventBus.getDefault().postSticky(restaurant); //

                cartPresenter.saveRestaurantOnCart(restaurant);
                Cart currentCart = new Cart(restaurant, 0, 0);
                cartPresenter.saveCart(currentCart);
                EventBus.getDefault().postSticky(currentCart);
                EventBus.getDefault().postSticky("default");
                getFragment(DetailRestaurantFragment.newInstance());
                // edge://inspect/#devices
            }
        });

    }

    @Override
    public void onErrorListRestaurant() {
        binding.rvListRestaurant.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.detail_search, menu);
        SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.ac_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                restaurantAdapter.getFilter().filter(query, new Filter.FilterListener() {
                    @Override
                    public void onFilterComplete(int count) {
                        binding.tvResFound.setText(count + " " + getContext().getResources().getString(R.string.tv_restaurant_found));
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                restaurantAdapter.getFilter().filter(newText);
                restaurantAdapter.getFilter().filter(newText, new Filter.FilterListener() {
                    @Override
                    public void onFilterComplete(int count) {
                        binding.tvResFound.setText(count + " " + getContext().getResources().getString(R.string.tv_restaurant_found));
                    }
                });
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                binding.tvResFound.setText(countRes + " " + getContext().getResources().getString(R.string.tv_restaurant_found));
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.ac_search:
                Toast.makeText(getContext(), "search", Toast.LENGTH_SHORT).show();
                return true;
            default:
                getActivity().finish();
//                EventBus.getDefault().removeAllStickyEvents();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onShowCart(Cart cart) {

    }

    //
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getListRestaurant(List<Restaurant> list) {
        if(list!=null) {
            restaurants = list;
        }
        EventBus.getDefault().removeStickyEvent(list);
    }


    @Override
    public void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();

    }
}
