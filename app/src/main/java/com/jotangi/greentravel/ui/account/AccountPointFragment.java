package com.jotangi.greentravel.ui.account;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jotangi.greentravel.ui.hPayMall.MemberBean;
import com.jotangi.greentravel.ProjConstraintFragment;
import com.jotangi.greentravel.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AccountPointFragment extends ProjConstraintFragment {
    private final String TAG = this.getClass().getSimpleName();

//    private List<PointTradeRecord> recordlist;
//    private PointRemainInfo pointRemainInfo;
//
//    private AccountPointRecordAdaptor recordAdaptor;
    protected RecyclerView.LayoutManager recordLayoutManager;

    RecyclerView rvDataView;
    TextView remainPointView;
    TextView remainPointNoteView;
    TextView noneDataView;

    public static AccountPointFragment newInstance() {
        AccountPointFragment fragment = new AccountPointFragment();
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        rootView = (ConstraintLayout) inflater.inflate(R.layout.fragment_account_point, container, false);
        return rootView;
    }

    @Override
    protected void initViews() {
        super.initViews();

        remainPointView = rootView.findViewById(R.id.tv_remain_point);
        remainPointNoteView = rootView.findViewById(R.id.tv_point_exp_time);

        noneDataView = rootView.findViewById(R.id.tv_no_point);

        rvDataView = rootView.findViewById(R.id.rv_data);
        recordLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvDataView.setLayoutManager(recordLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvDataView.getContext(),
                DividerItemDecoration.VERTICAL);
//        dividerItemDecoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(getContext(), R.drawable.rv_divider_grayc0)));
//        rvDataView.addItemDecoration(dividerItemDecoration);


//        recordAdaptor = new AccountPointRecordAdaptor();
//        rvDataView.setAdapter(recordAdaptor);
        rvDataView.scrollToPosition(0);
    }

    @Override
    public void onStart() {
        super.onStart();
        activityTitleRid = R.string.account_listitem_point;
    }


    @Override
    public void onResume() {
        super.onResume();
        updateData();
    }

    private void initDataSet() {
//        pointRemainInfo = new PointRemainInfo(Integer.valueOf(MemberBean.member_totalpoints), 10, "2021-12-31");
//
//        if (recordlist == null)
//            recordlist = new ArrayList<>();
//        else recordlist.clear();

        // for local test
//        recordlist.add(new PointTradeRecord(0, "", 0, null));
//        recordlist.add(new PointTradeRecord(PointTradeRecord.POINT_GET, "2021-09-15", 150, "2021-03-15"));
//        recordlist.add(new PointTradeRecord(PointTradeRecord.POINT_CANCEL, "2021-09-12", 20, "2021-12-10"));
//        recordlist.add(new PointTradeRecord(PointTradeRecord.POINT_USED, "2021-09-20", 20, "2021-12-10"));

    }

    private void updateData() {
        initDataSet();

        bonus_list();
//        updatPointDataView();
        updatePointInfoView();

//        recordAdaptor.setDatalist(recordlist);
//        noneDataView.setVisibility(recordlist.size() > 0 ? View.INVISIBLE : View.VISIBLE);


    }

    private void updatPointDataView() {
//        recordAdaptor.setDatalist(recordlist);
//        rvDataView.setVisibility(recordlist.size() > 0 ? View.VISIBLE : View.INVISIBLE);
//        noneDataView.setVisibility(recordlist.size() > 0 ? View.INVISIBLE : View.VISIBLE);

    }

    private void updatePointInfoView() {
//        if (pointRemainInfo.getRemainPoint() > 0) {
//            remainPointView.setText(String.valueOf(pointRemainInfo.getRemainPoint()));
//            remainPointNoteView.setVisibility(View.VISIBLE);
//            remainPointNoteView.setText(getString(
//                    R.string.point_remain_note,
//                    pointRemainInfo.getExpTime(),
//                    pointRemainInfo.getPointBeforeExpTime()));
//        } else {
//            remainPointView.setText(getString(R.string.point_remain_zero));
//            remainPointNoteView.setText(null);
//            remainPointNoteView.setVisibility(View.INVISIBLE);
//        }
    }


    /*
    * [
  {
    "0": "65",
    "1": "1",
    "2": "0931259760",
    "3": "2021-12-03 11:07:18",
    "4": "3",
    "5": "100",
    "bid": "65",
    "account": "1",
    "order_no": "0931259760",
    "bonus_date": "2021-12-03 11:07:18",
    "bonus_type": "3",
    "bonus": "100"
  }
]
    * */
//    private List<BonusBean> bonuslist = new ArrayList<>();
    OkHttpClient client = new OkHttpClient(

    );

    private void bonus_list() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String account = MemberBean.tel;
        String pwd = MemberBean.member_pwd;
        String bonus_startdate = sdf.format(new Date());
        String bonus_enddate = sdf.format(new Date());
        Log.d(TAG, "bonus_startdate : " + bonus_startdate + "\n"
                + "bonus_enddate : " + bonus_enddate);
        RequestBody body = new FormBody.Builder()
                .add("account", "0912345678")
                .add("member_pwd", "1234qwer")
                .build();

        Request request = new Request.Builder()
                .url("http://211.20.185.2/tours/api/bonus_list.php")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<String> orderArrayList = new ArrayList<>();


                        JSONObject jsonObject = null;
                        try {
                            String jsonString = response.body().string();
                            Log.e(TAG, jsonString);
                            JSONArray jsonArray = new JSONArray(jsonString);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jo = (JSONObject) jsonArray.get(i);
                                String s2 = jo.getString("bonus_date");
                                String s1 = jo.getString("bonus_type");

                                String s3 = jo.getString("bonus");
//                                if (s1.equals("2")) {
//                                    orderArrayList.add(s1);
//                                    orderArrayList.add(s2);
//                                    orderArrayList.add(s3);
//
//                                }
//                                recordlist.clear();
//                                recordlist.add(new PointTradeRecord(PointTradeRecord.POINT_GET, s2, Integer.valueOf(s3), s2));

//                                if (responseMessage.equals("1")) {
//                                    recommendArrayList.add(s1);
//                                    recommendArrayList.add(s2);
//                                    recommendArrayList.add(s3);
//                                    recordlist.add(new InviteFriendRecord(1, "健康point", 100, s2, s3));
//                                }

                            }
//                            recordAdaptor.setDatalist(recordlist);
//                            noneDataView.setVisibility(recordlist.size() > 0 ? View.INVISIBLE : View.VISIBLE);
//                            Log.d(TAG, "test" + recordAdaptor);
                        } catch (Exception E) {

                        } finally {

                        }


                    }
                });

            }
        });
//        ApiConUtils.bonus_list(ApiUrl.API_URL, ApiUrl.bonus_list, account, pwd, bonus_startdate, bonus_enddate, new ApiConUtils.OnConnect() {
//            @Override
//            public void onSuccess(String jsonString) throws JSONException {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.e(TAG, jsonString);
//
//                        JSONObject jsonObject = null;
//                        try {
//                            jsonObject = new JSONObject(jsonString);
//
//                            String status = jsonObject.getString("status");
//                            String code = jsonObject.getString("code");
//                            String responseMessage = jsonObject.getString("responseMessage");
//                            OrderBean.rcCount=responseMessage;
//                            String recommend_list = jsonObject.getString("recommend_list");
//                            JSONArray jsonArray = new JSONArray(recommend_list);
//                            bonuslist = new ArrayList<>();
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jo = (JSONObject) jsonArray.get(i);
//                                String s1 = jo.getString("recommend_code");
//                                String s2 = jo.getString("bonus_date");
//                                String s3 = jo.getString("bonus");
//
//                                recordlist.add(new PointTradeRecord(PointTradeRecord.POINT_GET, s2, Integer.valueOf(s3), s2));
//
////                                if (responseMessage.equals("1")) {
////                                    recommendArrayList.add(s1);
////                                    recommendArrayList.add(s2);
////                                    recommendArrayList.add(s3);
////                                    recordlist.add(new InviteFriendRecord(1, "健康point", 100, s2, s3));
////                                }
//
//                            }
//                            recordAdaptor.setDatalist(recordlist);
//                            noneDataView.setVisibility(recordlist.size() > 0 ? View.INVISIBLE : View.VISIBLE);
//                            Log.d(TAG, "test" + recordAdaptor);
//
//                        } catch (JSONException exception) {
//                            exception.printStackTrace();
//                        }
//
//                    }
//                });
//            }
//
//            @Override
//            public void onFailure(String message) {
//                Log.d("member_usercode", " fail" + message);
//
//            }
//        });
    }
}