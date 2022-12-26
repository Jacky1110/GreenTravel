package com.jotangi.greentravel.PagerStore;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.jotangi.greentravel.Api.ApiEnqueue;
import com.jotangi.greentravel.CouponActivity;
import com.jotangi.greentravel.MainCouponFragment;
import com.jotangi.greentravel.ProjConstraintFragment;
import com.jotangi.greentravel.R;
import com.jotangi.greentravel.ui.account.MemberFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class CouponListFragment extends ProjConstraintFragment {

    private String TAG = CouponListFragment.class.getSimpleName() + "(TAG)";

    private ApiEnqueue apiEnqueue;
    private RecyclerView recyclerView;
    private TabLayout tabLayout;
    private SwipeRefreshLayout swipe_refresh;
    private PagerAdapter pagerAdapter;
    private UnPageAdapter unpageAdapter;
    private int singleSize;
    private String product, qrconfirm;

    // 在可使用列表的首頁
    boolean isMainView;
    private Intent intent;

    ArrayList<JSONObject> multipleJson;

    private String status = "1"; //tab標籤蘭

    static ArrayList<CouponModel> couponList = new ArrayList<>(); //可以使用
    static ArrayList<UnCouponModel> couponList2 = new ArrayList<>(); //已過期或已使用

    public static CouponListFragment newInstance() {
        CouponListFragment fragment = new CouponListFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ConstraintLayout) inflater.inflate(R.layout.fragment_coupon_list, container, false);
        return rootView;
    }


    @Override
    protected void initViews() {
        super.initViews();

        apiEnqueue = new ApiEnqueue();

        swipe_refresh = rootView.findViewById(R.id.swipe_refresh);
        swipe_refresh.setColorSchemeColors(getResources().getColor(R.color.cornflowerblue));//设置下拉进度条颜色

        recyclerView = rootView.findViewById(R.id.rv_coupon);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        tabLayout = rootView.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("可使用"));
        tabLayout.addTab(tabLayout.newTab().setText("已使用"));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (Objects.requireNonNull(tab.getText()).toString()) {
                    case "可使用":
                        loadCouponData();
                        status = "1";
                        break;
                    case "已使用":
                        loadCouponData2();
                        status = "2";
                        break;
                    default:
                        recyclerView.setAdapter(null);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        loadCouponData();
    }

    private void loadCouponData() {

        String ispackage = "0";

        apiEnqueue.QRUnconfirmList(ispackage, new ApiEnqueue.resultListener() {
            @Override
            public void onSuccess(String message) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        couponList = new ArrayList<>();

                        try {
                            JSONArray couArray = new JSONArray(message);
                            for (int i = 0; i < couArray.length(); i++) {
                                CouponModel model = new CouponModel();
                                JSONObject jsonObject = (JSONObject) couArray.get(i);
                                Log.d("TAG", "jsonObject(0): " + jsonObject);
                                model.qrconfirm = jsonObject.getString("qrconfirm");
                                model.product_name = jsonObject.getString("product_name");
                                model.order_date = jsonObject.getString("order_date");
                                model.order_no = jsonObject.getString("order_no");
                                model.product_picture = jsonObject.getString("product_picture");
                                model.order_qty = jsonObject.getString("order_qty");
                                couponList.add(model);
                            }
                            singleSize = couponList.size();
                            Log.d(TAG, "data.size: " + singleSize);
                            UnCouponApiOne();

                        } catch (JSONException exception) {
                            exception.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(String message) {

            }
        });

    }

    private void UnCouponApiOne() {

        String ispackage = "1";

        apiEnqueue.QRUnconfirmList(ispackage, new ApiEnqueue.resultListener() {
            @Override
            public void onSuccess(String message) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        multipleJson = new ArrayList<>();

                        try {
                            JSONArray couArray = new JSONArray(message);

                            for (int i = 0; i < couArray.length(); i++) {

                                CouponModel model = new CouponModel();
                                // 內層
                                JSONObject jsonObject = (JSONObject) couArray.get(i);
                                multipleJson.add(jsonObject);
                                Log.d("TAG", "jsonObject(1): " + jsonObject);

                                model.product_name = jsonObject.getString("package_name");
                                model.order_date = jsonObject.getString("order_date");
                                model.order_no = jsonObject.getString("order_no");
                                model.product_picture = jsonObject.getString("package_picture");
                                model.order_qty = jsonObject.getString("order_qty");
                                model.product = jsonObject.getString("product");
                                couponList.add(model);
                                Log.d(TAG, "model.product: " + model.product);
                            }

                            Log.d(TAG, "data.size: " + couponList.size());

                            requireActivity().runOnUiThread(() -> {
                                pagerAdapter = new PagerAdapter();
                                pagerAdapter.setData(couponList);
                                recyclerView.setAdapter(pagerAdapter);
                                pagerAdapter.setClickListener(useClick);
                            });

                            swipe_refresh.setOnRefreshListener(() -> {
                                //延迟2秒设置不刷新,模拟耗时操作，需要放在子线程中
                                new Handler().postDelayed(() -> {
                                    requireActivity().runOnUiThread(() -> {

                                        pagerAdapter.setData(couponList);
                                        recyclerView.setAdapter(pagerAdapter);
                                        swipe_refresh.setRefreshing(false);//设置是否刷新
                                    });

                                }, 1000);
                            });

                        } catch (JSONException exception) {
                            exception.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    private void loadCouponData2() {
        String ispackage = "0";
        apiEnqueue.QRConfirmList(ispackage, new ApiEnqueue.resultListener() {
            @Override
            public void onSuccess(String message) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        couponList2 = new ArrayList<>();

                        try {
                            JSONArray jsonArray = new JSONArray(message);
                            Log.d(TAG, "jsonArray: " + jsonArray);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                UnCouponModel model = new UnCouponModel();
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                model.name = jsonObject.getString("product_name");
                                model.day = jsonObject.getString("order_date");
                                model.id = jsonObject.getString("order_no");
                                model.pic = jsonObject.getString("product_picture");
                                couponList2.add(model);
                            }
                            singleSize = couponList2.size();
                            Log.d(TAG, "data.size: " + singleSize);
                            CouponApiOne();


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

    private void CouponApiOne() {
        String ispackage = "1";

        apiEnqueue.QRConfirmList(ispackage, new ApiEnqueue.resultListener() {
            @Override
            public void onSuccess(String message) {
                requireActivity().runOnUiThread(() -> {
                    try {
                        JSONArray jsonArray = new JSONArray(message);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            UnCouponModel model = new UnCouponModel();
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            model.name = jsonObject.getString("package_name");
                            model.day = jsonObject.getString("order_date");
                            model.id = jsonObject.getString("order_no");
                            model.pic = jsonObject.getString("package_picture");
                            model.product = jsonObject.getString("product");
                            couponList2.add(model);

                            Log.d(TAG, "model.product: " + model.product);

                        }



//                        if (qrconfirm.equals(null)) {
//                            runOnUiThread(() -> {
//                                unpageAdapter = new UnPageAdapter();
//                                unpageAdapter.setmData(couponList2);
//                                recyclerView.setAdapter(unpageAdapter);
//                            });
//                        }
//                    }

                        requireActivity().runOnUiThread(() -> {
                            unpageAdapter = new UnPageAdapter();
                            unpageAdapter.setmData(couponList2);
                            recyclerView.setAdapter(unpageAdapter);
                        });

                        swipe_refresh.setOnRefreshListener(() -> {
                            //延迟2秒设置不刷新,模拟耗时操作，需要放在子线程中
                            new Handler().postDelayed(() -> {
                                requireActivity().runOnUiThread(() -> {
                                    unpageAdapter.setmData(couponList2);
                                    swipe_refresh.setRefreshing(false);//设置是否刷新
                                });

                            }, 1000);
                        });


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

    private PagerAdapter.ItemClickListener useClick = new PagerAdapter.ItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Log.d(TAG, "position: " + position);

            // 在首頁，且 單一商品的 position
            if (couponList.get(position).product.isEmpty()) {

                intent = new Intent();
                intent.putExtra("qrconfirm", couponList.get(position).qrconfirm);
                intent.putExtra("product_name", couponList.get(position).product_name);
                intent.putExtra("order_date", couponList.get(position).order_date);
                intent.putExtra("order_no", couponList.get(position).order_no);
                intent.setClass(requireContext(), CouponActivity.class);
                startActivity(intent);

            } else {
                intent = new Intent();
                intent.putExtra("order_no", couponList.get(position).order_no);
                intent.putExtra("order_date", couponList.get(position).order_date);
                intent.putExtra("product", couponList.get(position).product);
                intent.setClass(requireContext(), NewPageMainActivity.class);
                startActivity(intent);
            }
        }
    };
}