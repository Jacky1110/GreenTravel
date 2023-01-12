package com.jotangi.greentravel.ui.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jotangi.greentravel.AccountCustomerFragment;
import com.jotangi.greentravel.Api.ApiUrl;
import com.jotangi.greentravel.CarFixFragment;
import com.jotangi.greentravel.LoginMainActivity;
import com.jotangi.greentravel.MallPayFragment;
import com.jotangi.greentravel.MyBaseFragment;
import com.jotangi.greentravel.NoticeFragment;
import com.jotangi.greentravel.PagerStore.CouponListFragment;
import com.jotangi.greentravel.PayDataFragment;
import com.jotangi.greentravel.ProjBaseFragment;
import com.jotangi.greentravel.ProjSharePreference;
import com.jotangi.greentravel.R;
import com.jotangi.greentravel.ui.account.AccountDataFragment;
import com.jotangi.greentravel.ui.account.AccountForgetPasswordFragment;
import com.jotangi.greentravel.ui.account.AccountPointFragment;
import com.jotangi.greentravel.ui.account.AccountQAFragment;
import com.jotangi.greentravel.ui.account.AccountRegisterFragment;
import com.jotangi.greentravel.ui.account.AccountRuleFragment;
import com.jotangi.greentravel.ui.account.MemberFragment;
import com.jotangi.greentravel.ui.account.PointFragment;
import com.jotangi.greentravel.ui.account.accountOrder.AccountMallRecordFragment;
import com.jotangi.greentravel.ui.hPayMall.CDetailFragment;
import com.jotangi.greentravel.ui.hPayMall.DynamicTabFragment;
import com.jotangi.greentravel.ui.hPayMall.MemberBean;
import com.jotangi.greentravel.ui.hPayMall.SCartFragment;
import com.jotangi.greentravel.ui.mallOrder.MallOrderDetailFragment;
import com.jotangi.greentravel.ui.store.FillInInformationFragment;
import com.jotangi.greentravel.ui.store.HotelIntroduceFragment;
import com.jotangi.greentravel.ui.store.StoreTabFragment;
import com.jotangi.greentravel.ui.store.TimeSlotAppointmentFragment;
import com.jotangi.greentravel.ui.storeManager.StoreManager;
import com.jotangi.jotangi2022.ApiConUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyBaseFragment.FragmentListener {
    private static final int PERMISSION_REQUESTS = 1;
    protected final String TAG = this.getClass().getSimpleName();

    private HomeMainFragment fragment_Home;
    private ImageButton btnback, btncart, btnNotice;
    private TextView toolbarTitle;
    private BottomNavigationView bottomNavigationView;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bundle = this.getIntent().getExtras();

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_main);

        if (!allPermissionsGranted()) {
            getRuntimePermissions();
        }

        DisplayMetrics dm = getResources().getDisplayMetrics();
        Log.d(TAG, "width=" + dm.widthPixels);
        Log.d(TAG, "height=" + dm.heightPixels);
        Log.d(TAG, "xdpi=" + dm.xdpi);
        Log.d(TAG, "ydpi=" + dm.ydpi);
        Log.d(TAG, "density=" + dm.density);
        Log.d(TAG, "densitydpi=" + dm.densityDpi);
        Log.d(TAG, "scaledensity=" + dm.scaledDensity);


        initView();
        handleFragment();
        displayIntentData();
        updateCar();
    }

    private void initView() {
        toolbarTitle = findViewById(R.id.tv_toolbar_title);
        btncart = findViewById(R.id.btn_car);
        btncart.setOnClickListener(view -> {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().addToBackStack(null);
            transaction.replace(R.id.nav_host_fragment_activity_main, SCartFragment.Companion.newInstance());
            transaction.commit();

        });
        btnNotice = findViewById(R.id.btn_notice);
        btnNotice.setOnClickListener(view -> {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().addToBackStack(null);
            transaction.replace(R.id.nav_host_fragment_activity_main, NoticeFragment.newInstance());
            transaction.commit();
        });
        btnback = findViewById(R.id.btn_back);
        btnback.setOnClickListener(view -> onBackPressed());

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    HomeMainFragment homeMainFragment = new HomeMainFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.nav_host_fragment_activity_main, homeMainFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    break;
                case R.id.navigation_pointStore:
                    DynamicTabFragment dynamicTabFragment = new DynamicTabFragment();
                    FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
                    transaction1.replace(R.id.nav_host_fragment_activity_main, dynamicTabFragment);
                    transaction1.addToBackStack(null);
                    transaction1.commit();
                    break;
                case R.id.navigation_business:
                    StoreTabFragment storeTabFragment = new StoreTabFragment();
                    FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
                    transaction2.replace(R.id.nav_host_fragment_activity_main, storeTabFragment);
                    transaction2.addToBackStack(null);
                    transaction2.commit();
                    break;
                case R.id.navigation_member:
                    MemberFragment memberFragment = new MemberFragment();
                    FragmentTransaction transaction3 = getSupportFragmentManager().beginTransaction();
                    transaction3.replace(R.id.nav_host_fragment_activity_main, memberFragment);
                    transaction3.addToBackStack(null);
                    transaction3.commit();
                    break;
            }

            return true;
        });

        displayIntentData();
        updateCar();
    }

    private void handleFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment_Home = new HomeMainFragment();
        fragmentTransaction.add(R.id.nav_host_fragment_activity_main, fragment_Home);
        fragmentTransaction.show(fragment_Home);
        fragmentTransaction.commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Log.d(TAG, "onNewIntent");
        setIntent(intent);
        displayIntentData();
    }

    private void displayIntentData() {
        boolean islogin = ProjSharePreference.getLoginState(this);

        SharedPreferences isGetLogin = getSharedPreferences("loginInfo", MODE_PRIVATE);
        boolean loginresult = isGetLogin.getBoolean("isLogin", false);

        if (loginresult) {
            MemberBean.member_id = isGetLogin.getString("account", "");
            MemberBean.member_pwd = isGetLogin.getString("password", "");
        }

        Log.d(TAG, "islogin:" + (islogin ? "true" : "false"));
        if (islogin && loginresult) {
            Intent intent = getIntent();
            String action = intent.getAction();
            Uri data = intent.getData();
            String strdata = intent.getDataString();
            Log.d(TAG, "strdata=" + strdata);

            if (data != null) {
                String scheme = data.getScheme(); // "http"
                Log.d(TAG, "scheme=" + scheme);
                String host = data.getHost(); // "twitter.com"
                Log.d(TAG, "host=" + host);
                List<String> params = data.getPathSegments();

                if (params != null) {
                    for (int i = 0; i < params.size(); i++) {
                        Log.d(TAG, "p" + i + "=" + params.get(i));
                    }
                }

                String projScheme = getString(R.string.proj_scheme);
                String projHost = getString(R.string.proj_scheme_host);
                String schemeHome = getString(R.string.proj_scheme_home_main);
                String schemeTrade = getString(R.string.proj_scheme_account_trade);

                if (projScheme.equals(scheme) && projHost.equals(host)) {
                    if (params.size() > 0) {
                        if (schemeTrade.equals(params.get(0))) {
                            Log.d(TAG, "displayIntentData(), islogin=true, switch to trade");
                            switchToAccountTradeMainFragment(ProjBaseFragment.FUNC_MAIN_TO_ACCOUNT_TRADE, null);
                            return;
                        } else if (schemeHome.equals(params.get(0))) {

                        }
                    }
                }
            }
            Log.d(TAG, "displayIntentData(), islogin=true, switch to home");
            checkCart();

            switchToHomeMainFragment(ProjBaseFragment.FUNC_MAIN_TO_HOME, null);

        }
    }

    private void updateCar() {
        runOnUiThread(() -> {
            Log.d(TAG, "MemberBean.btnCart " + MemberBean.isShoppingCarPoint);

            if (MemberBean.isShoppingCarPoint) {
                btncart.setBackgroundResource(R.drawable.ic_shopping_cart_point);
            } else {
                btncart.setBackgroundResource(R.drawable.ic_shopping_cart);
            }
        });
    }

    @Override
    public void onBackPressed() {
        int fragmentSize = getSupportFragmentManager().getBackStackEntryCount();
        Log.d(TAG, "fragmentSize: " + fragmentSize);
        if (fragmentSize == 0) {
            new AlertDialog.Builder(this).setTitle("提示")
                    .setIconAttribute(android.R.attr.alertDialogIcon)
                    .setMessage("確認要離開綠悠遊嗎? ")
                    .setPositiveButton("確定", (dialog, which) -> finish())
                    .setNegativeButton("取消", null)
                    .create().show();
        } else {
            String fragmentName = getSupportFragmentManager().getBackStackEntryAt(fragmentSize - 1).getName();
            Log.d(TAG, "fragmentName: " + fragmentName);
            super.onBackPressed();
        }
    }

    private void updateActivityTitle(Integer rid) {
        toolbarTitle.setText(rid);
    }

    @Override
    public void onAction(int funcno, Object data) {
        switch (funcno) {
            case ProjBaseFragment.FUNC_FRAGMENT_RESUME:
                updateActivityTitle(Integer.valueOf(data.toString()));
                break;
            case ProjBaseFragment.FUNC_FRAGMENT_change:
                updateCar();
                break;
            case ProjBaseFragment.FUNC_LOGIN_TO_FORGET_PASSWORD:
                switchToAccountForgetFragment(funcno, data);
                break;
            case ProjBaseFragment.FUNC_LOGIN_TO_REGISTER:
                switchToAccountRegisterFragment(funcno, data);
                break;
            case ProjBaseFragment.FUNC_LOGIN_TO_RULE:
                switchToAccountRuleFragment(funcno, data);
                break;
            case ProjBaseFragment.FUNC_LOGIN_TO_ACCOUNT_MAIN: {
                switchToHomeFragment(funcno, data);
                break;
            }
            case ProjBaseFragment.FUNC_MAIN_TO_MALL: {
                switchToMall(funcno, data);
                break;
            }
            case ProjBaseFragment.FUNC_MallDetal: {
                switchToMallDetail(funcno, data);
                break;
            }
            case ProjBaseFragment.FUNC_REGISTER_TO_RULE:
                switchToAccountRuleFragment(funcno, data);
                break;
            case ProjBaseFragment.FUNC_ACCOUNT_MAIN_TO_DATA:
                switchToAccountDataFragment(funcno, data);
                break;
            case ProjBaseFragment.FUNC_ACCOUNT_MAIN_TO_TRADE_MAIN:
                switchToAccountTradeMainFragment(funcno, data);
                break;
            case ProjBaseFragment.FUNC_ACCOUNT_MAIN_TO_POINT:
                switchToAccountPointFragment(funcno, data);
                break;
            case ProjBaseFragment.FUNC_ACCOUNT_MAIN_TO_USERRULE:
                switchToAccountRuleFragment(funcno, data);
                break;
            case ProjBaseFragment.FUNC_ACCOUNT_MAIN_TO_QA:
                switchToAccountQAFragment(funcno, data);
                break;
            case ProjBaseFragment.FUNC_ACCOUNT_RECOMMEND_TO_RULE:
                switchToAccountRuleFragment(funcno, data);
                break;
            case ProjBaseFragment.FUNC_ACCOUNT_TRADE_TO_ORDER:
                switchToAccountTradeOrderFragment(funcno, data);
                break;
            case ProjBaseFragment.fraProductDetail:
                fraProDe(funcno, data);
                break;
            case ProjBaseFragment.fraPayData:
                fraPayData(funcno, data);
                break;
            case ProjBaseFragment.fraShoppingCart:
                fraSpCar(funcno, data);
                break;
            case ProjBaseFragment.fraDymaticTab:
                fraDyTab(funcno, data);
                break;
            case ProjBaseFragment.fraMallDetail:
                fraMallOderDetail(funcno, data);
                break;
            case ProjBaseFragment.fraMallRecord:
                fraMallRecord(funcno, data);
                break;
            case ProjBaseFragment.fraPay:
                fraMallPay(funcno, data);
                break;
            case ProjBaseFragment.fraStoreTab:
                fraStTab(funcno, data);
                break;
            case ProjBaseFragment.fraStoreDetail:
                fraStoreDl(funcno, data);
                break;
            case ProjBaseFragment.FUNC_HOTEL_FILL:
                switchHotelToFill(funcno, data);
                break;
            case ProjBaseFragment.FUNC_FILL_TIME:
                switchFillToTime(funcno, data);
                break;
            case ProjBaseFragment.FUNC_APP_STORETAB:
                switchAppTOStoreTab(funcno, data);
                break;
            case ProjBaseFragment.FUNC_MALL_PAY_TO_MEMBER:
                switchMallToMember(funcno, data);
                break;
            case ProjBaseFragment.FUNC_MEMBER_TO_FIX:
                switchMemberToFix(funcno, data);
                break;
            case ProjBaseFragment.FUNC_MEMBER_TO_CUSTOMER:
                switchMemberToCustomer(funcno, data);
                break;
            case ProjBaseFragment.FUNC_MEMBER_TO_COUPON:
                switchMemberToCoupon(funcno, data);
                break;
        }

    }

    private void switchMemberToCoupon(int funcno, Object data) {
        CouponListFragment fragment = CouponListFragment.newInstance();
        switchFragment(fragment, funcno);
    }

    private void switchMemberToCustomer(int funcno, Object data) {
        AccountCustomerFragment fragment = AccountCustomerFragment.newInstance();
        switchFragment(fragment, funcno);
    }

    private void switchMemberToFix(int funcno, Object data) {
        CarFixFragment fragment = CarFixFragment.newInstance();
        switchFragment(fragment, funcno);
    }

    private void switchMallToMember(int funcno, Object data) {
        MemberFragment fragment = MemberFragment.newInstance();
        switchFragment(fragment, funcno);
    }

    private void switchAppTOStoreTab(int funcno, Object data) {
        StoreTabFragment fragment = StoreTabFragment.Companion.newInstance();
        switchFragment(fragment, funcno);
    }

    private void switchFillToTime(int funcno, Object data) {
        TimeSlotAppointmentFragment fragment = TimeSlotAppointmentFragment.newInstance();
        switchFragment(fragment, funcno);
    }

    private void switchHotelToFill(int funcno, Object data) {
        FillInInformationFragment fragment = FillInInformationFragment.newInstance();
        switchFragment(fragment, funcno);
    }

    private void fraStoreDl(int funcno, Object data) {
        HotelIntroduceFragment fragment = HotelIntroduceFragment.Companion.newInstance();
        switchFragment(fragment, funcno);
    }

    private void fraStTab(int funcno, Object data) {
        StoreTabFragment fragment = StoreTabFragment.Companion.newInstance();
        switchFragment(fragment, funcno);
    }

    private void fraMallPay(int funcno, Object data) {
        MallPayFragment fragment = MallPayFragment.Companion.newInstance();
        switchFragment(fragment, funcno);
    }

    private void fraMallRecord(int funcno, Object data) {
        AccountMallRecordFragment fragment = AccountMallRecordFragment.Companion.newInstance();
        switchFragment(fragment, funcno);
    }

    private void fraMallOderDetail(int funcno, Object data) {
        MallOrderDetailFragment fragment = MallOrderDetailFragment.Companion.newInstance();
        switchFragment(fragment, funcno);
    }

    private void fraDyTab(int funcno, Object data) {
        DynamicTabFragment fragment = DynamicTabFragment.Companion.newInstance();
        switchFragment(fragment, funcno);
    }

    private void fraSpCar(int funcno, Object data) {
        SCartFragment fragment = SCartFragment.Companion.newInstance();
        switchFragment(fragment, funcno);
    }

    private void fraPayData(int funcno, Object data) {
        PayDataFragment fragment = PayDataFragment.Companion.newInstance();
        switchFragment(fragment, funcno);
    }

    private void fraProDe(int funcno, Object data) {
        CDetailFragment fragment = CDetailFragment.Companion.newInstance();
        switchFragment(fragment, funcno);
    }

    private void switchToAccountTradeOrderFragment(int funcno, Object data) {
        AccountMallRecordFragment fragment = AccountMallRecordFragment.Companion.newInstance();
        switchFragment(fragment, funcno);
    }

    private void switchToAccountQAFragment(int funcno, Object data) {
        AccountQAFragment fragment = AccountQAFragment.newInstance();
        switchFragment(fragment, funcno);
    }

    private void switchToAccountPointFragment(int funcno, Object data) {
        PointFragment fragment = PointFragment.newInstance();
        switchFragment(fragment, funcno);
    }

    private void switchToAccountDataFragment(int funcno, Object data) {
        AccountDataFragment fragment = AccountDataFragment.newInstance();
        switchFragment(fragment, funcno);
    }

    private void switchToHomeFragment(int funcno, Object data) {
        MemberFragment fragment = MemberFragment.newInstance();

        if (bundle != null) {
            fragment.setArguments(bundle);
        }

        switchFragment(fragment, funcno);
    }

    private void switchToAccountRuleFragment(int funcno, Object data) {
        AccountRuleFragment fragment = AccountRuleFragment.newInstance();
        switchFragment(fragment, funcno);
    }

    private void switchToAccountRegisterFragment(int funcno, Object data) {
        AccountRegisterFragment fragment = AccountRegisterFragment.newInstance();
        switchFragment(fragment, funcno);
    }

    private void switchToAccountForgetFragment(int funcno, Object data) {
        AccountForgetPasswordFragment fragment = AccountForgetPasswordFragment.newInstance();
        switchFragment(fragment, funcno);
    }

    private void switchToMallDetail(int funcno, Object data) {
        CDetailFragment fragment = CDetailFragment.Companion.newInstance();
        switchFragment(fragment, funcno);
    }

    private void switchFragment(MyBaseFragment fragment, int funcno) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Log.d(TAG, "transaction: " + transaction);
        // replace 替換

        transaction.replace(R.id.nav_host_fragment_activity_main, fragment);
        Log.d(TAG, "transaction.replace: " + transaction.replace(R.id.nav_host_fragment_activity_main, fragment));

        // 新增
        transaction.addToBackStack(fragment.getClass().getSimpleName());
        // commit 執行
        transaction.commit();
    }

    private void switchToHomeMainFragment(int funcno, Object data) {
        HomeMainFragment fragment = HomeMainFragment.newInstance();
        switchFragment(fragment, funcno);
        checkCart();
    }

    private void switchToMall(int funcno, Object data) {
        DynamicTabFragment fragment = DynamicTabFragment.Companion.newInstance();
        switchFragment(fragment, funcno);
    }

    private void switchToAccountTradeMainFragment(int funcno, Object data) {
        AccountMallRecordFragment fragment = AccountMallRecordFragment.Companion.newInstance();
        switchFragment(fragment, funcno);
    }

    private boolean allPermissionsGranted() {
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                return false;
            }
        }
        return true;
    }

    private String[] getRequiredPermissions() {
        try {
            PackageInfo info =
                    this.getPackageManager()
                            .getPackageInfo(this.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] ps = info.requestedPermissions;
            if (ps != null && ps.length > 0) {
                return ps;
            } else {
                return new String[0];
            }
        } catch (Exception e) {
            return new String[0];
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.i(TAG, "Permission granted!");
        if (allPermissionsGranted()) {

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean isPermissionGranted(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission granted: " + permission);
            return true;
        }
        Log.i(TAG, "Permission NOT granted: " + permission);
        return false;
    }

    private void getRuntimePermissions() {
        List<String> allNeededPermissions = new ArrayList<>();
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                allNeededPermissions.add(permission);
            }
        }

        if (!allNeededPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this, allNeededPermissions.toArray(new String[0]), PERMISSION_REQUESTS);
        }
    }

    private void checkCart() {

        String id = MemberBean.member_id;
        String pwd = MemberBean.member_pwd;
        if (id != null && pwd != null) {
            ApiConUtils.shoppingcart_count(ApiUrl.API_URL, ApiUrl.shoppingcart_count, id, pwd, new ApiConUtils.OnConnect() {
                @Override
                public void onSuccess(String jsonString) {
                    Log.d(TAG, "shoppingcart_count" + jsonString);

                    try {
                        JSONObject jsonObject = new JSONObject(jsonString);
                        String status = jsonObject.getString("status");
                        String code = jsonObject.getString("code");

                        String responseMessage = jsonObject.getString("responseMessage");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (code.equals("0x0200") && !responseMessage.equals("0")) {

                                    btncart.setBackgroundResource(R.drawable.ic_shopping_cart_point);

                                } else {

                                    btncart.setBackgroundResource(R.drawable.ic_shopping_cart);

                                }
                            }
                        });
                        new Handler(Looper.getMainLooper()).post(() -> {

                        });

                    } catch (Exception e) {

                    }
                }

                @Override
                public void onFailure(String message) {

                }
            });
        }
    }
}
