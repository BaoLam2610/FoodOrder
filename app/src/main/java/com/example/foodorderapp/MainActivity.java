package com.example.foodorderapp;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.foodorderapp.databinding.ActivityMainBinding;
import com.example.foodorderapp.databinding.NavHeaderMainBinding;
import com.example.foodorderapp.login.LoginSignUpFragment;
import com.example.foodorderapp.event.ICheckLogin;
import com.example.foodorderapp.event.IShowAccountInf;
import com.example.foodorderapp.model.UserAccount;
import com.example.foodorderapp.presenter.LoginSignUpPresenter;
import com.google.android.material.navigation.NavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity implements IShowAccountInf, ICheckLogin {

    private static final String TAG = "MainActivity";
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
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        setSupportActionBar(binding.appBarMain.toolbar);
//        binding.appBarMain.toolbar.getOverflowIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
//        binding.appBarMain.toolbar.setBackground(getDrawable(R.drawable.custom_action_bar_bgr));

        presenter = new LoginSignUpPresenter(this,this,getApplicationContext());
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
                switch (item.getItemId()){
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
        navBinding.btnLogin.setOnClickListener(v->{
//            Intent it = new Intent(getApplicationContext(),DetailActivity.class);
//            it.putExtra("login_screen","login");
//            startActivity(it);
            invalidateOptionsMenu();

            if(countFrag==0) {
                getFragment(LoginSignUpFragment.newInstance());
                countFrag++;
                binding.drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        presenter.showAccountInformation();
    }
    int countFrag = 0;
//    @Override
//    protected void onResume() {
//        super.onResume();
//        presenter.showAccountInformation();
//    }
    public void getFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment,fragment)
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
        navBinding.textView.setText(userAccount.getPhone() + "  " + userAccount.getUsername());
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

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void getCheckPointFragment(Integer i){
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
}