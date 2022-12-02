package com.jotangi.greentravel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jotangi.greentravel.ui.login.SignupActivity2;
import com.jotangi.greentravel.ui.main.MainActivity;

import java.util.List;

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

//        String url = "rilinkshop://payment?url=";
//        Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//        in.setPackage("com.jotangi.greentravel");
//        startActivity(in);

//        String url = "rilinkshop://payment?url=";
//        WebView view = new WebView(this);
//        view.loadUrl(url);

    }

    Handler handler = new Handler();
    Runnable runnable = () ->
    {
        startActivity(new Intent(this, LoginMainActivity.class));
        finish();
    };
}