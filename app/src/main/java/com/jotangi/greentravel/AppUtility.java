package com.jotangi.greentravel;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.nio.charset.StandardCharsets;

public class AppUtility {

    public static AlertDialog appDialog;

    public interface OnBtnClickListener
    {
        void onCheck();

        void onCancel();
    }

    public static void setWindowAlpha(Window window, float alpha)
    {
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = alpha;

        if (alpha == 1.0)
        {
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        } else
        {
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }

        window.setAttributes(lp);
    }

    public static void showMyDialog(Activity activity, String message, String checkString, String cancelString, @NonNull final OnBtnClickListener listener)
    {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog, viewGroup, false);
        buildDialog(dialogView, activity, message, checkString, cancelString, listener);
    }

    public static void buildDialog(View dialogView, Activity activity, String message, String checkString, String cancelString, @NonNull final OnBtnClickListener listener)
    {
        TextView messageTV = dialogView.findViewById(R.id.messageTV);
        Button checkBtn = dialogView.findViewById(R.id.btnCheck);
        Button cancelBtn = dialogView.findViewById(R.id.btnCancel);

        messageTV.setText(message);


        if (checkString == null || checkString.equals(""))
        {
            checkBtn.setVisibility(View.GONE);
        } else
        {
            checkBtn.setText(checkString);
        }

        if (cancelString == null || cancelString.equals(""))
        {
            cancelBtn.setVisibility(View.GONE);
        } else
        {
            cancelBtn.setText(cancelString);
        }

        cancelBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                appDialog.dismiss();
                listener.onCancel();
            }
        });

        checkBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                appDialog.dismiss();
                listener.onCheck();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogView);
        builder.setCancelable(true);

        appDialog = builder.create();
        appDialog.show();
    }

}
