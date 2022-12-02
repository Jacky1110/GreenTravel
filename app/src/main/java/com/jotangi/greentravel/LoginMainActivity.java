package com.jotangi.greentravel;

import static com.jotangi.greentravel.ui.account.AccountLoginFragment.getpaymenturl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;
import com.jotangi.greentravel.ProjBaseFragment;
import com.jotangi.greentravel.ProjSharePreference;
import com.jotangi.greentravel.R;
import com.jotangi.greentravel.ui.hPayMall.MemberBean;
import com.jotangi.greentravel.ui.main.HomeMainFragment;
import com.jotangi.greentravel.ui.storeManager.StoreManagerFragment;
import com.jotangi.greentravel.ui.account.AccountLoginFragment;

import java.util.List;

public class LoginMainActivity extends AppCompatActivity {

    private String TAG = getClass().getSimpleName() + "(TAG)";

    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        initData();
        init();
        initHandler();
        initScheme();
    }

    private void initScheme() {
        Intent intent = getIntent();
        String action = intent.getAction();
        if (Intent.ACTION_VIEW.equals(action)) {
            Uri uri = intent.getData();
            if (uri != null) {
                String host = uri.getHost();
                String dataString = intent.getDataString();
                String id = uri.getQueryParameter("id");
                String path = uri.getPath();
                String path1 = uri.getEncodedPath();
                String queryString = uri.getQuery();
                Log.d(TAG, "host:"+host);
                Log.d(TAG, "dataString:" + dataString);
                Log.d(TAG, "id:" + id);
                Log.d(TAG, "path:" + path);
                Log.d(TAG, "path1:" + path1);
                Log.d(TAG, "queryString:" + queryString);

            }
        }
    }


    private void initData() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.framelayout, new AccountLoginFragment());
        transaction.commit();

    }


    private void init() {
        tabLayout = findViewById(R.id.tab);

    }

    private void initHandler() {

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                fragmentChange(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });
    }

    private void fragmentChange(int position) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (0 == position) {
            transaction.replace(R.id.framelayout, new AccountLoginFragment());
        } else {
            transaction.replace(R.id.framelayout, new StoreManagerFragment());
        }
        transaction.commit();
    }

}