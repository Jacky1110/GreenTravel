package com.jotangi.greentravel.ui.account;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.jotangi.greentravel.ProjConstraintFragment;
import com.jotangi.greentravel.R;

import java.util.concurrent.atomic.AtomicInteger;

public class AccountForgetPasswordFragment extends ProjConstraintFragment {

    private String TAG = getClass().getSimpleName() + "(TAG)";

    private final int TOTAL_WAITTING_SECONDS = 60;
    private AtomicInteger getCodeTimer = new AtomicInteger(0);
    TextView bnGetIdentityCode;
    Handler handler;
    TextInputEditText edPhone, edIdentity, edNewpwd, edNewpwd2;
    ConstraintLayout btnsend;

    public static AccountForgetPasswordFragment newInstance() {
        AccountForgetPasswordFragment fragment = new AccountForgetPasswordFragment();
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        rootView = (ConstraintLayout) inflater.inflate(R.layout.fragment_account_forget_password, container, false);

        return rootView;
    }

    @Override
    protected void initViews() {
        super.initViews();
        handler = new Handler();
        edPhone = rootView.findViewById(R.id.ed_phone);
        edIdentity = rootView.findViewById(R.id.ed_identity);
        edNewpwd = rootView.findViewById(R.id.ed_newpwd);
        edNewpwd2 = rootView.findViewById(R.id.ed_password2);
        btnsend = rootView.findViewById(R.id.btn_sendASP);
        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetpwd();
            }
        });
        bnGetIdentityCode = rootView.findViewById(R.id.tv_get_identify_code);
        bnGetIdentityCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getCodeTimer.get() == 0) {
                    usercode();

                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        activityTitleRid = R.string.forget_password_title;
    }

    @Override
    public void onResume() {
        super.onResume();
        getCodeTimer.set(0);
        updateWaittingTimerUI(0);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(
                runUpdateWaittingTimer
        );
    }

    private Runnable runUpdateWaittingTimer = new Runnable() {
        @Override
        public void run() {
            int seconds = getCodeTimer.get();
            Log.d(TAG, "runUpdateWaittingTimer.run(), seconds=" + seconds);
            updateWaittingTimerUI(seconds);
            if (seconds > 0) {
                delayDoUpdateWaittingTimeUI(1000);
                getCodeTimer.set(seconds - 1);

            }
        }
    };

    private void updateWaittingTimerUI(int seconds) {
        if (seconds > 0) {
            int minute = seconds / 60;
            seconds = seconds % 60;
//            bnGetIdentityCode.setText(getString(R.string.minute_seconds, minute, seconds));
        } else {
//            bnGetIdentityCode.setText(R.string.button_identity_code);
        }
    }

    private void delayDoUpdateWaittingTimeUI(int delayms) {
        handler.postDelayed(runUpdateWaittingTimer, delayms);
    }


    private void usercode() {
        String account = edPhone.getText().toString();
        if (account.equals("")) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    CustomDaialog.showNormal(requireActivity(), "", "請輸入手機號碼", "確定", new CustomDaialog.OnBtnClickListener() {
//                        @Override
//                        public void onCheck() { }
//                        @Override
//                        public void onCancel() { CustomDaialog.closeDialog(); }
//                    });
                }
            });
            return;

        }
        getCodeTimer.set(TOTAL_WAITTING_SECONDS);
        updateWaittingTimerUI(TOTAL_WAITTING_SECONDS);
        delayDoUpdateWaittingTimeUI(1000);
//            ApiConUtils.member_usercode(ApiUrl.API_URL, ApiUrl.user_code, account, new ApiConUtils.OnConnect() {
//                @Override
//                public void onSuccess(String jsonString) {
//                    if (jsonString.equals("0x0200")) {
//                        Log.d(TAG, " success" + jsonString);
//                    } else {
//                        Log.d(TAG, " fail" + jsonString);
//                    }
//                }
//
//                @Override
//                public void onFailure(String message) {
//                    Log.d(TAG, " fail" + message);
//                }
//            });


    }

    private void resetpwd() {
        String account = edPhone.getText().toString();
        String pwd = edNewpwd.getText().toString();
        String pwd2 = edNewpwd2.getText().toString().trim();
        String code = edIdentity.getText().toString().trim();

        if (pwd.equals(pwd2)) {
//            ApiConUtils.member_resetpwd(ApiUrl.API_URL, ApiUrl.user_resetpwd, account, pwd, code, new ApiConUtils.OnConnect() {
//                @Override
//                public void onSuccess(String jsonString) {
//                    Log.d("重設密碼 ", " member_resetpwd success  " + jsonString);
//                    getActivity().runOnUiThread(() -> {
//                        if (jsonString.equals("0x0200")) {
//
//                            CustomDaialog.showNormal(requireActivity(), "", "驗證碼正確", "確定", new CustomDaialog.OnBtnClickListener() {
//                                @Override
//                                public void onCheck() {
//
//                                }
//
//                                @Override
//                                public void onCancel() {
//
//                                    fragmentListener.onAction(FUNC_ACCOUNT_MAIN_TO_LOGIN, null);
//                                    CustomDaialog.closeDialog();
//
//                                }
//                            });
//                            return;
//                        } else {
//                            Log.d(TAG, " fail" + jsonString);
//                        }
//                    });
//                }
//
//                @Override
//                public void onFailure(String message) {
//                    Log.d("重設密碼 ", " member_resetpwd Failure  " + message);
//                }
//            });
        } else {
            getActivity().runOnUiThread(() -> {
//                CustomDaialog.showNormal(requireActivity(), "", "密碼不相符", "確定", new CustomDaialog.OnBtnClickListener() {
//                    @Override
//                    public void onCheck() { }
//                    @Override
//                    public void onCancel() { CustomDaialog.closeDialog(); }
            });
            return;

//            });

        }

    }
}