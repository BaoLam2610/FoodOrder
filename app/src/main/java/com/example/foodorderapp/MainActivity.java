package com.example.foodorderapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.foodorderapp.databinding.ActivityMainBinding;
import com.example.foodorderapp.databinding.NavHeaderMainBinding;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private NavHeaderMainBinding navBinding;
    private ActivityMainBinding binding;
    private NavController navController;
    private int currentDestinationId;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        setSupportActionBar(binding.appBarMain.toolbar);

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

                if (!sameDestination) {
                    navController.navigate(item.getItemId());
                    currentDestinationId = item.getItemId();
                }

                binding.drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
//        navBinding = NavHeaderMainBinding.bind(binding.navView.getHeaderView(0));
//        navBinding.btnLogin.setOnClickListener(v->{
//            Intent it = new Intent(getApplicationContext(),DetailActivity.class);
//            it.putExtra("login_screen","login");
//            startActivity(it);
//        });

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
}