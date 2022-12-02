package com.jotangi.greentravel.ui.store;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.jotangi.greentravel.DataBeen;
import com.jotangi.greentravel.ProjConstraintFragment;
import com.jotangi.greentravel.R;


public class FillInInformationFragment extends ProjConstraintFragment {

    private String TAG = getClass().getSimpleName() + "(TAG)";

    private AlertDialog dialog = null;
    EditText etName, etPhone, etLicensePlate, etCarModel, etCondition;
    TextView repairTypeTextView;
    Button btnNext;
    private CheckBox[] checkBoxes = new CheckBox[3];
    private String fix;
    private CheckBox fixing;
    private Boolean check = false;
    private String[] repairTypeList = new String[]{"保養", "修車", "其他"};

    public static FillInInformationFragment newInstance() {
        FillInInformationFragment fragment = new FillInInformationFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ConstraintLayout) inflater.inflate(R.layout.fragment_fill_in_information, container, false);

        return rootView;
    }

    @Override
    protected void initViews() {
        super.initViews();

        etName = rootView.findViewById(R.id.et_modifyName);
        etPhone = rootView.findViewById(R.id.et_phone);
        etLicensePlate = rootView.findViewById(R.id.et_licensePlate);
        etCarModel = rootView.findViewById(R.id.et_carModel);
        etCondition = rootView.findViewById(R.id.et_condition);
        repairTypeTextView = rootView.findViewById(R.id.repair_type_textView);
        repairTypeTextView.setOnClickListener(view ->
                showRepairTypeDialog()
        );

//        checkBoxes[0] = rootView.findViewById(R.id.cb_one);
//        checkBoxes[0].setOnCheckedChangeListener(this);
//        checkBoxes[1] = rootView.findViewById(R.id.cb_two);
//        checkBoxes[1].setOnCheckedChangeListener(this);
//        checkBoxes[2] = rootView.findViewById(R.id.cb_three);
//        checkBoxes[2].setOnCheckedChangeListener(this);
        btnNext = rootView.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(view -> handleData());
    }

    @Override
    public void onStart() {
        super.onStart();
        activityTitleRid = R.string.title_readName;
    }

    private void handleData() {
        DataBeen.name = etName.getText().toString().trim();
        DataBeen.phone = etPhone.getText().toString().trim();
        DataBeen.licensePlate = etLicensePlate.getText().toString().trim();
        DataBeen.carModel = etCarModel.getText().toString().trim();
        DataBeen.fixCar = fix;
        DataBeen.condition = etCondition.getText().toString().trim();

        if (DataBeen.name.isEmpty()) {
            showDialog("", "名字欄位不能空白", (dialog1, which) -> {
                dialog.dismiss();
            });
            return;
        }

        if (DataBeen.phone.isEmpty()) {
            showDialog("", "電話欄位不能空白", (dialog1, which) -> {
                dialog.dismiss();
            });
            return;
        }

        if (DataBeen.licensePlate.isEmpty()) {
            showDialog("", "車牌欄位不能空白", (dialog1, which) -> {
                dialog.dismiss();
            });
            return;
        }

        if (DataBeen.carModel.isEmpty()) {
            showDialog("", "車型欄位不能空白", (dialog1, which) -> {
                dialog.dismiss();
            });
            return;
        }

        if (repairTypeTextView.getText().length() == 0) {
            showDialog("", "請選擇修車類型", (dialog1, which) -> {
                dialog.dismiss();
            });
            return;
        }

        if (fragmentListener != null) {
            fragmentListener.onAction(FUNC_FILL_TIME, null);
        }

    }

    private void showRepairTypeDialog() {
        //設定單選清單
        new AlertDialog.Builder(requireContext())
                .setTitle("請選擇：")
                .setSingleChoiceItems(repairTypeList, 0, (dialog, which) -> {
                    fix = repairTypeList[which];
                    repairTypeTextView.setText(fix);
                    dialog.dismiss();
                }).show();

    }

    private void showDialog(String title, String message, DialogInterface.OnClickListener listener) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = new AlertDialog.Builder(requireContext()).create();
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