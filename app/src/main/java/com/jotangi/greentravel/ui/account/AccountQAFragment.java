package com.jotangi.greentravel.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.jotangi.greentravel.ProjConstraintFragment;
import com.jotangi.greentravel.R;


public class AccountQAFragment extends ProjConstraintFragment {

    public static AccountQAFragment newInstance() {
        AccountQAFragment fragment = new AccountQAFragment();
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        activityTitleRid = R.string.account_listitem_qa;
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        rootView = (ConstraintLayout)inflater.inflate(R.layout.fragment_account_qa, container, false);
        return rootView;
    }

    @Override
    protected void initViews() {
        super.initViews();


    }
}