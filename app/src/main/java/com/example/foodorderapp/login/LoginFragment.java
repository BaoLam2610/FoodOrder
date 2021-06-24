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
import com.example.foodorderapp.MainActivity;
import com.example.foodorderapp.R;
import com.example.foodorderapp.databinding.FragmentLoginBinding;
import com.example.foodorderapp.detail.ListOrderCartFragment;
import com.example.foodorderapp.detail.OrderInformationFragment;
import com.example.foodorderapp.detail.PaymentsFragment;
import com.example.foodorderapp.event.ILogin;
import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.UserAccount;
import com.example.foodorderapp.presenter.LoginSignUpPresenter;

public class LoginFragment extends Fragment implements ILogin {
    FragmentLoginBinding binding;
    LoginSignUpPresenter presenter;

    public static LoginFragment newInstance(String phone, String username, String activity, Cart cart) {

        Bundle args = new Bundle();
        args.putString("phone", phone);
        args.putString("activity", activity);
        args.putString("username", username);
        args.putSerializable("cart", cart);
        LoginFragment fragment = new LoginFragment();
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

    public String getUsername() {
        Bundle args = getArguments();
        return args.getString("username", null);
    }

    public Cart getCart() {
        Bundle args = getArguments();
        return (Cart) args.getSerializable("cart");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        presenter = new LoginSignUpPresenter(this, getContext());
        setHasOptionsMenu(true);
        setTitleActionBar();
        binding.tvWelcome.setText(getResources().getString(R.string.login_welcome) + " " + getUsername());
        binding.btnContinue.setOnClickListener(v -> {
            String password = binding.etPassword.getText().toString();
            presenter.checkLogin(getPhone(), password);

        });

        return binding.getRoot();
    }

    public void setTitleActionBar() {
        ((DetailActivity) getActivity()).getSupportActionBar().setTitle(getContext().getResources().getString(R.string.login));
        ((DetailActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
    public void onSuccessful(int type, UserAccount userAccount) {

        switch (getNameActivity()) {
            case "main":
                getActivity().finish();
                break;
            case "detail":
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.flDetailFragment, OrderInformationFragment.newInstance(getCart(),userAccount))
                        .addToBackStack(ListOrderCartFragment.TAG)
                        .commit();
                break;
        }
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
