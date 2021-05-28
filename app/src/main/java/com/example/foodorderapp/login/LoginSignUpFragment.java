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
import com.example.foodorderapp.databinding.FragmentLoginSignUpBinding;
import com.example.foodorderapp.event.ILogin;
import com.example.foodorderapp.presenter.LoginSignUpPresenter;

public class LoginSignUpFragment extends Fragment implements ILogin {
    FragmentLoginSignUpBinding binding;
    LoginSignUpPresenter presenter;
    String phone;
    public static LoginSignUpFragment newInstance() {

        Bundle args = new Bundle();

        LoginSignUpFragment fragment = new LoginSignUpFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_sign_up,container,false);
        presenter = new LoginSignUpPresenter(this,getContext());
        binding.btnContinue.setOnClickListener(v->{
            phone = binding.tvPhoneNumber.getText().toString();
            presenter.checkLogin(phone);
        });
        return binding.getRoot();
    }

    @Override
    public void onSuccessful(int type) {
        switch (type){
            case 0: // not exists phone -> sign up
                getFragment(SignUpFragment.newInstance(phone));
                break;
            case 1: // exists phone -> login
                getFragment(LoginFragment.newInstance(phone));
                break;

        }
    }

    @Override
    public void onFailure(String mes) {
        Toast.makeText(getContext(), mes, Toast.LENGTH_SHORT).show();
    }

    public void getFragment(Fragment fragment){
        getParentFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment,fragment)
                .addToBackStack(null)
                .commit();
    }

}
