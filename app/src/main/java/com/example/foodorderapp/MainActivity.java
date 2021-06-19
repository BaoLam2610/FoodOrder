package com.example.foodorderapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.foodorderapp.databinding.ActivityMainBinding;
import com.example.foodorderapp.databinding.NavHeaderMainBinding;
import com.example.foodorderapp.event.ICheckLogin;
import com.example.foodorderapp.event.IDrawer;
import com.example.foodorderapp.event.IShowAccountInf;
import com.example.foodorderapp.helper.ConvertAvatarHelper;
import com.example.foodorderapp.model.UserAccount;
import com.example.foodorderapp.presenter.LoginSignUpPresenter;
import com.google.android.material.navigation.NavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity implements IShowAccountInf, ICheckLogin, IDrawer {

    public static final String TAG = "MainActivity";

    int countFrag = 0;
    ActionBarDrawerToggle toggle;
    private NavHeaderMainBinding navBinding;
    private LoginSignUpPresenter presenter;
    private ActivityMainBinding binding;
    private NavController navController;
    private int currentDestinationId;
    private AppBarConfiguration mAppBarConfiguration;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        checkNetwork();

        setSupportActionBar(binding.appBarMain.toolbar);
//        binding.appBarMain.toolbar.getOverflowIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
//        binding.appBarMain.toolbar.setBackground(getDrawable(R.drawable.custom_action_bar_bgr));

        presenter = new LoginSignUpPresenter(this, this, getApplicationContext());
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(binding.drawerLayout)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        binding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                final boolean sameDestination = item.getItemId() == currentDestinationId;
                switch (item.getItemId()) {
                    case R.id.nav_logout:
                        presenter.logoutAccount();
                        presenter.showAccountInformation();
                        invalidateOptionsMenu();
                        break;

                    default:
                        if (!sameDestination) {
                            navController.navigate(item.getItemId());
                            currentDestinationId = item.getItemId();
                        }
                        break;
                }

                binding.drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        navBinding = NavHeaderMainBinding.bind(binding.navView.getHeaderView(0));
        Glide.with(getApplicationContext()).load("https://blog.sebastiano.dev/content/images/2019/07/1_l3wujEgEKOecwVzf_dqVrQ.jpeg")
                .centerCrop()   // căn ảnh
//                    .placeholder(R.drawable.ic_baseline_image_24)  // đợi load ảnh
//                    .error(R.drawable.ic_baseline_error_24)        // load ảnh bị lỗi
                .into(navBinding.ivHeaderBgr);
        navBinding.btnLogin.setOnClickListener(v -> {
            invalidateOptionsMenu();
            Intent it = new Intent(this, DetailActivity.class);
            it.putExtra("login_screen", "login");
            startActivity(it);
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        });
        presenter.showAccountInformation();
    }

    public void checkNetwork() {

        if (!isCheckNetwork()) {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_network_disconnected);

            Window window = dialog.getWindow();
//        if(window == null){
//            return;
//        }
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams windowAttributes = window.getAttributes();
            windowAttributes.gravity = Gravity.CENTER;
            window.setAttributes(windowAttributes);

            TextView tvCancel = dialog.findViewById(R.id.tvCancel);

            tvCancel.setOnClickListener(view -> {
                dialog.dismiss();
            });
            dialog.show();
        }
    }

    public void setTileActionBar(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.showAccountInformation();
    }

    public void getFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .addToBackStack(TAG)
                .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
//

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();

    }

    @Override
    public void onExistsAccount(UserAccount userAccount) {

        navBinding.layoutAccount.setVisibility(View.VISIBLE);
        navBinding.btnLogin.setVisibility(View.INVISIBLE);
        navBinding.tvPhone.setText(getResources().getString(R.string.phone) + userAccount.getPhone());
        navBinding.tvName.setText(userAccount.getUsername());
        byte[] avatar = userAccount.getAvatar();
        if(avatar != null){
            Bitmap bitmap = ConvertAvatarHelper.getImage(avatar);

            navBinding.ivAvatar.setImageBitmap(bitmap);}
    }

    @Override
    public void onNotExistsAccount() {
        navBinding.layoutAccount.setVisibility(View.INVISIBLE);
        navBinding.btnLogin.setVisibility(View.VISIBLE);

    }

    @Override
    public void onExists(String mes) {
        Toast.makeText(this, mes, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNotExists(String mes) {
        Toast.makeText(this, mes, Toast.LENGTH_SHORT).show();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getCheckPointFragment(Integer i) {
        countFrag = i;
        presenter.showAccountInformation();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public boolean isCheckNetwork() {
        ConnectivityManager connect = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connect.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onShowBackPress(boolean enabled) {
//        binding.appBarMain.toolbar.setNavigationIcon(null);

//        binding.drawerLayout.setD

        toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.appBarMain.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        if (enabled == true) {
            toggle.syncState();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
            toggle.setDrawerIndicatorEnabled(false);
            toggle.setHomeAsUpIndicator(R.drawable.custom_back_press); //or you can add icon
            toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Doesn't have to be onBackPressed
                    getSupportFragmentManager().popBackStack();
                    toggle.setDrawerIndicatorEnabled(true);
                }
            });
        } else {
            toggle.syncState();
            getSupportActionBar().setTitle(R.string.menu_home);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
            toggle.setDrawerIndicatorEnabled(true);
        }
//        toggle.setHomeAsUpIndicator(R.id.icon);//add this for custom icon
    }

}