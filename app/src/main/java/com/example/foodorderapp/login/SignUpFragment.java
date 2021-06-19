package com.example.foodorderapp.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.foodorderapp.DetailActivity;
import com.example.foodorderapp.R;
import com.example.foodorderapp.databinding.FragmentSignUpBinding;
import com.example.foodorderapp.detail.ListOrderCartFragment;
import com.example.foodorderapp.detail.OrderInformationFragment;
import com.example.foodorderapp.detail.PaymentsFragment;
import com.example.foodorderapp.event.ISignUp;
import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.presenter.LoginSignUpPresenter;

public class SignUpFragment extends Fragment implements ISignUp {
    FragmentSignUpBinding binding;
    LoginSignUpPresenter presenter;

    public static SignUpFragment newInstance(String phone, String activity, Cart cart) {

        Bundle args = new Bundle();
        args.putString("phone", phone);
        args.putString("activity", activity);
        args.putSerializable("cart", cart);
        SignUpFragment fragment = new SignUpFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public String getPhone() {
        Bundle args = getArguments();
        return args.getString("phone", null);
    }

    public String getNameActivity() {
        Bundle args = getArguments();
        return args.getString("activity", null);
    }

    public Cart getCart() {
        Bundle args = getArguments();
        return (Cart) args.getSerializable("cart");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false);
        presenter = new LoginSignUpPresenter(this, getContext());
        setHasOptionsMenu(true);
        setTitleActionBar();
        binding.tvPhone.setText(getPhone());
        binding.btnContinue.setOnClickListener(v -> {
            String phone = binding.tvPhone.getText().toString();
            String username = binding.tvName.getText().toString().trim();
            String pass1 = binding.tvPassword1.getText().toString();
            String pass2 = binding.tvPassword2.getText().toString();
            presenter.checkSignUp(phone, username, pass1, pass2);

        });

        return binding.getRoot();
    }

    public void setTitleActionBar() {
        ((DetailActivity) getActivity()).getSupportActionBar().setTitle(getContext().getResources().getString(R.string.register));
        ((DetailActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onSuccessful() {
        switch (getNameActivity()) {
            case "main":
                getActivity().finish();
                break;
            case "detail":
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.flDetailFragment, OrderInformationFragment.newInstance(getCart()))
                        .addToBackStack(ListOrderCartFragment.TAG)
                        .commit();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                getParentFragmentManager().popBackStack();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFailure(String mes) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle(getResources().getString(R.string.title_login_dialog_error))
                .setMessage(mes)
                .setPositiveButton(getResources().getString(R.string.login_dialog_negative), null)
                .create();
        alertDialog.show();
    }
}
