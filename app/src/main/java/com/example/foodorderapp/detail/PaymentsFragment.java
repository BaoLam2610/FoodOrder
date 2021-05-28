package com.example.foodorderapp.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.foodorderapp.R;
import com.example.foodorderapp.databinding.FragmentPaymentOrderBinding;

public class PaymentsFragment extends Fragment {
    FragmentPaymentOrderBinding binding;

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
        return binding.getRoot();
    }
}
