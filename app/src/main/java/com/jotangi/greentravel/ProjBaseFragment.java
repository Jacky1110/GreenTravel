package com.jotangi.greentravel;

import android.content.res.Resources;

import com.jotangi.greentravel.ui.main.MainActivity;


public class ProjBaseFragment extends MyBaseFragment {
    public final static int MAIN_FUNC_HOME = 1;
    public final static int MAIN_FUNC_MALL = 2;
    public final static int MAIN_FUNC_ACCOUNT = 3;

    public final static int FUNC_MAIN_TO_HOME = 1;
    public final static int FUNC_MAIN_TO_MALL = 2;
    public final static int FUNC_MAIN_TO_BUSINESS = 3;
    public final static int FUNC_MAIN_TO_ACCOUNT= 4;
    public final static int FUNC_FRAGMENT_RESUME = 5;
    public final static int FUNC_FRAGMENT_change = 6;
    public final static int FUNC_MAIN_TO_ACCOUNT_TRADE = 7;

    public final static int FUNC_LOGIN_TO_FORGET_PASSWORD = 301;
    public final static int FUNC_LOGIN_TO_REGISTER = 302;
    public final static int FUNC_LOGIN_TO_RULE = 303;
    public final static int FUNC_LOGIN_TO_ACCOUNT_MAIN = 304;
    public final static int FUNC_REGISTER_TO_RULE = 311;
    public final static int FUNC_ACCOUNT_MAIN_TO_DATA = 321;
    public final static int FUNC_ACCOUNT_MAIN_TO_TRADE_MAIN = 322;
    public final static int FUNC_ACCOUNT_MAIN_TO_POINT = 323;
    public final static int FUNC_ACCOUNT_MAIN_TO_USERRULE = 324;
    public final static int FUNC_ACCOUNT_MAIN_TO_RECOMMEND = 325;
    public final static int FUNC_ACCOUNT_MAIN_TO_QA = 326;
    public final static int FUNC_ACCOUNT_MAIN_USER_HEAD_CLICKED = 327;
    public final static int FUNC_ACCOUNT_MAIN_TO_LOGIN = 328;
    public final static int FUNC_ACCOUNT_MAIN_TO_COUPON_INDEX = 329;
    public final static int FUNC_ACCOUNT_COUPON_TO_COUPON_MAIN = 330;
    public final static int FUNC_ACCOUNT_COUPON_TO_COUPON_DETAIL = 333;
    public final static int FUNC_ACCOUNT_RECOMMEND_TO_FRIENDS = 331;
    public final static int FUNC_ACCOUNT_RECOMMEND_TO_RULE = 332;
    public final static int FUNC_ACCOUNT_TRADE_TO_RECORD = 341;
    public final static int FUNC_ACCOUNT_TRADE_TO_ORDER = 342;
    public final static int FUNC_ACCOUNT_TRADE_TO_MALL = 342;
    public final static int FUNC_HOME_TO_SCAN = 101;
    public final static int FUNC_SCAN_TO_WEBPAY = 102;
    public final static int FUNC_MallDetal = 201;
    public final static int FUNC_MALL_PAY_TO_MEMBER = 202;
    public final static int FUNC_ACCOUNT_MAIN_TO_MYPAGE = 334;
    public final static int FUNC_ACCOUNT_MAIN_TO_NOUSEPAGE = 335;
    protected Integer activityTitleRid = R.string.title_home;
    protected Resources icon = null;
    protected boolean cart= false;
    /*商城切換*/
    public final static int fraProductDetail = 020;
    public final static int fraPayData = 021;
    public final static int fraShoppingCart = 022;
    public final static int fraShopTerms = 023;
    public final static int fraDymaticTab = 024;
    public final static int fraInfoToCustom = 025;
    public final static int fraMallRecord = 026;
    public final static int fraMallDetail= 027;
    public final static int fraPay =02701;

    /*商家切換*/
    public final static int fraStoreTab =31;
    public final static int fraStoreDetail = 032;

    //預約修車切換
    public final static int FUNC_HOTEL_FILL = 10;
    public final static int FUNC_FILL_TIME = 11;
    public final static int FUNC_APP_STORETAB = 12;

    //維修紀錄
    public final static int FUNC_MEMBER_TO_FIX = 50;

    public final static int FUNC_MEMBER_TO_CUSTOMER = 55;


    public final static int TAG_ZERO = 0;
    public final static int TAG_ONE = 1;


    @Override
    protected void initViews() {
        super.initViews();
        MainActivity activity = (MainActivity)getActivity();
        if (activity != null) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (fragmentListener != null) {
            fragmentListener.onAction(FUNC_FRAGMENT_RESUME, activityTitleRid);
        }

    }

}
