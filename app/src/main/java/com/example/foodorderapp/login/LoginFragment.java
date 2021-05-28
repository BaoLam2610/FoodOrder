package com.example.foodorderapp.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.foodorderapp.R;
import com.example.foodorderapp.databinding.FragmentLoginBinding;
import com.example.foodorderapp.event.ILogin;
import com.example.foodorderapp.presenter.LoginSignUpPresenter;

import org.greenrobot.eventbus.EventBus;

public class LoginFragment extends Fragment implements ILogin {
    FragmentLoginBinding binding;
    LoginSignUpPresenter presenter;

    public static LoginFragment newInstance(String phone) {
        
        Bundle args = new Bundle();
        args.putString("phone", phone);
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public String getPhone() {
        Bundle args = getArguments();
        return args.getString("phone", null);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login,container,false);
        presenter = new LoginSignUpPresenter(this,getContext());
        binding.btnContinue.setOnClickListener(v -> {
            String password = binding.etPassword.getText().toString();
            presenter.checkLogin(getPhone(),password);
        });

        return binding.getRoot();
    }

    @Override
    public void onSuccessful(int type) {
        getParentFragmentManager().beginTransaction()
                .remove(this)
                .commit();
        EventBus.getDefault().postSticky(0);
    }

    @Override
    public void onFailure(String mes) {
        Toast.makeText(getContext(), mes, Toast.LENGTH_SHORT).show();
    }
}
