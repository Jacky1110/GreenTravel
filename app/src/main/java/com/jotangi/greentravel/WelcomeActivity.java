package com.jotangi.greentravel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
    }

    Handler handler = new Handler();
    Runnable runnable = () -> {
        Bundle bundle = this.getIntent().getExtras();
        Intent intent = new Intent(
                this,
                LoginMainActivity.class
        );

        if (bundle != null) {
            intent.putExtras(bundle);
        }

        startActivity(intent);
        finish();
    };
}