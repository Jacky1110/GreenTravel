package com.jotangi.greentravel;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyBaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyBaseFragment extends Fragment {

    protected FragmentListener fragmentListener;
    public void setFragmentListener(FragmentListener listener) {
        this.fragmentListener = listener;
    }
    public void setFragmentListener2(FragmentListener listener) {
        this.fragmentListener = listener;
    }

    public interface FragmentListener {
        void onAction(int funcno, Object data);
    }
    public interface FragmentListener2 {
        void onAction(int funcno, Object data);
    }

    public MyBaseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MyBaseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyBaseFragment newInstance() {
        MyBaseFragment fragment = new MyBaseFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentListener) {
            fragmentListener = (FragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentListener");
        }
    }

    @Override
    public void onDetach() {
        fragmentListener = null;
        super.onDetach();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    protected void initViews() {

    }
}