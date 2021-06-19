package com.example.foodorderapp.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.foodorderapp.DetailActivity;
import com.example.foodorderapp.R;
import com.example.foodorderapp.databinding.FragmentPaymentSuccessfulBinding;

public class PaymentSuccessfulFragment extends Fragment {
    FragmentPaymentSuccessfulBinding binding;

    public static PaymentSuccessfulFragment newInstance() {

        Bundle args = new Bundle();

        PaymentSuccessfulFragment fragment = new PaymentSuccessfulFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment_successful,container,false);
        setTitleActionBar();
        binding.btnBackToHome.setOnClickListener(v->getActivity().finish());
        return binding.getRoot();
    }
    public void setTitleActionBar(){
        ((DetailActivity) getActivity()).getSupportActionBar().hide();
    }

}
