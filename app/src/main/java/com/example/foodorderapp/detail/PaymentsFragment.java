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

import com.example.foodorderapp.R;
import com.example.foodorderapp.databinding.FragmentPaymentOrderBinding;
import com.example.foodorderapp.event.ICartDatabase;
import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.presenter.CartDatabasePresenter;

public class PaymentsFragment extends Fragment implements ICartDatabase {
    FragmentPaymentOrderBinding binding;
    CartDatabasePresenter presenter;

    public static PaymentsFragment newInstance() {

        Bundle args = new Bundle();

        PaymentsFragment fragment = new PaymentsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment_order,container,false);
        setHasOptionsMenu(true);
        presenter = new CartDatabasePresenter(this,getContext());
        binding.btnBankCards.setOnClickListener(v->{
            presenter.setCart();
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
