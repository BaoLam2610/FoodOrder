package com.example.foodorderapp.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.TabLoginAdapter;
import com.example.foodorderapp.databinding.FragmentLoginSignUpBinding;
import com.example.foodorderapp.databinding.FragmentSignUpBinding;
import com.example.foodorderapp.tablayout.SignUpFragment;

public class LoginSignUpFragment extends Fragment {
    FragmentLoginSignUpBinding binding;
    TabLoginAdapter loginAdapter;

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
        loginAdapter = new TabLoginAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,getContext());
        binding.vpLogin.setAdapter(loginAdapter);
        binding.tabLoginSignUp.setupWithViewPager(binding.vpLogin);
        return binding.getRoot();
    }
}
