package com.jotangi.greentravel;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AccountCustomerFragment extends ProjConstraintFragment {


    public static AccountCustomerFragment newInstance() {
        AccountCustomerFragment fragment = new AccountCustomerFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ConstraintLayout) inflater.inflate(R.layout.fragment_account_customer, container, false);
        return rootView;
    }

}