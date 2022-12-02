package com.jotangi.greentravel.ui.account;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.jotangi.greentravel.ProjConstraintFragment;
import com.jotangi.greentravel.R;
import com.jotangi.greentravel.Api.ApiUrl;
import com.jotangi.jotangi2022.ApiConUtils;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;

import java.util.Arrays;

public class AccountRegisterFragment extends ProjConstraintFragment {
    private final String TAG = this.getClass().getSimpleName();

    TextView tvRule;
    EditText edName, edEmail, edPhone, edPassword, edInvite;
    TextInputLayout nameLayout, emailLayout, phoneLayout, passwordLayout, inviteLayout;
    ConstraintLayout btnRegister;
    CheckBox chAgree;

    public static AccountRegisterFragment newInstance() {
        AccountRegisterFragment fragment = new AccountRegisterFragment();
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ConstraintLayout) inflater.inflate(R.layout.fragment_account_register, container, false);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        activityTitleRid = R.string.register;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void initViews() {
        super.initViews();

        edName = rootView.findViewById(R.id.ed_name);
        edEmail = rootView.findViewById(R.id.ed_birthday);
        edPhone = rootView.findViewById(R.id.ed_phone);
        edPassword = rootView.findViewById(R.id.ed_home_number);
        edInvite = rootView.findViewById(R.id.ed_email);
        chAgree = rootView.findViewById(R.id.check_agree_rule);
        tvRule = rootView.findViewById(R.id.tv_account_rule);

        tvRule.setOnClickListener(view -> {
            if (fragmentListener != null){
                fragmentListener.onAction(FUNC_REGISTER_TO_RULE, null);
            }
        });
        btnRegister = rootView.findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });
    }

    private void Register() {
        String name = edName.getText().toString().trim();
        String phone = edPhone.getText().toString().trim();
        String pwd = edPassword.getText().toString().trim();
        String recommand = edInvite.getText().toString().trim();
        String email = edEmail.getText().toString().trim();
        String[] successArr = {"0x0202", "0x0203", "0x0204", "0x0206"};
        if (name.isEmpty()) {
            showDialog("", "請輸入姓名", (dialog1, which) -> {
                dialog.dismiss();
            });
            return;
        }
        if (phone.isEmpty()) {
            showDialog("", "請輸入電話", (dialog1, which) -> {
                dialog.dismiss();
            });
            return;
        }
        if (pwd.isEmpty()) {
            showDialog("", "請輸入密碼", (dialog1, which) -> {
                dialog.dismiss();
            });
            return;
        }
        if (email.isEmpty()) {
            showDialog("", "請輸入E-mail", (dialog1, which) -> {
                dialog.dismiss();
            });
            return;
        }
        if (!recommand
                .isEmpty() && recommand.length() < 10) {
            showDialog("", "請輸入正確推薦碼", (dialog1, which) -> {
                dialog.dismiss();
            });
            return;
        }
        if (!chAgree.isChecked()) {
            showDialog("", "請勾選同意聲明", (dialog1, which) -> {
                dialog.dismiss();
            });
            return;
        }
        ApiConUtils.MemberRegi(ApiUrl.API_URL, ApiUrl.user_register, phone, pwd, name, "1", email, recommand, new ApiConUtils.OnConnect() {
            @Override
            public void onSuccess(String jsonString) throws JSONException {
                Log.d(TAG, "Kelly find register call back : " + jsonString);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        try {
                            if (Arrays.asList(successArr).contains(jsonString)) {
                                showDialog("", "註冊失敗", (dialog1, which) -> {
                                    dialog.dismiss();
                                });

                                return;
                            }
                            switch (jsonString) {
                                case "0x0200":
                                    showDialog("", "註冊成功", (dialog1, which) -> {
                                        fragmentListener.onAction(FUNC_ACCOUNT_MAIN_TO_LOGIN, null);
                                        dialog.dismiss();
                                    });
                                    break;
                                case "0x0201":
                                    showDialog("", "註冊失敗已有相同帳號", (dialog1, which) -> {
                                        dialog.dismiss();
                                    });
                                    break;
                                case "0x0205":
                                    showDialog("", "請輸入6–12位英數密碼", (dialog1, which) -> {
                                        dialog.dismiss();
                                    });
                                    break;
                                default:
                                    showDialog("", "註冊失敗", (dialog1, which) -> {
                                        dialog.dismiss();
                                    });
                                    break;


                            }
                        } catch (Exception e) {

                        }
                    }
                });

            }

            @Override
            public void onFailure(String message) {
                Log.d(TAG, "Kelly find register call back : " + message);

            }
        });




    }



    private AlertDialog dialog = null;

    private void showDialog(String title, String message, DialogInterface.OnClickListener listener) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = new AlertDialog.Builder(requireContext()).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "確認", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onClick(dialog, which);
            }
        });
        dialog.show();
    }


}

