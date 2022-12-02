package com.jotangi.greentravel.ui.account;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jotangi.greentravel.Api.ApiEnqueue;
import com.jotangi.greentravel.ProjConstraintFragment;
import com.jotangi.greentravel.R;
import com.jotangi.greentravel.ui.hPayMall.MemberBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PointFragment extends ProjConstraintFragment {

    private String TAG = PointFragment.class.getSimpleName() + "(TAG)";

    private TextView txtPoint, txtScNd0;
    private ImageView ImgScNodata;
    ApiEnqueue apiEnqueue;
    View view;
    ArrayList<PointModel> data = new ArrayList<>();
    RecyclerView rv_point;
    PointAdapter adapter;

    public static PointFragment newInstance() {
        PointFragment fragment = new PointFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        activityTitleRid = R.string.account_listitem_mypoint;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_point, container, false);
        initView(view);
        handlePointHistory();
        return view;
    }


    private void initView(View v) {
        apiEnqueue = new ApiEnqueue();
        txtPoint = v.findViewById(R.id.tv_RPoint);
        txtScNd0 = v.findViewById(R.id.scNd0);
        ImgScNodata = v.findViewById(R.id.scNodata);
        rv_point = v.findViewById(R.id.rec_point);
        rv_point.setLayoutManager(new LinearLayoutManager(requireActivity()));
        rv_point.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));
    }

    private void handlePointHistory() {

        apiEnqueue.fetchPointhistory(new ApiEnqueue.resultListener() {
            @Override
            public void onSuccess(String message) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        data = new ArrayList();

                        try {
                            JSONArray jsonArray = new JSONArray(message);
                            Log.d(TAG, "jsonArray: " + jsonArray);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                PointModel model = new PointModel();
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                model.name = jsonObject.getString("msg");
                                model.time = jsonObject.getString("time");
                                model.point = jsonObject.getString("point");
                                data.add(model);
                            }

                            getActivity().runOnUiThread(() -> {
                                adapter = new PointAdapter();
                                adapter.setmData(data);
                                rv_point.setAdapter(adapter);
                                handlePoint();
                            });


                        } catch (JSONException e) {
                            e.printStackTrace();
                            getActivity().runOnUiThread(() -> {
                                rv_point.setVisibility(View.GONE);
                                txtScNd0.setVisibility(View.VISIBLE);
                                ImgScNodata.setVisibility(View.VISIBLE);
                            });
                        }
                    }
                });
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }


    private void handlePoint() {
        apiEnqueue.userGetdata(new ApiEnqueue.resultListener() {
            @Override
            public void onSuccess(String message) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            JSONObject jsonObject = new JSONObject(message);
                            Log.d(TAG, "jsonObject: " + jsonObject);
                            MemberBean.point = jsonObject.getString("point");
                            Log.d(TAG, "MemberBean.point: " + MemberBean.point);

                            txtPoint.setText(MemberBean.point);

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
}
