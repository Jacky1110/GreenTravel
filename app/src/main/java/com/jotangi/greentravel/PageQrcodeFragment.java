package com.jotangi.greentravel;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.jotangi.greentravel.ui.account.AccountLoginFragment;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

public class PageQrcodeFragment extends ProjConstraintFragment {

    private ImageView imageView;
    private TextView tvname, tvday, tvid;
    ArrayList data = new ArrayList();




    public static PageQrcodeFragment newInstance() {
        PageQrcodeFragment fragment = new PageQrcodeFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = (ConstraintLayout) inflater.inflate(R.layout.fragment_page_qrcode, container, false);

        return rootView;
    }

    @Override
    protected void initViews(){
        super.initViews();


        tvname = rootView.findViewById(R.id.tvName);
        tvday = rootView.findViewById(R.id.tvDay);
        tvid = rootView.findViewById(R.id.tvId);
        imageView = rootView.findViewById(R.id.iv);
        imageView.setImageBitmap(QrCode("1gjdrig23456"));


    }

    private Bitmap QrCode(String content) {
// QR code 內容編碼
        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);

        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        // 設定四周白邊大小
        hints.put(EncodeHintType.MARGIN, 0);

        // 建立 QR code 的資料矩陣
        BitMatrix result = null;
        // format 對應的寬高
        int CODE_WIDTH = 280;
        int CODE_HEIGHT = 280;

        Writer writer = new QRCodeWriter();

        try {
            result = writer.encode(content , BarcodeFormat.QR_CODE,
                    CODE_WIDTH ,CODE_HEIGHT ,hints);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        // 建立點陣圖
        Bitmap bitmap = Bitmap.createBitmap(
                result.getWidth(), result.getHeight(),
                Bitmap.Config.ARGB_8888);

        // 將 QR code 資料矩陣繪製到點陣圖上
        for (int y = 0; y < result.getHeight() ; y++) {
            for (int x = 0; x < result.getWidth() ; x++) {
                bitmap.setPixel(x, y, result.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }

        return bitmap;
    }
}