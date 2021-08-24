package com.example.foodorderapp.network;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodorderapp.MainActivity;
import com.example.foodorderapp.R;

public class BroadCastNetWork extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final Dialog dialog = new Dialog(context);

        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            if (isCheckNetwork(context)) {
                Toast.makeText(context, "Internet connected", Toast.LENGTH_SHORT).show();


                if(dialog.isShowing())
                    dialog.dismiss();
            }else {
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

    public static boolean isCheckNetwork(Context context) {
        ConnectivityManager connect = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connect.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
