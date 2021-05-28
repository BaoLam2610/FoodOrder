package com.example.foodorderapp.ui.home.menu;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.ListGroupAdapter;
import com.example.foodorderapp.databinding.FragmentHomeListBinding;
import com.example.foodorderapp.event.IHomeListHelper;
import com.example.foodorderapp.event.IOnClickItem;
import com.example.foodorderapp.model.GroupList;
import com.example.foodorderapp.presenter.HomeListPresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class HomeListFragment extends Fragment implements IHomeListHelper {
    FragmentHomeListBinding binding;
    ListGroupAdapter groupAdapter;
    HomeListPresenter presenter;


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


        presenter = new HomeListPresenter(getContext(), this);

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
        Intent it = new Intent(getContext(), DetailActivity.class);
        groupAdapter.setIOnClickItem(new IOnClickItem() {
            @Override
            public void onClickShowMore(int type, int position) {

                switch (type) {
                    case ListGroupAdapter.TYPE_FOOD_QUICK_DELIVERIES:
                        EventBus.getDefault().postSticky(groupList.get(ListGroupAdapter.TYPE_FOOD_QUICK_DELIVERIES - 1)
                                .getRestaurants());
                        it.putExtra("quick_deliveries", "show_more");
                        startActivity(it);

                        break;
                    case ListGroupAdapter.TYPE_FOOD_BEST_RATED:
                        EventBus.getDefault().postSticky(groupList.get(ListGroupAdapter.TYPE_FOOD_BEST_RATED - 1)
                                .getRestaurants());
                        it.putExtra("best_rated", "show_more");
                        startActivity(it);
                        break;
                }
            }
        });
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
}
