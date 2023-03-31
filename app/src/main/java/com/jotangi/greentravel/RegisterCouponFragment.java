package com.jotangi.greentravel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.jotangi.greentravel.Api.ApiEnqueue;
import com.jotangi.greentravel.PagerStore.CouponFragment;
import com.jotangi.greentravel.PagerStore.CouponListFragment;
import com.jotangi.greentravel.PagerStore.UsePageFragment;

import org.jetbrains.annotations.NotNull;


public class RegisterCouponFragment extends ProjConstraintFragment implements View.OnClickListener {

    private String TAG = RegisterCouponFragment.class.getSimpleName() + "(TAG)";

    private ApiEnqueue apiEnqueue;
    private TextView couponName, couponData, couponContent;
    private Button btnUse, btnBack;
    private ImageView couponPicture;
    private AlertDialog dialog = null;

    private static final String PRODUCTNAME = "PRODUCTNAME";
    private static final String ORDERDATE = "ORDERDATE";
    private static final String PRODUCTPICTURE = "PRODUCTPICTURE";
    private static final String COUPONDESCRIPTION = "COUPONDESCRIPTION";
    private static final String COUPONID = "COUPONID";


    // value
    private String productName;
    private String orderDate;
    private String productPicture;
    private String couponDescription;
    private String couponId;

    public static RegisterCouponFragment newInstance(
            String productName,
            String orderDate,
            String productPicture,
            String couponDescription,
            String couponId) {

        RegisterCouponFragment fragment = new RegisterCouponFragment();
        Bundle args = new Bundle();
        args.putString(PRODUCTNAME, productName);
        args.putString(ORDERDATE, orderDate);
        args.putString(PRODUCTPICTURE, productPicture);
        args.putString(COUPONDESCRIPTION, couponDescription);
        args.putString(COUPONID, couponId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register_coupon, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        handleDate();
        initHandler();
    }


    private void initView(View view) {
        apiEnqueue = new ApiEnqueue();
        couponName = view.findViewById(R.id.tv_couponName);
        couponData = view.findViewById(R.id.tv_couponData);
        couponContent = view.findViewById(R.id.tv_couponContent);
        btnUse = view.findViewById(R.id.btn_use);
        btnUse.setOnClickListener(this);
        btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);
        couponPicture = view.findViewById(R.id.iv_picture);

    }

    private void handleDate() {
        if (getArguments() != null) {
            productName = getArguments().getString(PRODUCTNAME);
            orderDate = getArguments().getString(ORDERDATE);
            productPicture = getArguments().getString(PRODUCTPICTURE);
            couponDescription = getArguments().getString(COUPONDESCRIPTION);
            couponId = getArguments().getString(COUPONID);
        }
    }

    private void initHandler() {
        couponName.setText(productName);
        couponData.setText("有效期限: " + orderDate);
        couponContent.setText(couponDescription);
        Glide.with(requireContext())
                .load(productPicture).into(couponPicture);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_use:
                showDialog("提醒", "執行操作後即無法取消。請出示畫\n面由店員點選「使用優惠劵」,或\n依店家指示自行操作。", (dialog1, which) -> {

                    dialog.dismiss();
                });
                break;
            case R.id.btn_back:
                CouponListFragment clFragment = new CouponListFragment();
                RegisterCouponFragment registerCouponFragment = new RegisterCouponFragment();
                FragmentTransaction rcTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
                rcTransaction.replace(R.id.nav_host_fragment_activity_main, clFragment);
                rcTransaction.remove(registerCouponFragment);
                rcTransaction.commit();
                break;

        }

    }

    private void getCouponApi() {

        apiEnqueue.newMemberCouponConfirm(couponId, new ApiEnqueue.resultListener() {
            @Override
            public void onSuccess(String message) {

                requireActivity().runOnUiThread(() -> Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show());
                CouponListFragment clFragment = new CouponListFragment();
                RegisterCouponFragment registerCouponFragment = new RegisterCouponFragment();
                FragmentTransaction rcTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
                rcTransaction.replace(R.id.nav_host_fragment_activity_main, clFragment);
                rcTransaction.remove(registerCouponFragment);
                rcTransaction.commit();

            }

            @Override
            public void onFailure(String message) {

                requireActivity().runOnUiThread(() -> Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show());
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
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "確認", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getCouponApi();
            }
        });
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onClick(dialog, which);
            }
        });
        dialog.show();
    }
}