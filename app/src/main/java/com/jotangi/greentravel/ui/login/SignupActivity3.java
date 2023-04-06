package com.jotangi.greentravel.ui.login;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jotangi.greentravel.Api.ApiEnqueue;
import com.jotangi.greentravel.AppUtility;
import com.jotangi.greentravel.Base.BaseActivity;
import com.jotangi.greentravel.Api.ApiUrl;
import com.jotangi.greentravel.Keychain;
import com.jotangi.greentravel.R;
import com.jotangi.greentravel.ui.hPayMall.MemberBean;
import com.jotangi.jotangi2022.ApiConUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class SignupActivity3 extends BaseActivity implements View.OnClickListener {

    private static final String TAG = SignupActivity3.class.getSimpleName();

    EditText etName;  //姓名
    EditText etMail;  //email
    EditText etReferrerPhone;  //推薦人

    TextView tvPhone, tvCategoryView, tvStoreView;  //account

    EditText etAdd;  //地址

    Button btnBirthday;  //日期選擇
    Button submitBtn; //同意並完成註冊

    Button link_ReadTerms; //會員條款

    ImageButton BtnGoBack;

    ProgressBar progressBar;

    ApiEnqueue apiEnqueue;

    private DatePickerDialog datePickerDialog;

    private String selectedDate;

    private RadioButton r1, r2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup3);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);

        initView();
    }

    private void initView() {

        apiEnqueue = new ApiEnqueue();

        progressBar = findViewById(R.id.progressBar);

        etName = findViewById(R.id.etName);
        etMail = findViewById(R.id.etMail);
        r1 = findViewById(R.id.rBtn_Male);
        r2 = findViewById(R.id.rBtn_Female);

        btnBirthday = findViewById(R.id.etBirthday);
        btnBirthday.setOnClickListener(this);

        submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(this);

        //datePicker
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, 0, 1);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(context, AlertDialog.THEME_HOLO_DARK, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                selectedDate = String.format("%d-%02d-%02d", year, (month + 1), dayOfMonth);
                String dateString = year + "年" + (month + 1) + "月" + dayOfMonth + "日";
                Log.e(TAG, "select = " + dateString);
                btnBirthday.setText(dateString);
            }
        }, year, month, day);

        tvPhone = findViewById(R.id.tvPhone);
        tvPhone.setText(Keychain.getString(context, Keychain.ACCOUNT, ""));


        link_ReadTerms = findViewById(R.id.link_ReadTerms);
        link_ReadTerms.setOnClickListener(this);

        BtnGoBack = findViewById(R.id.btn_go_back);
        BtnGoBack.setOnClickListener(this);

        etReferrerPhone = findViewById(R.id.etReferrerPhone);

        tvStoreView = findViewById(R.id.tv_store_view);
        tvStoreView.setOnClickListener(view -> {
            getRecommendedStoreName();
        });
        tvCategoryView = findViewById(R.id.tv_category_view);
        tvCategoryView.setOnClickListener(view -> {
            tvStoreView.setText("");
            getRecommendedStoreId();
        });
    }

    private void getRecommendedStoreId() {

        apiEnqueue.storetypeList(new ApiEnqueue.resultListener() {
            @Override
            public void onSuccess(String message) {
                runOnUiThread(() -> {
                    try {
                        JSONArray jsonArray = new JSONArray(message);
                        Log.d(TAG, "storetype: " + jsonArray);
                        String[] name = new String[jsonArray.length()];
                        JSONObject storeObject;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            storeObject = (JSONObject) jsonArray.get(i);
                            name[i] = storeObject.getString("storetype_name");
                            Log.d(TAG, "storetype_name: " + name[i]);

                        }
                        ArrayList<String> typeName = new ArrayList<>(Arrays.asList(name));
                        typeName.add("無");

                        showChooseDialog(typeName);

                    } catch (JSONException e) {

                        e.printStackTrace();

                    }

                });
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    private void showChooseDialog(ArrayList<String> typeName) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(typeName.toArray(new String[0]), (dialog, which) -> {

            tvCategoryView.setText(typeName.get(which));

            switch (typeName.get(which)) {
                case "租車":

                    MemberBean.storetype_id = "2";

                    break;
                case "購車維修保養":

                    MemberBean.storetype_id = "1";

                    break;
                case "無":

                    MemberBean.storetype_id = "-1";
                    tvStoreView.setText("無");
                    break;
            }

            dialog.dismiss();

            Log.d(TAG, "showChooseDialog: " + typeName.get(which));

        });
        builder.show();
    }

    private void getRecommendedStoreName() {

        String storeType = MemberBean.storetype_id;

        apiEnqueue.storeList(storeType, new ApiEnqueue.resultListener() {
            @Override
            public void onSuccess(String message) {
                runOnUiThread(() -> {
                    try {
                        JSONArray jsonArray = new JSONArray(message);
                        String[] storeName = new String[jsonArray.length()];
                        String[] storeId = new String[jsonArray.length()];
                        JSONObject storeObject;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            storeObject = (JSONObject) jsonArray.get(i);
                            storeName[i] = storeObject.getString("store_name");
                            storeId[i] = storeObject.getString("store_id");
                            Log.d(TAG, "store_name: " + storeName[i]);

                        }

                        showChooseStoreDialog(storeName, storeId);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                });
            }

            @Override
            public void onFailure(String message) {

            }
        });

    }

    private void showChooseStoreDialog(String[] storeName, String[] storeId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setSingleChoiceItems(storeName, 0, (dialog, which) -> {
            tvStoreView.setText(storeName[which]);
            MemberBean.storetype_name = storeName[which];
            switch (which) {
                case 0:

                    MemberBean.storeId = storeId[0];

                    break;
                case 1:

                    MemberBean.storeId = storeId[1];

                    break;
                case 2:

                    MemberBean.storeId = storeId[2];
                    break;
                case 3:

                    MemberBean.storeId = storeId[3];
                    break;
            }


            dialog.dismiss();//随便点击一个item消失对话框，不用点击确认取消

            Log.d("TAG", "showChooseStoreDialog: " + which);
            Log.d("TAG", "showChooseStoreDialog1: " + storeName[which]);
        });

        builder.show();

    }

    private void showStoreDialog(String[] storeName) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setSingleChoiceItems(storeName, 0, (dialog, which) -> {

            tvStoreView.setText(storeName[which]);
            dialog.dismiss();

            Log.d("TAG", "showStoreDialog: " + which);
            Log.d("TAG", "showStoreDialog1: " + storeName[which]);
        });

        builder.show();
    }

    @Override
    public void onClick(View view) {
        if (view == btnBirthday) {

            datePickerDialog.show();

        } else if (view == submitBtn) {
            submitData();
        } else if (view == link_ReadTerms) {
            //開啟網頁
            String url = "https://www.privacypolicies.com/privacy/view/35884d34c7937c9efe8f565a8dfdf74c";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);

        } else if (view == BtnGoBack) {
            finish();
        }
    }

    private void submitData() {

        String account = Keychain.getString(context, Keychain.ACCOUNT, "");
        String pw = Keychain.getString(context, Keychain.PW, "");
        String accountType = "0"; //帳號類型 0:一般 1:fb
        final String name = etName.getText().toString().trim();
        final String tel = tvPhone.getText().toString().trim();
        String birthday = selectedDate;
        final String email = etMail.getText().toString().trim();

        String referrerPhone = etReferrerPhone.getText().toString().trim();

        String address = "";
        String city = "";
        String region = "";
        String sex = "";
        if (r1.isChecked()) {
            sex = "0";
            Log.d("TAG", "sex: " + sex);
        } else if (r2.isChecked()) {
            sex = "1";
            Log.d("TAG", "sex: " + sex);
        }
        String shopStoreId = MemberBean.storeId;
        Log.d(TAG, "shopStoreId: " + shopStoreId);
        String shopStoreType = MemberBean.storetype_id;
        Log.d(TAG, "shopStoreType: " + shopStoreType);
        String referrerStore = "1";

        progressBar.setVisibility(View.VISIBLE);
        ApiConUtils.initPersonalData(ApiUrl.API_URL2, ApiUrl.modifypersondata, account, pw, accountType, name, tel, birthday, email, sex, city, region, address, referrerPhone, shopStoreId, shopStoreType, referrerStore, new ApiConUtils.OnConnectResultListener() {

            @Override
            public void onSuccess(final String jsonString) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Keychain.setString(context, Keychain.TEL, tel);
                        Keychain.setString(context, Keychain.NAME, name);
                        Keychain.setString(context, Keychain.E_MAIL, email);

                        progressBar.setVisibility(View.GONE);
                        AppUtility.showMyDialog(SignupActivity3.this, jsonString, "返回", null, new AppUtility.OnBtnClickListener() {
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

            @Override
            public void onFailure(final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        progressBar.setVisibility(View.GONE);
                        AppUtility.showMyDialog(SignupActivity3.this, message, "返回", null, new AppUtility.OnBtnClickListener() {
                            @Override
                            public void onCheck() {

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
