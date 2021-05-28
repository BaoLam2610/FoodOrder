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

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class ListRestaurantFragment extends Fragment implements IListRestaurant, ICartDatabase {
    private static final String TAG = "ListQuickDeliveriesFrag";
    FragmentListQuickDeliveriesBinding binding;
    List<Restaurant> restaurantList;
    ListRestaurantAdapter restaurantAdapter;
    CartDatabasePresenter cartPresenter;
    DetailPresenter presenter;
    SearchView searchView;
    String type;
    int countRes;
    public static ListRestaurantFragment newInstance(String type) {

        Bundle args = new Bundle();
        args.putString("list_type", type);
        ListRestaurantFragment fragment = new ListRestaurantFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public String getListType() {
        Bundle args = getArguments();
        return args.getString("list_type", "NO TITLE FOUND");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_quick_deliveries, container, false);
        presenter = new DetailPresenter(this, getContext());
        presenter.showListRestaurant(getListType()); // from API
        setHasOptionsMenu(true);
        setTitleActionBar();

        return binding.getRoot();
    }

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
        restaurantAdapter = new ListRestaurantAdapter(restaurantList, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        countRes = restaurantList.size();
        binding.tvResFound.setText(countRes + " " + getContext().getResources().getString(R.string.tv_restaurant_found));
        binding.rvListRestaurant.setAdapter(restaurantAdapter);
        binding.rvListRestaurant.setLayoutManager(layoutManager);
        cartPresenter = new CartDatabasePresenter(this, getContext());
        restaurantAdapter.setClickItemRestaurant(new IOnClickItemRestaurant() {
            @Override
            public void onClickItem(Restaurant restaurant) {
                EventBus.getDefault().postSticky(restaurant); //
                cartPresenter.saveRestaurantOnCart(restaurant);
                Cart currentCart = new Cart("001", restaurant, 0, 0);

                EventBus.getDefault().postSticky(currentCart);

                getFragment(DetailRestaurantFragment.newInstance());
                // edge://inspect/#devices
            }
        });

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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onShowCart(Cart cart) {

    }
}
