package com.example.foodorderapp.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.foodorderapp.DetailActivity;
import com.example.foodorderapp.R;
import com.example.foodorderapp.databinding.FragmentPaymentOrderBinding;
import com.example.foodorderapp.event.ICartDatabase;
import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.UserAccount;
import com.example.foodorderapp.presenter.CartDatabasePresenter;

public class PaymentsFragment extends Fragment implements ICartDatabase {
    FragmentPaymentOrderBinding binding;
    CartDatabasePresenter presenter;

    public static PaymentsFragment newInstance(Cart cart,UserAccount userAccount) {

        Bundle args = new Bundle();
        args.putSerializable("cart", cart);
        args.putSerializable("user", userAccount);
        PaymentsFragment fragment = new PaymentsFragment();
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
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment_order,container,false);
        setHasOptionsMenu(true);
        presenter = new CartDatabasePresenter(this,getContext());
        binding.btnBankCards.setOnClickListener(v->{
            presenter.setCart(getUser());
            presenter.destroyAllData();
            getFragment(PaymentSuccessfulFragment.newInstance());
        });
        return binding.getRoot();
    }
    public void getFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.flDetailFragment, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void setTitleActionBar() {
        ((DetailActivity) getActivity()).getSupportActionBar().setTitle(getContext().getResources().getString(R.string.payment_title));
        ((DetailActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
    public void onShowCart(Cart cart) {

    }
}
