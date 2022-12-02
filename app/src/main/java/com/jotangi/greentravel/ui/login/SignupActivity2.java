package com.jotangi.greentravel.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.jotangi.greentravel.AppUtility;
import com.jotangi.greentravel.Base.BaseActivity;
import com.jotangi.greentravel.Api.ApiUrl;
import com.jotangi.greentravel.Keychain;
import com.jotangi.greentravel.R;
import com.jotangi.jotangi2022.ApiConUtils;

public class SignupActivity2 extends BaseActivity implements View.OnClickListener {

    ProgressBar progressBar;

    EditText et_inCaptcha;
    EditText etPassword;
    EditText etPassword2;

    //送出
    Button btnSend;

    //重送
    Button btnReCaptcha;

    ImageButton BtnGoBack;

    String account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);

        account = getIntent().getStringExtra("account");

        initView();

    }

    private void initView() {
        progressBar = findViewById(R.id.progressBar);

        et_inCaptcha = findViewById(R.id.et_inCaptcha);
        etPassword = findViewById(R.id.etPassword);
        etPassword2 = findViewById(R.id.etPassword2);

        btnSend = findViewById(R.id.btnSend);
        btnReCaptcha = findViewById(R.id.btnReCaptcha);
        btnSend.setOnClickListener(this);
        btnReCaptcha.setOnClickListener(this);

        BtnGoBack = findViewById(R.id.btn_go_back);
        BtnGoBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSend) {
            //按了送出驗證
            codeVerify();

        } else if (view.getId() == R.id.btnReCaptcha) {
            //按了重送
            reSendVerifyCode();
        } else if (view == BtnGoBack) {
            finish();
        }
    }

    private void codeVerify() {
        String code = et_inCaptcha.getText().toString().trim();

        if (code.length() == 0) {
            String message = "請輸入簡訊驗證碼。";
            makeToastTextAndShow(message, 3500);
        }
        final String pw1 = etPassword.getText().toString().trim();
        String pw2 = etPassword2.getText().toString().trim();
        String type = "0"; //請帶0 請於app端檢查格式

        String action = "";

        progressBar.setVisibility(View.VISIBLE);
        ApiConUtils.codeverify(ApiUrl.API_URL2, ApiUrl.codeverify, action, account, type, code, pw1, pw2, new ApiConUtils.OnConnectResultListener() {

            @Override
            public void onSuccess(String jsonString) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        AppUtility.showMyDialog(SignupActivity2.this, jsonString, "確認", null, new AppUtility.OnBtnClickListener() {
                            @Override
                            public void onCheck() {
                                //記住已註冊完成資訊
                                Keychain.setString(context, Keychain.ACCOUNT, account);
                                Keychain.setString(context, Keychain.PW, pw1);

                                //前往填寫資料
                                Intent intent = new Intent();
                                intent.setClass(context, SignupActivity3.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
                    }
                });
            }

            @Override
            public void onFailure(String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        AppUtility.showMyDialog(SignupActivity2.this, message, "確認", null, new AppUtility.OnBtnClickListener() {
                            @Override
                            public void onCheck() {
                                et_inCaptcha.setText("");
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


    private void reSendVerifyCode() {

        String type = "0"; //請帶0 請於app端檢查格式
        String action = "0"; //型態 0:註冊驗證碼重送 1:忘記密碼驗證碼發送
        progressBar.setVisibility(View.VISIBLE);
        ApiConUtils.resendcode(ApiUrl.API_URL2, ApiUrl.resendcode, account, type, action, new ApiConUtils.OnConnectResultListener() {
            @Override
            public void onSuccess(String jsonString) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        String message = "驗證碼已重新發送。";
                        AppUtility.showMyDialog(SignupActivity2.this, message, "確認", null, new AppUtility.OnBtnClickListener() {
                            @Override
                            public void onCheck() {
                                et_inCaptcha.setText("");
                                et_inCaptcha.findFocus();
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
                    }
                });
            }

            @Override
            public void onFailure(final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        AppUtility.showMyDialog(SignupActivity2.this, message, "確認", null, new AppUtility.OnBtnClickListener() {
                            @Override
                            public void onCheck() {
                                et_inCaptcha.setText("");
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


}
