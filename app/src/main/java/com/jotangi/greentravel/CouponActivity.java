package com.jotangi.greentravel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.EnumMap;
import java.util.Map;

public class CouponActivity extends AppCompatActivity {

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

    private ScreenShotListenManager screenManager;

    private AlertDialog dialog = null;

    // UI
    private ImageView qrconfirmIV;
    private TextView product_nameTV, order_dateTV, order_noTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);

        initView();
        initHandler();
        handleManager();
    }

    private void handleManager() {
        screenManager = ScreenShotListenManager.newInstance(CouponActivity.this);
        screenManager.startListen();

        if (ContextCompat.checkSelfPermission(CouponActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CouponActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
        } else {
            listenerScreenShot();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 123:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(CouponActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
                } else {
                    listenerScreenShot();
                }
                break;
        }
    }

    // 截屏监听
    private void listenerScreenShot() {
        screenManager.setListener(
                new ScreenShotListenManager.OnScreenShotListener() {
                    public void onShot(final String imagePath) {
                        showDialog("", "QR code可供他人使用，請小心留存", (dialog1, which) -> {
                            dialog.dismiss();
                            Log.d("my_main_activity", "地址是:" + imagePath);
                        });
                    }
                }
        );
    }


    private void initView() {
        qrconfirmIV = findViewById(R.id.iv_qrconfirm);
        product_nameTV = findViewById(R.id.tv_product_name);
        order_dateTV = findViewById(R.id.tv_order_date);
        order_noTV = findViewById(R.id.tv_order_no);
    }

    private void initHandler() {
        qrconfirmIV.setImageBitmap(generateQrCode());
        product_nameTV.setText(getIntent().getStringExtra("product_name"));
        order_dateTV.setText("購買日期: " + getIntent().getStringExtra("order_date"));
        order_noTV.setText("訂單編號: " + getIntent().getStringExtra("order_no"));
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
            result = writer.encode(getIntent().getStringExtra("qrconfirm"), BarcodeFormat.QR_CODE,
                    CODE_WIDTH, CODE_HEIGHT, hints);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        // 建立點陣圖
        Bitmap bitmap = Bitmap.createBitmap(
                result.getWidth(), result.getHeight(),
                Bitmap.Config.ARGB_8888);

        // 將 QR code 資料矩陣繪製到點陣圖上
        for (int y = 0; y < result.getHeight(); y++) {
            for (int x = 0; x < result.getWidth(); x++) {
                bitmap.setPixel(x, y, result.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }

        return bitmap;
    }

    private void showDialog(String title, String message, DialogInterface.OnClickListener listener) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = new AlertDialog.Builder(this).create();
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