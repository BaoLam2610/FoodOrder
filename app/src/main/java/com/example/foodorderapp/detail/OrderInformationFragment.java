package com.example.foodorderapp.detail;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.DetailActivity;
import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.MyOrderListFoodAdapter;
import com.example.foodorderapp.databinding.FragmentOrderInformationBinding;
import com.example.foodorderapp.event.ICartDatabase;
import com.example.foodorderapp.event.IDetailRestaurant;
import com.example.foodorderapp.event.IOrderCart;
import com.example.foodorderapp.event.IShowAccountInf;
import com.example.foodorderapp.helper.FormatHelper;
import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.model.Restaurant;
import com.example.foodorderapp.model.UserAccount;
import com.example.foodorderapp.model.Voucher;
import com.example.foodorderapp.presenter.CartDatabasePresenter;
import com.example.foodorderapp.presenter.OrderInformationPresenter;

import java.util.List;

public class OrderInformationFragment extends Fragment implements IOrderCart, IDetailRestaurant, IShowAccountInf {
    FragmentOrderInformationBinding binding;
    OrderInformationPresenter presenter;
    MyOrderListFoodAdapter foodAdapter;
    CartDatabasePresenter cartPresenter;

    public static OrderInformationFragment newInstance(Cart cart, UserAccount userAccount) {

        Bundle args = new Bundle();
        args.putSerializable("cart", cart);
        args.putSerializable("user", userAccount);
        OrderInformationFragment fragment = new OrderInformationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public Cart getCart() {
        Bundle args = getArguments();
        return (Cart) args.getSerializable("cart");
    }

    public UserAccount getUser() {
        Bundle args = getArguments();
        return (UserAccount) args.getSerializable("user");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_information, container, false);
        setHasOptionsMenu(true);
        setTitleActionBar();
        presenter = new OrderInformationPresenter(this, this,this, getContext());
        cartPresenter = new CartDatabasePresenter( this,null,null,getContext());
        presenter.showDetailOrder(getCart(),getUser());

        binding.btnPayment.setOnClickListener(v->{
            getFragment(PaymentsFragment.newInstance(getCart(),getUser()));
        });
        return binding.getRoot();
    }

    public void setTitleActionBar() {
        ((DetailActivity) getActivity()).getSupportActionBar().setTitle(getContext().getResources().getString(R.string.order_inf_title));
        ((DetailActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void getFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.flDetailFragment, fragment)
                .addToBackStack(ListRestaurantFragment.TAG)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            default:
                getParentFragmentManager().popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onShowListFoodOrder(Cart cart, List<Food> foodList) {
        foodAdapter = new MyOrderListFoodAdapter(foodList, cart, getContext(), "order_inf");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.rvFoodOrder.setAdapter(foodAdapter);
        binding.rvFoodOrder.setLayoutManager(layoutManager);

        // note
        binding.tvOrderNote.setText(cart.getNote());


        // price
        if (cart.getVoucher() == null)
            binding.tvAmountToPay.setText(FormatHelper.formatPrice(cart.getTotalPrice()));
        binding.tvSubTotal.setText(FormatHelper.formatPrice(cart.getTotalPrice()));


    }

    @Override
    public void onEmptyListFoodOrder() {

    }

    @Override
    public void onShowVoucher(Voucher voucher, Cart cart) {
        cartPresenter.calculationPrice(cart,voucher);
        binding.tvSubTotal.setText(FormatHelper.formatPrice(cart.getTotalPrice()));
    }

    @Override
    public void onEmptyVoucher(Cart cart) {
        // not thing
    }

    @Override
    public void onCalculationPrice(long price,long discountPercent, long discount,long deliveryFee, long totalPrice) {
        String titleDiscount = "Discount";
        binding.tvTitleDiscount.setText(titleDiscount + "(" + discountPercent + "%)");
        binding.tvDiscount.setText("-" + FormatHelper.formatPrice(discount));
        binding.tvAmountToPay.setText(FormatHelper.formatPrice(totalPrice));
    }


    @Override
    public void onShowDetailRestaurant(Restaurant restaurant) {
        binding.tvResName.setText("Restaurant's name: " + restaurant.getName());
    }

    @Override
    public void onExistsAccount(UserAccount userAccount) {
        binding.etUsername.setText(userAccount.getUsername());

        binding.etPhone.setText(userAccount.getPhone());
        binding.etPhone.setEnabled(false);

        binding.etAddress.setText(userAccount.getAddress());
    }

    @Override
    public void onNotExistsAccount() {
        // Nothing
    }
}
