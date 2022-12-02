package com.jotangi.greentravel.ui.store;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jotangi.greentravel.Api.ApiEnqueue;
import com.jotangi.greentravel.DataBeen;
import com.jotangi.greentravel.ProjConstraintFragment;
import com.jotangi.greentravel.R;

public class AppointmentConfirmationFragment extends ProjConstraintFragment {

    private String TAG = getClass().getSimpleName() + "(TAG)";

    // value
    private String duration;
    private String bookingdate;

    private AlertDialog dialog = null;

    private ApiEnqueue apiEnqueue;
    private TextView txtServicePoint, txtName, txtPhone, txtCar, txtCarSize, txtType, txtVehicle, txtTime;
    private Button btnClear;


    public static AppointmentConfirmationFragment newInstance(
            String duration,
            String bookingdate) {

        AppointmentConfirmationFragment fragment = new AppointmentConfirmationFragment();
        Bundle args = new Bundle();
        args.putString("duration", duration);
        args.putString("bookingdate", bookingdate);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            duration = getArguments().getString("duration");
            bookingdate = getArguments().getString("bookingdate");

            Log.d(TAG, "duration: " + duration);
            Log.d(TAG, "bookingdate: " + bookingdate);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ConstraintLayout) inflater.inflate(R.layout.fragment_appointment_confirmation, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        activityTitleRid = R.string.appointment_confirmation;
    }

    @Override
    protected void initViews() {
        super.initViews();
        apiEnqueue = new ApiEnqueue();
        txtServicePoint = rootView.findViewById(R.id.tv_servicePoint);
        txtServicePoint.setText("預約服務地點: " + DataBeen.storeName);
        txtName = rootView.findViewById(R.id.tvname);
        txtName.setText("姓名: " + DataBeen.name);
        txtPhone = rootView.findViewById(R.id.tvphone);
        txtPhone.setText("電話號碼: " + DataBeen.phone);
        txtCar = rootView.findViewById(R.id.tvcar);
        txtCar.setText("車牌: " + DataBeen.licensePlate);
        txtCarSize = rootView.findViewById(R.id.tvcarsize);
        txtCarSize.setText("車型: " + DataBeen.carModel);
        txtType = rootView.findViewById(R.id.tvtype);
        txtType.setText("修車類型: " + DataBeen.fixCar);
        txtVehicle = rootView.findViewById(R.id.tvvehicle);
        txtVehicle.setText("車況: " + DataBeen.condition);
        txtTime = rootView.findViewById(R.id.tvtime);
        txtTime.setText("預約時間: " + bookingdate + duration);
        btnClear = rootView.findViewById(R.id.bt_clear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAllData();
            }
        });

    }

    private void handleAllData() {

        apiEnqueue.bookingFixmotor(bookingdate, duration,new ApiEnqueue.resultListener() {
            @Override
            public void onSuccess(String message) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        showDialog("", "預約成功", (dialog1, which) ->
                        {
                            fragmentListener.onAction(FUNC_APP_STORETAB, null);
                            dialog.dismiss();
                        });
                    }
                });
            }


            @Override
            public void onFailure(String message) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        showDialog("時段已額滿", "請重新預約", (dialog1, which) ->
                        {
                            fragmentListener.onAction(FUNC_APP_STORETAB, null);
                            dialog.dismiss();
                        });
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