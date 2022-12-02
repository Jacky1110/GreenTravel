package com.jotangi.greentravel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jotangi.greentravel.Api.ApiEnqueue;
import com.jotangi.greentravel.PagerStore.UnUsePageFragment;

import org.json.JSONException;

import java.util.ArrayList;

public class NoticeFragment extends Fragment {

    private String TAG = NoticeFragment.class.getSimpleName() + "(TAG)";
    ApiEnqueue apiEnqueue;
    RecyclerView recyclerView;
    ArrayList data = new ArrayList();
    NotifyAdapter notifyAdapter;


    public static NoticeFragment newInstance() {
        NoticeFragment fragment = new NoticeFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notice, container, false);
        recyclerView = view.findViewById(R.id.notice_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));

        getNoticeApi();
        return view;
    }

    private void getNoticeApi() {

        data = new ArrayList();

//        try {
        for (int i = 0; i < 10; i++) {
            NotifyModel model = new NotifyModel();

            model.title = "哈哈哈哈";
            model.content = "1122331111121213131";
            model.date = "2022/10/25";
            data.add(model);

        }

        notifyAdapter = new NotifyAdapter();
        notifyAdapter.setmData(data);
        recyclerView.setAdapter(notifyAdapter);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

    }

}