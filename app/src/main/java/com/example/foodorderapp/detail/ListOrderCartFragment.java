package com.example.foodorderapp.detail;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.DetailActivity;
import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.OrderCartAdapter;
import com.example.foodorderapp.databinding.FragmentOrderCartBinding;
import com.example.foodorderapp.dialog.VoucherBottomDialog;
import com.example.foodorderapp.event.IOnClickDeleteOrder;
import com.example.foodorderapp.event.IOnClickItemVoucher;
import com.example.foodorderapp.event.IOnShowCart;
import com.example.foodorderapp.event.IOrderCart;
import com.example.foodorderapp.event.IShowAccountInf;
import com.example.foodorderapp.event.IVoucher;
import com.example.foodorderapp.helper.FormatHelper;
import com.example.foodorderapp.login.LoginSignUpFragment;
import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.model.UserAccount;
import com.example.foodorderapp.model.Voucher;
import com.example.foodorderapp.presenter.CartDatabasePresenter;
import com.example.foodorderapp.presenter.DetailPresenter;
import com.example.foodorderapp.presenter.LoginSignUpPresenter;
import com.example.foodorderapp.presenter.VoucherPresenter;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class ListOrderCartFragment extends Fragment implements IOrderCart, IOnShowCart, IShowAccountInf, IVoucher {

    public static final String TAG = "ListOrderCartFragment";
    DecimalFormat df = new DecimalFormat("###,###");
    FragmentOrderCartBinding binding;
    OrderCartAdapter orderCartAdapter;
    CartDatabasePresenter cartPresenter;
    DetailPresenter presenter;
    VoucherPresenter voucherPresenter;
    LoginSignUpPresenter loginPresenter;
    Cart tempCart;
    VoucherBottomDialog voucherDialog;

    public static ListOrderCartFragment newInstance() {

        Bundle args = new Bundle();

        ListOrderCartFragment fragment = new ListOrderCartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_cart, container, false);
        cartPresenter = new CartDatabasePresenter(this, null, null, getContext());
        presenter = new DetailPresenter(this, this, getContext(), cartPresenter);
        loginPresenter = new LoginSignUpPresenter(this, getContext());
        voucherPresenter = new VoucherPresenter(this, getContext());
        setHasOptionsMenu(true);
        setTitleActionBar();
        cartPresenter.showListOrder();


        return binding.getRoot();
    }

    @Override
    public void onShowListFoodOrder(Cart cart, List<Food> foodList) {
        tempCart = cart;
        binding.rvFoodOrderCart.setVisibility(View.VISIBLE);
        binding.layoutEmpty.setVisibility(View.GONE);

        orderCartAdapter = new OrderCartAdapter(foodList, cart, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.rvFoodOrderCart.setAdapter(orderCartAdapter);
        binding.rvFoodOrderCart.setLayoutManager(layoutManager);
        orderCartAdapter.setIOnClickDeleteOrder(new IOnClickDeleteOrder() {
            @Override
            public void onClickDeleteOrder(Food food) {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setTitle(getResources().getString(R.string.title_login_dialog))
                        .setMessage(getResources().getString(R.string.cart_dialog_content_delete_order))
                        .setNegativeButton(getResources().getString(R.string.cart_dialog_positive), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                foodList.remove(food);
                                presenter.deleteOrder(food, cart);
                                if (foodList.size() == 0)
                                    onEmptyListFoodOrder();
                            }
                        })
                        .setPositiveButton(getResources().getString(R.string.login_dialog_negative), null)
                        .create();
                alertDialog.show();
            }
        });

        binding.tvSubTotal.setText(FormatHelper.formatPrice(cart.getTotalPrice()));
//        binding.tvCoupon.setText(cart.getTotalPrice()+"");
        if(cart.getVoucher()==null)
            binding.tvAmountToPay.setText(FormatHelper.formatPrice(cart.getTotalPrice()));
        binding.btnOrder.setOnClickListener(v -> {
            if (foodList.size() == 0) {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setTitle(getResources().getString(R.string.cart_dialog_error_title))
                        .setMessage(getResources().getString(R.string.cart_dialog_error_content))
                        .setNegativeButton(getResources().getString(R.string.cart_dialog_negative), null)
                        .create();
                alertDialog.show();
                return;
            }
            EventBus.getDefault().postSticky(cart);
            loginPresenter.showAccountInformation();
        });

        binding.btnDeleteVoucher.setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                    .setTitle(getResources().getString(R.string.title_login_dialog))
                    .setMessage(getResources().getString(R.string.cart_dialog_content_delete_order))
                    .setNegativeButton(getResources().getString(R.string.cart_dialog_positive), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            binding.layoutShowVoucher.setVisibility(View.INVISIBLE);
                            binding.btnPromoCode.setVisibility(View.VISIBLE);
                            cart.setVoucher(null);
                            cartPresenter.editCart(cart);
                            binding.tvDiscount.setText("");
                            binding.tvAmountToPay.setText(FormatHelper.formatPrice(cart.getTotalPrice()));
                            binding.tvTotalCost.setText(binding.tvAmountToPay.getText().toString());
                        }
                    })
                    .setPositiveButton(getResources().getString(R.string.login_dialog_negative), null)
                    .create();
            alertDialog.show();
        });


        binding.btnPromoCode.setOnClickListener(v -> {
            voucherPresenter.showListVoucher(cart);

        });
        binding.etOrderNote.setText(cart.getNote());
        binding.tvItemCart.setText(cart.getAmount() + " Item(s)");
        binding.tvTotalCost.setText(FormatHelper.formatPrice(cart.getTotalPrice()));
    }

    @Override
    public void onEmptyListFoodOrder() {
        binding.rvFoodOrderCart.setVisibility(View.GONE);
        binding.layoutEmpty.setVisibility(View.VISIBLE);
        binding.tvItemCart.setText(0 + " " + "Item(s)");
        binding.tvTotalCost.setText("0đ");
        binding.btnOrder.setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                    .setTitle(getResources().getString(R.string.cart_dialog_error_title))
                    .setMessage(getResources().getString(R.string.cart_dialog_error_content))
                    .setNegativeButton(getResources().getString(R.string.cart_dialog_negative), null)
                    .create();
            alertDialog.show();
        });
        binding.layoutShowVoucher.setVisibility(View.INVISIBLE);
        binding.btnPromoCode.setVisibility(View.VISIBLE);
        binding.btnPromoCode.setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                    .setTitle(getResources().getString(R.string.cart_dialog_error_title))
                    .setMessage(getResources().getString(R.string.cart_dialog_content_require_promo))
                    .setNegativeButton(getResources().getString(R.string.cart_dialog_negative), null)
                    .create();
            alertDialog.show();
        });

    }

    @Override
    public void onShowVoucher(Voucher voucher, Cart cart) {
        binding.btnPromoCode.setVisibility(View.INVISIBLE);
        binding.layoutShowVoucher.setVisibility(View.VISIBLE);
        binding.tvVoucher.setText(voucher.getId());

        long price = cart.getTotalPrice();
        long discount = (long) ((voucher.getDiscount() / 100.0f) * price);
        binding.tvDiscount.setText("-" + FormatHelper.formatPrice(discount));

        binding.tvAmountToPay.setText(FormatHelper.formatPrice(price - discount));
        binding.tvTotalCost.setText(binding.tvAmountToPay.getText().toString());
    }

    public void setTitleActionBar() {
//        ((DetailActivity) getActivity()).getSupportActionBar().setTitle(getContext().getResources().getString(R.string.detail_restaurant));
        ((DetailActivity) getActivity()).getSupportActionBar().setTitle(getContext().getResources().getString(R.string.cart_title));
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
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onShowCart(int totalAmount, long totalPrice) {
        orderCartAdapter.notifyDataSetChanged();
        binding.tvSubTotal.setText(FormatHelper.formatPrice(totalPrice));
        binding.tvAmountToPay.setText(FormatHelper.formatPrice(totalPrice));
        binding.tvItemCart.setText(totalAmount + " Item(s)");
        binding.tvTotalCost.setText(FormatHelper.formatPrice(totalPrice));
    }

    @Override
    public void onExistsAccount(UserAccount userAccount) {
        getFragment(PaymentsFragment.newInstance());
    }

    // payment
    @Override
    public void onNotExistsAccount() {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle(getResources().getString(R.string.title_login_dialog))
                .setMessage(getResources().getString(R.string.title_login_content))
                .setNegativeButton(getResources().getString(R.string.login_dialog_positive), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getFragment(LoginSignUpFragment.newInstance("detail",tempCart));
                    }
                })
                .setPositiveButton(getResources().getString(R.string.login_dialog_negative), null)
                .setNeutralButton("No login required", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String note = binding.etOrderNote.getText().toString();
                        tempCart.setNote(note);
                        cartPresenter.editCart(tempCart);
                        getFragment(OrderInformationFragment.newInstance(tempCart));
                    }
                })
                .create();
        alertDialog.show();
    }

    @Override
    public void onShowListVoucher(List<Voucher> voucherList, Cart cart) {
        voucherDialog = new VoucherBottomDialog(voucherList, new IOnClickItemVoucher() {
            @Override
            public void onClickItem(Voucher voucher) {
                binding.btnPromoCode.setVisibility(View.INVISIBLE);
                binding.layoutShowVoucher.setVisibility(View.VISIBLE);
                binding.tvVoucher.setText(voucher.getId());
                cart.setVoucher(voucher);
                cartPresenter.editCart(cart);

                long price = cart.getTotalPrice();
                long discount = (long) ((voucher.getDiscount() / 100.0f) * price);
                binding.tvDiscount.setText("-" + FormatHelper.formatPrice(discount));


                binding.tvAmountToPay.setText(FormatHelper.formatPrice(price - discount));
                binding.tvTotalCost.setText(binding.tvAmountToPay.getText().toString());
                voucherDialog.dismiss();
            }
        });
        voucherDialog.show(getParentFragmentManager(), getTag());
    }
}
