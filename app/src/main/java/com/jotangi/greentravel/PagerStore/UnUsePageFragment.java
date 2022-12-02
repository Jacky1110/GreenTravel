package com.jotangi.greentravel.PagerStore;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.jotangi.greentravel.Api.ApiEnqueue;
import com.jotangi.greentravel.ProjConstraintFragment;
import com.jotangi.greentravel.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UnUsePageFragment extends ProjConstraintFragment {

    private String TAG = UnUsePageFragment.class.getSimpleName() + "(TAG)";

    private SwipeRefreshLayout swipe_refresh;

    View view;
    ApiEnqueue apiEnqueue;
    RecyclerView recyView;
    ArrayList data = new ArrayList();
    UnPageAdapter adapter;
    int singleSize;


    public static UnUsePageFragment newInstance() {
        UnUsePageFragment fragment = new UnUsePageFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ConstraintLayout) inflater.inflate(R.layout.fragment_page_unuse, container, false);

        apiEnqueue = new ApiEnqueue();

        swipe_refresh = rootView.findViewById(R.id.swipe_refresh);
        swipe_refresh.setColorSchemeResources(R.color.cornflowerblue);//设置下拉进度条颜色
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //延迟2秒设置不刷新,模拟耗时操作，需要放在子线程中
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int type = 0;
                                if (getArguments() != null) {
                                    type = getArguments().getInt("type");
                                    Log.d("tag", "type" + type);
                                }

                                adapter = new UnPageAdapter();

                                if (type == 0) {
                                    adapter.setmData(data);  //call api

                                }
                                recyView.setAdapter(adapter);
                            }
                        });
                        swipe_refresh.setRefreshing(false);//设置是否刷新

                    }
                }, 2000);
            }
        });

        recyView = rootView.findViewById(R.id.recY);
        recyView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyView.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));

        UnCouponApi();
        return rootView;


    }

    private void UnCouponApi() {
        String ispackage = "0";
        apiEnqueue.QRConfirmList(ispackage, new ApiEnqueue.resultListener() {
            @Override
            public void onSuccess(String message) {
                // 給data新的空白list
                data = new ArrayList();

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
                        data.add(model);
                    }
                    singleSize = data.size();
                    Log.d(TAG, "data.size: " + singleSize);
                    CouponApiOne();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                try {
                    JSONArray jsonArray = new JSONArray(message);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        UnCouponModel model = new UnCouponModel();
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        model.name = jsonObject.getString("package_name");
                        model.day = jsonObject.getString("order_date");
                        model.id = jsonObject.getString("order_no");
                        model.pic = jsonObject.getString("package_picture");
                        data.add(model);
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int type = 0;
                            if (getArguments() != null) {
                                type = getArguments().getInt("type");
                                Log.d("tag", "type" + type);
                            }

                            adapter = new UnPageAdapter();

                            if (type == 0) {
                                adapter.setmData(data);  //call api

                            }
                            recyView.setAdapter(adapter);

//                            adapter.setClickListener(useClick);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();


                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }
}
