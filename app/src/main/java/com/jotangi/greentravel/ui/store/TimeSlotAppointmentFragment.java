package com.jotangi.greentravel.ui.store;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.widget.CalendarView;

import com.jotangi.greentravel.Api.ApiEnqueue;
import com.jotangi.greentravel.BannerListBean;
import com.jotangi.greentravel.DataBeen;
import com.jotangi.greentravel.ui.hPayMall.MemberBean;
import com.jotangi.greentravel.ProjConstraintFragment;
import com.jotangi.greentravel.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TimeSlotAppointmentFragment extends ProjConstraintFragment {

    private String TAG = TimeSlotAppointmentFragment.class.getSimpleName() + "(TAG)";


    CalendarView calendarView;
    ApiEnqueue apiEnqueue;
    ArrayList<ChooseModel> data = new ArrayList<>();
    ArrayList<ChooseModel>chooseModelArrayList = new ArrayList<>();
    RecyclerView recyclerView;
    ChooseModel model;
    ChooseAdapter adapter;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String time;
    private LinearLayoutManager mManager;

    private BannerListBean bannerData;


    public static TimeSlotAppointmentFragment newInstance() {
        TimeSlotAppointmentFragment fragment = new TimeSlotAppointmentFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ConstraintLayout) inflater.inflate(R.layout.fragment_time_slot_appointment, container, false);
        return rootView;
    }


    @Override
    protected void initViews() {
        super.initViews();

        apiEnqueue = new ApiEnqueue();

        recyclerView = rootView.findViewById(R.id.rec_time);
        adapter = new ChooseAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setItemClickListener(clickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));

        calendarView = rootView.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.YEAR, 0);
                calendar.add(Calendar.MONTH, 0);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                time = simpleDateFormat.format(calendar.getTime());
                Log.d(TAG, "time: " + time);

                updateTime(time);

            }
        });
        handleDay();
    }


    @Override
    public void onStart() {
        super.onStart();
        activityTitleRid = R.string.time;
    }

    @Override
    public void onResume() {
        super.onResume();
        handleDay();
    }

    private void handleDay() {

        String storeId = MemberBean.store_id;

        apiEnqueue.fixmotorInfo(storeId, new ApiEnqueue.resultListener() {
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
                                model = new ChooseModel();
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                model.bookingdate = jsonObject.getString("bookingdate");
                                Log.d(TAG, "model.bookingdate: " + model.bookingdate);
                                model.time = jsonObject.getString("duration");
                                Log.d(TAG, "model.time: " + model.time);
                                model.quota = jsonObject.getString("quota");
                                model.used = jsonObject.getString("used");
                                data.add(model);
                            }


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

    private void updateTime(String time) {

        if (chooseModelArrayList == null){
            chooseModelArrayList = new ArrayList();
        } else {
            chooseModelArrayList.clear();
        }

        for (ChooseModel item : data) {
            if (item.bookingdate.equals(time)) {
                chooseModelArrayList.add(item);
            }
            Log.d(TAG, "DataBeen.duration: " + DataBeen.duration);
        }

        adapter.setmData(chooseModelArrayList);
    }

    private ChooseAdapter.ItemClickListener clickListener = position -> {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_activity_main,
                AppointmentConfirmationFragment.newInstance(
                        chooseModelArrayList.get(position).time,
                        chooseModelArrayList.get(position).bookingdate));
        transaction.addToBackStack(null);
        transaction.commit();
    };
}