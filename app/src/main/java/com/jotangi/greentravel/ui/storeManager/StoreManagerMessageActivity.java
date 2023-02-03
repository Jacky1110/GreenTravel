package com.jotangi.greentravel.ui.storeManager;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jotangi.greentravel.Api.ApiEnqueue;
import com.jotangi.greentravel.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class StoreManagerMessageActivity extends AppCompatActivity {

    private ApiEnqueue apiEnqueue;
    private RecyclerView recyclerView;
    private StoreMessageAdapter storeMessageAdapter;

    private static ArrayList<MessageModel> messageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_manager_message);

        initView();
        getMessagedata();
    }

    private void initView() {
        apiEnqueue = new ApiEnqueue();

        recyclerView = findViewById(R.id.messageRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }


    private void getMessagedata() {
        apiEnqueue.storeadminGetNotifyHistory(new ApiEnqueue.resultListener() {
            @Override
            public void onSuccess(String message) {
                runOnUiThread(() -> {
                    messageList = new ArrayList<>();

                    try {
                        JSONArray jsonArray = new JSONArray(message);
                        for (int i = 0; i < jsonArray.length(); i++) {

                            MessageModel messageModel = new MessageModel();

                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                            messageModel.title = jsonObject.getString("title");
                            messageModel.date = jsonObject.getString("updatetime");
                            messageModel.content = jsonObject.getString("message");
                            messageList.add(messageModel);
                        }


                        Collections.sort(messageList, new Comparator<MessageModel>() {
                            @Override
                            public int compare(MessageModel o1, MessageModel o2) {


                                Date date1 = DateUtil.stringToDate(o1.date);
                                Date date2 = DateUtil.stringToDate(o2.date);
                                if (date1.before(date2)) {
                                    return 1;
                                } else {
                                    return -1;
                                }
                            }
                        });

                        storeMessageAdapter = new StoreMessageAdapter();
                        storeMessageAdapter.setData(messageList);
                        recyclerView.setAdapter(storeMessageAdapter);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }
}