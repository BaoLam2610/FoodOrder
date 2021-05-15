package com.example.foodorderapp.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.TabFoodCategoryAdapter;
import com.example.foodorderapp.databinding.FragmentDetailRestaurantBinding;
import com.example.foodorderapp.event.ICartDatabase;
import com.example.foodorderapp.event.IDetailRestaurant;
import com.example.foodorderapp.event.IOnBackPressed;
import com.example.foodorderapp.event.IOnShowCart;
import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.model.Restaurant;
import com.example.foodorderapp.presenter.CartDatabasePresenter;
import com.example.foodorderapp.presenter.DetailPresenter;
import com.example.foodorderapp.sql.CartDatabaseHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class DetailRestaurantFragment extends Fragment implements IDetailRestaurant, ICartDatabase, IOnBackPressed {
    private static final String TAG = "DetailRestaurantFragment";
    FragmentDetailRestaurantBinding binding;
    TabFoodCategoryAdapter categoryAdapter;
    DetailPresenter presenter, presenter1;
    CartDatabasePresenter cartPresenter;
    List<Food> foodList;
    Cart cart;
    Restaurant currentRes;
    CartDatabaseHelper helper = new CartDatabaseHelper(getContext());
    Cart currentCart;

    public static DetailRestaurantFragment newInstance() {

        Bundle args = new Bundle();

        DetailRestaurantFragment fragment = new DetailRestaurantFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_restaurant, container, false);
        categoryAdapter = new TabFoodCategoryAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, getContext());
        binding.vpListFood.setAdapter(categoryAdapter);
        binding.tlFoodCategory.setupWithViewPager(binding.vpListFood);

        presenter = new DetailPresenter((IDetailRestaurant) this, getContext());
        presenter.showDetailRestaurant();
//        presenter1 = new DetailPresenter((IOnShowCart) this, getContext());
        cartPresenter = new CartDatabasePresenter(this, getContext());


//        presenter1.showTotalItemCart();


        return binding.getRoot();
    }

    public void getFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.flDetailFragment, fragment)
                .addToBackStack(TAG)
                .commit();
    }

    @Override
    public void onShowDetailRestaurant(Restaurant restaurant) {
        currentRes = restaurant;
        binding.tvResName.setText(restaurant.getName());
        binding.tvResProvide.setText(restaurant.getProvideType());
        binding.tvResAddress.setText(restaurant.getAddress());
        binding.rbResRate.setText(restaurant.getRate() + "");

        String title = categoryAdapter.getPageTitle(0).toString();
        binding.tvItemCart.setOnClickListener(v-> Toast.makeText(getContext(), "2" + title, Toast.LENGTH_SHORT).show());

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getListFoodOrder(Cart cart) {
        currentCart = cart;
//        currentCart.setIdCart(cart.getIdCart());
        if(cart.getAmount()==0)
            return;

        currentCart.setAmount(cart.getAmount());
        currentCart.setTotalPrice(cart.getTotalPrice());
        binding.btnViewCart.setOnClickListener(v -> {
            Toast.makeText(getContext(), currentCart.getIdRes(), Toast.LENGTH_SHORT).show();
            EventBus.getDefault().postSticky(currentCart);
            getFragment(ListOrderCartFragment.newInstance());
            if(!cartPresenter.checkCart(cart))
                cartPresenter.saveCart(currentCart.getIdCart(), currentCart.getIdRes(),
                        currentCart.getAmount(), (int) currentCart.getTotalPrice());
            else
                cartPresenter.editCart(cart);
        });

        binding.tvItemCart.setText(currentCart.getAmount() + " Item(s)");


        binding.tvTotalCost.setText(currentCart.getTotalPrice() + "đ");
    }

    @Override
    public void onStart() {

        super.onStart();
        EventBus.getDefault().register(this);
    }


    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        if(isRemoving()){
//            cartPresenter.destroyRestaurant(currentRes);
//            Toast.makeText(getContext(), "Back", Toast.LENGTH_SHORT).show();
//        }
//    }




    @Override
    public boolean onBackPressed() {
        Toast.makeText(getContext(), "Back pressed", Toast.LENGTH_SHORT).show();
        cartPresenter.destroyAllData(currentRes, currentCart);
        binding.tvTotalCost.setText("0đ");
        binding.tvItemCart.setText("Item");

        Bundle bundle = getArguments();
        if(bundle!=null){
            String check = bundle.getString("quick_deliveries");
            if(check!=null)
                getActivity().finish();
            else
                getParentFragmentManager().popBackStack();
        }

        return true;
    }

    @Override
    public void onShowCart(Cart cart) {

    }
}
