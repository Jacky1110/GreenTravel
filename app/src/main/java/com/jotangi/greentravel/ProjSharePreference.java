package com.jotangi.greentravel;

import android.content.Context;
import android.content.SharedPreferences;

public class ProjSharePreference {
    public final static String KEY_PREFERENCE_NAME = "jotangi_hpay";
    public final static String KEY_LOGIN_STATE = "login_state";

    public static void setLoginState(Context context, boolean value) {
        SharedPreferences pref = context.getSharedPreferences(KEY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(KEY_LOGIN_STATE, value);


        editor.commit();
    }

    public static boolean getLoginState(Context context) {
        SharedPreferences pref = context.getSharedPreferences(KEY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return pref.getBoolean(KEY_LOGIN_STATE, false);
    }
}
