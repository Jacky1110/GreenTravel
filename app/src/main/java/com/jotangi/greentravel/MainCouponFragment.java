package com.jotangi.greentravel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;
import com.jotangi.greentravel.PagerStore.UnUsePageFragment;
import com.jotangi.greentravel.PagerStore.UsePageFragment;

public class MainCouponFragment extends ProjConstraintFragment {

    private String TAG = MainCouponFragment.class.getSimpleName() + "(TAG)";

    TabLayout tabLayout;
    FragmentManager fragmentManager;

    public static MainCouponFragment newInstance() {
        MainCouponFragment fragment = new MainCouponFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ConstraintLayout) inflater.inflate(R.layout.fragment_main_coupon, container, false);
        return rootView;
    }

    @Override
    protected void initViews() {
        super.initViews();

        tabLayout = rootView.findViewById(R.id.tabLayout);

        fragmentManager = requireActivity().getSupportFragmentManager();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                fragmentChange(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragmentContainerView, new UsePageFragment());
        transaction.addToBackStack(MainCouponFragment.class.getSimpleName());
        transaction.commit();
    }

    private void fragmentChange(int position) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (0 == position) {
            transaction.replace(R.id.fragmentContainerView, new UsePageFragment());
            transaction.addToBackStack(UsePageFragment.class.getSimpleName());
        } else {
            transaction.replace(R.id.fragmentContainerView, new UnUsePageFragment());
            transaction.addToBackStack(UnUsePageFragment.class.getSimpleName());
        }
        transaction.commit();
    }
}
