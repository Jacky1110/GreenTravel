package com.jotangi.greentravel.PagerStore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jotangi.greentravel.Api.ApiEnqueue;
import com.jotangi.greentravel.Base.BaseActivity;
import com.jotangi.greentravel.CouponActivity;
import com.jotangi.greentravel.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewPageMainActivity extends BaseActivity {

    private String TAG = getClass().getSimpleName() + "(TAG)";

    private ApiEnqueue apiEnqueue;
    private RecyclerView recyclerView;
    private NewPageAdapter adapter;
    private Intent intent;
    ArrayList<NewCouponModel> data = new ArrayList<>();

    // value
    private String order_date;
    private String order_no;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_page_main);
        initViews();
        handleData();
    }


    private void initViews() {

        apiEnqueue = new ApiEnqueue();
        recyclerView = findViewById(R.id.recycler_NewPage);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        order_date = getIntent().getStringExtra("order_date");
        order_no = getIntent().getStringExtra("order_no");
    }

    private void handleData() {
        try {
            JSONArray array = new JSONArray(getIntent().getStringExtra("product"));

            data = new ArrayList();
            for (int i = 0; i < array.length(); i++) {
                // 內層
                JSONObject jsonObject = (JSONObject) array.get(i);
                NewCouponModel model = new NewCouponModel();
                model.qrconfirm = jsonObject.getString("qrconfirm");
                model.productName = jsonObject.getString("product_name");
                model.storeName = jsonObject.getString("store_name");
                model.math = jsonObject.getString("order_qty");
                model.product_picture = jsonObject.getString("product_picture");
                data.add(model);
            }

            runOnUiThread(() -> {
                adapter = new NewPageAdapter();
                adapter.setData(data);
                recyclerView.setAdapter(adapter);
                adapter.setClickListener(onClick);

            });


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private final NewPageAdapter.ItemClickListener onClick = (view, position) -> {

        Log.d(TAG, "position: " + position);

        intent = new Intent();
        intent.putExtra("qrconfirm", data.get(position).qrconfirm);
        intent.putExtra("product_name", data.get(position).productName);
        intent.putExtra("order_date", order_date);
        intent.putExtra("order_no", order_no);
        intent.setClass(context, CouponActivity.class);
        startActivity(intent);
    };

}
