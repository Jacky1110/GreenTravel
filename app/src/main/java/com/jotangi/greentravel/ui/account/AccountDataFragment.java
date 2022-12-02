package com.jotangi.greentravel.ui.account;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.jotangi.greentravel.Api.ApiEnqueue;
import com.jotangi.greentravel.CustomDaialog;
import com.jotangi.greentravel.ui.hPayMall.MemberBean;
import com.jotangi.greentravel.ProjConstraintFragment;
import com.jotangi.greentravel.R;
import com.jotangi.greentravel.Utils.Utility;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class AccountDataFragment extends ProjConstraintFragment {

    private String TAG = getClass().getSimpleName() + "(TAG)";

    TextInputEditText edName, edBirthday, edMail, edPhone;
    RadioButton r1, r2;
    ConstraintLayout btnModify;
    ApiEnqueue apiEnqueue;

//    EditText etName;  //姓名
//    EditText etMail;  //email
//
//    TextView tvPhone;  //account
//
//    EditText etAdd;  //地址
//
//    Button btnBirthday;  //日期選擇
//    Button submitBtn; //同意並完成註冊
//
//    RadioButton rBtn_Male;
//    RadioButton rBtn_Female;
//
//    ImageView userIV;
//    CardView iv_User_Photo;
//
//    ImageButton BtnGoBack;

    public static AccountDataFragment newInstance() {
        AccountDataFragment fragment = new AccountDataFragment();
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        rootView = (ConstraintLayout) inflater.inflate(R.layout.fragment_account_data, container, false);

        return rootView;
    }

    @Override
    protected void initViews() {
        super.initViews();

        apiEnqueue = new ApiEnqueue();
        edName = rootView.findViewById(R.id.ed_name);
        edBirthday = rootView.findViewById(R.id.ed_birthday);
        edMail = rootView.findViewById(R.id.ed_email);
        edPhone = rootView.findViewById(R.id.ed_phone);
        r1 = rootView.findViewById(R.id.radio0);
        r2 = rootView.findViewById(R.id.radio1);
        edName.setText(MemberBean.name);
        edPhone.setText(MemberBean.member_id);

        if (MemberBean.birthday != null && MemberBean.birthday.equals("null")) {
            edBirthday.setText(null);
            //edHomenumber.setText(null);
        } else {
            edBirthday.setText(MemberBean.birthday);
        }

        edMail.setText(MemberBean.email);

//        switch (MemberBean.sex) {
//            case "0":
//                r1.setChecked(false);
//                r2.setChecked(true);
//                break;
//            case "1":
//                r1.setChecked(true);
//                r2.setChecked(false);
//                break;
//
//        }
        btnModify = rootView.findViewById(R.id.btn_register);
        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useredit();
            }
        });
        memberInfor();
    }

    @Override
    public void onStart() {
        super.onStart();
        activityTitleRid = R.string.account_listitem_data;
    }

    private void useredit() {
        String city = "";
        String region = "";
        String address = "";
        String name = edName.getText().toString();
        String sex = MemberBean.sex;
        if (r1.isChecked()) {
            sex = "1";
        } else if (r2.isChecked()) {
            sex = "0";
        }
        String email = edMail.getText().toString().trim();
        String birthday = edBirthday.getText().toString().trim();
        String tel = edPhone.getText().toString();

        if (birthday.isEmpty() || birthday.equals("null")) {
            requireActivity().runOnUiThread(() -> {
                showDialog("", "請輸入生日", (dialog, which) -> dialog.dismiss());
            });
            return;
        }
        apiEnqueue.modifypersondata(tel, name, sex, city, region, birthday, email, address, new ApiEnqueue.resultListener() {
            @Override
            public void onSuccess(String message) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (message.equals("101")) {
                            CustomDaialog.showNormal(requireActivity(), "", "資料修改成功", "確定", new CustomDaialog.OnBtnClickListener() {
                                @Override
                                public void onCheck() {

                                }

                                @Override
                                public void onCancel() {

                                    fragmentListener.onAction(FUNC_LOGIN_TO_ACCOUNT_MAIN, null);
                                    CustomDaialog.closeDialog();

                                }
                            });
                            return;
                        }
                    }
                });
            }

            @Override
            public void onFailure(String message) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CustomDaialog.showNormal(requireActivity(), "", "連線失敗", "確定", new CustomDaialog.OnBtnClickListener() {
                            @Override
                            public void onCheck() {

                            }

                            @Override
                            public void onCancel() {

//                                        fragmentListener.onAction(FUNC_ACCOUNT_MAIN_TO_LOGIN, null);
                                CustomDaialog.closeDialog();

                            }
                        });
                        return;
                    }
                });
            }

        });
    }

    private void memberInfor() {

        // 27.綠悠遊商城/租車取得會員詳細資料
        apiEnqueue.getPersonalData(new ApiEnqueue.resultListener() {
            @Override
            public void onSuccess(String message) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(message);
                            MemberBean.name = jsonObject.getString("name");
                            MemberBean.sex = jsonObject.getString("sex");
                            MemberBean.email = jsonObject.getString("email");
                            MemberBean.tel = jsonObject.getString("tel");
                            MemberBean.birthday = jsonObject.getString("birthday");
                            MemberBean.cmdImageFile = jsonObject.getString("cmdImageFile");



                            edName.setText(MemberBean.name);
                            edMail.setText(MemberBean.email);
                            if (MemberBean.birthday != null && MemberBean.birthday.equals("null")) {
                                edBirthday.setText(null);
                            } else {
                                edBirthday.setText(MemberBean.birthday);
                            }
                            switch (MemberBean.sex) {
                                case "0":
                                    r1.setChecked(false);
                                    r2.setChecked(true);
                                    break;
                                case "1":
                                    r1.setChecked(true);
                                    r2.setChecked(false);
                                    break;

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Utility.endLoading("Api 27: 欄位剖析失敗");
                        }
                    }
                });
            }

            @Override
            public void onFailure(String message) {
                Utility.endLoading(message);
            }
        });

    }

    private AlertDialog dialog = null;

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

