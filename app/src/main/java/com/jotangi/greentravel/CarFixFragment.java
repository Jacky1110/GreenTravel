package com.jotangi.greentravel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jotangi.greentravel.Api.ApiEnqueue;
import com.jotangi.greentravel.ui.hPayMall.MemberBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CarFixFragment extends ProjConstraintFragment {

    private String TAG = CarFixFragment.class.getSimpleName() + "(TAG)";

    private ApiEnqueue apiEnqueue;
    private RecyclerView recyView;
    private String motorNo;
    private Spinner spinner;
    private String cardNumber;

    private AlertDialog dialog = null;

    private CarFixAdapter adapter;

    ArrayList<CarFixModel> data = new ArrayList<>();
    ArrayList<CarFixModel> carFixModelArrayList = new ArrayList<>();

    public static CarFixFragment newInstance() {
        CarFixFragment fragment = new CarFixFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ConstraintLayout) inflater.inflate(R.layout.fragment_car_fix, container, false);
        initViews();
        storeIdlist();
        return rootView;
    }

    @Override
    protected void initViews() {
        super.initViews();

        apiEnqueue = new ApiEnqueue();
        recyView = rootView.findViewById(R.id.rec_fix);
        recyView.setHasFixedSize(true);
        recyView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyView.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));

    }

    private void storeIdlist() {

        String motorNo = "";

        apiEnqueue.fixMotorList(motorNo, new ApiEnqueue.resultListener() {
            @Override
            public void onSuccess(String message) {
                requireActivity().runOnUiThread(() -> {

                    try {
                        JSONArray jsonArray = new JSONArray(message);
                        String[] no = new String[jsonArray.length()];
                        JSONObject storeNameJB;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            storeNameJB = (JSONObject) jsonArray.get(i);
                            Log.d(TAG, "storeNameJB: " + storeNameJB);
                            no[i] = storeNameJB.getString("motor_no");
                            Log.d(TAG, "no[i]: " + no[i]);
                        }
                        upUiSpinner(no);
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

    private void upUiSpinner(String[] no) {
        requireActivity().runOnUiThread(() -> {
            spinner = rootView.findViewById(R.id.spinner);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                    requireActivity(),
                    R.layout.spinner_item_selected,
                    no
            );
            dataAdapter.setDropDownViewResource(
                    R.layout.spinner_item_selected);
            spinner.setAdapter(dataAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                    cardNumber = adapterView.getSelectedItem().toString();
                    Log.d(TAG, "cardNumber: " + cardNumber);
                    getFixRecord(cardNumber);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        });

    }

    private void getFixRecord(String cardNumber) {

        String NO = cardNumber;

        apiEnqueue.fixMotorList(NO, new ApiEnqueue.resultListener() {

            @Override
            public void onSuccess(String message) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        data = new ArrayList();

                        try {
                            JSONArray jsonArray = new JSONArray(message);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                CarFixModel model = new CarFixModel();
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                model.bid = jsonObject.getString("bid");
                                model.cancel = jsonObject.getString("cancel");
                                model.name = jsonObject.getString("name");
                                model.phone = jsonObject.getString("phone");
                                model.motorNo = jsonObject.getString("motor_no");
                                model.motorType = jsonObject.getString("motor_type");
                                model.fixType = jsonObject.getString("fixtype");
                                model.description = jsonObject.getString("description");
                                model.storeName = jsonObject.getString("store_name");
                                model.duration = jsonObject.getString("duration");
                                model.bookingDate = jsonObject.getString("bookingdate");
                                model.canCancel = jsonObject.getString("can_cancel");
                                data.add(model);

                                requireActivity().runOnUiThread(() -> {
                                    adapter = new CarFixAdapter();
                                    adapter.setData(data);
                                    recyView.setAdapter(adapter);
                                    adapter.setClickListener(clickListener);
                                    adapter.notifyDataSetChanged();
                                });
                                if (carFixModelArrayList == null) {
                                    carFixModelArrayList = new ArrayList();
                                } else {
                                    carFixModelArrayList.clear();
                                }

                                carFixModelArrayList.addAll(data);


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

    private CarFixAdapter.ItemClickListener clickListener = this::handleCancel;

    private void handleCancel(int position) {

        String bid = carFixModelArrayList.get(position).bid;

        apiEnqueue.fixmotorCancel(bid, new ApiEnqueue.resultListener() {
            @Override
            public void onSuccess(String message) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showDialog("", "取消預約成功!", (dialog1, which) ->
                        {
                            dialog.dismiss();
                            getFixRecord(cardNumber);
                        });
//                        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onFailure(String message) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showDialog(String title, String message, DialogInterface.OnClickListener listener) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = new AlertDialog.Builder(requireContext()).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "確認", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onClick(dialog, which);
            }
        });
        dialog.show();
    }


}