package com.jotangi.greentravel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jotangi.greentravel.Api.ApiEnqueue;
import com.jotangi.greentravel.Api.ApiUrl;
import com.jotangi.greentravel.ui.hPayMall.MemberBean;
import com.squareup.picasso.Picasso;

public class PagerCommodityFragment extends ProjConstraintFragment {

    private String TAG = getClass().getSimpleName() + "(TAG)";

    private ApiEnqueue apiEnqueue;


    // value
    private String product_no;
    private String product_name;
    private String product_price;
    private String product_picture;
    private String product_description;
    private String product_stock;

    private int buyCount = 1;
    private AlertDialog dialog = null;


    // UI
    private ImageView ig_pagpic;
    private TextView pagName, DollarCd, DesCd, Count;
    private Button btnAdd, btnBuy;
    private ImageButton btnPlus, btnMinus;


    public static PagerCommodityFragment newInstance(
            String package_no,
            String product_name,
            String product_price,
            String product_picture,
            String product_description,
            String product_stock) {

        PagerCommodityFragment fragment = new PagerCommodityFragment();
        Bundle args = new Bundle();
        args.putString("package_no", package_no);
        args.putString("product_name", product_name);
        args.putString("product_price", product_price);
        args.putString("product_picture", product_picture);
        args.putString("product_description", product_description);
        args.putString("product_stock", product_stock);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            product_no = getArguments().getString("package_no");
            product_name = getArguments().getString("product_name");
            product_price = getArguments().getString("product_price");
            product_picture = getArguments().getString("product_picture");
            product_description = getArguments().getString("product_description");
            product_stock = getArguments().getString("product_stock");
            Log.d(TAG, "package_no: " + product_no);
            Log.d(TAG, "product_name: " + product_name);
            Log.d(TAG, "product_price: " + product_price);
            Log.d(TAG, "product_picture: " + product_picture);
            Log.d(TAG, "product_description: " + product_description);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pager_commodity, container, false);

        apiEnqueue = new ApiEnqueue();

        init(view);
        initHandler();

        return view;

    }

    private void init(View view) {

        pagName = view.findViewById(R.id.pagName);
        DollarCd = view.findViewById(R.id.DollarCd);
        DesCd = view.findViewById(R.id.DesCd);
        ig_pagpic = view.findViewById(R.id.ig_pagpic);
        Count = view.findViewById(R.id.CountCd);
        btnAdd = view.findViewById(R.id.btn_add);
        btnBuy = view.findViewById(R.id.btn_buy);
        btnPlus = view.findViewById(R.id.PlusCd);
        btnMinus = view.findViewById(R.id.ReduceCd);
    }

    private void initHandler() {

        activityTitleRid = R.string.main_page;

        Picasso.get().load(ApiUrl.API_URL + "ticketec/" + product_picture).into(ig_pagpic);
        pagName.setText(product_name);
        DollarCd.setText("$ " + product_price);
        Count.setText(String.valueOf(buyCount));
        DesCd.setText(product_description);

        // 增加
        btnPlus.setOnClickListener(view -> {
            if (Integer.parseInt(product_stock) > 0) {
                buyCount += 1;
                Count.setText(String.valueOf(buyCount));
            } else {
                showDialog("", "超過庫存數量", (dialog1, which) -> {
                    dialog.dismiss();
                });
            }
        });

        // 減少
        btnMinus.setOnClickListener(view -> {
            if (buyCount > 1) {
                buyCount -= 1;
                Count.setText(String.valueOf(buyCount));
            }else {
                showDialog("", "商品數目不得小於1，請重新確認", (dialog1, which) -> {
                    dialog.dismiss();
                });
            }
        });

        // 立即購買
        btnBuy.setOnClickListener(view -> PurchaseNow());

        // 加入購物車
        btnAdd.setOnClickListener(view -> {
            PurchaseAdd();
            MemberBean.isShoppingCarPoint = true;
            fragmentListener.onAction(FUNC_FRAGMENT_change, MemberBean.isShoppingCarPoint);
        });

    }

    // 立即購買
    private void PurchaseNow() {
        String total = String.valueOf(buyCount * Integer.parseInt(product_price));

        apiEnqueue.addShoppingCar(
                product_no,
                "1",
                product_price,
                String.valueOf(buyCount),
                total,
                new ApiEnqueue.resultListener() {
                    @Override
                    public void onSuccess(String message) {
                        getActivity().runOnUiThread(() -> {


                                    if (fragmentListener != null) {
                                        MemberBean.isShoppingCarPoint = true;
                                        fragmentListener.onAction(FUNC_FRAGMENT_change, MemberBean.isShoppingCarPoint);
                                        fragmentListener.onAction(fraShoppingCart, null);
                                    }

                                }
                        );
                    }

                    @Override
                    public void onFailure(String message) {
                        getActivity().runOnUiThread(() ->
                                Toast.makeText(requireActivity(), "商品補貨中", Toast.LENGTH_SHORT).show()
                        );

                    }
                });
    }


    // 9.新增商品到購物車
    private void PurchaseAdd() {

        String total = String.valueOf(buyCount * Integer.parseInt(product_price));

        apiEnqueue.addShoppingCar(
                product_no,
                "1",
                product_price,
                String.valueOf(buyCount),
                total,
                new ApiEnqueue.resultListener() {
                    @Override
                    public void onSuccess(String message) {
                        getActivity().runOnUiThread(() -> {


                                    if (message.equals("0x0200")) {
                                        showDialog("", "加入購物車成功", (dialog1, which) ->
                                        {
                                            if (fragmentListener != null) {
                                                MemberBean.isShoppingCarPoint = true;
                                                fragmentListener.onAction(FUNC_FRAGMENT_change, MemberBean.isShoppingCarPoint);
                                                dialog.dismiss();
                                            }
                                        });

                                    } else {
                                        showDialog("", "商品補貨中", (dialog1, which) ->
                                        {
                                            dialog.dismiss();
                                        });

                                    }


//                                    if (fragmentListener != null) {
//                                        MemberBean.isShoppingCarPoint = true;
//                                        fragmentListener.onAction(
//                                                FUNC_FRAGMENT_change,
//                                                MemberBean.isShoppingCarPoint
//                                        );
//                                        fragmentListener.onAction(fraShoppingCart, null);
//
//                                    }
                                }
                        );
                    }

                    @Override
                    public void onFailure(String message) {
                        getActivity().runOnUiThread(() ->
                                Toast.makeText(requireActivity(), "商品補貨中", Toast.LENGTH_SHORT).show()
                        );

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