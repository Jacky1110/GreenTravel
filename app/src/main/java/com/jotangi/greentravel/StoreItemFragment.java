package com.jotangi.greentravel;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jotangi.greentravel.Api.ApiUrl;
import com.squareup.picasso.Picasso;

public class StoreItemFragment extends Fragment {

    private String TAG = getClass().getSimpleName() + "(TAG)";

//    private static final String URL_Store_Picture = "URL_Store_Picture";

    private String storePictureUrl;

    public StoreItemFragment(String storePictureUrl) {
        this.storePictureUrl = storePictureUrl;
    }

//    public static StoreItemFragment newInstance(String param1) {
//        StoreItemFragment fragment = new StoreItemFragment();
//        Bundle args = new Bundle();
//        args.putString(URL_Store_Picture, param1);
//        fragment.setArguments(args);
//        return fragment;
//    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            storePictureUrl = getArguments().getString(URL_Store_Picture);
//            Log.d(TAG, "storePictureUrl: " + storePictureUrl);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_item, container, false);
        ImageView storeIV = view.findViewById(R.id.iv_store);

        Picasso.get().load(ApiUrl.API_URL + "ticketec/" + storePictureUrl).into(storeIV);

        return view;
    }
}