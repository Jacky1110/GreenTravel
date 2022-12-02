package com.jotangi.greentravel.PagerStore;


import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.jotangi.greentravel.Api.ApiUrl;
import com.jotangi.greentravel.ui.hPayMall.MemberBean;
import com.jotangi.greentravel.R;
import com.jotangi.jotangi2022.ApiConUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UsePageFragment extends Fragment {

    private String TAG = UsePageFragment.class.getSimpleName() + "(TAG)";

    private SwipeRefreshLayout swipe_refresh;

    View view;
    ArrayList<CouponModel> data = new ArrayList<>();
    RecyclerView rv_coupon;
    PagerAdapter adapter;
    int singleSize;
    ArrayList<JSONObject> multipleJson;
    // 在可使用列表的首頁
    boolean isMainView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_page_use, container, false);
        isMainView = true;

        swipe_refresh=view.findViewById(R.id.swipe_refresh);
        swipe_refresh.setColorSchemeResources(R.color.cornflowerblue);//设置下拉进度条颜色
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //延迟2秒设置不刷新,模拟耗时操作，需要放在子线程中
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(() -> {
                            adapter = new PagerAdapter();
                            adapter.setData(data);
                            rv_coupon.setAdapter(adapter);
                            adapter.setClickListener(useClick);
                        });
                        swipe_refresh.setRefreshing(false);//设置是否刷新

                    }
                },2000);
            }
        });
        rv_coupon = view.findViewById(R.id.rv_coupon);
        rv_coupon.setLayoutManager(new LinearLayoutManager(requireActivity()));
        rv_coupon.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));
        CouponApi();
        return view;
    }

    private void CouponApi() {
        String id = MemberBean.member_id;
        String pwd = MemberBean.member_pwd;
        String ispackage = "0";
        ApiConUtils.qr_unconfirm_list(ApiUrl.API_URL, ApiUrl.qr_unconfirm_list, id, pwd, ispackage, new ApiConUtils.OnConnect() {
            @Override
            public void onSuccess(String jsonString) throws JSONException {

                data = new ArrayList();

                try {
                    JSONArray couArray = new JSONArray(jsonString);
                    for (int i = 0; i < couArray.length(); i++) {
                        CouponModel model = new CouponModel();
                        JSONObject jsonObject = (JSONObject) couArray.get(i);
                        Log.d("TAG", "jsonObject(0): " + jsonObject);

                        model.qrconfirm = jsonObject.getString("qrconfirm");
                        model.product_name = jsonObject.getString("product_name");
                        model.order_date = jsonObject.getString("order_date");
                        model.order_no = jsonObject.getString("order_no");

                        model.product_picture = jsonObject.getString("product_picture");
                        data.add(model);
                    }
                    singleSize = data.size();
                    Log.d(TAG, "data.size: " + singleSize);
                    CouponApiOne();

                } catch (JSONException exception) {
                    exception.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });

            }
        });
    }

    private void CouponApiOne() {
        String id = MemberBean.member_id;
        String pwd = MemberBean.member_pwd;
        String ispackage = "1";

        // 20.會員未核銷商品/套票
        ApiConUtils.qr_unconfirm_list(ApiUrl.API_URL, ApiUrl.qr_unconfirm_list, id, pwd, ispackage, new ApiConUtils.OnConnect() {
            @Override
            public void onSuccess(String jsonString) throws JSONException {

                multipleJson = new ArrayList<>();

                try {
                    JSONArray couArray = new JSONArray(jsonString);

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
                        data.add(model);
                    }

                    Log.d(TAG, "data.size: " + data.size());

                    getActivity().runOnUiThread(() -> {
                        adapter = new PagerAdapter();
                        adapter.setData(data);
                        rv_coupon.setAdapter(adapter);
                        adapter.setClickListener(useClick);
                    });

                } catch (JSONException exception) {
                    exception.printStackTrace();
                }
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
            if (isMainView && position < singleSize) {

                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_activity_main,
                        CouponFragment.newInstance(
                                data.get(position).qrconfirm,
                                data.get(position).product_name,
                                data.get(position).order_date,
                                data.get(position).order_no));
                transaction.addToBackStack(null);
                transaction.commit();
            }else
                // 在首頁，且 position位置為 套票
                if (isMainView && position >= singleSize) {

                    // 外層
                    JSONObject object = multipleJson.get(position - singleSize);

                    try {
                        JSONArray array = new JSONArray(object.getString("product"));

                        data = new ArrayList();
                        for (int i = 0 ; i < array.length() ; i++) {
                            // 內層
                            JSONObject jsonObject = (JSONObject) array.get(i);
                            CouponModel model = new CouponModel();

                            try {
                                // 這可能會沒抓到值
                                model.qrconfirm = jsonObject.getString("qrconfirm");
                            }catch (JSONException e) {
                                e.printStackTrace();
                                continue;
                            }
                            model.product_name = jsonObject.getString("product_name");
                            model.order_date = object.getString("order_date");
                            model.order_no = object.getString("order_no");
                            model.store_name = jsonObject.getString("store_name");

                            model.product_picture = jsonObject.getString("product_picture");
                            data.add(model);
                        }
                        isMainView = false;
                        adapter.setData(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else
                    // 不在首頁
                    if (!isMainView) {
                        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.nav_host_fragment_activity_main,
                                CouponFragment.newInstance(
                                        data.get(position).qrconfirm,
                                        data.get(position).product_name,
                                        data.get(position).order_date,
                                        data.get(position).order_no));
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
        }
    };
}