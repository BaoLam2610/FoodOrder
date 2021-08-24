package com.example.foodorderapp.ui.terms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.foodorderapp.R;
import com.example.foodorderapp.databinding.FragmentTermsConditionsBinding;

public class TermsConditionsFragment extends Fragment {

    FragmentTermsConditionsBinding binding;

    public static TermsConditionsFragment newInstance() {

        Bundle args = new Bundle();

        TermsConditionsFragment fragment = new TermsConditionsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_terms_conditions, container,false);
        return binding.getRoot();
    }
}