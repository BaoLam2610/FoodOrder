package com.example.foodorderapp.detail;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.DetailActivity;
import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.MyOrderListFoodAdapter;
import com.example.foodorderapp.databinding.FragmentOrderInformationBinding;
import com.example.foodorderapp.event.IBackFragment;
import com.example.foodorderapp.event.ICartDatabase;
import com.example.foodorderapp.event.ICheckCartInf;
import com.example.foodorderapp.event.IDetailRestaurant;
import com.example.foodorderapp.event.IOrderCart;
import com.example.foodorderapp.event.IShowAccountInf;
import com.example.foodorderapp.helper.FormatHelper;
import com.example.foodorderapp.login.LoginSignUpFragment;
import com.example.foodorderapp.map.ViewMapFragment;
import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.model.Restaurant;
import com.example.foodorderapp.model.UserAccount;
import com.example.foodorderapp.model.Voucher;
import com.example.foodorderapp.presenter.CartDatabasePresenter;
import com.example.foodorderapp.presenter.OrderInformationPresenter;

import java.util.List;

public class OrderInformationFragment extends Fragment implements IOrderCart, IDetailRestaurant,
        IShowAccountInf, ICheckCartInf, IBackFragment {
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



        presenter = new OrderInformationPresenter(this,this,this, this,this, getContext());
        cartPresenter = new CartDatabasePresenter( this,null,null,getContext());
        presenter.showDetailOrder(getCart(),getUser());

        binding.btnChooseLocation.setOnClickListener(v->{
            presenter.showRestaurantAddress(getCart());
        });

        binding.btnPayment.setOnClickListener(v->{
            String address = binding.etAddress.getText().toString();
            String user = binding.etUsername.getText().toString();
            presenter.checkInformation(user,address);
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
                .addToBackStack(TAG)
                .commit();
        fragment.setTargetFragment(OrderInformationFragment.this, 111);
    }

    private static final String TAG = "OrderInformationFragment";

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setTitle(getResources().getString(R.string.order_inf_title_attention))
                        .setMessage(getResources().getString(R.string.order_inf_content_attention))
                        .setPositiveButton(getResources().getString(R.string.cart_dialog_negative), null)
                        .setNegativeButton(getResources().getString(R.string.cart_dialog_positive), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int index = getParentFragmentManager().getBackStackEntryCount() - 1;
                                presenter.backFragment(index);
                            }
                        })
                        .create();
                alertDialog.show();
            break;
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
        String note = cart.getNote();
        if(note != null)
            binding.tvOrderNote.setText(note);
        else
            binding.tvOrderNote.setText("No notes");


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
        cartPresenter.calculationPrice(cart);
        binding.tvSubTotal.setText(FormatHelper.formatPrice(cart.getTotalPrice()));
    }

    @Override
    public void onEmptyVoucher(Cart cart) {
        // not thing
    }
    long _totalPrice;
    @Override
    public void onCalculationPrice(long price,long discountPercent, long discount,long deliveryFee, long totalPrice) {
        String titleDiscount = getResources().getString(R.string.cart_discount);
        binding.tvTitleDiscount.setText(titleDiscount + "(" + discountPercent + "%)");
        binding.tvDiscount.setText("-" + FormatHelper.formatPrice(discount));
        if(deliveryFee > 0)
            binding.tvDeliveryFee.setText("+" + FormatHelper.formatPrice(deliveryFee));
        binding.tvAmountToPay.setText(FormatHelper.formatPrice(totalPrice));
    }


    @Override
    public void onShowDetailRestaurant(Restaurant restaurant, String type) {
        switch (type){
            case "show_name":
                binding.tvResName.setText(getResources().getString(R.string.order_inf_restaurant_name) + " "
                        + restaurant.getName());
                break;
            case "show_address":
                getFragment(ViewMapFragment.newInstance(restaurant));
                break;
        }

    }

    @Override
    public void onExistsAccount(UserAccount userAccount) {
        binding.etUsername.setText(userAccount.getUsername());

        binding.etPhone.setText(userAccount.getPhone());
        binding.etPhone.setEnabled(false);
        binding.etAddress.setText(userAccount.getAddress());
    }

    @Override
    public void onStart() {
        ((DetailActivity) getActivity()).setActionBarDefault(50);
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.etAddress.setText(userAddress);
//        presenter.saveUserAddress(getUser(),userAddress);
        cartPresenter.calculationFee(getCart(),range);
    }

    @Override
    public void onNotExistsAccount() {

    }
    String userAddress;
    double range;
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == 111) {
             userAddress = data.getStringExtra("user_address");
            range = data.getDoubleExtra("range",0);

            // min 10k
            // tu 1km tro len lay 4k tien ship 2km -> 14k
            // maxL 50k
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCorrectly() {
        presenter.saveUserAddress(getUser(),userAddress);
        presenter.saveDeliveryFee(getCart(),range);
        getFragment(PaymentsFragment.newInstance(getCart(),getUser()));
    }

    @Override
    public void onIncorrect(String mes) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle(getResources().getString(R.string.title_login_dialog))
                .setMessage(mes)
                .setPositiveButton(getResources().getString(R.string.cart_dialog_negative), null)
                .create();
        alertDialog.show();
    }

    @Override
    public void onBackTwo(int index) {
        FragmentManager.BackStackEntry lastEntry = getParentFragmentManager().getBackStackEntryAt(index);
        FragmentManager.BackStackEntry secondLastEntry = getParentFragmentManager().getBackStackEntryAt(index - 1);
        FragmentManager.BackStackEntry thirdLastEntry = getParentFragmentManager().getBackStackEntryAt(index - 2);
        getParentFragmentManager().popBackStack(lastEntry.getId(), 1);
        getParentFragmentManager().popBackStack(secondLastEntry.getId(), 1);
        getParentFragmentManager().popBackStack(thirdLastEntry.getId(), 1);
    }

    @Override
    public void onBackOne() {
        getParentFragmentManager().popBackStack();
    }
}
