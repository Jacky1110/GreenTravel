package com.jotangi.greentravel.ui.storeManager;

import static com.jotangi.greentravel.ui.account.AccountLoginFragment.KEY_IS_LOGIN;
import static com.jotangi.greentravel.ui.account.AccountLoginFragment.REG_PREF_NAME;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.jotangi.greentravel.Api.ApiEnqueue;
import com.jotangi.greentravel.CameraQRcode;
import com.jotangi.greentravel.R;

public class StoreManager extends AppCompatActivity {
    private String TAG = getClass().getSimpleName() + "(TAG)";

    private ApiEnqueue apiEnqueue;
    private Button logoutBT;
    private ImageView qrcodeIV;
    private SharedPreferences pref;
    private final int REDULT = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_manager);

        apiEnqueue = new ApiEnqueue();

        init();
        initHandler();
    }


    @Override
    public void onBackPressed() {

    }

    private void init() {
        logoutBT = findViewById(R.id.bt_logout);
        qrcodeIV = findViewById(R.id.imageView);

    }

    private void initHandler() {
        pref = getSharedPreferences(REG_PREF_NAME, MODE_PRIVATE);
        boolean isLogin = pref.getBoolean(KEY_IS_LOGIN, false);
        if (isLogin) {
            logoutBT.setOnClickListener(view -> {
                SharedPreferences pref = getSharedPreferences("loginInfo", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(this, AppCompatActivity.class);
                startActivity(intent);
                finish();
            });
        }

        qrcodeIV.setOnClickListener(view -> {
            Intent intent = new Intent(this, CameraQRcode.class);
            startActivityForResult(intent, REDULT);

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REDULT) {

            apiEnqueue.storeappQRconfirm(new ApiEnqueue.resultListener() {
                @Override
                public void onSuccess(String message) {
                    runOnUiThread(() -> {
                        Toast.makeText(StoreManager.this, message, Toast.LENGTH_SHORT).show();
                    });
                }

                @Override
                public void onFailure(String message) {
                    runOnUiThread(() -> {
                        Toast.makeText(StoreManager.this, message, Toast.LENGTH_SHORT).show();
                    });
                }
            });

        }
    }
}