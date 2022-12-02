package com.jotangi.jotangi2022;


import static com.jotangi.jotangi2022.StringUtils.base64;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

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


public class ApiConUtils {

    private static final String TAG = ApiConUtils.class.getSimpleName() + "(TAG)";
    private static final String RETURN_SUCCESS = "101";

    private static Handler handler;
    private static OkHttpClient client;


    public interface OnConnect {
        void onSuccess(final String jsonString) throws JSONException;

        void onFailure(final String message);
    }

    public interface OnConnectResultListener {
        void onSuccess(final String jsonString);

        void onFailure(final String message);
    }

    /*
     * Get Member_Login code ,using code to boolean
     *
     * */
    @NonNull

    //member_login
    public static void member_login(String api_url, String api_constant, String account, String pwd, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody.Builder().add("member_id", account).add("member_pwd", pwd).build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        Log.d("登入", "fuck " + api_url + " " + api_constant + " " + account + " " + pwd);
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();

                try {
                    con.onSuccess(body);

//                    if (code.equals("0x0200")) {
//                        con.onSuccess(code);
//                    } else {
//                        con.onSuccess(error);
//                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    con.onFailure(e.getMessage());
                }
            }
        });
    }

    /*
     * Member reset password
     * */
    public static void member_resetCode(String api_url, String api_constant, String account, String pwd, String code, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody.Builder().add("member_id", account).add("member_pwd", pwd).add("code", code).build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    Log.e(TAG, jsonObject.toString());
                    //{"status":"false","code":"0x0201","responseMessage":"ID or code is wrong!"}
                    String status = jsonObject.getString("status");
                    String code = jsonObject.getString("code");
                    String responseMessage = jsonObject.getString("responseMessage");
                    String error = "code :" + code + "responseMessage :" + responseMessage;

                    if (code.equals("0x0200")) {
                        con.onSuccess(code);
                    } else {
                        con.onSuccess(error);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    con.onFailure(e.getMessage());
                }
            }
        });
    }

    /*
     * 新的會員註冊
     * */
    public static void MemberRegi(String api_url, String api_constant, String account, String pwd, String name, String type, String email, String recommend, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody.Builder RegiBody = new FormBody.Builder();
        Log.d("登入", "fuckRecommend " + recommend + "\r\n"
                + "\r\n" + api_url + "\r\n"
                + "\r\n" + api_constant + "\r\n" + "\r\n" + account + "\r\n"
                + "\r\n" + recommend + "\r\n");
        RegiBody.add("member_id", account)
                .add("member_pwd", pwd)
                .add("name", name)
                .add("accountType", type)
                .add("recommend_code", recommend)
                .add("email", email);

        Request request = new Request.Builder().url(url).post(RegiBody.build()).build();
        if (client == null) client = new OkHttpClient();
        handler = new Handler();
        client.newCall(request);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                con.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = null;
                ResponseBody responseBody = response.body();
                if (responseBody != null)
                    body = responseBody.string();
                Log.d("登入", "Recommend " + body);
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    String status = jsonObject.getString("status");
                    String code = jsonObject.getString("code");
                    String responseMessage = jsonObject.getString("responseMessage");
                    String error = "code :" + code + "responseMessage :" + responseMessage;
                    if (code.equals("0x0200")) {
                        con.onSuccess(code);
                    } else {
                        con.onSuccess(code);
                    }
                } catch (JSONException e) {
                    con.onFailure(e.getMessage());
                }
            }
        });

    }


    /*
     * Get user Code
     * */
    public static void member_usercode(String api_url, String api_constant, String account, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody.Builder()
                .add("member_id", account).build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
//                    if (responseBody != null)
//                        body = responseBody.string();
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    Log.e(TAG, jsonObject.toString());
                    //{"status":"false","code":"0x0201","responseMessage":"ID or code is wrong!"}
                    String status = jsonObject.getString("status");
                    String code = jsonObject.getString("code");
                    String responseMessage = jsonObject.getString("responseMessage");
                    String error = "code :" + code + " responseMessage :" + responseMessage;

                    if (code.equals("0x0200")) {
                        con.onSuccess(code);
                    } else {
                        con.onSuccess(error);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    con.onFailure(e.getMessage());
                }
            }
        });
    }

    /*
     * Get member information
     * */
    public static void member_info(String api_url, String api_constant, String account, String pw, String accountType, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody.Builder()
                .add("account", base64(account))
                .add("accountType", base64(accountType))
                .add("pw", base64(pw))
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();

                try {
                    Log.e(TAG, body);
                    con.onSuccess(body);


                } catch (JSONException e) {
                    e.printStackTrace();
                    con.onFailure(e.getMessage());
                }

            }
        });
    }

    /*
     * Reset Member pwd
     * */
    public static void member_resetpwd(String api_url, String api_constant, String account, String pwd, String code, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody.Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)
                .add("code", code).build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    Log.e(TAG, jsonObject.toString());
                    //{"status":"false","code":"0x0201","responseMessage":"ID or code is wrong!"}
                    String status = jsonObject.getString("status");
                    String code = jsonObject.getString("code");
                    String responseMessage = jsonObject.getString("responseMessage");
                    String error = "code :" + code + " responseMessage :" + responseMessage;

                    if (code.equals("0x0200")) {
                        con.onSuccess(code);
                    } else {
                        con.onSuccess(error);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    con.onFailure(e.getMessage());
                }
            }
        });
    }

    /*
     * Reset Member information
     * */
    public static void member_edit(String api_url, String api_constant, String account, String pwd, String name, String gender, String email, String birthday, String phone, final OnConnect con) {
        String url = api_url + api_constant;

        FormBody formBody = new FormBody.Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)
                .add("name", name)
                .add("sex", gender)
                .add("email", email)
                .add("birthday", birthday)
                .add("tel", phone)
                .add("address", "test")
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();
                Log.d("三小  ", "" + body);

                try {
                    JSONObject jsonObject = new JSONObject(body);
                    Log.e(TAG, jsonObject.toString());
                    //{"status":"false","code":"0x0201","responseMessage":"ID or code is wrong!"}
                    String status = jsonObject.getString("status");
                    String code = jsonObject.getString("code");
                    String responseMessage = jsonObject.getString("responseMessage");
                    String error = "code :" + code + " responseMessage :" + responseMessage;

                    if (code.equals("0x0200")) {
                        con.onSuccess(code);
                    } else {
                        con.onSuccess(error);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    con.onFailure(e.getMessage());
                }
            }
        });
    }

    /*
     * Get order list
     * */
    public static void order_list(String api_url, String api_constant, String account, String pwd, String startdate, String enddate, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody.Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)
//                .add("order_startdate", startdate)
//                .add("order_enddate", enddate)
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();
                try {
                    con.onSuccess(body);

                } catch (JSONException e) {
                    e.printStackTrace();
                    con.onFailure(e.getMessage());
                }
            }
        });
    }

    /*
     * Get bonus
     * */
    public static void bonus_list(String api_url, String api_constant, String account, String pwd, String startdate, String enddate, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody.Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)
//                .add("bonus_startdate", startdate)
//                .add("bonus_enddate", startdate)
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();
                try {
                    con.onSuccess(body);
                } catch (JSONException e) {
                    e.printStackTrace();
                    con.onFailure(e.getMessage());
                }
            }
        });
    }

    /*
     * Member login out
     *
     * */
    @NonNull
    public static void member_logout(String api_url, String api_constant, String account, String pwd, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody.Builder().add("member_id", account)
                .add("member_pwd", pwd).build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        handler = new Handler();
        client.newCall(request);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                con.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = null;
                ResponseBody responseBody = response.body();
                if (responseBody != null)
                    body = responseBody.string();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    String status = jsonObject.getString("status");
                    String code = jsonObject.getString("code");
                    String responseMessage = jsonObject.getString("responseMessage");
                    String error = "code :" + code + "responseMessage :" + responseMessage;
                    if (code.equals("0x0200")) {
                        con.onSuccess(code);
                    } else {
                        con.onSuccess(error);
                    }
                } catch (JSONException e) {
                    con.onFailure(e.getMessage());
                }
            }
        });

    }

    /*
     * Get payment url
     *
     * */
    @NonNull
    public static void payment_url(String api_url, String api_constant, String account, String pwd, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody.Builder().add("member_id", account)
                .add("member_pwd", pwd).build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        handler = new Handler();
        client.newCall(request);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                con.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = null;
                ResponseBody responseBody = response.body();
                if (responseBody != null)
                    body = responseBody.string();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    String status = jsonObject.getString("status");
                    String code = jsonObject.getString("code");
                    String responseMessage = jsonObject.getString("responseMessage");
                    String error = "code :" + code + "responseMessage :" + responseMessage;
                    if (code.equals("0x0200")) {
                        con.onSuccess(responseMessage);
                    } else {
                        con.onSuccess(body);
                    }
                } catch (JSONException e) {
                    con.onFailure(e.getMessage());
                }
            }
        });

    }

    public static void coupon_list(String api_url, String api_constant, String account, String pwd, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody formBody = null;
        formBody = new FormBody.Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)

                .build();

        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();
                try {
                    Log.d(TAG, "Kelly find coupon list : " + body);

                    con.onSuccess(body);
                } catch (JSONException e) {
                    e.printStackTrace();
                    con.onFailure(e.getMessage());
                }
            }
        });
    }

    /*
     * get recommand list
     * "recommend_list": [
    {
      "0": "0965026589",
      "1": "2021-12-03",
      "2": "12342234",
      "recommend_code": "0965026589",
      "recommend_date": "2021-12-03",
      "recommend_member": "12342234"
    }
  ]
     *
     * */
    public static void recommand_list(String api_url, String api_constant, String account, String pwd, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody.Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();
                try {
                    Log.d(TAG, "Kelly find recommand list : " + body);

                    con.onSuccess(body);
                } catch (JSONException e) {
                    e.printStackTrace();
                    con.onFailure(e.getMessage());
                }
            }
        });
    }

    // 4.商城商品類別
    public static void product_type(String api_url, String api_constant, String account, String pwd, final OnConnect con) {

        Log.w(TAG, "4.商城商品類別");

        String url = api_url + api_constant;
        Log.d(TAG, "URL: " + url);

        FormBody formBody = new FormBody.Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)
                .build();
        Log.d(TAG, "member_id: " + account);
        Log.d(TAG, "member_pwd: " + pwd);

        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse: =================");
                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null) {
                    body = responseBody.string();
                    Log.d(TAG, "body: " + body);
                }

                try {
                    con.onSuccess(body);
                } catch (JSONException e) {
                    e.printStackTrace();
                    con.onFailure(e.getMessage());
                }

            }
        });
    }

    /*
     * Get store_type
     * */
    public static void store_type(String api_url, String api_constant, String account, String pwd, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody.Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();

                try {
                    Log.e(TAG, body);
                    con.onSuccess(body);

                } catch (JSONException e) {
                    e.printStackTrace();
                    con.onFailure(e.getMessage());
                }

            }
        });
    }

    /*
     * Get store_list
     * */
    public static void store_list(String api_url, String api_constant, String account, String pwd, String
            type, final OnConnect con) {
        String url = api_url + api_constant;
        Log.d("store_list", "store_list : " + " member_id:  " + account + "\n" + "member_pwd: " + pwd + "\n"
                + "store_type: " + type);
        FormBody formBody = new FormBody.Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)
                .add("store_type", type)
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();

                try {
                    Log.e(TAG, "store_list: " + body);
                    con.onSuccess(body);

                } catch (JSONException e) {

                    con.onFailure(e.getMessage());
                }

            }
        });
    }


    // 5.商城商品列表
    public static void product_list(
            String api_url,
            String api_constant,
            String account,
            String pwd,
            final OnConnect con) {

        Log.w(TAG, "5.商城商品列表");

        String url = api_url + api_constant;
        Log.d(TAG, "URL: " + url);

        FormBody formBody = new FormBody.Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)
                .build();
        Log.d(TAG, "member_id: " + account);
        Log.d(TAG, "member_pwd: " + pwd);

        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse: ==============");

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null) {
                    body = responseBody.string();
                    Log.d(TAG, "body: " +body);
                }

                try {
                    con.onSuccess(body);
                } catch (JSONException e) {
                    con.onFailure(e.getMessage());
                }

            }
        });
    }

    /*
     * Get product_info
     * */
    public static void product_info(String api_url, String api_constant, String account, String
            pwd, String type, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody.Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)
                .add("product_no", type)
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();

                try {
                    Log.e(TAG, body);
                    con.onSuccess(body);

                } catch (JSONException e) {

                    con.onFailure(e.getMessage());
                }

            }
        });
    }

    /*
     * add_shoppingcart新增商品到購物車
     * */
    public static void add_shoppingcart(String api_url, String api_constant, String
            account, String pwd, String no, String spec, Integer price, Integer qty, Integer amount,
                                        final OnConnect con) {
        String url = api_url + api_constant;
        Log.d("add_shoppingcart", "是甚麼壓~ : " + " account " + account + "\n"
                + "pwd : " + pwd + "\r\n" + "商品編號: " + no + "\r\n" + "商品類別: " + spec + "\r\n" + "價格: " + price + "\r\n" + "數量: " + qty + "\r\n" + "總計 :" + amount + "\r\n");
        FormBody formBody = new FormBody.Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)
                .add("product_no", no)
                .add("product_spec", spec)
                .add("product_price", String.valueOf(price))
                .add("order_qty", String.valueOf(qty))
                .add("total_amount", String.valueOf(amount))
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();

                try {
                    Log.d(TAG, "新增商品到購物車 " + body + "\n\n");

                    con.onSuccess(body);

                } catch (JSONException e) {

                    con.onFailure(e.getMessage());
                }

            }
        });
    }

    /*
     * shoppingcart_list購物車內商品列表
     * */
    public static void shoppingcart_list(String api_url, String api_constant, String
            account, String pwd, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody.Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();

                try {
                    Log.e(TAG, body);
                    con.onSuccess(body);

                } catch (JSONException e) {

                    con.onFailure(e.getMessage());
                }

            }
        });
    }

    /*
     * edit_shoppingcart 修改購物車內商品數量
     * */
    public static void edit_shoppingcart(String api_url, String api_constant, String
            account, String pwd, String no, String qty, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody.Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)
                .add("product_no", no)
                .add("order_qty", qty)
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();

                try {
                    Log.e(TAG, body);
                    con.onSuccess(body);

                } catch (JSONException e) {

                    con.onFailure(e.getMessage());
                }

            }
        });
    }

    /*
     * del_shoppingcart 修改購物車內商品數量
     * */
    public static void del_shoppingcart(String api_url, String api_constant, String
            account, String pwd, String no, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody.Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)
                .add("product_no", no)
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();

                try {
                    Log.e(TAG, body);
                    con.onSuccess(body);

                } catch (JSONException e) {

                    con.onFailure(e.getMessage());
                }

            }
        });
    }

    /*
     * shoppingcart_count 購物車內商品總數
     * */
    public static void shoppingcart_count(String api_url, String api_constant, String
            account, String pwd, final OnConnect con) {
        String url = api_url + api_constant;
        Log.e("fuck3", "shoppingcart_count " + account + pwd);
        FormBody formBody = new FormBody.Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();

                try {
                    Log.e(TAG, body);
                    con.onSuccess(body);

                } catch (JSONException e) {

                    con.onFailure(e.getMessage());
                }

            }
        });
    }

    /*
     *add_ecorder 新增商城訂單
     * */
    public static void add_ecorder(String api_url
            , String api_constant
            , String account
            , String pwd
            , String order_amount
            , String discount_amount
            , String order_pay
            , final OnConnect con) {
        String url = api_url + api_constant;
        FormBody.Builder Builder = new FormBody.Builder();
//53758995
        Log.e("sid value ", "account: " + account
                + "\r\n" + "pwd: " + pwd
                + "\r\n" + "order amount :" + order_amount
                + "\r\n" + "diaamoumt " + discount_amount
                + "\r\n" + "order pay " + order_pay
        );
        Builder
                .add("member_id", account)
                .add("member_pwd", pwd)
                .add("order_amount", order_amount)
                .add("discount_amount", discount_amount)
                .add("order_pay", order_pay);


//        Log.d(" 3 ", uniform_no + " " + company_title);
        Request request = new Request.Builder().url(url).post(Builder.build()).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(" add eco ", "甚麼勒~" + e.getMessage());

                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();

                try {
                    Log.e(TAG, body);
                    con.onSuccess(body);

                } catch (JSONException e) {

                    con.onFailure(e.getMessage());
                }

            }
        });
    }

    /*
     * ecorder_list 購物車內商品總數
     * */
    public static void ecorder_list(String api_url, String api_constant, String account, String
            pwd, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody.Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();

                try {
                    Log.e(TAG, body);
                    con.onSuccess(body);

                } catch (JSONException e) {

                    con.onFailure(e.getMessage());
                }

            }
        });
    }

    /*
     * ecorder_info 購物車內商品總數
     * */
    public static void ecorder_info(String api_url, String api_constant, String account, String
            pwd, String no, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody.Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)
                .add("order_no", no)
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();

                try {
                    Log.e(TAG, body);
                    con.onSuccess(body);

                } catch (JSONException e) {

                    con.onFailure(e.getMessage());
                }

            }
        });
    }

    /*store_info*/
    public static void store_info(
            String api_url,
            String api_constant,
            String account,
            String pwd,
            String no,
            final OnConnect con) {

        Log.w(TAG, "store_info: ");

        String url = api_url + api_constant;
        Log.d(TAG, "Url: ");

        FormBody formBody = new FormBody.Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)
                .add("sid", no)
                .build();
        Log.d(TAG, "member_id: " + account);
        Log.d(TAG, "member_pwd: " + pwd);
        Log.d(TAG, "sid: " + no);

        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Log.d(TAG, "onResponse: ==================");

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null) {
                    body = responseBody.string();
                    Log.d(TAG, "body: " + body);
                }


                try {
                    Log.e(TAG, body);
                    con.onSuccess(body);

                } catch (JSONException e) {

                    con.onFailure(e.getMessage());
                }

            }
        });
    }

    /*clear shopping cart清空購物車*/
    public static void clear_shoppingcart(String api_url, String api_constant, String
            account, String pwd, final OnConnect con) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody.Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)

                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();

                try {
                    Log.e(TAG, body);
                    con.onSuccess(body);

                } catch (JSONException e) {

                    con.onFailure(e.getMessage());
                }

            }
        });
    }

    /*qr_unconfirm_list 會員未核銷商品/套票 */
    public static void qr_unconfirm_list(String api_url, String api_constant, String
            account, String pwd, String ispackage, final OnConnect con) {
        String url = api_url + api_constant;
        Log.e("sid value ", "account: " + account
                + "\r\n" + "pwd: " + pwd
                + "\r\n" + "order ispackage :" + ispackage
        );
        FormBody formBody = new FormBody.Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)
                .add("ispackage", ispackage)
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                con.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();

                try {
                    Log.e(TAG, body);
                    con.onSuccess(body);

                } catch (JSONException e) {

                    con.onFailure(e.getMessage());
                }

            }
        });
    }

    //註冊
    public static void signup(String api_url, String api_constant, String account, String accountType, @NonNull final OnConnectResultListener listener) {

        String url = api_url + api_constant;

        FormBody formBody = new FormBody.Builder()
                .add("account", base64(account))
                .add("accountType", base64(accountType))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        if (client == null) client = new OkHttpClient();
        if (handler == null) handler = new Handler();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                listener.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();

                try {
                    JSONObject jsonObject = new JSONObject(body);
                    Log.e(TAG, jsonObject.toString());

                    String state = jsonObject.getString("returnCode");
                    String[] successArr = {"101", "102", "103"};

                    if (Arrays.asList(successArr).contains(state)) {
                        listener.onSuccess(state);
                    } else {
                        listener.onFailure(jsonObject.getString("errorMsg"));

                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                    listener.onFailure(e.getMessage());
                }

            }
        });
    }

    //註冊驗證
    public static void codeverify(String api_url, String api_constant, String action, String account, String accountType, String registeCode,
                                  String pw, String pw2, @NonNull final OnConnectResultListener listener) {

        String url = api_url + api_constant;
        FormBody formBody = new FormBody
                .Builder()
                .add("action", base64(action))
                .add("account", base64(account))
                .add("accountType", base64(accountType))
                .add("registeCode", base64(registeCode))
                .add("pw", base64(pw))
                .add("pw2", base64(pw2))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        if (client == null)
            client = new OkHttpClient();

        if (handler == null)
            handler = new Handler();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                listener.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    Log.e(TAG, jsonObject.toString());

                    String state = jsonObject.getString("returnCode");
                    if (state.equals(ApiConUtils.RETURN_SUCCESS)) {
                        listener.onSuccess("驗證成功");
                    } else {
                        listener.onFailure(jsonObject.getString("errorMsg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(e.getMessage());
                }
            }
        });

    }

    //重送驗證碼
    public static void resendcode(String api_url, String api_constant, String account, String accountType, String action, @NonNull final OnConnectResultListener listener) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody
                .Builder()
                .add("account", base64(account))
                .add("accountType", base64(accountType))
                .add("action", base64(action))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        if (client == null)
            client = new OkHttpClient();

        if (handler == null)
            handler = new Handler();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                listener.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    Log.e(TAG, jsonObject.toString());

                    String state = jsonObject.getString("returnCode");
                    if (state.equals(ApiConUtils.RETURN_SUCCESS)) {
                        listener.onSuccess("重送成功");
                    } else {
                        listener.onFailure(jsonObject.getString("errorMsg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(e.getMessage());
                }
            }
        });

    }

    //註冊驗證通過 (初始個人資料)
    public static void initPersonalData(String api_url, String api_constant, String account, String pw, String accountType, String name,
                                        String tel, String birthday, String email, String sex, String city, String region, String address, String referrerPhone, @NonNull final OnConnectResultListener listener) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody
                .Builder()
                .add("account", base64(account))
                .add("pw", base64(pw))
                .add("accountType", base64(accountType))
                .add("name", base64(name))
                .add("tel", base64(tel))
                .add("birthday", base64(birthday))
                .add("email", base64(email))
                .add("sex", base64(sex))
                .add("city", base64(city))
                .add("region", base64(region))
                .add("address", base64(address))
                .add("referrerPhone", base64(referrerPhone))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        if (client == null)
            client = new OkHttpClient();

        if (handler == null)
            handler = new Handler();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                listener.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    Log.e(TAG, jsonObject.toString());

                    String state = jsonObject.getString("returnCode");
                    if (state.equals(ApiConUtils.RETURN_SUCCESS)) {
                        listener.onSuccess("資料更新成功，請重新登入使用。");
                    } else {
                        listener.onFailure(jsonObject.getString("errorMsg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(e.getMessage());
                }
            }
        });
    }

    //獲取個人資料
//    public static void getPersonalData(String api_url, String api_constant, String account, String pw, String accountType, @NonNull final OnConnectResultListener listener) {
//        String url = api_url + api_constant;
//        FormBody formBody = new FormBody
//                .Builder()
//                .add("account", base64(account))
//                .add("pw", base64(pw))
//                .add("accountType", base64(accountType))
//                .build();
//        Log.d(TAG, "account: " + base64(account));
//        Log.d(TAG, "pw: " + base64(pw));
//        Log.d(TAG, "accountType: " + base64(accountType));
//        Request request = new Request.Builder()
//                .url(url)
//                .post(formBody)
//                .build();
//        if (client == null)
//            client = new OkHttpClient();
//
//        if (handler == null)
//            handler = new Handler();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                if (e.getLocalizedMessage() != null) {
//                    Log.e(TAG, e.getLocalizedMessage());
//                }
//                listener.onFailure("與伺服器連線失敗。");
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//
//                String body = null;
//                ResponseBody responseBody = response.body();
//
//                if (responseBody != null)
//                    body = responseBody.string();
//                try {
//                    JSONObject jsonObject = new JSONObject(body);
//                    Log.e(TAG, jsonObject.toString());
//
//                    String state = jsonObject.getString("returnCode");
//                    if (state.equals(ApiConUtils.RETURN_SUCCESS)) {
//                        listener.onSuccess(jsonObject.getJSONObject("returnData").toString());
//                    } else {
//                        listener.onFailure(jsonObject.getString("errorMsg"));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    listener.onFailure(e.getMessage());
//                }
//            }
//        });
//
//    }

    //編輯個人資料
    public static void modifyPersonalData(String api_url,
                                          String api_constant,
                                          String account,
                                          String accountType,
                                          String pw,
                                          String tel,
                                          String name,
                                          String sex,
                                          String city,
                                          String region,
                                          String birthday,
                                          String email,
                                          String address,
                                          @NonNull final OnConnectResultListener listener) {
        String url = api_url + api_constant;
        FormBody formBody = new FormBody
                .Builder()
                .add("account", base64(account))
                .add("accountType", base64(accountType))
                .add("pw", base64(pw))
                .add("tel", base64(tel))
                .add("name", base64(name))
                .add("sex", base64(sex))
                .add("city",base64(city))
                .add("region",base64(region))
                .add("birthday", base64(birthday))
                .add("email", base64(email))
                .add("address", base64(address))
                .build();
        Log.d(TAG, "formBody: " + formBody);

        Log.d(TAG, "account: " + base64(account));
        Log.d(TAG, "pw: " + base64(pw));
        Log.d(TAG, "accountType: " + base64(accountType));
        Log.d(TAG, "name: " + base64(name));
        Log.d(TAG, "tel: " + base64(tel));
        Log.d(TAG, "birthday: " + base64(birthday));
        Log.d(TAG, "email: " + base64(email));
        Log.d(TAG, "sex: " + base64(sex));
        Log.d(TAG, "city: " + base64(city));
        Log.d(TAG, "region: " + base64(region));
        Log.d(TAG, "address: " + base64(address));


        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        if (client == null)
            client = new OkHttpClient();

        if (handler == null)
            handler = new Handler();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                listener.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    Log.e(TAG, jsonObject.toString());

                    String state = jsonObject.getString("returnCode");
                    if (state.equals(ApiConUtils.RETURN_SUCCESS)) {
                        listener.onSuccess("資料更新成功，請重新登入使用。");
                    } else {
                        listener.onFailure(jsonObject.getString("errorMsg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(e.getMessage());
                }
            }
        });
    }

    //上傳圖片
//    public static void uploadFile(String api_url, String api_constant, String pw, String imgtitle, File cmdImageFile, @NonNull final OnConnectResultListener listener)
//    {
//
//        String url = api_url + api_constant;
//
////        ByteArrayOutputStream baos = new ByteArrayOutputStream();
////        cmdImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
////        byte[] imageBytes = baos.toByteArray();
////        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("cmdImageFile", cmdImageFile.getName(), RequestBody.create(cmdImageFile, MediaType.parse("image/jpg")))
////                .addFormDataPart("cmdImageFile",encodedImage)
//                .addFormDataPart("account", base64(Global.ACCOUNT))
//                .addFormDataPart("accountType", base64(Global.ACCOUNT_TYPE))
//                .addFormDataPart("pw", base64(pw))
//                .addFormDataPart("imgtitle", base64(imgtitle))
//                .build();
//
//        Request request = new Request.Builder()
//                .url(url)
//                .post(requestBody)
//                .build();
//
//        if (client == null)
//            client = new OkHttpClient();
//
//        if (handler == null)
//            handler = new Handler();
//
//        client.newCall(request).enqueue(new Callback()
//        {
//            @Override
//            public void onFailure(Call call, IOException e)
//            {
//                if (e.getLocalizedMessage() != null)
//                {
//                    Log.e(TAG, e.getLocalizedMessage());
//                }
//                listener.onFailure("與伺服器連線失敗。");
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException
//            {
//
//                String body = null;
//                ResponseBody responseBody = response.body();
//
//                if (responseBody != null)
//                    body = responseBody.string();
//                try
//                {
//                    JSONObject jsonObject = new JSONObject(body);
//                    Log.e(TAG, jsonObject.toString());
//                    String state = jsonObject.getString("returnCode");
//                    if (state.equals(ApiConUtils.RETURN_SUCCESS))
//                    {
//                        listener.onSuccess(jsonObject.getString("personalDataUpdateed"));
//                    } else
//                    {
//                        listener.onFailure(jsonObject.getString("errorMsg"));
//                    }
//                } catch (JSONException e)
//                {
//                    e.printStackTrace();
//                    listener.onFailure(e.getMessage());
//                }
//                Log.d("測試連線", "AA");
//            }
//        });
//
//
//    }
}
