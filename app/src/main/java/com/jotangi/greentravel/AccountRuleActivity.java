package com.jotangi.greentravel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class AccountRuleActivity extends AppCompatActivity {

    TextView tvRule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_rule);

        init();
    }

    private void init(){
        tvRule = findViewById(R.id.tv_rule);
        String terms = getString(R.string.rule_msg);
        String test1=terms.substring(0,4);
        String test=terms.substring(4, terms.length());
        tvRule.setText(test);
        savaTermsStatus(false, test1);
        tvRule.setMovementMethod(new ScrollingMovementMethod());
    }

    private void savaTermsStatus(boolean status, String code) {

        SharedPreferences pref = this.getSharedPreferences("UserTerms", MODE_PRIVATE);
        pref.edit()
                .putBoolean("Terms", status)
                .putString("code", code)
                .commit();
    }
}