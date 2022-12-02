package com.jotangi.greentravel.PagerStore;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.jotangi.greentravel.R;

import java.util.EnumMap;
import java.util.Map;

public class CouponFragment extends Fragment {

    private String TAG = getClass().getSimpleName() + "(TAG)";

    // QR code
    private static final String QRCONFIRM = "QRCONFIRM";
    // 產品名稱
    private static final String PRODUCT_NAME = "PRODUCT_NAME";
    // 購買日期
    private static final String ORDER_DATE = "ORDER_DATE";
    // 訂單編號
    private static final String ORDER_NO = "ORDER_NO";

    // value
    private String qrconfirm;
    private String product_name;
    private String order_date;
    private String order_no;

    // UI
    private ImageView qrconfirmIV;
    private TextView product_nameTV, order_dateTV, order_noTV;

    public CouponFragment() {
        // Required empty public constructor
    }

    public static CouponFragment newInstance(
            String qrconfirm,
            String product_name,
            String order_date,
            String order_no) {

        CouponFragment fragment = new CouponFragment();
        Bundle args = new Bundle();
        args.putString(QRCONFIRM, qrconfirm);
        args.putString(PRODUCT_NAME, product_name);
        args.putString(ORDER_DATE, order_date);
        args.putString(ORDER_NO, order_no);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            qrconfirm = getArguments().getString(QRCONFIRM);
            product_name = getArguments().getString(PRODUCT_NAME);
            order_date = getArguments().getString(ORDER_DATE);
            order_no = getArguments().getString(ORDER_NO);
            Log.d(TAG, "qrconfirm: " + qrconfirm);
            Log.d(TAG, "product_name: " + product_name);
            Log.d(TAG, "order_date: " + order_date);
            Log.d(TAG, "order_no: " + order_no);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_coupon, container, false);

        init(view);
        initHandler();

        return view;
    }

    private void init(View view) {
        qrconfirmIV = view.findViewById(R.id.iv_qrconfirm);
        product_nameTV = view.findViewById(R.id.tv_product_name);
        order_dateTV = view.findViewById(R.id.tv_order_date);
        order_noTV = view.findViewById(R.id.tv_order_no);
    }

    private void initHandler() {
        qrconfirmIV.setImageBitmap(generateQrCode());
        product_nameTV.setText(product_name);
        order_dateTV.setText("購買日期: " + order_date);
        order_noTV.setText("訂單編號: " + order_no);
    }

    private Bitmap generateQrCode() {

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
            result = writer.encode(qrconfirm , BarcodeFormat.QR_CODE,
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
