package com.jotangi.greentravel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.jotangi.greentravel.ui.hPayMall.MemberBean;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.jotangi.greentravel.ui.storeManager.StoreManager;

public class WelcomeActivity extends AppCompatActivity {

    public static Uri uri;
    public static String storeID;
    public static String type;
    public static Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorDarkToast));
        handler.postDelayed(runnable, 1250);

        uri = getIntent().getData();
        Log.d("豪豪", "uri: " + uri);

        if (uri != null) {

            String str = uri.getHost();
            String[] tokens = str.split("=|&");
            storeID = tokens[1];
            type = tokens[3];
            Log.d("豪豪", "onCreate: " + storeID + type);
        }

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            //Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        MemberBean.NOTIFICATION_TOKEN = token;
                        Log.d("TAGE", "FCM推播：" + token);
                    }
                });
    }

    Handler handler = new Handler();

    Runnable runnable = () ->
    {
        SharedPreferences isGetLogin = getSharedPreferences("loginInfo", MODE_PRIVATE);
        boolean loginresult = isGetLogin.getBoolean("isLogin", false);
        Bundle bundle = this.getIntent().getBundleExtra("bundle");
        Intent intent = new Intent(
                this,
                LoginMainActivity.class
        );
        if (bundle != null) {

            MemberBean.rilink_rent_id = bundle.getString("rilink_rent_id", "");
            Log.d("TAG", "MemberBean.rilink_rent_id: " + MemberBean.rilink_rent_id);
            MemberBean.rilink_password = bundle.getString("rilink_password","");


            intent.putExtra(
                    "bundle",
                    bundle
            );
        }
        if (loginresult) {
            MemberBean.store_acc = isGetLogin.getString("storeAccount", "");
            MemberBean.store_pwd = isGetLogin.getString("storePassword", "");
            MemberBean.store_id = isGetLogin.getString("storeId", "");
            MemberBean.member_id = isGetLogin.getString("account", "");
            if (!MemberBean.store_acc.equals("") && MemberBean.store_acc.length() != 10) {
                startActivity(new Intent(this, StoreManager.class));
                finish();
            } else if (!MemberBean.member_id.equals("") && MemberBean.member_id.length() == 10) {
                startActivity(intent);
                finish();
            }
        } else {
            startActivity(intent);
            finish();
        }
    };
}