package com.example.foodorderapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.foodorderapp.databinding.ActivitySplashBinding;
import com.example.foodorderapp.network.BroadCastNetWork;

public class SplashActivity extends AppCompatActivity {

    ActivitySplashBinding binding;
    BroadCastNetWork broadCastNetWork;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_splash);
        getSupportActionBar().hide();

        broadCastNetWork = new BroadCastNetWork();
    if(BroadCastNetWork.isCheckNetwork(getApplicationContext())){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        }, 3000);
    } else {
        final Dialog dialog = new Dialog(getApplicationContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_network_disconnected);

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        TextView tvCancel = dialog.findViewById(R.id.tvCancel);

        dialog.show();
    }
    }
}