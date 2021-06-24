package com.example.foodorderapp.ui.profile;

import android.Manifest;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.foodorderapp.MainActivity;
import com.example.foodorderapp.R;
import com.example.foodorderapp.databinding.FragmentProfileBinding;
import com.example.foodorderapp.event.IOnProfile;
import com.example.foodorderapp.event.IShowAccountInf;
import com.example.foodorderapp.helper.ConvertAvatarHelper;
import com.example.foodorderapp.model.UserAccount;
import com.example.foodorderapp.presenter.LoginSignUpPresenter;
import com.example.foodorderapp.presenter.ProfilePresenter;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.IOException;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;

public class ProfileFragment extends Fragment implements IShowAccountInf, IOnProfile {

    FragmentProfileBinding binding;
    LoginSignUpPresenter loginPresenter;
    ProfilePresenter profilePresenter;
    Bitmap bitmap;
    byte[] avatar;

    public static ProfileFragment newInstance() {

        Bundle args = new Bundle();

        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);

        loginPresenter = new LoginSignUpPresenter(null, this, getContext());
        loginPresenter.showAccountInformation();
        profilePresenter = new ProfilePresenter(this, getContext());
        binding.btnEditAvatar.setOnClickListener(v -> {
            requestPermission();
        });

        binding.btnSaveProfile.setOnClickListener(v -> {
            String username = binding.etUsername.getText().toString();
            String phone = binding.etPhone.getText().toString();
            String address = binding.etAddress.getText().toString();

            if (bitmap != null)
                avatar = ConvertAvatarHelper.getBytes(bitmap);
            UserAccount userAccount = new UserAccount(phone, username, null, avatar, address, 1);
            profilePresenter.editProfile(userAccount);
            ((MainActivity)getActivity()).onExistsAccount(userAccount);
        });

        return binding.getRoot();
    }

    private void requestPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                openImagePicker();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getContext(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.with(getContext())
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .check();
    }

    private void openImagePicker() {
        TedBottomSheetDialogFragment.OnImageSelectedListener listener = new TedBottomSheetDialogFragment.OnImageSelectedListener() {
            @Override
            public void onImageSelected(Uri uri) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                    binding.ivAvatar.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        TedBottomPicker.with(getActivity())
                .setOnImageSelectedListener(listener)
                .create()
                .show(getActivity().getSupportFragmentManager());
    }

    @Override
    public void onExistsAccount(UserAccount userAccount) {
        String username = userAccount.getUsername();
        String phone = userAccount.getPhone();
        byte[] avatar = userAccount.getAvatar();
        String address = userAccount.getAddress();
        binding.tvUsername.setText(username);
        binding.etUsername.setText(username);
        binding.etPhone.setText(phone);
        binding.etAddress.setText(address);
        if (avatar != null) {
            Bitmap bitmap = ConvertAvatarHelper.getImage(avatar);
            binding.ivAvatar.setImageBitmap(bitmap);
        }
    }


    @Override
    public void onNotExistsAccount() {

    }

    @Override
    public void onSaveSuccessful() {
        Toast.makeText(getContext(), "Insert successful", Toast.LENGTH_SHORT).show();

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