package com.jotangi.greentravel.ui.account;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.jotangi.greentravel.ui.account.AccountLoginFragment.KEY_IS_LOGIN;
import static com.jotangi.greentravel.ui.account.AccountLoginFragment.REG_PREF_NAME;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.jotangi.greentravel.Api.ApiEnqueue;
import com.jotangi.greentravel.AppUtility;
import com.jotangi.greentravel.Api.ApiUrl;
import com.jotangi.greentravel.LoginMainActivity;
import com.jotangi.greentravel.ui.hPayMall.MemberBean;
import com.jotangi.greentravel.PagerStore.CouponMainActivity;
import com.jotangi.greentravel.ProjConstraintFragment;
import com.jotangi.greentravel.R;
import com.jotangi.greentravel.Utils.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;


public class MemberFragment extends ProjConstraintFragment {

    private static final int REQUEST_CAMERA = 332;
    private static final int REQUEST_EXTERNAL_STORAGE = 333;
    private static final int REQUEST_SELECT_VIDEO = 334;
    private String TAG = getClass().getSimpleName() + "(TAG)";
    private Button bnAccountData;
    private Button loginOutButton;
    private TextView rPointTextView, txtUserName;
    private RelativeLayout bnRecord;
    private LinearLayout bnPoint, bnPacker, btnFix;
    private RelativeLayout bnUserRule;
    private RelativeLayout bnRecommend;
    private RelativeLayout bnCoupon;
    private RelativeLayout bnQA;
    private RelativeLayout bnCS;
    private RelativeLayout bnLogout;
    private CardView vwUserCard;
    private ImageView vwHeadImage;
    private ApiEnqueue apiEnqueue;
    private Bitmap tempImage;
    private SharedPreferences pref;

    public static MemberFragment newInstance() {
        MemberFragment fragment = new MemberFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        activityTitleRid = R.string.main_member;
    }

    @Override
    public void onResume() {
        super.onResume();
        handleData();
//        updateHeadPhoto();
    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ConstraintLayout) inflater.inflate(R.layout.fragment_member, container, false);
        return rootView;
    }


    @Override
    protected void initViews() {
        super.initViews();

        loginOutButton = rootView.findViewById(R.id.btnEditUser);
        pref = requireActivity().getSharedPreferences(REG_PREF_NAME, MODE_PRIVATE);
        boolean isLogin = pref.getBoolean(KEY_IS_LOGIN, false);
        if (isLogin) {
            loginOutButton.setText(getString(R.string.logout));
            loginOutButton.setOnClickListener(v -> {

                SharedPreferences pref = requireActivity().getSharedPreferences("loginInfo", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(requireActivity(), LoginMainActivity.class);
                startActivity(intent);
                requireActivity().finish();

            });
        }

        apiEnqueue = new ApiEnqueue();

        bnAccountData = rootView.findViewById(R.id.item_account);
        bnAccountData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragmentListener != null) {
                    fragmentListener.onAction(FUNC_ACCOUNT_MAIN_TO_DATA, null);
//                    member_information();
                }
            }
        });

        rPointTextView = rootView.findViewById(R.id.tv_R_Point);

        bnPacker = rootView.findViewById(R.id.item_packer);
        bnPacker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (fragmentListener != null) {
//                    fragmentListener.onAction(FUNC_ACCOUNT_COUPON_TO_COUPON_MAIN, null);
//                }

                Intent intent = new Intent(requireActivity(), CouponMainActivity.class);
                startActivity(intent);
                requireActivity();
            }
        });

        btnFix = rootView.findViewById(R.id.item_fix);
        btnFix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragmentListener != null) {
                    fragmentListener.onAction(FUNC_MEMBER_TO_FIX, null);
                }
            }
        });


        bnRecord = rootView.findViewById(R.id.item_record);
        bnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragmentListener != null) {
                    fragmentListener.onAction(FUNC_ACCOUNT_MAIN_TO_TRADE_MAIN, null);
                }
            }
        });
        bnPoint = rootView.findViewById(R.id.item_point);
        bnPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentListener != null) {
                    fragmentListener.onAction(FUNC_ACCOUNT_MAIN_TO_POINT, null);
                }
            }
        });
        bnUserRule = rootView.findViewById(R.id.item_rule);
        bnUserRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentListener != null) {

                    fragmentListener.onAction(FUNC_ACCOUNT_MAIN_TO_USERRULE, null);
                }
            }
        });
//        bnRecommend = rootView.findViewById(R.id.item_recommend);
//        bnRecommend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (fragmentListener != null) {
//                    fragmentListener.onAction(FUNC_ACCOUNT_MAIN_TO_RECOMMEND, null);
//                }
//            }
//        });

        bnQA = rootView.findViewById(R.id.item_qa);
        bnQA.setOnClickListener(v -> {
            if (fragmentListener != null) {

                fragmentListener.onAction(FUNC_ACCOUNT_MAIN_TO_QA, null);
            }
//            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
//            builder.setTitle("尚未開放");
//            builder.setMessage("敬請期待");
//            builder.setPositiveButton("確定", (dialog, which) -> {
//            });
//            builder.create().show();
//            if (fragmentListener != null) {
//                fragmentListener.onAction(FUNC_ACCOUNT_MAIN_TO_QA, null);
//            }
        });

        bnCS = rootView.findViewById(R.id.item_cs);
        bnCS.setOnClickListener(v -> {
            if (fragmentListener != null) {

                fragmentListener.onAction(FUNC_MEMBER_TO_CUSTOMER, null);
            }
//            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
//            builder.setTitle("尚未開放");
//            builder.setMessage("敬請期待");
//            builder.setPositiveButton("確定", (dialog, which) -> {
//            });
//            builder.create().show();
        });

//        bnLogout = rootView.findViewById(R.id.bn_logout);
//        bnLogout.setOnClickListener(v -> {
//
//            SharedPreferences pref = requireActivity().getSharedPreferences("loginInfo", MODE_PRIVATE);
//            SharedPreferences.Editor editor = pref.edit();
//            editor.clear();
//            editor.commit();
//            Intent intent = new Intent(requireActivity(), LoginMain.class);
//            startActivity(intent);
//            requireActivity().finish();
//
//        });
        vwUserCard = rootView.findViewById(R.id.cv_user);
        vwUserCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentListener != null) {
                    fragmentListener.onAction(FUNC_ACCOUNT_MAIN_USER_HEAD_CLICKED, null);
                }
            }
        });
        vwHeadImage = rootView.findViewById(R.id.iv_head);
        vwHeadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImage();
            }
        });

        txtUserName = rootView.findViewById(R.id.tv_User_name);
        memberInfor();
        handleData();

    }


    private void setImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("請選擇圖片來源")
                .setItems(R.array.choose_images_from, (dialog, which) ->
                {

                    if (which == 0) {

                        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);

                        } else {
                            requestCamera();
                        }


                    } else if (which == 1) {

                        if (ActivityCompat.checkSelfPermission(getActivity(), READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(getActivity(), new String[]{READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);

                        } else {
                            requestPick();
                        }
                    }
                });
        builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create();
        builder.show();
    }


    private void requestCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);

    }

    private void requestPick() {
        final Uri mUri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        final Intent mIntent = new Intent(Intent.ACTION_PICK, mUri);
        final PackageManager mPackageManager = getActivity().getPackageManager();
        List<ResolveInfo> list = mPackageManager.queryIntentActivities(mIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 1) {
            startActivityForResult(
                    Intent.createChooser(
                            new Intent(Intent.ACTION_PICK, mUri), "選取圖片"
                    ),
                    REQUEST_SELECT_VIDEO
            );
        } else {
            startActivityForResult(mIntent, REQUEST_SELECT_VIDEO);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_SELECT_VIDEO) {
                // ...
                // 在處理檔案讀取時，缺少 android.permission.READ_EXTERNAL_STORAGE 會造成 IOException:
                //  open failed: EACCES (Permission denied)
                // ...

                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                    storeImage(selectedImage);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            } else if (requestCode == REQUEST_CAMERA) {

                Bitmap photo = data.getParcelableExtra("data");
                storeImage(photo);
            }
        }
    }

    private void storeImage(Bitmap image) {
        int width = image.getWidth(), height = image.getHeight();

        // 將圖檔等比例縮小至寬度為
        final int MAX_WIDTH = 256;

        float resize = 1; // 縮小值 resize 可為任意小數
        if (width > height) {
            //landscape
            resize = ((float) MAX_WIDTH) / width;
        } else {
            //portrait
            resize = ((float) MAX_WIDTH) / height;
        }

        int nWidth = (int) (width * resize);
        int nHeight = (int) (height * resize);

        tempImage = Bitmap.createScaledBitmap(image, nWidth, nHeight, true);

//        Log.e(TAG,"new Width = " + tempImage.getWidth());
//        Log.e(TAG,"new Height = " + tempImage.getHeight());

        vwHeadImage.setImageBitmap(tempImage);
        uploadImage();
    }

    private void uploadImage() {
        if (tempImage == null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String message = "資料更新成功";
                    AppUtility.showMyDialog(getActivity(), message, "確定", null, new AppUtility.OnBtnClickListener() {
                        @Override
                        public void onCheck() {
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
            });
            return;
        }

        File filesDir = getActivity().getFilesDir();
        File cmdImageFile = new File(filesDir, "image" + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(cmdImageFile);
            tempImage.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        apiEnqueue.uploadimage(cmdImageFile, new ApiEnqueue.resultListener() {
            @Override
            public void onSuccess(String message) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "圖片已上傳成功", Toast.LENGTH_LONG).show();
                        Log.d("會員大頭貼", "照片更新成功");
                    }
                });

            }

            @Override
            public void onFailure(String message) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Toast.makeText(getActivity(), "圖片已上傳失敗，請重試", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void handleData() {
        apiEnqueue.getPersonalData(new ApiEnqueue.resultListener() {
            @Override
            public void onSuccess(String message) {
                requireActivity().runOnUiThread(() -> {
                    try {
                        JSONObject jsonObject = new JSONObject(message);
                        MemberBean.name = jsonObject.getString("name");
                        Log.d(TAG, "MemberBean.name: " + MemberBean.name);
                        MemberBean.point = jsonObject.getString("point");
                        rPointTextView.setText(MemberBean.point);
                        MemberBean.cmdImageFile = jsonObject.getString("cmdImageFile");
                        Log.d(TAG, "MemberBean.cmdImageFile: " + MemberBean.cmdImageFile);

                        Picasso.get().load(ApiUrl.MEMBER_IMG_URL + MemberBean.cmdImageFile)
                                .placeholder(R.drawable.default_head)
                                .into(vwHeadImage);

                        txtUserName.setText("Hi~ " + MemberBean.name);
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


    private void savaTermsStatus(boolean status, String code) {
        SharedPreferences pref = requireActivity().getSharedPreferences("UserTerms", MODE_PRIVATE);
        pref.edit()
                .putBoolean("Terms", status)
                .putString("code", code)
                .commit();
    }

    private void updateHeadPhoto() {
        Picasso.get().load(ApiUrl.MEMBER_IMG_URL + MemberBean.cmdImageFile)
                .into(vwHeadImage);
    }

//    private void UserPic(File cmdImageFile) {
//        apiEnqueue.uploadimage(cmdImageFile, new ApiEnqueue.resultListener() {
//
//            @Override
//            public void onSuccess(String message) {
//                Log.d("會員大頭貼", "照片更新成功");
//            }
//
//            @Override
//            public void onFailure(String message) {
//
//            }
//        });
//
//    }

    // 27.綠悠遊商城/租車取得會員詳細資料
    private void memberInfor() {

        apiEnqueue.getPersonalData(new ApiEnqueue.resultListener() {
            @Override
            public void onSuccess(String message) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(message);
                            MemberBean.name = jsonObject.getString("name");
                            MemberBean.cmdImageFile = jsonObject.getString("cmdImageFile");
                            MemberBean.sex = jsonObject.getString("sex");
                            MemberBean.email = jsonObject.getString("email");
                            MemberBean.tel = jsonObject.getString("tel");
                            MemberBean.birthday = jsonObject.getString("birthday");
                            MemberBean.cmdImageFile = jsonObject.getString("cmdImageFile");

                            Log.d(TAG, "cmdImageFile: " + MemberBean.cmdImageFile);
                            Log.d(TAG, "MemberBean.name: " + MemberBean.name);


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

    private void savaLoginStatus(boolean status, String account, String pwd) {

        SharedPreferences pref = requireActivity().getSharedPreferences("LoginInfo", MODE_PRIVATE);
        pref.edit()
                .putBoolean("isLogin", status)
                .putString("member_id", account)
                .putString("password", pwd)
                .commit();
        Log.d("登出", "成功" + status + "/" + account + "/" + pwd);
    }


}
