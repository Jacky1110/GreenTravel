package com.jotangi.greentravel.ui.storeManager;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.jotangi.greentravel.Api.ApiEnqueue;
import com.jotangi.greentravel.ProjSharePreference;
import com.jotangi.greentravel.R;
import com.jotangi.greentravel.ui.hPayMall.MemberBean;
import com.jotangi.greentravel.Utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StoreManagerFragment extends Fragment {
    private String TAG = getClass().getSimpleName() + "(TAG)";

    View rootView;
    ApiEnqueue apiEnqueue;
    EditText edNumber, edPwd;
    ConstraintLayout btnLogin;
    Spinner spinner;
    JSONArray jsonArray;

    private String storeAcc, storePwd, storeId;

    private SharedPreferences pref;

    public final static String REG_PREF_NAME = "loginInfo";
    public final static String KEY_IS_LOGIN = "isLogin";
    public final static String KEY_ACCOUNT = "account";
    public final static String KEY_PASSWORD = "password";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_store_manager, container, false);
        apiEnqueue = new ApiEnqueue();
        // 28.商店Id列表
        storeIdlist();

        edNumber = rootView.findViewById(R.id.mangerPh);
        edPwd = rootView.findViewById(R.id.mangerPw);


        btnLogin = rootView.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(view -> login());

        handleLoin();
        return rootView;
    }

    private void storeIdlist() {

        apiEnqueue.storeIdlist(new ApiEnqueue.resultListener() {
            @Override
            public void onSuccess(String message) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            jsonArray = new JSONArray(message);
                            String[] storeName = new String[jsonArray.length()];
                            JSONObject storeNameJB;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                storeNameJB = (JSONObject) jsonArray.get(i);
                                storeName[i] = storeNameJB.getString("store_name");
                            }
                            Log.d(TAG, "storeName: " + storeName);

                            upUiSpinner(storeName);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    private void upUiSpinner(String[] storeName) {

        requireActivity().runOnUiThread(() -> {
            spinner = rootView.findViewById(R.id.spinner);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                    requireActivity(), R.layout.spinner_item_selected, storeName);
            dataAdapter.setDropDownViewResource(
                    R.layout.spinner_item_selected);
            spinner.setAdapter(dataAdapter);
        });
    }


    private void login() {

        storeAcc = edNumber.getText().toString().trim();
        storePwd = edPwd.getText().toString().trim();
        storeId = "";

        Utility.startLoading(requireActivity());


        try {
            JSONObject jsonObject = (JSONObject) jsonArray.get(spinner.getSelectedItemPosition());
            storeId = jsonObject.getString("store_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        apiEnqueue.storeAdminLogin(storeAcc, storePwd, storeId, new ApiEnqueue.resultListener() {

            @Override
            public void onSuccess(String message) {
                ProjSharePreference.setLoginState(getActivity(), true);
                savaLoginStatus(true, storeAcc, storePwd, storeId);
                MemberBean.store_acc = storeAcc;
                MemberBean.store_pwd = storePwd;
                MemberBean.store_id = storeId;

                Utility.endLoading();

                Intent intent = new Intent(requireActivity(), StoreManager.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(String message) {
                requireActivity().runOnUiThread(() -> {
                    Utility.endLoading(message);
                    Toast.makeText(requireActivity(), "帳號密碼錯誤", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void savaLoginStatus(boolean status, String storeAcc, String storePwd, String storeId) {
        pref = requireActivity().getSharedPreferences(REG_PREF_NAME, MODE_PRIVATE);
        pref.edit()
                .putBoolean(KEY_IS_LOGIN, status)
                .putString(KEY_ACCOUNT, storeAcc)
                .putString(KEY_PASSWORD, storePwd)
                .commit();
    }

    private void handleLoin() {
        pref = requireActivity().getSharedPreferences(REG_PREF_NAME, MODE_PRIVATE);
        boolean signed = pref.getBoolean(KEY_IS_LOGIN, false);
        if (signed == true) {
            MemberBean.store_acc = pref.getString(KEY_ACCOUNT, storeAcc);
            MemberBean.store_pwd = pref.getString(KEY_PASSWORD, storePwd);

            Intent intent = new Intent(requireActivity(), StoreManager.class);
            startActivity(intent);
            requireActivity().finish();

        } else {
            Toast.makeText(getActivity(), "請重新輸入", Toast.LENGTH_SHORT).show();
        }
    }
}