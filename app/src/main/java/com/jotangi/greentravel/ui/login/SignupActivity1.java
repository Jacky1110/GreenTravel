package com.jotangi.greentravel.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import com.jotangi.greentravel.AppUtility;
import com.jotangi.greentravel.Base.BaseActivity;
import com.jotangi.greentravel.Api.ApiUrl;
import com.jotangi.greentravel.R;
import com.jotangi.jotangi2022.ApiConUtils;

public class SignupActivity1 extends BaseActivity implements View.OnClickListener {

    EditText et_Phone;
    Button btnSend;
    ImageButton BtnGoBack;

    ProgressBar progressBar;

    Button link_ReadTerms; //會員條款

    CheckBox checkBox;
    private PopupWindow ruleWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup1);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);

        initView();

    }

    private void initView() {
        progressBar = findViewById(R.id.progressBar);

        et_Phone = findViewById(R.id.et_Phone);
        btnSend = findViewById(R.id.btnSend);
        link_ReadTerms = findViewById(R.id.link_ReadTerms);


        checkBox = findViewById(R.id.checkBox);


        link_ReadTerms.setOnClickListener(this);
        btnSend.setOnClickListener(this);

        BtnGoBack = findViewById(R.id.btn_go_back);
        BtnGoBack.setOnClickListener(this);

        initRulesDialog();
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSend) {
            sendPhoneNumber();
        } else if (view.getId() == R.id.link_ReadTerms) {
            showRulePopup();
        } else if (view == BtnGoBack) {
            finish();
        }
    }

    private void initRulesDialog() {
        View popupWindowView = View.inflate(context, R.layout.dialog_rules, null);

        ruleWindow = new PopupWindow(popupWindowView);
        ruleWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        ruleWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

        WebView webView = popupWindowView.findViewById(R.id.webView);
        webView.loadUrl("file:///android_asset/rules.html");

        Button back = popupWindowView.findViewById(R.id.backBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ruleWindow.dismiss();
                AppUtility.setWindowAlpha(getWindow(), 1);
            }
        });

    }

    private void showRulePopup() {
        View anchorView = findViewById(R.id.popup_show_as_view);
        ruleWindow.showAsDropDown(anchorView, 0, 0);

        AppUtility.setWindowAlpha(getWindow(), 0.5f);

    }

    private void sendPhoneNumber() {
        final String phone = et_Phone.getText().toString().trim();
        if (phone.equals("")) {
            String message = "請先輸入10碼手機號碼。";
            makeToastTextAndShow(message, 3500);
            return;
        } else if (phone.length() != 10) {
            String message = "輸入格式錯誤,請輸入10碼手機號碼。";
            makeToastTextAndShow(message, 3500);
            return;
        }
        if (!checkBox.isChecked()) {
            String message = "請先勾選同意《會員服務條款》以及《隱私權政策》";
            makeToastTextAndShow(message, 3500);
            return;
        }

        String type = "0"; //請帶0 請於app端檢查格式
        progressBar.setVisibility(View.VISIBLE);
        ApiConUtils.signup(ApiUrl.API_URL2, ApiUrl.signup,phone, type, new ApiConUtils.OnConnectResultListener()
        {
            @Override
            public void onSuccess(final String jsonString) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);

                        if (jsonString.equals("101")) {

                            String message = "帳號新增成功\n驗證碼已發送";
                            AppUtility.showMyDialog(SignupActivity1.this, message, "確認", "", new AppUtility.OnBtnClickListener() {
                                @Override
                                public void onCheck() {
                                    //前往驗證碼輸入頁
                                    Intent intent = new Intent();
                                    intent.putExtra("account", phone);
                                    intent.setClass(context, SignupActivity2.class);
                                    startActivity(intent);
                                    finish();

                                }

                                @Override
                                public void onCancel() {

                                }
                            });


                        } else if (jsonString.equals("102")) {

                            String message = "帳號存在\n驗證碼已重新發送";
                            AppUtility.showMyDialog(SignupActivity1.this, message, "確認", "", new AppUtility.OnBtnClickListener() {
                                @Override
                                public void onCheck() {
                                    //前往驗證碼輸入頁
                                    Intent intent = new Intent();
                                    intent.putExtra("account", phone);
                                    intent.setClass(context, SignupActivity2.class);
                                    startActivity(intent);
                                    finish();

                                }

                                @Override
                                public void onCancel() {

                                }
                            });

                        } else {

                            String message = "帳號已存在\n請前往登入";
                            AppUtility.showMyDialog(SignupActivity1.this, message, "確認", "", new AppUtility.OnBtnClickListener() {
                                @Override
                                public void onCheck() {
                                    finish();
                                }

                                @Override
                                public void onCancel() {

                                }
                            });


                        }

                    }
                });
            }

            @Override
            public void onFailure(final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        progressBar.setVisibility(View.GONE);
                        AppUtility.showMyDialog(SignupActivity1.this, message, "返回", null, new AppUtility.OnBtnClickListener() {
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


}
