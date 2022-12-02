package com.jotangi.greentravel;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.security.KeyChain;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Keychain {

    private static final String TAG = KeyChain.class.getSimpleName();

    public static final String ACCESS_TOKEN = "key_access_token";

    public static final String ACCOUNT = "key_account";
    public static final String PW      = "key_pw";
    public static final String TEL     = "key_tel";
    public static final String NAME    = "key_name";
    public static final String E_MAIL  = "key_email";
    public static final String BOSSOWNERSTOREID  = "key_bossOwner";
    public static final String BOSS_STORE_LIST  = "key_bossStore";


    private static SharedPreferences appSharedPrefs;
    private static SharedPreferences.Editor prefsEditor;


    public static void init(Context context) {
        appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = appSharedPrefs.edit();
    }

    public static int getInt(Context context, String key, int defValue) {
        init(context);

        return appSharedPrefs.getInt(key, defValue);
    }

    public static void setInt(Context context, String key, int value) {
        init(context);

        prefsEditor.putInt(key, value);
        prefsEditor.commit();

    }


    public static long getLong(Context context, String key, long defValue) {
        init(context);

        return appSharedPrefs.getLong(key, defValue);
    }

    public static void setLong(Context context, String key, long value) {
        init(context);

        prefsEditor.putLong(key, value);
        prefsEditor.commit();
    }

    public static float getFloat(Context context, String key, float defValue) {
        init(context);

        return appSharedPrefs.getFloat(key, defValue);
    }

    public static void setFloat(Context context, String key, float value) {
        init(context);

        prefsEditor.putFloat(key, value);
        prefsEditor.commit();
    }

    public static String getString(Context context, String key, String defValue) {

        init(context);

        try {

            String base64Str = appSharedPrefs.getString(key, defValue);

            if (base64Str.equals("")) {
                return base64Str;
            } else {
                String decrypt = decrypt(base64Str);
                return decrypt;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static void setString(Context context, String key, String data) {

        init(context);
        try {

            if (data.equals("")) {
                prefsEditor.putString(key, data);
            } else {
                String encryptStr = encrypt(data);
                prefsEditor.putString(key, encryptStr);
            }

            prefsEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        init(context);

        return appSharedPrefs.getBoolean(key, defValue);
    }

    public static boolean setBoolean(Context context, String key, boolean data) {
        init(context);

        prefsEditor.putBoolean(key, data);
        prefsEditor.commit();
        return data;
    }


    public static Set<String> getStringSet(Context context, String key) {
        init(context);

        return appSharedPrefs.getStringSet(key, new HashSet<String>());
    }

    public static void setStringSet(Context context, String key, Set<String> data_set) {
        init(context);

        prefsEditor.putStringSet(key, data_set);
        prefsEditor.commit();
    }

    public static ArrayList<String> getStringArray(Context context, String key, String defValue) {
        init(context);
        String data = appSharedPrefs.getString(key, defValue);
        ArrayList<String> string_array = new ArrayList<>();

        if (data != null) {
            for (int i = 0; i < data.split("\n").length; i++) {
                string_array.add(data.split("\n")[i]);
            }
        }
        return string_array;
    }

    public static void setStringArray(Context context, String key, ArrayList<String> data) {
        init(context);
        String string_array = "";
        if (data != null) {
            for (int i = 0; i < data.size() - 1; i++) {
                string_array = string_array + data.get(i) + "\n";
            }
        } else string_array = null;
        prefsEditor.putString(key, string_array);
        prefsEditor.commit();
    }

    public static ArrayList<Float> getFloatArray(Context context, String key, String defValue) {
        init(context);
        String data = appSharedPrefs.getString(key, defValue);
        ArrayList<Float> string_array = new ArrayList<>();

        if (data != null) {
            for (int i = 0; i < data.split("\n").length - 1; i++) {
                string_array.add(Float.parseFloat(data.split("\n")[i]));
            }
        }
        return string_array;
    }

    public static void setFloatArray(Context context, String key, ArrayList<Float> data) {
        init(context);
        String string_array = "";
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                string_array = string_array + data.get(i) + "\n";
            }
        } else string_array = null;
        prefsEditor.putString(key, string_array);
        prefsEditor.commit();
    }



    private static final String CipherMode = "AES/CFB/NoPadding";  //儲存機敏資料使用

    private static final String AllPayCipherMode = "AES/CBC/PKCS5Padding";  //allPay使用

    private static final String key = "Y3UJ147HKIYRT8Ovrsik0A==";
    private static final String iv  = "8672731586517315";

    public static String allPayEncrypt(String data) {

        try {
            Cipher cipher = Cipher.getInstance(AllPayCipherMode);
            SecretKeySpec keyspec = new SecretKeySpec(Base64.decode(key,Base64.DEFAULT), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, new IvParameterSpec(iv.getBytes()));
            byte[] encrypted = cipher.doFinal(data.getBytes());
            return Base64.encodeToString(encrypted, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String allPayDecrypt(String data) {
        try {
            byte[] encrypted1 = Base64.decode(data.getBytes(), Base64.DEFAULT);
            Cipher cipher = Cipher.getInstance(AllPayCipherMode);
            SecretKeySpec keyspec = new SecretKeySpec(Base64.decode(key,Base64.DEFAULT), "AES");
            cipher.init(Cipher.DECRYPT_MODE, keyspec, new IvParameterSpec(iv.getBytes()));
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String encrypt(String data) throws Exception {

        try {
            Cipher cipher = Cipher.getInstance(CipherMode);
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, new IvParameterSpec(iv.getBytes()));
            byte[] encrypted = cipher.doFinal(data.getBytes());
            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String decrypt(String data) throws Exception {
        try {
            byte[] encrypted1 = Base64.decode(data.getBytes(), Base64.DEFAULT);
            Cipher cipher = Cipher.getInstance(CipherMode);
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, keyspec, new IvParameterSpec(iv.getBytes()));
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decryptResponseAndGetHppURL(String encryptStr){

        String decryptStr = Keychain.allPayDecrypt(encryptStr);
        Log.e(TAG,"解密內文 = " + decryptStr);

        try {
            JSONObject jsonObject = new JSONObject(decryptStr);
            String dataString = jsonObject.getString("Data");
            JSONObject dataObject = new JSONObject(dataString);
            String urlString = dataObject.getString("HppURL");
            return urlString;

        } catch (JSONException e) {
            return "";
        }

    }

    /** Created by KuenFu on 2020/08/28 */
    public static String decryptResponseAndGetJKOPayPaymentURL(String encryptStr){

        String decryptStr = Keychain.allPayDecrypt(encryptStr);
        Log.e(TAG,"解密內文 = " + decryptStr);

        try {
            JSONObject jsonObject = new JSONObject(decryptStr);
            String dataString = jsonObject.getString("Data");
            JSONObject dataObject = new JSONObject(dataString);
            String urlString = dataObject.getString("PaymentURL");
            return urlString;

        } catch (JSONException e) {
            return "";
        }

    }
    /** Created by KuenFu on 2020/08/28 */

}
