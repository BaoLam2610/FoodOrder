package com.example.foodorderapp.ui.home.menu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.DetailActivity;
import com.example.foodorderapp.MainActivity;
import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.ListGroupAdapter;
import com.example.foodorderapp.databinding.FragmentHomeListBinding;
import com.example.foodorderapp.event.ICartDatabase;
import com.example.foodorderapp.event.IHomeListHelper;
import com.example.foodorderapp.event.IOnClickItem;
import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.GroupList;
import com.example.foodorderapp.model.Restaurant;
import com.example.foodorderapp.presenter.CartDatabasePresenter;
import com.example.foodorderapp.presenter.HomeListPresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class HomeListFragment extends Fragment implements IHomeListHelper, ICartDatabase {
    FragmentHomeListBinding binding;
    ListGroupAdapter groupAdapter;
    HomeListPresenter presenter;
    CartDatabasePresenter cartPresenter;


    public static HomeListFragment newInstance() {

        Bundle args = new Bundle();

        HomeListFragment fragment = new HomeListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_list, container, false);
//        setTitleActionBar();
cartPresenter = new CartDatabasePresenter(this,getContext());
        presenter = new HomeListPresenter(getContext(), this);
//        ((MainActivity)getActivity()).setTileActionBar(getString(R.string.action_bar_home));
        presenter.showGroupList();
//        getApiRestaurant();
        return binding.getRoot();
    }


    @Override
    public void onShowGroupList(List<GroupList> groupList) {
        groupAdapter = new ListGroupAdapter(groupList, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.rvListGroup.setAdapter(groupAdapter);
        binding.rvListGroup.setLayoutManager(layoutManager);

        groupAdapter.setIOnClickItem(new IOnClickItem() {
            @Override
            public void onClickShowMore(int type, int position) {

                switch (type) {
                    case ListGroupAdapter.TYPE_FOOD_QUICK_DELIVERIES:
                        List<Restaurant> quickDeliveriesList = groupList.get(ListGroupAdapter.TYPE_FOOD_QUICK_DELIVERIES - 1)
                                .getRestaurants();

                        EventBus.getDefault().postSticky(quickDeliveriesList);
                        Intent it = new Intent(getContext(), DetailActivity.class);
                        it.putExtra("quick_deliveries", "show_more");
//                        it.putParcelableArrayListExtra("quick_deliveries_list", (ArrayList<? extends Parcelable>) quickDeliveriesList);
                        startActivity(it);

                        break;
                    case ListGroupAdapter.TYPE_FOOD_BEST_RATED:
                        List<Restaurant> restaurantList = groupList.get(ListGroupAdapter.TYPE_FOOD_BEST_RATED - 1)
                                .getRestaurants();

                        List<Restaurant> bestRatedList = new ArrayList<>();
                        for (int i = 0; i < restaurantList.size(); i++) {
                            if(restaurantList.get(i).getRate()>= 4.0)
                                bestRatedList.add(restaurantList.get(i));
                        }

                        EventBus.getDefault().postSticky(bestRatedList);
                        Intent it1 = new Intent(getContext(), DetailActivity.class);
                        it1.putExtra("best_rated", "show_more");
                        startActivity(it1);
                        break;
                }
            }
        });
    }

    public void setTitleActionBar(){
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getContext().getString(R.string.action_bar_home));
    }

    public void getFragment(Fragment fragment) {

//        getActivity().getSupportFragmentManager().beginTransaction()
//                .add(R.id.vpFragment, fragment)
//                .addToBackStack(null)
//                .commit();

        getChildFragmentManager().beginTransaction()
                .replace(R.id.vpFragment, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onShowCart(Cart cart) {

    }
}
