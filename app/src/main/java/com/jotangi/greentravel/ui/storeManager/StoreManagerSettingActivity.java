package com.jotangi.greentravel.ui.storeManager;

import static com.jotangi.greentravel.ui.account.AccountLoginFragment.KEY_IS_LOGIN;
import static com.jotangi.greentravel.ui.account.AccountLoginFragment.REG_PREF_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.jotangi.greentravel.Api.ApiEnqueue;
import com.jotangi.greentravel.LoginMainActivity;
import com.jotangi.greentravel.R;

public class StoreManagerSettingActivity extends AppCompatActivity {

    private Button logout;
    private SwitchCompat switchButton;
    private ApiEnqueue apiEnqueue;
    private SharedPreferences pref;
    private String notifyStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_manager_setting);

        apiEnqueue = new ApiEnqueue();
        getNotifyStatus();
        initView();
    }

    private void getNotifyStatus() {
        apiEnqueue.storeadminIsnotify(new ApiEnqueue.resultListener() {
            @Override
            public void onSuccess(String data) {
                runOnUiThread(() -> {
                    notifyStatus = data;

                    switchButtonStatus();
                });
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    private void initView() {

        switchButton = findViewById(R.id.switch1);

        logout = findViewById(R.id.btn_logOut);
        pref = getSharedPreferences(REG_PREF_NAME, MODE_PRIVATE);
        boolean isLogin = pref.getBoolean(KEY_IS_LOGIN, false);
        if (isLogin) {
            logout.setOnClickListener(view -> {
                SharedPreferences pref = getSharedPreferences("loginInfo", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(this, LoginMainActivity.class);
                startActivity(intent);
                finish();
            });
        }
    }

    private void switchButtonStatus() {
        switchButton.setChecked(notifyStatus.equals("on"));
        switchButton.setOnCheckedChangeListener((compoundButton, b) -> {
            if (compoundButton.isChecked()) {
                String notify = "on";
                setNotify(notify);
            } else {
                String notify = "off";
                setNotify(notify);
            }

        });

    }

    private void setNotify(String notify) {

        apiEnqueue.storeadminSetNotify(notify, new ApiEnqueue.resultListener() {
            @Override
            public void onSuccess(String message) {
                runOnUiThread(() -> {

                    getNotifyStatus();

                });
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(StoreManagerSettingActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}