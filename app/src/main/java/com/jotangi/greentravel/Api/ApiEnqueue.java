package com.jotangi.greentravel.Api;

import android.util.Log;

import androidx.annotation.NonNull;

import com.jotangi.greentravel.DataBeen;
import com.jotangi.greentravel.ui.hPayMall.MemberBean;
import com.jotangi.greentravel.Utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ApiEnqueue {
    private String TAG = ApiEnqueue.class.getSimpleName() + "(TAG)";

    public interface resultListener {
        void onSuccess(final String message);

        void onFailure(final String message);
    }

    private ApiEnqueue.resultListener listener;

    public static String runTask = "";

    public final String TASK_MEMBER_LOGIN = "TASK_MEMBER_LOGIN";
    public final String TASK_USER_EDIT = "TASK_USER_EDIT";
    public final String TASK_USER_GET_DATA = "TASK_USER_GET_DATA";
    public final String TASK_PERSONAL_DATA = "TASK_PERSONAL_DATA";
    public final String TASK_QR_CONFIRM_LIST = "TASK_QR_CONFIRM_LIST";
    public final String TASK_STORE_ADMIN_LOGIN = "STORE_ADMIN_LOGIN";
    public final String TASK_STOREID_LIST = "TASK_STOREID_LIST";
    public final String TASK_STOREAPP_QRCONFIRM = "TASK_STOREAPP_QRCONFIRM";
    public final String TASK_SHOPPING_CART_COUNT = "TASK_SHOPPING_CART_COUNT";
    public final String TASK_PACKAGE_LIST = "TASK_PACKAGE_LIST";
    public final String TASK_ADD_SHOPPING_CAR = "TASK_ADD_SHOPPING_CAR";
    private final String TASK_PACKAGE_INFO = "TASK_PACKAGE_INFO";
    private final String TASK_STORE_LIST = "TASK_STORE_LIST";
    private final String TASK_RESEND_CODE = "TASK_RESEND_CODE";
    private final String TASK_CODE_VERIFY = "TASK_CODE_VERIFY";
    private final String TASK_MALL_UPDATE_PASSWORD = "TASK_MALL_UPDATE_PASSWORD";
    private final String TASK_UP_LOAD_IMAGE = "TASK_UP_LOAD_IMAGE";
    private final String TASK_FETCH_POINT_HISTORY = "TASK_FETCH_POINT_HISTORY";
    private final String TASK_EDIT_SHOPPING_INGCART = "TASK_EDIT_SHOPPING_INGCART";
    private final String TASK_FIXMOTOR_INFO = "TASK_FIXMOTOR_INFO";
    private final String TASK_MODIFY_PERSONDATA = "TASK_MODIFY_PERSONDATA";
    private final String TASK_BOOKING_FIXMOTOR = "TASK_BOOKING_FIXMOTOR";
    private final String TASK_SHOPPINGCART_LIST = "TASK_SHOPPINGCART_LIST";
    private final String TASK_QR_UNCONFIRM_LIST = "TASK_QR_UNCONFIRM_LIST";
    private final String TASK_ECORDER_INFO = "TASK_ECORDER_INFO";
    private final String TASK_FIX_MOTOR_LIST = "TASK_FIX_MOTOR_LIST";
    public final String TASK_FIX_MOTOR_CANCEL = "TASK_FIX_MOTOR_CANCEL";
    public final String TASK_BANNER_LIST = "TASK_BANNER_LIST";
    private final String TASK_PRODUCT_TYPE = "TASK_PRODUCT_TYPE";
    public final String TASK_PRODUCT_LIST = "TASK_PRODUCT_LIST";


    // 1.使用者登入
    public void member_login(String account, String pw, resultListener listen) {

        runTask = TASK_MEMBER_LOGIN;

        listener = listen;

        String url = ApiUrl.API_URL + ApiUrl.user_login;
        Log.d(TAG, "URL: " + url);

        FormBody formBody = new FormBody.Builder()
                .add("member_id", account)
                .add("member_pwd", pw)
                .build();
        Log.d(TAG, "member_id: " + account);
        Log.d(TAG, "member_pwd: " + pw);

        runOkHttp(url, formBody);
    }

    // 2.會員資料修改
    public void userEdit(String name, String sex, String email, String birthday, String address, String phone, resultListener listen) {

        runTask = TASK_USER_EDIT;

        listener = listen;

        String url = ApiUrl.API_URL + ApiUrl.user_edit;
        Log.d(TAG, "URL: " + url);

        FormBody formBody = new FormBody.Builder()
                .add("member_id", MemberBean.member_id)
                .add("member_pwd", MemberBean.member_pwd)
                .add("member_name", name)
                .add("member_gender", sex)
                .add("member_email", email)
                .add("member_birthday", birthday)
                .add("member_address", address)
                .add("member_phone", phone)
                .build();
        Log.d(TAG, "member_id: " + MemberBean.member_id);
        Log.d(TAG, "member_pwd: " + MemberBean.member_pwd);
        Log.d(TAG, "member_name: " + name);
        Log.d(TAG, "member_gender: " + sex);
        Log.d(TAG, "member_email: " + email);
        Log.d(TAG, "member_birthday: " + birthday);
        Log.d(TAG, "member_address: " + address);
        Log.d(TAG, "member_phone: " + phone);

        runOkHttp(url, formBody);
    }

    // 3.取得會員資料
    public void userGetdata(resultListener listen) {

        runTask = TASK_USER_GET_DATA;

        listener = listen;

        String url = ApiUrl.API_URL + ApiUrl.user_getdata;
        Log.d(TAG, "url: " + url);

        FormBody formBody = new FormBody.Builder()
                .add("member_id", MemberBean.member_id)
                .add("member_pwd", MemberBean.member_pwd)
                .build();
        Log.d(TAG, "member_id: " + MemberBean.member_id);
        Log.d(TAG, "member_pwd: " + MemberBean.member_pwd);

        runOkHttp(url, formBody);
    }


    //4.商城商品類別
    public void product_type(resultListener listen) {

        runTask = TASK_PRODUCT_TYPE;

        listener = listen;

        String url = ApiUrl.API_URL + ApiUrl.product_type;
        Log.d(TAG, "url: " + url);

        FormBody formBody = new FormBody.Builder()
                .add("member_id", MemberBean.member_id)
                .add("member_pwd", MemberBean.member_pwd)
                .build();
        Log.d(TAG, "member_id: " + MemberBean.member_id);
        Log.d(TAG, "member_pwd: " + MemberBean.member_pwd);

        runOkHttp(url, formBody);
    }

    // 5.商城商品列表
    public void product_list(String type, resultListener listen) {

        runTask = TASK_PRODUCT_LIST;

        listener = listen;

        String url = ApiUrl.API_URL + ApiUrl.product_list;
        Log.d(TAG, "url: " + url);

        FormBody formBody = new FormBody.Builder()
                .add("member_id", MemberBean.member_id)
                .add("member_pwd", MemberBean.member_pwd)
                .add("product_type", type)
                .build();
        Log.d(TAG, "member_id: " + MemberBean.member_id);
        Log.d(TAG, "member_pwd: " + MemberBean.member_pwd);
        Log.d(TAG, "type: " + type);

        runOkHttp(url, formBody);
    }


    // 6.商城套票列表
    public void package_list(resultListener listen) {

        runTask = TASK_PACKAGE_LIST;

        listener = listen;

        String url = ApiUrl.API_URL + ApiUrl.package_list;
        Log.d(TAG, "URL: " + url);

        FormBody formBody = new FormBody.Builder()
                .add("member_id", MemberBean.member_id)
                .add("member_pwd", MemberBean.member_pwd)
                .build();
        Log.d(TAG, "member_id: " + MemberBean.member_id);
        Log.d(TAG, "member_pwd: " + MemberBean.member_pwd);

        runOkHttp(url, formBody);

    }

    // 8.商城套票資訊
    public void packageInfo(String package_no, resultListener listen) {

        runTask = TASK_PACKAGE_INFO;

        listener = listen;

        String url = ApiUrl.API_URL + ApiUrl.package_info;
        Log.d(TAG, "URL: " + url);

        FormBody formBody = new FormBody.Builder()
                .add("member_id", MemberBean.member_id)
                .add("member_pwd", MemberBean.member_pwd)
                .add("package_no", package_no)
                .build();
        Log.d(TAG, "member_id: " + MemberBean.member_id);
        Log.d(TAG, "member_pwd: " + MemberBean.member_pwd);
        Log.d(TAG, "package_no: " + package_no);

        runOkHttp(url, formBody);
    }

    // 9.新增商品到購物車
    public void addShoppingCar(
            String product_no,
            String product_spec,
            String product_price,
            String order_qty,
            String total_amount,
            resultListener listen) {

        runTask = TASK_ADD_SHOPPING_CAR;
        listener = listen;

        String url = ApiUrl.API_URL + ApiUrl.add_shoppingcart;
        Log.d(TAG, "URL: " + url);
        Log.d(TAG, "product_no: " + MemberBean.product_no);
        FormBody formBody = new FormBody.Builder()
                .add("member_id", MemberBean.member_id)
                .add("member_pwd", MemberBean.member_pwd)
                .add("product_no", product_no)
                .add("product_spec", product_spec)
                .add("product_price", product_price)
                .add("order_qty", order_qty)
                .add("total_amount", total_amount)
                .build();
        Log.d(TAG, "member_id: " + MemberBean.member_id);
        Log.d(TAG, "member_pwd: " + MemberBean.member_pwd);
        Log.d(TAG, "product_no: " + product_no);
        Log.d(TAG, "product_spec: " + product_spec);
        Log.d(TAG, "product_price: " + product_price);
        Log.d(TAG, "order_qty: " + order_qty);
        Log.d(TAG, "total_amount: " + total_amount);

        runOkHttp(url, formBody);
    }

    // 10.購物車內商品列表
    public void shoppingcart_list(resultListener listen) {

        runTask = TASK_SHOPPINGCART_LIST;

        listener = listen;

        String url = ApiUrl.API_URL + ApiUrl.shoppingcart_list;
        Log.d(TAG, "URL: " + url);

        FormBody formBody = new FormBody.Builder()
                .add("member_id", MemberBean.member_id)
                .add("member_pwd", MemberBean.member_pwd)
                .build();
        Log.d(TAG, "member_id: " + MemberBean.member_id);
        Log.d(TAG, "member_pwd: " + MemberBean.member_pwd);


        runOkHttp(url, formBody);
    }

    // 11.修改購物車內商品數量
    public void editShoppingcart(String product_no, String order_qty, resultListener listen) {

        runTask = TASK_EDIT_SHOPPING_INGCART;

        listener = listen;

        String url = ApiUrl.API_URL + ApiUrl.edit_shoppingcart;
        Log.d(TAG, "URL: " + url);

        FormBody formBody = new FormBody.Builder()
                .add("member_id", MemberBean.member_id)
                .add("member_pwd", MemberBean.member_pwd)
                .add("product_no", product_no)
                .add("order_qty", order_qty)
                .build();
        Log.d(TAG, "member_id: " + MemberBean.member_id);
        Log.d(TAG, "member_pwd: " + MemberBean.member_pwd);
        Log.d(TAG, "product_no: " + product_no);
        Log.d(TAG, "order_qty: " + order_qty);

        runOkHttp(url, formBody);
    }

    // 13.購物車內商品總數
    public void shoppingcart_count(resultListener listen) {

        runTask = TASK_SHOPPING_CART_COUNT;

        listener = listen;

        String url = ApiUrl.API_URL + ApiUrl.shoppingcart_count;
        Log.d(TAG, "URL: " + url);

        FormBody formBody = new FormBody.Builder()
                .add("member_id", MemberBean.member_id)
                .add("member_pwd", MemberBean.member_pwd)
                .build();
        Log.d(TAG, "member_id: " + MemberBean.member_id);
        Log.d(TAG, "member_pwd: " + MemberBean.member_pwd);

        runOkHttp(url, formBody);

    }

    // 17.商店列表
    public void storeList(resultListener listen) {

        runTask = TASK_STORE_LIST;

        listener = listen;

        String url = ApiUrl.API_URL + ApiUrl.store_list;
        Log.d(TAG, "URL: " + url);

        FormBody formBody = new FormBody.Builder()
                .add("member_id", MemberBean.member_id)
                .add("member_pwd", MemberBean.member_pwd)
                .build();
        Log.d(TAG, "member_id: " + MemberBean.member_id);
        Log.d(TAG, "member_pwd: " + MemberBean.member_pwd);

        runOkHttp(url, formBody);
    }

    // 19.會員訂單詳細資料
    public void ecorderInfo(String order_no, resultListener listen) {

        runTask = TASK_ECORDER_INFO;

        listener = listen;

        String url = ApiUrl.API_URL + ApiUrl.ecorder_info;
        Log.d(TAG, "URL: " + url);

        FormBody formBody = new FormBody.Builder()
                .add("member_id", MemberBean.member_id)
                .add("member_pwd", MemberBean.member_pwd)
                .add("order_no", order_no)
                .build();
        Log.d(TAG, "member_id: " + MemberBean.member_id);
        Log.d(TAG, "member_pwd: " + MemberBean.member_pwd);
        Log.d(TAG, "order_no: " + order_no);

        runOkHttp(url, formBody);
    }

    // 20.會員未核銷商品/套票
    public void QRUnconfirmList(String ispackage, resultListener listen) {

        runTask = TASK_QR_UNCONFIRM_LIST;

        listener = listen;

        String url = ApiUrl.API_URL + ApiUrl.qr_unconfirm_list;
        Log.d(TAG, "URL: " + url);

        FormBody formBody = new FormBody.Builder()
                .add("member_id", MemberBean.member_id)
                .add("member_pwd", MemberBean.member_pwd)
                .add("ispackage", ispackage)
                .build();
        Log.d(TAG, "account: " + MemberBean.member_id);
        Log.d(TAG, "pw: " + MemberBean.member_pwd);
        Log.d(TAG, "ispackage: " + ispackage);

        runOkHttp(url, formBody);
    }

    // 21.會員已核銷商品/套票
    public void QRConfirmList(String ispackage, resultListener listen) {

        runTask = TASK_QR_CONFIRM_LIST;
        listener = listen;

        String url = ApiUrl.API_URL + ApiUrl.qr_confirm_list;
        Log.d(TAG, "URL: " + url);

        FormBody formBody = new FormBody.Builder()
                .add("member_id", MemberBean.member_id)
                .add("member_pwd", MemberBean.member_pwd)
                .add("ispackage", ispackage)
                .build();
        Log.d(TAG, "account: " + MemberBean.member_id);
        Log.d(TAG, "pw: " + MemberBean.member_pwd);
        Log.d(TAG, "ispackage: " + ispackage);

        runOkHttp(url, formBody);

    }

    // 22.店家核銷商品/套票
    public void storeappQRconfirm(resultListener listen) {

        runTask = TASK_STOREAPP_QRCONFIRM;

        listener = listen;

        String url = ApiUrl.API_URL + ApiUrl.storeapp_qrconfirm;
        Log.d(TAG, "url: " + url);

        FormBody formBody = new FormBody.Builder()
                .add("store_acc", MemberBean.store_acc)
                .add("store_pwd", MemberBean.store_pwd)
                .add("qrcode", MemberBean.qrconfirm)
                .build();
        Log.d(TAG, "store_acc: " + MemberBean.store_acc);
        Log.d(TAG, "store_pwd: " + MemberBean.store_pwd);
        Log.d(TAG, "qrcode: " + MemberBean.qrconfirm);
        runOkHttp(url, formBody);
    }

    // 27.綠悠遊商城/租車取得會員詳細資料
    public void getPersonalData(resultListener listen) {

        runTask = TASK_PERSONAL_DATA;

        listener = listen;

        String url = ApiUrl.API_URL2 + ApiUrl.getpersondata;
        Log.d(TAG, "URL: " + url);

        String account = Utility.encodeFileToBase64(MemberBean.member_id);
        String pw = Utility.encodeFileToBase64(MemberBean.member_pwd);
        String accountType = Utility.encodeFileToBase64("0");

        FormBody formBody = new FormBody.Builder()
                .add("account", account)
                .add("pw", pw)
                .add("accountType", accountType)
                .build();
        Log.d(TAG, "account: " + account);
        Log.d(TAG, "pw: " + pw);
        Log.d(TAG, "accountType: " + accountType);

        runOkHttp(url, formBody);
    }

    // 28.商店Id列表
    public void storeIdlist(resultListener listen) {

        runTask = TASK_STOREID_LIST;

        listener = listen;

        String url = ApiUrl.API_URL + ApiUrl.storeId_list;
        Log.d(TAG, "URL: " + url);

        FormBody formBody = new FormBody.Builder()
                .add("store_acc", "99999")
                .add("store_pwd", "99999")
                .build();
        Log.d(TAG, "store_acc: " + "99999");
        Log.d(TAG, "store_pwd: " + "99999");

        runOkHttp(url, formBody);
    }

    //綠悠遊商城/租車修改會員詳細資料
    public void modifypersondata(String tel, String name, String sex, String city, String region, String birthday, String email,
                                 String address, resultListener listen) {

        runTask = TASK_MODIFY_PERSONDATA;

        listener = listen;

        String url = ApiUrl.API_URL2 + ApiUrl.modifypersondata;
        Log.d(TAG, "URL: " + url);

        String account = Utility.encodeFileToBase64(MemberBean.member_id);
        String pw = Utility.encodeFileToBase64(MemberBean.member_pwd);
        String accountType = Utility.encodeFileToBase64("0");

        FormBody formBody = new FormBody.Builder()
                .add("account", account)
                .add("pw", pw)
                .add("accountType", accountType)
                .add("tel", Utility.encodeFileToBase64(tel))
                .add("name", Utility.encodeFileToBase64(name))
                .add("sex", Utility.encodeFileToBase64(sex))
                .add("city", Utility.encodeFileToBase64(city))
                .add("region", Utility.encodeFileToBase64(region))
                .add("birthday", Utility.encodeFileToBase64(birthday))
                .add("email", Utility.encodeFileToBase64(email))
                .add("address", Utility.encodeFileToBase64(address))
                .build();

        Log.d(TAG, "account: " + account);
        Log.d(TAG, "pw: " + pw);
        Log.d(TAG, "accountType: " + accountType);
        Log.d(TAG, "name: " + Utility.encodeFileToBase64(name));
        Log.d(TAG, "tel: " + Utility.encodeFileToBase64(tel));
        Log.d(TAG, "birthday: " + Utility.encodeFileToBase64(birthday));
        Log.d(TAG, "email: " + Utility.encodeFileToBase64(email));
        Log.d(TAG, "sex: " + Utility.encodeFileToBase64(sex));
        Log.d(TAG, "city: " + Utility.encodeFileToBase64(city));
        Log.d(TAG, "region: " + Utility.encodeFileToBase64(region));
        Log.d(TAG, "address: " + Utility.encodeFileToBase64(address));

        runOkHttp(url, formBody);
    }


    // 29.店長登入
    public void storeAdminLogin(String storeAcc, String storePwd, String storeId, resultListener listen) {

        runTask = TASK_STORE_ADMIN_LOGIN;

        listener = listen;

        String url = ApiUrl.API_URL + ApiUrl.storeadmin_login;
        Log.d(TAG, "URL: " + url);

        FormBody formBody = new FormBody.Builder()
                .add("store_acc", storeAcc)
                .add("store_pwd", storePwd)
                .add("store_id", storeId)
                .build();
        Log.d(TAG, "storeAcc: " + storeAcc);
        Log.d(TAG, "storePwd: " + storePwd);
        Log.d(TAG, "storeId: " + storeId);

        runOkHttp(url, formBody);

    }

    //重送驗證碼
    public void resendcode(String account, String accountType, String action, resultListener listen) {

        runTask = TASK_RESEND_CODE;

        listener = listen;


        String url = ApiUrl.API_URL2 + ApiUrl.resendcode;
        Log.d(TAG, "url: " + url);


        FormBody formBody = new FormBody.Builder()
                .add("account", Utility.encodeFileToBase64(account))
                .add("accountType", Utility.encodeFileToBase64(accountType))
                .add("action", Utility.encodeFileToBase64(action))
                .build();
        Log.d(TAG, "account: " + Utility.encodeFileToBase64(account));
        Log.d(TAG, "accountType: " + Utility.encodeFileToBase64(accountType));
        Log.d(TAG, "action: " + Utility.encodeFileToBase64(action));

        runOkHttp(url, formBody);

    }

    //註冊驗證
    public void codeverify(String action, String account, String accountType, String registeCode,
                           String pw, String pw2, resultListener listen) {

        runTask = TASK_CODE_VERIFY;

        listener = listen;

        String url = ApiUrl.API_URL2 + ApiUrl.codeverify;
        Log.d(TAG, "url: " + url);


        FormBody formBody = new FormBody.Builder()
                .add("action", Utility.encodeFileToBase64(action))
                .add("account", Utility.encodeFileToBase64(account))
                .add("accountType", Utility.encodeFileToBase64(accountType))
                .add("registeCode", Utility.encodeFileToBase64(registeCode))
                .add("pw", Utility.encodeFileToBase64(pw))
                .add("pw2", Utility.encodeFileToBase64(pw2))
                .build();
        Log.d(TAG, "action: " + Utility.encodeFileToBase64(action));
        Log.d(TAG, "account: " + Utility.encodeFileToBase64(account));
        Log.d(TAG, "accountType: " + Utility.encodeFileToBase64(accountType));
        Log.d(TAG, "registeCode: " + Utility.encodeFileToBase64(registeCode));
        Log.d(TAG, "pw: " + Utility.encodeFileToBase64(pw));
        Log.d(TAG, "pw2: " + Utility.encodeFileToBase64(pw2));

        runOkHttp(url, formBody);

    }

    //更新商城密碼
    public void mallUpdatePassword(String mobile, String password, resultListener listen) {

        runTask = TASK_MALL_UPDATE_PASSWORD;

        listener = listen;

        String url = ApiUrl.MALL_REWRITE_PWD;
        Log.d(TAG, "url: " + url);

        FormBody formBody = new FormBody
                .Builder()
                .add("mobile", mobile)
                .add("password", password)
                .build();
        Log.d(TAG, "mobile: " + mobile);
        Log.d(TAG, "password: " + password);

        runOkHttp(url, formBody);
    }

    // 30.上傳會員照片
    public void uploadimage(File cmdImageFile, resultListener listen) {

        runTask = TASK_UP_LOAD_IMAGE;

        listener = listen;

        String url = ApiUrl.API_URL2 + ApiUrl.uploadimage;
        Log.d(TAG, "url: " + url);

        String account = Utility.encodeFileToBase64(MemberBean.member_id);
        String pw = Utility.encodeFileToBase64(MemberBean.member_pwd);
        String accountType = Utility.encodeFileToBase64("0");
        String imgtitle = Utility.encodeFileToBase64(MemberBean.member_id);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("cmdImageFile", cmdImageFile.getName(), RequestBody.create(cmdImageFile, MediaType.parse("image/jpg")))
                .addFormDataPart("account", account)
                .addFormDataPart("accountType", accountType)
                .addFormDataPart("pw", pw)
                .addFormDataPart("imgtitle", imgtitle)
                .build();
        Log.d(TAG, "account: " + account);
        Log.d(TAG, "accountType: " + accountType);
        Log.d(TAG, "pw: " + pw);
        Log.d(TAG, "imgtitle: " + imgtitle);
        Log.d(TAG, "cmdImageFile: " + cmdImageFile.getName());


        runOkHttps(url, requestBody);
    }

    // 31.商店可預約服務列表
    public void fixmotorInfo(String storeId, resultListener listen) {

        runTask = TASK_FIXMOTOR_INFO;

        listener = listen;

        String url = ApiUrl.API_URL + ApiUrl.fixmotor_info;
        Log.d(TAG, "url: " + url);

        FormBody formBody = new FormBody.Builder()
                .add("member_id", MemberBean.member_id)
                .add("member_pwd", MemberBean.member_pwd)
                .add("store_id", storeId)
                .build();
        Log.d(TAG, "member_id: " + MemberBean.member_id);
        Log.d(TAG, "member_pwd: " + MemberBean.member_pwd);
        Log.d(TAG, "store_id: " + storeId);

        runOkHttps(url, formBody);
    }


    // 32.客戶開始預約
    public void bookingFixmotor(String bookingdate, String duration, resultListener listen) {

        runTask = TASK_BOOKING_FIXMOTOR;

        listener = listen;

        String url = ApiUrl.API_URL + ApiUrl.booking_fixmotor;
        Log.d(TAG, "url: " + url);

        FormBody formBody = new FormBody.Builder()
                .add("member_id", MemberBean.member_id)
                .add("member_pwd", MemberBean.member_pwd)
                .add("store_id", MemberBean.store_id)
                .add("bookingdate", bookingdate)
                .add("duration", duration)
                .add("name", DataBeen.name)
                .add("phone", DataBeen.phone)
                .add("motor_no", DataBeen.licensePlate)
                .add("motor_type", DataBeen.carModel)
                .add("fixtype", DataBeen.fixCar)
                .add("description", DataBeen.condition)
                .build();
        Log.d(TAG, "member_id: " + MemberBean.member_id);
        Log.d(TAG, "member_pwd: " + MemberBean.member_pwd);
        Log.d(TAG, "store_id: " + MemberBean.store_id);
        Log.d(TAG, "bookingdate: " + DataBeen.bookingdate);
        Log.d(TAG, "duration: " + DataBeen.duration);
        Log.d(TAG, "name: " + DataBeen.name);
        Log.d(TAG, "phone: " + DataBeen.phone);
        Log.d(TAG, "motor_no: " + DataBeen.licensePlate);
        Log.d(TAG, "motor_type: " + DataBeen.carModel);
        Log.d(TAG, "fixtype: " + DataBeen.fixCar);
        Log.d(TAG, "description: " + DataBeen.condition);


        runOkHttps(url, formBody);

    }

    // 33.取得紅利點數歷史紀錄
    public void fetchPointhistory(resultListener listen) {

        runTask = TASK_FETCH_POINT_HISTORY;

        listener = listen;

        String url = ApiUrl.API_URL + ApiUrl.fetch_pointhistory;
        Log.d(TAG, "url: " + url);

        FormBody formBody = new FormBody.Builder()
                .add("member_id", MemberBean.member_id)
                .add("member_pwd", MemberBean.member_pwd)
                .build();
        Log.d(TAG, "member_id: " + MemberBean.member_id);
        Log.d(TAG, "member_pwd: " + MemberBean.member_pwd);

        runOkHttps(url, formBody);

    }

    // 35.查詢會員預約服務列表
    public void fixMotorList(String no, resultListener listen) {

        runTask = TASK_FIX_MOTOR_LIST;

        listener = listen;

        String url = ApiUrl.API_URL + ApiUrl.fixmotor_list;
        Log.d(TAG, "url: " + url);

        FormBody formBody = new FormBody.Builder()
                .add("member_id", MemberBean.member_id)
                .add("member_pwd", MemberBean.member_pwd)
                .add("motor_no", no)
                .build();

        Log.d(TAG, "member_id: " + MemberBean.member_id);
        Log.d(TAG, "member_pwd: " + MemberBean.member_pwd);
        Log.d(TAG, "motor_no: " + no);

        runOkHttps(url, formBody);
    }

    // 36.會員取消預約服務
    public void fixmotorCancel(String bid, resultListener listen) {

        runTask = TASK_FIX_MOTOR_CANCEL;

        listener = listen;

        String url = ApiUrl.API_URL + ApiUrl.fixmotor_cancel;
        Log.d(TAG, "url: " + url);

        FormBody formBody = new FormBody.Builder()
                .add("member_id", MemberBean.member_id)
                .add("member_pwd", MemberBean.member_pwd)
                .add("bid", bid)
                .build();

        Log.d(TAG, "member_id: " + MemberBean.member_id);
        Log.d(TAG, "member_pwd: " + MemberBean.member_pwd);
        Log.d(TAG, "bid: " + bid);

        runOkHttps(url, formBody);
    }

    // 37.新增取得banner資訊
    public void bannerList(resultListener listen) {

        runTask = TASK_BANNER_LIST;

        listener = listen;

        String url = ApiUrl.API_URL + ApiUrl.banner_list;
        Log.d(TAG, "url: " + url);

        FormBody formBody = new FormBody.Builder()
                .add("member_id", MemberBean.member_id)
                .add("member_pwd", MemberBean.member_pwd)
                .build();
        Log.d(TAG, "member_id: " + MemberBean.member_id);
        Log.d(TAG, "member_pwd: " + MemberBean.member_pwd);

        runOkHttps(url, formBody);
    }

    private void runOkHttps(String url, RequestBody requestBody) {
        Request request = new Request.Builder().url(url).post(requestBody).build();

        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, Utility.logTitle("onFailure"));
                e.printStackTrace();
                listener.onFailure("連線失敗");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                ResponseBody responseBody = response.body();

                if (responseBody != null) {
                    String body = responseBody.string();
                    Log.d(TAG, "body: " + body);

                    processBody(body);
                } else {
                    listener.onFailure("無回傳資料");
                }
            }

        });
    }


    private void runOkHttp(String url, FormBody formBody) {

        Request request = new Request.Builder().url(url).post(formBody).build();


        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, Utility.logTitle("onFailure"));
                e.printStackTrace();
                listener.onFailure("連線失敗");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                Log.d(TAG, Utility.logTitle("onResponse"));
                ResponseBody responseBody = response.body();

                if (responseBody != null) {
                    String body = responseBody.string();
                    Log.d(TAG, "body: " + body);

                    processBody(body);
                } else {
                    listener.onFailure("無回傳資料");
                }
            }

        });
    }

    private void processBody(String body) {


        switch (runTask) {

            // 1.使用者登入
            case TASK_MEMBER_LOGIN:
                taskMembrLogin(body, TASK_MEMBER_LOGIN);
                break;

            // 2.會員資料修改
            case TASK_USER_EDIT:
                taskUserEdit(body, TASK_USER_EDIT);
                break;

            // 3.取得會員資料
            case TASK_USER_GET_DATA:
                taskUserGetData(body, TASK_USER_GET_DATA);
                break;
            // 4.商城商品類別
            case TASK_PRODUCT_TYPE:
                taskProductType(body);
                break;
            // 5.商城商品列表
            case TASK_PRODUCT_LIST:
                taskProductList(body);
                break;

            // 6.商城套票列表
            case TASK_PACKAGE_LIST:
                taskPackageList(body, TASK_PACKAGE_LIST);
                break;

            // 8.商城套票資訊
            case TASK_PACKAGE_INFO:
                taskPackageInfo(body);
                break;

            // 9.新增商品到購物車
            case TASK_ADD_SHOPPING_CAR:
                taskAddSoppingCart(body, TASK_ADD_SHOPPING_CAR);
                break;
            // 10.購物車內商品列表
            case TASK_SHOPPINGCART_LIST:
                taskShoppingCart_List(body, TASK_SHOPPINGCART_LIST);
                break;

            // 11.修改購物車內商品數量
            case TASK_EDIT_SHOPPING_INGCART:
                taskEditShoppingIngCart(body, TASK_EDIT_SHOPPING_INGCART);
                break;

            // 13.購物車內商品總數
            case TASK_SHOPPING_CART_COUNT:
                taskShoppingCartCount(body, TASK_SHOPPING_CART_COUNT);
                break;

            // 17.商店列表
            case TASK_STORE_LIST:
                taskStoreList(body);
                break;

            // 19.會員訂單詳細資料
            case TASK_ECORDER_INFO:
                taskEcorderInfo(body);
                break;

            // 20.會員未核銷商品/套票
            case TASK_QR_UNCONFIRM_LIST:
                taskQRUnCoufirmList(body);
                break;

            // 21.會員已核銷商品/套票
            case TASK_QR_CONFIRM_LIST:
                taskQrConfirmList(body);
                break;

            // 22.店家核銷商品/套票
            case TASK_STOREAPP_QRCONFIRM:
                taskStoreAppQRconfirm(body, TASK_STOREAPP_QRCONFIRM);
                break;

            // 27.綠悠遊商城/租車取得會員詳細資料
            case TASK_PERSONAL_DATA:
                taskPersonalData(body, TASK_PERSONAL_DATA);
                break;

            // 28.商店Id列表
            case TASK_STOREID_LIST:
                taskStoreId(body, TASK_STOREID_LIST);
                break;

            //
            case TASK_MODIFY_PERSONDATA:
                taskModifyPersonData(body, TASK_MODIFY_PERSONDATA);
                break;

            // 29.店長登入
            case TASK_STORE_ADMIN_LOGIN:
                taskStoreAdminLogin(body, TASK_STORE_ADMIN_LOGIN);
                break;

            //重送驗證碼
            case TASK_RESEND_CODE:
                taskResendCode(body, TASK_RESEND_CODE);
                break;

            //註冊驗證
            case TASK_CODE_VERIFY:
                taskCodeVerify(body, TASK_CODE_VERIFY);
                break;

            //更新商城密碼
            case TASK_MALL_UPDATE_PASSWORD:
                taskMallUpdate_PASSWORD(body, TASK_MALL_UPDATE_PASSWORD);

                // 30.上傳會員照片
            case TASK_UP_LOAD_IMAGE:
                taskUpLoadImage(body, TASK_UP_LOAD_IMAGE);
                break;
            // 31.商店可預約服務列表
            case TASK_FIXMOTOR_INFO:
                taskFixmotorInfo(body, TASK_FIXMOTOR_INFO);
                break;
            // 32.客戶開始預約
            case TASK_BOOKING_FIXMOTOR:
                taskBookingFixmotor(body, TASK_BOOKING_FIXMOTOR);
                break;
            // 33.取得紅利點數歷史紀錄
            case TASK_FETCH_POINT_HISTORY:
                taskFetchPointHistory(body, TASK_FETCH_POINT_HISTORY);
                break;
            // 35.查詢會員預約服務列表
            case TASK_FIX_MOTOR_LIST:
                taskFixMotorList(body, TASK_FETCH_POINT_HISTORY);
                break;
            //36.會員取消預約服務
            case TASK_FIX_MOTOR_CANCEL:
                taskFixMotorCancel(body, TASK_FIX_MOTOR_CANCEL);
                break;
            // 37.新增取得banner資訊
            case TASK_BANNER_LIST:
                taskBannerList(body);
                break;

        }


    }

    // 1.使用者登入
    private void taskMembrLogin(String body, String task) {
        try {
            // JASON需要try/catch
            JSONObject jsonObject = new JSONObject(body);
            String code = jsonObject.getString("code");
            // code = "0x0200"
            Log.d(TAG, "code: " + code);
            String responseMessage = jsonObject.getString("responseMessage");
            Log.d(TAG, "responseMessage: " + responseMessage);

            // 判斷 A.equals(B)
            if ("0x0200".equals(code)) {
                listener.onSuccess(responseMessage);
            } else {
                listener.onFailure(responseMessage);
            }
        } catch (JSONException e) {
            Log.d(TAG, task + " 剖析失敗：欄位不存在");
            e.printStackTrace();
        }

    }

    // 2.會員資料修改
    private void taskUserEdit(String body, String task) {
        try {
            // JASON需要try/catch
            JSONObject jsonObject = new JSONObject(body);
            String code = jsonObject.getString("code");
            // code = "0x0200"
            Log.d(TAG, "code: " + code);
            String responseMessage = jsonObject.getString("responseMessage");
            Log.d(TAG, "responseMessage: " + responseMessage);

            // 判斷 A.equals(B)
            if ("0x0200".equals(code)) {
                listener.onSuccess(responseMessage);
            } else {
                listener.onFailure(responseMessage);
            }
        } catch (JSONException e) {
            Log.d(TAG, task + " 剖析失敗：欄位不存在");
            e.printStackTrace();
        }
    }

    // 3.取得會員資料
    private void taskUserGetData(String body, String task) {
        try {
            JSONObject jsonObject = new JSONObject(body);
            String returnCode = jsonObject.getString("returnCode");
            Log.d(TAG, "returnCode: " + returnCode);
            if ("101".equals(returnCode)) {
                JSONObject object = jsonObject.getJSONObject("returnData");
                listener.onSuccess(String.valueOf(object));
            }
        } catch (JSONException e) {
            Log.d(TAG, task + " 剖析失敗：欄位不存在");
            e.printStackTrace();
        }

    }

    // 4.商城商品類別
    private void taskProductType(String body) {
        listener.onSuccess(body);
    }

    // 5.商城商品列表
    private void taskProductList(String body) {
        listener.onSuccess(body);
    }

    // 6.商城套票列表
    private void taskPackageList(String body, String task) {
        listener.onSuccess(body);
    }

    // 8.商城套票資訊
    private void taskPackageInfo(String body) {
        listener.onSuccess(body);
    }

    // 9.新增商品到購物車
    private void taskAddSoppingCart(String body, String task) {
        try {
            JSONObject jsonObject = new JSONObject(body);
            String code = jsonObject.getString("code");
            Log.d(TAG, "code: " + code);
            String responseMessage = jsonObject.getString("responseMessage");
            Log.d(TAG, "responseMessage: " + responseMessage);

            if ("0x0200".equals(code)) {
                listener.onSuccess(code);
            } else {
                listener.onFailure(responseMessage);
            }

        } catch (JSONException e) {
            Log.d(TAG, task + " 剖析失敗：欄位不存在");
            e.printStackTrace();
        }
    }

    // 10.購物車內商品列表
    private void taskShoppingCart_List(String body, String task) {
        listener.onSuccess(body);
    }

    // 11.修改購物車內商品數量
    private void taskEditShoppingIngCart(String body, String task) {
        try {
            // JASON需要try/catch
            JSONObject jsonObject = new JSONObject(body);
            String code = jsonObject.getString("code");
            // code = "0x0200"
            Log.d(TAG, "code: " + code);
            String responseMessage = jsonObject.getString("responseMessage");
            Log.d(TAG, "responseMessage: " + responseMessage);

            // 判斷 A.equals(B)
            if ("0x0200".equals(code)) {
                listener.onSuccess(responseMessage);
            } else {
                listener.onFailure(responseMessage);
            }
        } catch (JSONException e) {
            Log.d(TAG, task + " 剖析失敗：欄位不存在");
            e.printStackTrace();
        }
    }

    // 13.購物車內商品總數
    private void taskShoppingCartCount(String body, String task) {
        try {
            JSONObject jsonObject = new JSONObject(body);
            String code = jsonObject.getString("code");
            Log.d(TAG, "code: " + code);
            String responseMessage = jsonObject.getString("responseMessage");
            Log.d(TAG, "responseMessage: " + responseMessage);

            if ("0x0200".equals(code) || "0x0201".equals(code)) {
                listener.onSuccess(code);
            } else {
                listener.onFailure(responseMessage);
            }
        } catch (JSONException e) {
            Log.d(TAG, task + " 剖析失敗：欄位不存在");
            e.printStackTrace();
        }

    }

    // 17.商店列表
    private void taskStoreList(String body) {

        listener.onSuccess(body);
    }

    // 19.會員訂單詳細資料
    private void taskEcorderInfo(String body) {

        listener.onSuccess(body);

    }

    // 20.會員未核銷商品/套票
    private void taskQRUnCoufirmList(String body) {
        listener.onSuccess(body);
    }

    // 21.會員已核銷商品/套票
    private void taskQrConfirmList(String body) {

        listener.onSuccess(body);
    }

    // 22.店家核銷商品/套票
    private void taskStoreAppQRconfirm(String body, String task) {

        try {
            JSONObject jsonObject = new JSONObject(body);
            String code = jsonObject.getString("code");
            Log.d(TAG, "code: " + code);
            String responseMessage = jsonObject.getString("responseMessage");
            Log.d(TAG, "responseMessage: " + responseMessage);

            if ("0x0200".equals(code)) {
                listener.onSuccess(responseMessage);
            } else {
                listener.onFailure(responseMessage);
            }
        } catch (JSONException e) {
            Log.d(TAG, task + " 剖析失敗：欄位不存在");
            e.printStackTrace();
        }
    }

    // 27.綠悠遊商城/租車取得會員詳細資料
    private void taskPersonalData(String body, String task) {

        try {
            JSONObject jsonObject = new JSONObject(body);
            String returnCode = jsonObject.getString("returnCode");
            Log.d(TAG, "returnCode: " + returnCode);
            if ("101".equals(returnCode)) {
                JSONObject object = jsonObject.getJSONObject("returnData");
                listener.onSuccess(String.valueOf(object));
            }
        } catch (JSONException e) {
            Log.d(TAG, task + " 剖析失敗：欄位不存在");
            e.printStackTrace();
        }
    }

    // 28.商店Id列表
    private void taskStoreId(String body, String task) {

        try {
            JSONObject jsonObject = new JSONObject(body);
            String code = jsonObject.getString("code");
            Log.d(TAG, "code: " + code);

            if ("0x0200".equals(code)) {
                listener.onSuccess(jsonObject.getString("info"));
            } else {
                listener.onFailure(TASK_STOREID_LIST);
            }

        } catch (JSONException e) {
            Log.d(TAG, task + " 剖析失敗：欄位不存在");
            e.printStackTrace();
        }
    }


    private void taskModifyPersonData(String body, String task) {

        try {
            JSONObject jsonObject = new JSONObject(body);
            String returnCode = jsonObject.getString("returnCode");
            Log.d(TAG, "returnCode: " + returnCode);
            if ("101".equals(returnCode)) {
                listener.onSuccess(returnCode);
            }
        } catch (JSONException e) {
            Log.d(TAG, task + " 剖析失敗：欄位不存在");
            e.printStackTrace();
        }
    }

    // 29.店長登入
    private void taskStoreAdminLogin(String body, String task) {

        try {
            JSONObject jsonObject = new JSONObject(body);
            String code = jsonObject.getString("code");
            Log.d(TAG, "code: " + code);
            String responseMessage = jsonObject.getString("responseMessage");
            Log.d(TAG, "responseMessage: " + responseMessage);

            if ("0x0200".equals(code)) {
                JSONObject object = jsonObject.getJSONObject("info");
                listener.onSuccess(String.valueOf(object));
            } else {
                listener.onFailure(responseMessage);
            }
        } catch (JSONException e) {
            Log.d(TAG, task + " 剖析失敗：欄位不存在");
            e.printStackTrace();
        }

    }

    // 重送驗證碼
    private void taskResendCode(String body, String task) {

        try {
            JSONObject jsonObject = new JSONObject(body);
            String state = jsonObject.getString("returnCode");
            Log.d(TAG, "state: " + state);

            if ("101".equals(state)) {
                listener.onSuccess("重送成功");
            } else {
                listener.onFailure(jsonObject.getString("errorMsg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            listener.onFailure(e.getMessage());
        }
    }

    // 註冊驗證
    private void taskCodeVerify(String body, String task) {

        try {
            JSONObject jsonObject = new JSONObject(body);
            String state = jsonObject.getString("returnCode");

            if ("101".equals(state)) {
                listener.onSuccess("驗證成功");
            } else {
                listener.onFailure(jsonObject.getString("errorMsg"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            listener.onFailure(e.getMessage());
        }
    }

    //更新商城密碼
    private void taskMallUpdate_PASSWORD(String body, String task) {
        try {
            JSONObject jsonObject = new JSONObject(body);
            String code = jsonObject.getString("code");
            String responseStr = jsonObject.getString("message");

            String[] successArr = {"0x0000"};

            if (Arrays.asList(successArr).contains(code)) {
                listener.onSuccess(responseStr);
            } else {
                listener.onFailure(responseStr);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            listener.onFailure(e.getMessage());
        }
    }

    // 30.上傳會員照片
    private void taskUpLoadImage(String body, String task) {
        try {
            JSONObject jsonObject = new JSONObject(body);
            Log.e(TAG, "jsonObject :" + jsonObject);
            String returnCode = jsonObject.getString("returnCode");
            Log.d(TAG, "returnCode: " + returnCode);

            if ("101".equals(returnCode)) {
                listener.onSuccess(jsonObject.getString("responseMessage"));
            } else {
                listener.onFailure(jsonObject.getString("errorMsg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            listener.onFailure(e.getMessage());
        }

    }

    // 31.商店可預約服務列表
    private void taskFixmotorInfo(String body, String task) {
        try {
            // JASON需要try/catch
            JSONObject jsonObject = new JSONObject(body);
            String code = jsonObject.getString("code");
            // code = "0x0200"
            Log.d(TAG, "code: " + code);
            String data = jsonObject.getString("data");
            Log.d(TAG, "data: " + data);

            // 判斷 A.equals(B)
            if ("0x0200".equals(code)) {
                listener.onSuccess(data);
            } else {
                listener.onFailure(data);
            }
        } catch (JSONException e) {
            Log.d(TAG, task + " 剖析失敗：欄位不存在");
            e.printStackTrace();
        }
    }

    // 32.客戶開始預約
    private void taskBookingFixmotor(String body, String task) {
        try {
            // JASON需要try/catch
            JSONObject jsonObject = new JSONObject(body);
            String code = jsonObject.getString("code");
            // code = "0x0200"
            Log.d(TAG, "code: " + code);
            String responseMessage = jsonObject.getString("responseMessage");
            Log.d(TAG, "responseMessage: " + responseMessage);

            // 判斷 A.equals(B)
            if ("0x0200".equals(code)) {
                listener.onSuccess(responseMessage);
            } else {
                listener.onFailure(responseMessage);
            }
        } catch (JSONException e) {
            Log.d(TAG, task + " 剖析失敗：欄位不存在");
            e.printStackTrace();
        }

    }

    // 33.取得紅利點數歷史紀錄
    private void taskFetchPointHistory(String body, String task) {
        try {
            JSONObject jsonObject = new JSONObject(body);
            String code = jsonObject.getString("code");
            Log.d(TAG, "code: " + code);

            if ("0x0200".equals(code)) {
                listener.onSuccess(jsonObject.getString("data"));
            }

        } catch (JSONException e) {
            Log.d(TAG, task + " 剖析失敗：欄位不存在");
            e.printStackTrace();
        }
    }

    // 35.查詢會員預約服務列表
    private void taskFixMotorList(String body, String task) {
        try {
            JSONObject jsonObject = new JSONObject(body);
            String code = jsonObject.getString("code");
            Log.d(TAG, "code: " + code);

            if ("0x0200".equals(code)) {
                listener.onSuccess(jsonObject.getString("data"));
            }

        } catch (JSONException e) {
            Log.d(TAG, task + " 剖析失敗：欄位不存在");
            e.printStackTrace();
        }

    }

    //36.會員取消預約服務
    private void taskFixMotorCancel(String body, String task) {
        try {
            // JASON需要try/catch
            JSONObject jsonObject = new JSONObject(body);
            String code = jsonObject.getString("code");
            // code = "0x0200"
            Log.d(TAG, "code: " + code);
            String responseMessage = jsonObject.getString("responseMessage");
            Log.d(TAG, "responseMessage: " + responseMessage);

            // 判斷 A.equals(B)
            if ("0x0200".equals(code)) {
                listener.onSuccess(responseMessage);
            } else {
                listener.onFailure(responseMessage);
            }
        } catch (JSONException e) {
            Log.d(TAG, task + " 剖析失敗：欄位不存在");
            e.printStackTrace();
        }
    }

    // 37.新增取得banner資訊
    private void taskBannerList(String body) {
        listener.onSuccess(body);
    }
}

