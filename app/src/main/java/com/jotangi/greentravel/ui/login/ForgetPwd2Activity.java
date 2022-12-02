package com.jotangi.greentravel.ui.login;

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


public class ForgetPwd2Activity extends BaseActivity implements View.OnClickListener {

    ApiEnqueue apiEnqueue;

    Button btnSend;
    EditText et_inCaptcha;
    EditText etPassword_1;
    EditText etPassword_2;
    ProgressBar progressBar;
    ImageButton BtnGoBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd2);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        initView();
    }

    private void initView(){
        apiEnqueue = new ApiEnqueue();
        btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);
        et_inCaptcha = findViewById(R.id.et_inCaptcha);
        etPassword_1 = findViewById(R.id.etPassword_1);
        etPassword_2 = findViewById(R.id.etPassword_2);
        progressBar = findViewById(R.id.progressBar);
        BtnGoBack = findViewById(R.id.btn_go_back);
        BtnGoBack.setOnClickListener(this);
    }

    private void sendVerify(){
        String accountType = "0";  //類型 0:一般 1:fb
        String registeCode = et_inCaptcha.getText().toString().trim();  //驗證碼
        final String pw1 = etPassword_1.getText().toString().trim();
        String pw2 = etPassword_2.getText().toString().trim();
        String action = "resetpw";
        progressBar.setVisibility(View.VISIBLE);
        apiEnqueue.codeverify(action, Global.TEMP_ACCOUNT, accountType, registeCode, pw1, pw2, new ApiEnqueue.resultListener() {
            @Override
            public void onSuccess(String jsonString) {
                updateMallPassword(Global.TEMP_ACCOUNT,pw1);
            }

            @Override
            public void onFailure(final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        AppUtility.showMyDialog(ForgetPwd2Activity.this, message, "確認", "", new AppUtility.OnBtnClickListener() {
                            @Override
                            public void onCheck() {
                                finish();
                            }
                            @Override
                            public void onCancel() {}
                        });
                    }
                });
            }
        });
    }

    //更新優惠券密碼
//    private void updateCouponPassword(String customerid, String oldpassword, String newpassword){
//        ApiConnection.couponUpdatePassword(customerid, oldpassword, newpassword, new ApiConnection.OnConnectResultListener() {
//            @Override
//            public void onSuccess(String jsonString) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        progressBar.setVisibility(View.GONE);
//                        String message = "密碼已修改，請以新密碼重新登入。";
//                        AppUtility.showMyDialog(ForgetPwd2Activity.this, message, "確認", "", new AppUtility.OnBtnClickListener() {
//                            @Override
//                            public void onCheck() {
//                                finish();
//                            }
//                            @Override
//                            public void onCancel() {}
//                        });
//                    }
//                });
//            }
//
//            @Override
//            public void onFailure(String message) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        progressBar.setVisibility(View.GONE);
//                        String message = "密碼已修改，請以新密碼重新登入。";
//                        AppUtility.showMyDialog(ForgetPwd2Activity.this, message, "確認", "", new AppUtility.OnBtnClickListener() {
//                            @Override
//                            public void onCheck() {
//                                finish();
//                            }
//                            @Override
//                            public void onCancel() {}
//                        });
//                    }
//                });
//            }
//        });
//    }

    //更新商城密碼
    private void updateMallPassword(final String mobile, final String password){
        apiEnqueue.mallUpdatePassword(mobile, password, new ApiEnqueue.resultListener() {
            @Override
            public void onSuccess(String jsonString) {
//                updateCouponPassword(mobile,"",password);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        String message = "密碼已修改，請以新密碼重新登入。";
                        AppUtility.showMyDialog(ForgetPwd2Activity.this, message, "確認", "", new AppUtility.OnBtnClickListener() {
                            @Override
                            public void onCheck() {
                                finish();
                            }
                            @Override
                            public void onCancel() {}
                        });
                    }
                });
            }

            @Override
            public void onFailure(String message) {
//                updateCouponPassword(mobile,"",password);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        String message = "密碼已修改，請以新密碼重新登入。";
                        AppUtility.showMyDialog(ForgetPwd2Activity.this, message, "確認", "", new AppUtility.OnBtnClickListener() {
                            @Override
                            public void onCheck() {
                                finish();
                            }
                            @Override
                            public void onCancel() {}
                        });
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == btnSend) {
            sendVerify();
        } else if (v == BtnGoBack) {
            finish();
        }
    }
}
