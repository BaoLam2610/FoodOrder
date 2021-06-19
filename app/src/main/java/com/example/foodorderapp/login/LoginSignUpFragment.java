package com.example.foodorderapp.login;

import android.content.DialogInterface;
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
import com.example.foodorderapp.databinding.FragmentLoginSignUpBinding;
import com.example.foodorderapp.detail.ListOrderCartFragment;
import com.example.foodorderapp.event.ILogin;
import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.presenter.LoginSignUpPresenter;

public class LoginSignUpFragment extends Fragment implements ILogin {
    FragmentLoginSignUpBinding binding;
    LoginSignUpPresenter presenter;
    String phone;

    public static LoginSignUpFragment newInstance(String activity, Cart cart) {

        Bundle args = new Bundle();
        args.putString("activity", activity);
        args.putSerializable("cart", cart);
        LoginSignUpFragment fragment = new LoginSignUpFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public String getNameActivity() {
        Bundle args = getArguments();
        return args.getString("activity");
    }

    public Cart getCart() {
        Bundle args = getArguments();
        return (Cart) args.getSerializable("cart");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_sign_up, container, false);
        presenter = new LoginSignUpPresenter(this, getContext());
        setHasOptionsMenu(true);
        setTitleActionBar();


        binding.btnContinue.setOnClickListener(v -> {
            phone = binding.tvPhoneNumber.getText().toString();
            presenter.checkLogin(phone);
        });
        return binding.getRoot();
    }

    public void setTitleActionBar() {
        ((DetailActivity) getActivity()).getSupportActionBar().setTitle(getContext().getResources().getString(R.string.login));
        ((DetailActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onSuccessful(int type, String username) {
        switch (type) {
            case 0: // not exists phone -> sign up
                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setTitle(getResources().getString(R.string.title_login_dialog))
                        .setMessage(R.string.login_dialog_register_required)
                        .setPositiveButton(getResources().getString(R.string.login_dialog_negative), null)
                        .setNegativeButton(getResources().getString(R.string.register), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getFragment(SignUpFragment.newInstance(phone,getNameActivity(),getCart()));
                            }
                        })
                        .create();
                alertDialog.show();
                break;
            case 1: // exists phone -> login
                getFragment(LoginFragment.newInstance(phone, username,getNameActivity(),getCart()));
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                if (getNameActivity().equals("detail"))
                    getParentFragmentManager().popBackStack();
                if (getNameActivity().equals("main"))
                    getActivity().finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getFragment(Fragment fragment) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.flDetailFragment, fragment)
                        .addToBackStack(ListOrderCartFragment.TAG)
                        .commit();
    }

}
