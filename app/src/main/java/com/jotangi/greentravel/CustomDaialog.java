package com.jotangi.greentravel;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;


/**
 * @author Kelly
 * @version hpay_34版
 * @Title: CustomDaialog.kt
 * @Package com.jotangi.healthy.HpayMall
 * @Description: CustomDaialog
 * @date 2021-12
 */
public class CustomDaialog {
    public static AlertDialog appDialog;
    public static CheckBox checkBox;

    public interface OnBtnClickListener {
        void onCheck();

        void onCancel();
    }

    public interface OnBtn3ClickListener {
        void b1();

        void b2();

        void b3();
    }

    public static void showMyDialog(Activity activity, String tx1, String tx2, String confirmString, @NonNull final OnBtnClickListener listener) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);

        View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_terms, viewGroup, false);
        ConstraintLayout cl = dialogView.findViewById(R.id.lAgreee);
        CheckBox cb = dialogView.findViewById(R.id.c1);
        TextView tx = dialogView.findViewById(R.id.t1);
        Button yes = dialogView.findViewById(R.id.Yes);
        buildDialog(dialogView, activity, tx1, tx2, confirmString, listener);
        appDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
    }

    public static void buildDialog(View dialogView, Activity activity, String tx1, String tx2, String confirmString, @NonNull final OnBtnClickListener listener) {
        ConstraintLayout cl = dialogView.findViewById(R.id.lAgreee);
        CheckBox cb = dialogView.findViewById(R.id.c1);
        TextView tx = dialogView.findViewById(R.id.t1);
        Button yes = dialogView.findViewById(R.id.Yes);
        tx.setText(tx1);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCheck();

            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onCancel();

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setView(dialogView);
        builder.setCancelable(true);
        // appDialog.setCanceledOnTouchOutside(false);
        appDialog = builder.create();
        appDialog.setCanceledOnTouchOutside(false);
        appDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        appDialog.show();
    }

    public static void closeDialog() {
        if (appDialog != null) {
            appDialog.dismiss();
//            appDialog = null;
        }
    }

    public static void check() {
        if (checkBox != null) {
            checkBox.isChecked();
            checkBox = null;
        }
    }

    public static void NoSeeCheck() {
        if (checkBox != null) {

            checkBox = null;
        }
    }

    public static void showNormal(Activity activity, String tx1, String tx2, String confirmString, @NonNull final OnBtnClickListener listener) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);

        View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_normal, viewGroup, false);
        ConstraintLayout cl = dialogView.findViewById(R.id.lAgreee);
        TextView cb = dialogView.findViewById(R.id.c1);
        TextView tx = dialogView.findViewById(R.id.t1);
        Button yes = dialogView.findViewById(R.id.Yes);
        buildNormal(dialogView, activity, tx1, tx2, confirmString, listener);
        appDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
    }

    public static void buildNormal(View dialogView, Activity activity, String tx1, String tx2, String confirmString, @NonNull final OnBtnClickListener listener) {
        ConstraintLayout cl = dialogView.findViewById(R.id.lAgreee);
        TextView cb = dialogView.findViewById(R.id.c1);
        TextView tx = dialogView.findViewById(R.id.t1);
        Button yes = dialogView.findViewById(R.id.Yes);
        tx.setText(tx1);
        cb.setText(tx2);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onCancel();

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setView(dialogView);
        builder.setCancelable(true);
        // appDialog.setCanceledOnTouchOutside(false);
        appDialog = builder.create();
        appDialog.setCanceledOnTouchOutside(false);
        appDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        appDialog.show();
    }

    public static void showTwo(Activity activity, String tx1, String tx2, String confirmString, @NonNull final OnBtnClickListener listener) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);

        View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_two_button, viewGroup, false);
        ConstraintLayout cl = dialogView.findViewById(R.id.lAgreee);
        TextView cb = dialogView.findViewById(R.id.c1);
        TextView tx = dialogView.findViewById(R.id.t1);
        Button yes = dialogView.findViewById(R.id.Yes0);
        Button No = dialogView.findViewById(R.id.No0);

        buildTwo(dialogView, activity, tx1, tx2, confirmString, listener);
        appDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
    }

    public static void buildTwo(View dialogView, Activity activity, String tx1, String tx2, String confirmString, @NonNull final OnBtnClickListener listener) {
        ConstraintLayout cl = dialogView.findViewById(R.id.lAgreee);
        TextView cb = dialogView.findViewById(R.id.c1);
        TextView tx = dialogView.findViewById(R.id.t1);
        Button yes = dialogView.findViewById(R.id.Yes0);
        Button No = dialogView.findViewById(R.id.No0);
        yes.setText(tx1);
        No.setText(tx2);
        //tx.setText(tx1);
        //cb.setText(tx2);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onCheck();

            }
        });
        No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onCancel();


            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setView(dialogView);
        builder.setCancelable(true);
        // appDialog.setCanceledOnTouchOutside(false);
        if(appDialog!=null&&appDialog.isShowing())
        {
            appDialog.dismiss();
        }
        appDialog = builder.create();
        appDialog.setCanceledOnTouchOutside(false);
        appDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        appDialog.show();
    }

    public static void showOne(Activity activity, String tx1, String tx2, String confirmString, @NonNull final OnBtnClickListener listener) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);

        View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_one_button, viewGroup, false);
        ConstraintLayout cl = dialogView.findViewById(R.id.lAgreee1);
        TextView cb = dialogView.findViewById(R.id.c11);
        TextView tx = dialogView.findViewById(R.id.t11);
        Button yes = dialogView.findViewById(R.id.Yes1);


        buildOne(dialogView, activity, tx1, tx2, confirmString, listener);
        appDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
    }

    public static void buildOne(View dialogView, Activity activity, String tx1, String tx2, String confirmString, @NonNull final OnBtnClickListener listener) {
        ConstraintLayout cl = dialogView.findViewById(R.id.lAgreee1);
        TextView cb = dialogView.findViewById(R.id.c11);
        TextView tx = dialogView.findViewById(R.id.t11);
        Button yes = dialogView.findViewById(R.id.Yes1);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCheck();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogView);
        builder.setCancelable(true);
        // appDialog.setCanceledOnTouchOutside(false);
        if(appDialog!=null&&appDialog.isShowing())
        {
            appDialog.dismiss();
        }
        appDialog = builder.create();
        appDialog.setCanceledOnTouchOutside(false);
        appDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        appDialog.show();
    }
    //


    public static void showthree(Activity activity, String tx1, String tx2, String confirmString, @NonNull final OnBtn3ClickListener listener) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);

        View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_three_button, viewGroup, false);
        ConstraintLayout cl = dialogView.findViewById(R.id.lAgreee1);
        Button b1 = dialogView.findViewById(R.id.b1);
        Button b2 = dialogView.findViewById(R.id.b2);
        Button b3 = dialogView.findViewById(R.id.b3);


        buildthree(dialogView, activity, tx1, tx2, confirmString, listener);
        appDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
    }

    public static void buildthree(View dialogView, Activity activity, String tx1, String tx2, String confirmString, @NonNull final OnBtn3ClickListener listener) {
        ConstraintLayout cl = dialogView.findViewById(R.id.lAgreee1);
        Button b1 = dialogView.findViewById(R.id.b1);
        Button b2 = dialogView.findViewById(R.id.b2);
        Button b3 = dialogView.findViewById(R.id.b3);
       b1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               listener.b1();
           }
       });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.b2();
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.b3();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogView);
        builder.setCancelable(true);
        // appDialog.setCanceledOnTouchOutside(false);
        appDialog = builder.create();
        appDialog.setCanceledOnTouchOutside(false);
        appDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        appDialog.show();
    }
}
