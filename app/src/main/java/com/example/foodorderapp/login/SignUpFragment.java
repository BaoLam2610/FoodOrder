package com.example.foodorderapp.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.foodorderapp.event.ICheck;
import com.example.foodorderapp.event.ISignUp;
import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.UserAccount;
import com.example.foodorderapp.presenter.LoginSignUpPresenter;

public class SignUpFragment extends Fragment implements ISignUp, ICheck {
    FragmentSignUpBinding binding;
    LoginSignUpPresenter presenter;
    public static final int TYPE_PHONE = 0;
    public static final int TYPE_FULL_NAME = 1;
    public static final int TYPE_PASSWORD = 2;
    public static final int TYPE_CONFIRM_PASSWORD = 3;

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
        presenter = new LoginSignUpPresenter(this,this, getContext());
        setHasOptionsMenu(true);
        setTitleActionBar();

        binding.tvPhone.setText(getPhone());
        binding.tvPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phone = binding.tvPhone.getText().toString();
                presenter.checkInput(TYPE_PHONE,phone,null,null,null);
            }
        });
        binding.tvName.setText("");
        String name = binding.tvName.getText().toString();
        presenter.checkInput(TYPE_FULL_NAME,null,name,null,null);
        binding.tvName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String name = binding.tvName.getText().toString();
                presenter.checkInput(TYPE_FULL_NAME,null,name,null,null);
            }
        });
        String p1 = binding.tvPassword1.getText().toString();
        presenter.checkInput(TYPE_PASSWORD,null,null,p1,null);
        binding.tvPassword1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String pass1 = binding.tvPassword1.getText().toString();
                presenter.checkInput(TYPE_PASSWORD,null,null,pass1,null);
            }
        });
//        String p2 = binding.tvPassword2.getText().toString();
//        presenter.checkInput(TYPE_CONFIRM_PASSWORD,null,null,null,p2);
        binding.tvPassword2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String p1 = binding.tvPassword1.getText().toString();
                String p2 = binding.tvPassword2.getText().toString();
                presenter.checkInput(TYPE_CONFIRM_PASSWORD,null,null,p1,p2);
            }
        });

//        binding.tvPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(!hasFocus)
//                    presenter.checkPhone(getPhone());
//            }
//        });

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
    public void onSuccessful(UserAccount userAccount) {

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

    @Override
    public void onCorrectCheck(int type) {
        switch (type){
            case 0:
                binding.tiPhone.setError(null);
                break;
            case 1:
                binding.tiUsername.setError(null);
                break;
            case 2:
                binding.tiPassword.setError(null);
                break;
            case 3:
                binding.tiConfirmPassword.setError(null);
                break;
        }

    }

    @Override
    public void onFailureCheck(int type, String mes) {
        switch (type){
            case 0:
                binding.tiPhone.setError(mes);
                break;
            case 1:
                binding.tiUsername.setError(mes);
                break;
            case 2:
                binding.tiPassword.setError(mes);
                break;
            case 3:
                binding.tiConfirmPassword.setError(mes);
                break;
        }

    }
}
