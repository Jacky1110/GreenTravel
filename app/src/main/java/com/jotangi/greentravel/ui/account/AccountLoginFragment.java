package com.jotangi.greentravel.ui.account;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.jotangi.greentravel.AccountRuleActivity;
import com.jotangi.greentravel.Api.ApiEnqueue;
import com.jotangi.greentravel.CustomDaialog;
import com.jotangi.greentravel.ui.login.ForgetPwd1Activity;
import com.jotangi.greentravel.Api.ApiUrl;
import com.jotangi.greentravel.ui.hPayMall.MemberBean;
import com.jotangi.greentravel.ui.login.SignupActivity1;
import com.jotangi.greentravel.ui.main.MainActivity;
import com.jotangi.greentravel.ProjSharePreference;
import com.jotangi.greentravel.R;
import com.jotangi.greentravel.Utils.Utility;
import com.jotangi.jotangi2022.ApiConUtils;

import org.json.JSONException;
import org.json.JSONObject;


public class AccountLoginFragment extends Fragment {
    private String TAG = getClass().getSimpleName() + "(TAG)";

    public final static String REG_PREF_NAME = "loginInfo";
    public final static String KEY_IS_LOGIN = "isLogin";
    public final static String KEY_ACCOUNT = "account";
    public final static String KEY_PASSWORD = "password";


    private String acc, pwd;

    private SharedPreferences pref;

    View rootView;
    ApiEnqueue apiEnqueue;
    LayoutInflater rule;
    EditText edPhone, edPassword;
    TextView tvForgetPassword, tvRule;
    ConstraintLayout btnRegister, btnLogin;

    public static AccountLoginFragment newInstance() {
        AccountLoginFragment fragment = new AccountLoginFragment();
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ConstraintLayout) inflater.inflate(R.layout.fragment_account_login, container, false);
        init();
        initHandler();
        handleLoin();
        return rootView;

    }

    private void init() {
        apiEnqueue = new ApiEnqueue();
        edPhone = rootView.findViewById(R.id.ed_phone);
        edPassword = rootView.findViewById(R.id.ed_home_number);
        tvRule = rootView.findViewById(R.id.txv_rule);
        btnRegister = rootView.findViewById(R.id.btn_register);
        btnLogin = rootView.findViewById(R.id.btn_login);
        tvForgetPassword = rootView.findViewById(R.id.tv_forget_password);


    }

    private void initHandler() {
        tvRule.setOnClickListener(view -> {
//                if (fragmentListener != null) {
//                    fragmentListener.onAction(FUNC_LOGIN_TO_RULE, null);
//                }
            Intent intent = new Intent(getActivity(), AccountRuleActivity.class);
            startActivity(intent);
        });

        btnRegister.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), SignupActivity1.class);
            startActivity(intent);
        });

        tvForgetPassword.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ForgetPwd1Activity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(
                view -> login()
        );
//        if (pref.getBoolean("rem_isCheck", false)) {
//            remember_key.setChecked(true);
//            edPhone.setText(pref.getString(KEY_ACCOUNT, acc));
//            edPassword.setText(pref.getString(KEY_PASSWORD, pwd));
//
//            if (pref.getBoolean("auto_isCheck", false)) {
//                automatic_login.setChecked(true);
//
//                Intent intent = new Intent(requireActivity(), MainActivity.class);
//                startActivity(intent);
//                requireActivity().finish();
//            }
//        }
    }


    public static void getpaymenturl() {
        String account = MemberBean.member_id;
        String pwd = MemberBean.member_pwd;
        ApiConUtils.payment_url(ApiUrl.API_URL, ApiUrl.get_payment_url, account, pwd, new ApiConUtils.OnConnect() {
            @Override
            public void onSuccess(String jsonString) throws JSONException {
                Log.d(" MemberBean.payment_url", " 三小?" + jsonString);
                MemberBean.payment_url = jsonString;
            }

            @Override
            public void onFailure(String message) {
                Log.d("member_usercode", " fail" + message);

            }
        });
    }

    private void login() {

        // trim:去除字串左右空格
        acc = edPhone.getText().toString().trim();
        pwd = edPassword.getText().toString().trim();

        Utility.startLoading(requireActivity());

        // 1.使用者登入
        apiEnqueue.member_login(acc, pwd, new ApiEnqueue.resultListener() {
            @Override
            public void onSuccess(String message) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ProjSharePreference.setLoginState(getActivity(), true);
                        savaLoginStatus(true, acc, pwd);
                        MemberBean.member_id = acc;
                        MemberBean.member_pwd = pwd;

//                if (acc.equals(MemberBean.member_id) && pwd.equals(MemberBean.member_pwd)) {
//                    Toast.makeText(requireActivity(), "登入成功", Toast.LENGTH_SHORT).show();
//                }

                        if (MemberBean.member_id != null && MemberBean.member_pwd != null) {
                            memberInfor();

                        } else {
                            CustomDaialog.showNormal(requireActivity(), "", "登入失敗", "確定", new CustomDaialog.OnBtnClickListener() {
                                @Override
                                public void onCheck() {

                                }

                                @Override
                                public void onCancel() {
                                    CustomDaialog.closeDialog();

                                }
                            });
                        }
                    }
                });

            }

            @Override
            public void onFailure(String message) {
                Utility.endLoading(message);
            }
        });
    }

    private void savaLoginStatus(boolean status, String acc, String pwd) {

        pref = requireActivity().getSharedPreferences(REG_PREF_NAME, MODE_PRIVATE);
        pref.edit()
                .putBoolean(KEY_IS_LOGIN, status)
                .putString(KEY_ACCOUNT, acc)
                .putString(KEY_PASSWORD, pwd)
                .commit();

        Log.d(TAG, "pref" + status + "/" + acc + "/" + pwd);


    }

    private void handleLoin() {
        pref = requireActivity().getSharedPreferences(REG_PREF_NAME, MODE_PRIVATE);
        boolean signed = pref.getBoolean(KEY_IS_LOGIN, false);
        if (signed == true) {
            MemberBean.member_id = pref.getString(KEY_ACCOUNT, acc);
            MemberBean.member_pwd = pref.getString(KEY_PASSWORD, pwd);

            Intent intent = new Intent(requireActivity(), MainActivity.class);
            startActivity(intent);
            requireActivity().finish();

        } else {
            Toast.makeText(getActivity(), "請重新輸入", Toast.LENGTH_SHORT).show();
        }
    }


    private void memberInfor() {

        // 27.綠悠遊商城/租車取得會員詳細資料
        apiEnqueue.getPersonalData(new ApiEnqueue.resultListener() {
            @Override
            public void onSuccess(String message) {
                try {
                    JSONObject jsonObject = new JSONObject(message);
                    MemberBean.name = jsonObject.getString("name");
                    MemberBean.sex = jsonObject.getString("sex");
                    MemberBean.email = jsonObject.getString("email");
                    MemberBean.tel = jsonObject.getString("tel");
                    MemberBean.birthday = jsonObject.getString("birthday");
                    MemberBean.cmdImageFile = jsonObject.getString("cmdImageFile");

                    Log.d(TAG, "cmdImageFile: " + MemberBean.cmdImageFile);

                    shoppingCar();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utility.endLoading("Api 27: 欄位剖析失敗");
                }
            }

            @Override
            public void onFailure(String message) {
                Utility.endLoading(message);
            }
        });

    }

    private void shoppingCar() {

        // 13.購物車內商品總數
        apiEnqueue.shoppingcart_count(new ApiEnqueue.resultListener() {
            @Override
            public void onSuccess(String message) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        MemberBean.isShoppingCarPoint = "0x0200".equals(message);

                        Utility.endLoading();


                        Intent intent = new Intent(requireActivity(), MainActivity.class);
                        startActivity(intent);
                        requireActivity().finish();
                    }
                });
            }

            @Override
            public void onFailure(String message) {
                Utility.endLoading(message);
            }
        });

    }


}