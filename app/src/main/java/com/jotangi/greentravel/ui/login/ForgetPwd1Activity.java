package com.jotangi.greentravel.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.jotangi.greentravel.Api.ApiEnqueue;
import com.jotangi.greentravel.AppUtility;
import com.jotangi.greentravel.Base.BaseActivity;
import com.jotangi.greentravel.Global;
import com.jotangi.greentravel.R;


public class ForgetPwd1Activity extends BaseActivity implements View.OnClickListener {

    private ApiEnqueue apiEnqueue;

    private Button btnSend;

    private ProgressBar progressBar;

    private EditText et_Phone;

    ImageButton BtnGoBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd1);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);

        initView();

    }

    private void initView(){

        apiEnqueue = new ApiEnqueue();
        btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);

        et_Phone = findViewById(R.id.et_Phone);

        progressBar = findViewById(R.id.progressBar);

        BtnGoBack = findViewById(R.id.btn_go_back);
        BtnGoBack.setOnClickListener(this);



    }

    private void setSmsRequest(){

        final String account = et_Phone.getText().toString().trim();

        if (account.equals("")) {
            String message = "請先輸入10碼手機號碼。";
            makeToastTextAndShow(message,3500);
            return;
        } else if (account.length() != 10) {
            String message = "輸入格式錯誤,請輸入10碼手機號碼。";
            makeToastTextAndShow(message,3500);
            return;
        }


        String accountType = "0";
        String action = "1";  //0:註冊重送  1:忘記密碼

        progressBar.setVisibility(View.VISIBLE);
        apiEnqueue.resendcode(account, accountType, action, new ApiEnqueue.resultListener() {
            @Override
            public void onSuccess(String jsonString) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        progressBar.setVisibility(View.GONE);

                        Global.TEMP_ACCOUNT = account;

                        Intent intent = new Intent();
                        intent.setClass(context, ForgetPwd2Activity.class);
                        startActivity(intent);
                        finish();

                    }
                });
            }

            @Override
            public void onFailure(final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);

                        AppUtility.showMyDialog(ForgetPwd1Activity.this, message, "確認", "", new AppUtility.OnBtnClickListener() {
                            @Override
                            public void onCheck() {
                                finish();
                            }

                            @Override
                            public void onCancel() {

                            }
                        });

                    }
                });

            }
        });

    }


    @Override
    public void onClick(View v) {

        if (v == btnSend) {

            setSmsRequest();
        } else if (v == BtnGoBack) {
            finish();
        }
    }
}
