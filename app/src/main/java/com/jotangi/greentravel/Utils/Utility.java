package com.jotangi.greentravel.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.jotangi.greentravel.R;

import java.nio.charset.StandardCharsets;

public class Utility {

    public static Dialog dialog;
    public static Context context;

    public static String encodeFileToBase64(String string) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                return Base64.encodeToString(string.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String logTitle(String str) {
        return "------------------- " + str + " -------------------";
    }

    public static void startLoading(Context cont){

        context = cont;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.loading_progress,null);
        dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setContentView(view);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.dismiss();
        dialog.show();
    }

    public static void endLoading(String message) {

        ((Activity)context).runOnUiThread(() -> {
            dialog.dismiss();
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        });
    }

    public static void endLoading(){

        ((Activity)context).runOnUiThread(() -> dialog.dismiss());
    }
}


