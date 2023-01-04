package com.jotangi.greentravel.Api;

public class ApiUrl {
//    public static final String API_URL = "https://rilink.com.tw/"; //正式機
    public static final String API_URL = "https://rilink.jotangi.com.tw:11074/"; // 測試機
    public static final String payUrl = "http://211.20.181.125:11073/ticketec/ecpay/ecpayindex.php?orderid="; //測試
//    public static final String payUrl = "https://rilink.com.tw/ticketec/ecpay/ecpayindex.php?orderid="; //正式
//    public static final String API_URL2 = "https://rilink.com.tw/api/v1"; // 正式
    public static final String API_URL2 = "https://rilink.jotangi.com.tw:11074/api/v1"; // 測試
    public static final String MEMBER_IMG_URL = API_URL2 + "/personimages/";

    //會員密碼變更
    public static final String MALL_REWRITE_PWD = "https://ks-api.jotangi.net/api/auth/rewritepwd";


    //	使用者登入
    public static String user_login = "ticketec/api/user_login.php";
    //  會員資料修改
    public static String user_edit = "ticketec/api/user_edit.php";
    //	取得會員資料
    public static String user_getdata = "ticketec/api/user_getdata.php";
    // 4.商城商品類別
    public  static String product_type = "ticketec/api/product_type.php";
    // 5.商城商品列表
    public static String product_list = "ticketec/api/product_list.php";
    // 6.商城套票列表
    public static String package_list = "ticketec/api/package_list.php";
    //	商城商品資訊
    public static String product_info = "ticketec/api/product_info.php";
    //	商城套票資訊
    public static String package_info = "ticketec/api/package_info.php";
    //	新增商品到購物車
    public static String add_shoppingcart = "ticketec/api/add_shoppingcart.php";
    //	購物車內商品列表
    public static String shoppingcart_list = "ticketec/api/shoppingcart_list.php";
    //	修改購物車內商品數量
    public static String edit_shoppingcart = "ticketec/api/edit_shoppingcart.php";
    //	刪除購物車內某項商品
    public static String del_shoppingcart = "ticketec/api/del_shoppingcart.php";
    //	購物車內商品總數
    public static String shoppingcart_count = "ticketec/api/shoppingcart_count.php";
    //	清空購物車內的商品
    public static String clear_shoppingcart = "ticketec/api/clear_shoppingcart.php";
    //	新增商城訂單
    public static String add_ecorder = "ticketec/api/add_ecorder.php";
    // 16.商店類別
    public static String store_type = "ticketec/api/store_type.php";
    // 17.商店列表
    public static String store_list = "ticketec/api/store_list.php";
    //	會員訂單列表
    public static String ecorder_list = "ticketec/api/ecorder_list.php";
    //	會員訂單詳細資料
    public static String ecorder_info = "ticketec/api/ecorder_info.php";
    //	會員未核銷商品/套票
    public static String qr_unconfirm_list = "ticketec/api/qr_unconfirm_list.php";
    //	會員已核銷商品/套票
    public static String qr_confirm_list = "ticketec/api/qr_confirm_list.php";
    //	店家核銷商品/套票
    public static String toreapp_qrconfirm = "ticketec/api/toreapp_qrconfirm.php";
    //帳號註冊(傳送手機號) 一般用 fb認證勿使用
    public static final String signup = "/account/signup";
    //註冊驗證(一般用 fb認證勿使用)
    public static final String codeverify = "/account/codeverify";
    //重送驗證碼
    public static final String resendcode = "/account/resendcode";
    //註冊驗證通過 (初始個人資料)
    public static final String modifypersondata = "/account/modifypersondata";
    //獲取個人資料
    public static final String getpersondata = "/account/getpersondata";
    // 22.店家核銷商品/套票
    public static final String storeapp_qrconfirm = "ticketec/api/storeapp_qrconfirm.php";
    // 28.商店Id列表
    public static final String storeId_list = "ticketec/api/storeId_list.php";
    // 29.店長登入
    public static final String storeadmin_login = "ticketec/api/storeadmin_login.php";
    // 30.上傳會員照片
    public static final String uploadimage = "/account/uploadimage";
    // 31.	商店可預約服務列表
    public static final String fixmotor_info = "ticketec/api/fixmotor_info.php";
    // 32. 客戶開始預約
    public static final String booking_fixmotor = "ticketec/api/booking_fixmotor.php";
    // 33.	取得紅利點數歷史紀錄
    public static final String fetch_pointhistory = "ticketec/api/fetch_pointhistory.php";
    // 35.	查詢會員預約服務列表
    public static final String fixmotor_list = "ticketec/api/booking_fixmotor_list.php";
    // 36.	會員取消預約服務
    public static final String fixmotor_cancel = "ticketec/api/booking_fixmotor_cancel.php";
    // 37.	新增取得banner資訊
    public static final String banner_list = "ticketec/api/banner_list.php";
    // 39.	設定會員推播token
    public static final String member_set_token = "ticketec/api/member_set_token.php";
    // 40.	清除會員推播token
    public static final String member_clear_token = "ticketec/api/member_clear_token.php";
    // 41.	取得會員推播歷史資料
    public static final String pushmsg_get_history = "ticketec/api/pushmsg_get_history.php";




    public static String user_register;

    public static String get_payment_url;

    public static String store_info;


}
