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
import com.example.foodorderapp.MainActivity;
import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.ListGroupAdapter;
import com.example.foodorderapp.databinding.FragmentHomeListBinding;
import com.example.foodorderapp.event.IHomeListHelper;
import com.example.foodorderapp.event.IOnClickItem;
import com.example.foodorderapp.model.GroupList;
import com.example.foodorderapp.presenter.HomeListPresenter;
import com.example.foodorderapp.detail.ListQuickDeliveriesFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class HomeListFragment extends Fragment implements IHomeListHelper {
    FragmentHomeListBinding binding;
    ListGroupAdapter groupAdapter;
    HomeListPresenter presenter;
    List<GroupList> groupList;


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
                switch (type){
                    case ListGroupAdapter.TYPE_FOOD_QUICK_DELIVERIES:
//                        getFragment(ListQuickDeliveriesFragment.newInstance());
                        Intent it = new Intent(getContext(), DetailActivity.class);
                        EventBus.getDefault().postSticky(groupList.get(ListGroupAdapter.TYPE_FOOD_QUICK_DELIVERIES-1)
                        .getRestaurants());
                        it.putExtra("quick_deliveries","show_more");
                        startActivity(it);

                        break;
                }
            }
        });
    }

    public void getFragment(Fragment fragment){

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }
}
