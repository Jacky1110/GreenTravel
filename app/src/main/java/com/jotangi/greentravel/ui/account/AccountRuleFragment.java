package com.jotangi.greentravel.ui.account;

import static android.content.Context.MODE_PRIVATE;
import static com.jotangi.greentravel.ui.account.AccountLoginFragment.getpaymenturl;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.jotangi.greentravel.ui.hPayMall.MemberBean;
import com.jotangi.greentravel.ProjConstraintFragment;
import com.jotangi.greentravel.R;


public class AccountRuleFragment extends ProjConstraintFragment {

    TextView tvRule;
    public static AccountRuleFragment newInstance() {
        AccountRuleFragment fragment = new AccountRuleFragment();
        return fragment;
    }
    @Override
    public void onStart() {
        super.onStart();
        activityTitleRid = R.string.account_rule;
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        rootView = (ConstraintLayout) inflater.inflate(R.layout.fragment_account_rule, container, false);

        return rootView;
    }

    @Override
    protected void initViews() {
        super.initViews();

        tvRule = rootView.findViewById(R.id.tv_rule);
        String terms = getString(R.string.rule_msg);
        String test1=terms.substring(0,4);
        String test=terms.substring(4, terms.length());
        tvRule.setText(test);
        savaTermsStatus(false, test1);
        tvRule.setMovementMethod(new ScrollingMovementMethod());
    }

    private void savaTermsStatus(boolean status, String code) {

        SharedPreferences pref = requireActivity().getSharedPreferences("UserTerms", MODE_PRIVATE);
        pref.edit()
                .putBoolean("Terms", status)
                .putString("code", code)
                .commit();
    }

    private void compare() {
        SharedPreferences isGetLogin = getActivity().getSharedPreferences("UserTerms", MODE_PRIVATE);
        boolean termsresult = isGetLogin.getBoolean("TermsUpdate", false);
        String termsCode = isGetLogin.getString("Now", "");
        if (termsresult) {
            MemberBean.tel = isGetLogin.getString("account", "");
            MemberBean.member_pwd = isGetLogin.getString("password", "");
            getpaymenturl();
        }
    }
}