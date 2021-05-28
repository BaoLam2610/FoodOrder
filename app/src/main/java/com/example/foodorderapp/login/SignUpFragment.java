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

import com.example.foodorderapp.MainActivity;
import com.example.foodorderapp.R;
import com.example.foodorderapp.databinding.FragmentSignUpBinding;
import com.example.foodorderapp.event.ISignUp;
import com.example.foodorderapp.presenter.LoginSignUpPresenter;

import org.greenrobot.eventbus.EventBus;

public class SignUpFragment extends Fragment implements ISignUp {
    FragmentSignUpBinding binding;
    LoginSignUpPresenter presenter;

    public static SignUpFragment newInstance(String phone) {

        Bundle args = new Bundle();
        args.putString("phone", phone);
        SignUpFragment fragment = new SignUpFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up,container,false);
        presenter = new LoginSignUpPresenter(this,getContext());
        setTitleActionBar();
        binding.btnContinue.setOnClickListener(v -> {
            String username = binding.tvName.getText().toString();
            String pass1 = binding.tvPassword1.getText().toString();
            String pass2 = binding.tvPassword2.getText().toString();
            presenter.checkSignUp(getPhone(),username,pass1,pass2);
        });
        return binding.getRoot();
    }

    public void setTitleActionBar(){
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Register");
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onSuccessful() {
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
