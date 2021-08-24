package com.example.foodorderapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.example.foodorderapp.network.BroadCastNetWork;
import com.example.foodorderapp.presenter.CartDatabasePresenter;
import com.example.foodorderapp.presenter.LoginSignUpPresenter;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements IShowAccountInf, ICheckLogin, IDrawer {

    public static final String TAG = "MainActivity";

    int countFrag = 0;
    ActionBarDrawerToggle toggle;
    boolean checkLogin = true;
    BroadCastNetWork broadCastNetWork;
    BroadcastReceiver broadcastReceiver;
    boolean check = true;
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
//        checkNetwork();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final Dialog dialog = new Dialog(context);

                if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                    if (isCheckNetwork(context)) {

                    } else {
                        check = false;
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_network_disconnected);

                        Window window = dialog.getWindow();
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
            }
        };

        setSupportActionBar(binding.appBarMain.toolbar);
//        binding.appBarMain.toolbar.getOverflowIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
//        binding.appBarMain.toolbar.setBackground(getDrawable(R.drawable.custom_action_bar_bgr));

        presenter = new LoginSignUpPresenter(this, this, getApplicationContext());
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_terms_conditions)
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
//                        final Dialog dialog1 = new Dialog(getApplicationContext());
//                        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                        dialog1.setContentView(R.layout.dialog_logout);
//
//                        Window window1 = dialog1.getWindow();
//                        window1.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//                        window1.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//                        WindowManager.LayoutParams windowAttributes1 = window1.getAttributes();
//                        windowAttributes1.gravity = Gravity.CENTER;
//                        window1.setAttributes(windowAttributes1);
//                        dialog1.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//
//                        TextView tvCancel1 = dialog1.findViewById(R.id.tvCancel);
//                        TextView tvLogout = dialog1.findViewById(R.id.tvLogout);
//                        dialog1.show();
//                        tvCancel1.setOnClickListener(view -> {
//                            dialog1.dismiss();
//
//                        });

//                        tvLogout.setOnClickListener(v -> {
                            presenter.logoutAccount();
                            presenter.showAccountInformation();
                            checkLogin = false;
                            invalidateOptionsMenu();
                            navController.navigate(R.id.nav_home);
//                            dialog1.dismiss();
//                       });

                        binding.navView.getMenu().getItem(0).setChecked(true);


                        break;
                    case R.id.nav_profile:
                        if (checkLogin == false) {

                            final Dialog dialog = new Dialog(getApplicationContext());
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.dialog_login_check);

                            Window window = dialog.getWindow();
                            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            WindowManager.LayoutParams windowAttributes = window.getAttributes();
                            windowAttributes.gravity = Gravity.CENTER;
                            window.setAttributes(windowAttributes);
                            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                            TextView tvCancel = dialog.findViewById(R.id.tvCancel);

                            tvCancel.setOnClickListener(view -> {
                                dialog.dismiss();
                                binding.navView.getMenu().getItem(0).setChecked(true);
                            });
                            dialog.show();

                        } else
                            navController.navigate(R.id.nav_profile);
                        break;
                    case R.id.nav_home:
                        navController.navigate(R.id.nav_home);
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

    public boolean isCheckNetwork(Context context) {
        ConnectivityManager connect = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connect.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void setTileActionBar(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);
        presenter.showAccountInformation();
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
        if (avatar != null) {
            Bitmap bitmap = ConvertAvatarHelper.getImage(avatar);

            navBinding.ivAvatar.setImageBitmap(bitmap);
        }
        checkLogin = true;
    }

    @Override
    public void onNotExistsAccount() {
        navBinding.layoutAccount.setVisibility(View.INVISIBLE);
        navBinding.btnLogin.setVisibility(View.VISIBLE);
        checkLogin = false;
        binding.navView.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public void onExists(String mes) {
        binding.navView.getMenu().getItem(0).setChecked(true);
        Toast.makeText(this, mes, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {

//        CartDatabasePresenter.destroyAll(getBaseContext());
        super.onDestroy();
    }

    @Override
    public void onNotExists(String mes) {
        binding.navView.getMenu().getItem(0).setChecked(true);
        Toast.makeText(this, mes, Toast.LENGTH_SHORT).show();
    }
//
//    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
//    public void getCheckPointFragment(Integer i) {
//        countFrag = i;
//        presenter.showAccountInformation();
//    }

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