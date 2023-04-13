package com.jotangi.greentravel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jotangi.greentravel.Api.ApiEnqueue;
import com.jotangi.greentravel.ui.storeManager.DateUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class NoticeFragment extends ProjConstraintFragment {

    private String TAG = NoticeFragment.class.getSimpleName() + "(TAG)";
    ApiEnqueue apiEnqueue;
    RecyclerView recyclerView;
    NotifyAdapter notifyAdapter;

    private static ArrayList<NotifyModel> notifyList = new ArrayList<>();


    public static NoticeFragment newInstance() {
        NoticeFragment fragment = new NoticeFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notice, container, false);

        initView(view);
        getNoticeApi();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        activityTitleRid = R.string.main_Push;
    }

    private void initView(View view) {

        apiEnqueue = new ApiEnqueue();

        recyclerView = view.findViewById(R.id.notice_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));
    }

    private void getNoticeApi() {

        apiEnqueue.pushmsgGetHistory(new ApiEnqueue.resultListener() {
            @Override
            public void onSuccess(String message) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyList = new ArrayList<>();
                        try {
                            JSONArray jsonArray = new JSONArray(message);
                            for (int i = 0; i < jsonArray.length(); i++) {

                                NotifyModel notifyModel = new NotifyModel();

                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                                notifyModel.title = jsonObject.getString("title");
                                notifyModel.content = jsonObject.getString("message");
                                notifyModel.date = jsonObject.getString("push_datetime");
                                notifyList.add(notifyModel);

                            }


                            Collections.sort(notifyList, new Comparator<NotifyModel>() {
                                @Override
                                public int compare(NotifyModel o1, NotifyModel o2) {
                                    Date date1 = DateUtil.stringToDate(o1.date);
                                    Date date2 = DateUtil.stringToDate(o2.date);
                                    if (date1.before(date2)) {
                                        return 1;
                                    } else {
                                        return -1;
                                    }
                                }
                            });

                            notifyAdapter = new NotifyAdapter();
                            notifyAdapter.setmData(notifyList);
                            recyclerView.setAdapter(notifyAdapter);
//                            notifyAdapter.setClickListener(useClick);

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