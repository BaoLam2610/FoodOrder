package com.example.foodorderapp.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.bumptech.glide.Glide;
import com.example.foodorderapp.DetailActivity;
import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.TabFoodCategoryAdapter;
import com.example.foodorderapp.databinding.FragmentDetailRestaurantBinding;
import com.example.foodorderapp.event.ICartDatabase;
import com.example.foodorderapp.event.ICheckFavorite;
import com.example.foodorderapp.event.IDetailRestaurant;
import com.example.foodorderapp.event.IOnBackPressed;
import com.example.foodorderapp.event.IOnModifyFavorite;
import com.example.foodorderapp.event.IRestoreList;
import com.example.foodorderapp.helper.FormatHelper;
import com.example.foodorderapp.map.ViewMapFragment;
import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.model.Restaurant;
import com.example.foodorderapp.presenter.CartDatabasePresenter;
import com.example.foodorderapp.presenter.DetailPresenter;
import com.example.foodorderapp.presenter.FavoriteRestaurantPresenter;
import com.example.foodorderapp.sql.CartDatabaseHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class DetailRestaurantFragment extends Fragment implements IDetailRestaurant, ICartDatabase, IOnBackPressed,
        IRestoreList, IOnModifyFavorite, ICheckFavorite {
    private static final String TAG = "DetailRestaurantFragment";
    FragmentDetailRestaurantBinding binding;
    TabFoodCategoryAdapter categoryAdapter;
    DetailPresenter presenter;
    FavoriteRestaurantPresenter favoritePresenter;
    CartDatabasePresenter cartPresenter;
    List<Restaurant> restaurants;
    Cart cart;
    Restaurant currentRes;
    CartDatabaseHelper helper = new CartDatabaseHelper(getContext());
    Cart currentCart;
    List<Food> foodList;

    public static DetailRestaurantFragment newInstance() {

        Bundle args = new Bundle();
//        args.putString("list_type", type);
        DetailRestaurantFragment fragment = new DetailRestaurantFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public String getListType() {
        Bundle args = getArguments();
        return args.getString("list_type", "NO TITLE FOUND");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_restaurant, container, false);

        setHasOptionsMenu(true);
        setTitleActionBar();
        presenter = new DetailPresenter(this, getContext(), this);
        favoritePresenter = new FavoriteRestaurantPresenter(this, this, getContext());
        presenter.showDetailRestaurant();
//        presenter1 = new DetailPresenter((IOnShowCart) this, getContext());
        cartPresenter = new CartDatabasePresenter(this, getContext());



        return binding.getRoot();
    }

    public void setTitleActionBar() {
        ((DetailActivity) getActivity()).getSupportActionBar().setTitle(getContext().getResources().getString(R.string.detail_restaurant));
//        ((DetailActivity) getActivity()).getSupportActionBar().setTitle("");
        ((DetailActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void getFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.flDetailFragment, fragment)
                .addToBackStack(TAG)
                .commit();
        fragment.setTargetFragment(DetailRestaurantFragment.this, 100);
    }


    @Override
    public void onShowDetailRestaurant(Restaurant restaurant,String type) {
        currentRes = restaurant;
        binding.tvResName.setText(restaurant.getName());
        Glide.with(getContext()).load(restaurant.getImage())
                .centerCrop()   // căn ảnh
//                    .placeholder(R.drawable.ic_baseline_image_24)  // đợi load ảnh
//                    .error(R.drawable.ic_baseline_error_24)        // load ảnh bị lỗi
                .into(binding.ivResImage);
        binding.tvResProvide.setText(restaurant.getProvideType());
        binding.tvResAddress.setText(" " + restaurant.getAddress().getAddress());
        binding.tvResPhone.setText(" " + restaurant.getPhone());
        binding.tvResEmail.setText(" " + restaurant.getEmail());
        binding.tvResRate.setText(restaurant.getRate() + " ");
        binding.rbResRate.setRating((float)restaurant.getRate());



        foodList = restaurant.getFoodList();
        categoryAdapter = new TabFoodCategoryAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                getContext());
        binding.vpListFood.setAdapter(categoryAdapter);
        binding.tlFoodCategory.setupWithViewPager(binding.vpListFood);

        binding.btnFindLocation.setOnClickListener(v->{
            getFragment(ViewMapFragment.newInstance(restaurant));
        });
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getListFoodOrder(Cart cart) {
        currentCart = cart;
//        if (cart.getAmount() == 0)
//            return;

        currentCart.setAmount(cart.getAmount());
        currentCart.setTotalPrice(cart.getTotalPrice());
        binding.btnViewCart.setOnClickListener(v -> {

            EventBus.getDefault().postSticky(currentCart);
            getFragment(ListOrderCartFragment.newInstance());
//            if (!cartPresenter.checkCart(cart))
//                cartPresenter.saveCart(cart);
//            else
//                cartPresenter.editCart(cart);
        });

        binding.tvItemCart.setText(currentCart.getAmount() + " " + getContext().getString(R.string.cart_items));


        binding.tvTotalCost.setText(FormatHelper.formatPrice(currentCart.getTotalPrice()));
    }

    @Override
    public void onStart() {
        ((DetailActivity) getActivity()).setActionBarDefault(50);
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getListRestaurant(List<Restaurant> list) {
//        if (list != null)
//            restaurantList = list;
        restaurants = list;
    }

    @Override
    public void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
    }


    public void resetFragment() {

        Bundle bundle = getArguments();
        if (bundle != null) {
            String check = bundle.getString("back_pressed");
            if (check != null)
                ((DetailActivity) getActivity()).finish();
            else {
                getParentFragmentManager().popBackStack();
            }
        }

        List<Food> foodList = currentRes.getFoodList();
        if(foodList!=null)
            for (int i = 0; i < foodList.size(); i++) {
                foodList.get(i).setCount(0);
            }

        if (restaurants != null)
            EventBus.getDefault().postSticky(restaurants);


    }

    @Override
    public boolean onBackPressed() {

//        cartPresenter
        cartPresenter.destroyAllData();

        resetFragment();
//        presenter.restoreList();

        return true;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.detail_favorite, menu);
        MenuItem menuItem = menu.findItem(R.id.ac_Favorite);
        menuItem.setActionView(R.layout.notification_badge_favorite);
        View view = menuItem.getActionView();
        ImageView ivChecked, ivUnchecked;
        ivChecked = view.findViewById(R.id.ivFavoriteChecked);
        ivUnchecked = view.findViewById(R.id.ivFavoriteUnchecked);
        favoritePresenter.checkFavoriteRestaurant(currentRes, ivChecked, ivUnchecked);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case android.R.id.home:
                cartPresenter.destroyAllData();
                resetFragment();
//                presenter.restoreList();
                break;
            case R.id.ac_Favorite:
                item.setActionView(R.layout.notification_badge_favorite);
                View view = item.getActionView();

                ImageView ivFavoriteUnchecked, ivFavoriteChecked;
                ivFavoriteUnchecked = view.findViewById(R.id.ivFavoriteUnchecked);
                ivFavoriteChecked = view.findViewById(R.id.ivFavoriteChecked);
                // first touch
                favoritePresenter.checkFavoriteRestaurant(currentRes,ivFavoriteChecked,ivFavoriteUnchecked);
                boolean checked = ivFavoriteChecked.getVisibility() == View.VISIBLE ? true : false;
                boolean unchecked = ivFavoriteUnchecked.getVisibility() == View.VISIBLE ? true : false;
                if(unchecked) {

                    favoritePresenter.saveFavoriteRestaurant(currentRes);
                    ivFavoriteChecked.setVisibility(View.VISIBLE);
                    ivFavoriteUnchecked.setVisibility(View.INVISIBLE);
                }
                if(checked){
                    favoritePresenter.destroyFavoriteRestaurant(currentRes);
                    ivFavoriteChecked.setVisibility(View.INVISIBLE);
                    ivFavoriteUnchecked.setVisibility(View.VISIBLE);
                }
                ivFavoriteUnchecked.setOnClickListener(v -> {
                    favoritePresenter.saveFavoriteRestaurant(currentRes);
                    ivFavoriteUnchecked.setVisibility(View.INVISIBLE);
                    ivFavoriteChecked.setVisibility(View.VISIBLE);
                });

                ivFavoriteChecked.setOnClickListener(v -> {
                    favoritePresenter.destroyFavoriteRestaurant(currentRes);
                    ivFavoriteUnchecked.setVisibility(View.VISIBLE);
                    ivFavoriteChecked.setVisibility(View.INVISIBLE);
                });


                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onShowCart(Cart cart) {

    }

    @Override
    public void onRestore(List<Restaurant> restaurantList) {
        restaurants = restaurantList;
    }

    @Override
    public void onSaveFavorite() {
        Toast.makeText(getContext(), getContext().getString(R.string.favorite_insert), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyFavorite() {
        Toast.makeText(getContext(), getContext().getString(R.string.favorite_remove), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChecked(ImageView ivChecked, ImageView ivUnchecked) {
        ivChecked.setVisibility(View.VISIBLE);
        ivUnchecked.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onUnchecked(ImageView ivChecked, ImageView ivUnchecked) {
        ivChecked.setVisibility(View.INVISIBLE);
        ivUnchecked.setVisibility(View.VISIBLE);
    }
}
