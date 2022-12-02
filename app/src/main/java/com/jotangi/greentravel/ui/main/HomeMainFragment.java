package com.jotangi.greentravel.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jotangi.greentravel.Api.ApiEnqueue;
import com.jotangi.greentravel.BannerListBean;
import com.jotangi.greentravel.ui.hPayMall.DynamicTabFragment;
import com.jotangi.greentravel.ui.store.HotelIntroduceFragment;
import com.jotangi.greentravel.Api.ApiUrl;
import com.jotangi.greentravel.PagerCommodityFragment;
import com.jotangi.greentravel.ProjConstraintFragment;
import com.jotangi.greentravel.R;
import com.jotangi.greentravel.ui.store.StoreTabFragment;
import com.squareup.picasso.Picasso;
import com.stx.xhb.xbanner.XBanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;


public class HomeMainFragment extends ProjConstraintFragment implements View.OnClickListener {
    ArrayList<HomePageModel> data = new ArrayList<>();
    private String TAG = getClass().getSimpleName() + "(TAG)";
    private ApiEnqueue apiEnqueue;
    private RecyclerView recyView;
    private RecyclerView categoryRecyclerView;
    private XBanner mXBanner;
    private ArrayList<BannerListBean> bannerListBeans;
    private ArrayList<ProductBean> productBeans;
    private ProductBean productData;
    private BannerListBean bannerData;
    private HomePageAdapter adapter;
    private ProgressBar progressBar;
    private Intent intent;
    private Uri url;

    private View NewCar, OldCar, FixCar, CarLease, Boutique;
    private CategoryAdapter categoryAdapter;
    private ArrayList<String> categoryList = new ArrayList(Arrays.asList(
            "新車",
            "二手車",
            "維修保養",
            "機車租賃",
            "精品配件"
    ));
    private HomePageAdapter.ItemClickListener click = (view, position) -> {
        Log.d(TAG, "position: " + position);

        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_activity_main,
                PagerCommodityFragment.newInstance(
                        data.get(position).id,
                        data.get(position).name,
                        data.get(position).pric,
                        data.get(position).pic,
                        data.get(position).description,
                        data.get(position).product_stock));
        transaction.addToBackStack(null);
        transaction.commit();
    };

    public static HomeMainFragment newInstance() {
        HomeMainFragment fragment = new HomeMainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        activityTitleRid = R.string.account_listitem_front;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ConstraintLayout) inflater.inflate(R.layout.fragment_home, container, false);

        apiEnqueue = new ApiEnqueue();
        return rootView;
    }

    @Override
    protected void initViews() {
        super.initViews();

        mXBanner = rootView.findViewById(R.id.banner);
        progressBar = rootView.findViewById(R.id.progressBar);

        NewCar = rootView.findViewById(R.id.v_NewCar);
        NewCar.setOnClickListener(this);
        OldCar = rootView.findViewById(R.id.v_OldCar);
        OldCar.setOnClickListener(this);
        FixCar = rootView.findViewById(R.id.v_FixCar);
        FixCar.setOnClickListener(this);
        CarLease = rootView.findViewById(R.id.v_CarLease);
        CarLease.setOnClickListener(this);
        Boutique = rootView.findViewById(R.id.v_Boutique);
        Boutique.setOnClickListener(this);


        recyView = rootView.findViewById(R.id.home_RecyView);
        recyView.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
//        initCategoryRecyclerView();

        apiStoreList();
    }

//    private void initCategoryRecyclerView() {
//        categoryRecyclerView = rootView.findViewById(R.id.category_recyclerView);
//        categoryAdapter = new CategoryAdapter(
//                getContext(),
//                categoryList
//        );
//        categoryRecyclerView.setAdapter(categoryAdapter);
//    }

    // 17.商店列表
    private void apiStoreList() {
        progressBar.setVisibility(View.VISIBLE);
        apiEnqueue.bannerList(new ApiEnqueue.resultListener() {
            @Override
            public void onSuccess(String message) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);

//                ArrayList<String> storePictureList = new ArrayList<>();
                        bannerListBeans = new ArrayList<BannerListBean>();

                        try {
                            JSONArray jsonArray = new JSONArray(message);
                            JSONObject jsonObject;

                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject = (JSONObject) jsonArray.get(i);
                                bannerData = new BannerListBean();
                                bannerData.setBanner_picture(jsonObject.getString("banner_picture"));
                                bannerData.setBanner_link(jsonObject.getString("banner_link"));
                                bannerListBeans.add(bannerData);
                            }

                            setStoreList(bannerListBeans);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(String message) {

            }
        });

    }

    private void setStoreList(ArrayList<BannerListBean> data) {

        mXBanner.setData(R.layout.fragment_store_item, bannerListBeans, null);
        mXBanner.loadImage(new XBanner.XBannerAdapter() {
            @Override
            public void loadBanner(XBanner banner, Object model, View view, int position) {

                ImageView img_carousel = view.findViewById(R.id.iv_store);
                Picasso.get().load(ApiUrl.API_URL + "ticketec/" + data.get(position).getBanner_picture()).into(img_carousel);
            }
        });
        mXBanner.setOnItemClickListener(new XBanner.OnItemClickListener() {
            @Override
            public void onItemClick(XBanner banner, Object model, View view, int position) {
                if (!data.get(position).getBanner_link().isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri uri = Uri.parse(data.get(position).getBanner_link()); //拿官網測試用
                    intent.setData(uri);
                    startActivity(intent);
                }
//                HotelIntroduceFragment fragment = new HotelIntroduceFragment();
//                Bundle args = new Bundle();
//                args.putString("store_name", data.get(position).getBanner_storeName());
//                args.putString("store_picture", data.get(position).getBanner_picture());
//                args.putString("store_id", data.get(position).getBanner_link());
//                args.putString("store_opentime", data.get(position).getBanner_openTime());
//                args.putString("store_phone", data.get(position).getBanner_telephone());
//                args.putString("store_descript", data.get(position).getBanner_hotelIntroduction());
//                args.putString("store_fixmotor", data.get(position).getBanner_fixmotor());
//                fragment.setArguments(args);
//                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.nav_host_fragment_activity_main, fragment);
//                transaction.addToBackStack(null);
//                transaction.commit();

            }
        });


//        List<Fragment> listSP = new ArrayList<>();
//
//        for (int i = 0; i < list.size(); i++) {
//            listSP.add(new StoreItemFragment(list.get(i)));
//        }

//        PagerAdapter pagerAdapter = new SlidePagerAdapter(
//                requireActivity().getSupportFragmentManager(), listSP
//        );


//        requireActivity().runOnUiThread(() -> {
//            viewPager.setAdapter(pagerAdapter);
//
//            viewPager.setOnClickListener(view -> {
//                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.nav_host_fragment_activity_main,new HotelIntroduceFragment());
//                transaction.addToBackStack(null);
//                transaction.commit();
//            });
//        });

        apiTicketList();
    }

    // 6.商城套票列表
    private void apiTicketList() {

        progressBar.setVisibility(View.VISIBLE);

        apiEnqueue.package_list(new ApiEnqueue.resultListener() {
            @Override
            public void onSuccess(String message) {
                requireActivity().runOnUiThread(() -> {

                    progressBar.setVisibility(View.GONE);

                    data = new ArrayList();

                    try {
                        JSONArray jsonArray = new JSONArray(message);
                        Log.d(TAG, "jsonArray: " + jsonArray);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            HomePageModel model = new HomePageModel();
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            model.name = jsonObject.getString("product_name");
                            model.pric = jsonObject.getString("product_price");
                            model.id = jsonObject.getString("package_no");
                            model.pic = jsonObject.getString("product_picture");
                            model.description = jsonObject.getString("product_description");
                            model.product_stock = jsonObject.getString("product_stock");
                            data.add(model);
                        }

                        getActivity().runOnUiThread(() -> {
                            adapter = new HomePageAdapter();
                            adapter.setmData(data);
                            recyView.setAdapter(adapter);
                            adapter.setClickListener(click);

                        });

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



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.v_NewCar:
                intent = new Intent(Intent.ACTION_VIEW);
                url = Uri.parse("https://www.hsinhungchia.com/brand-type/");
                intent.setData(url);
                startActivity(intent);
                break;
            case R.id.v_OldCar:
                intent = new Intent(Intent.ACTION_VIEW);
                url = Uri.parse("https://www.facebook.com/rilinkiscooter/shop/?referral_code=page_shop_tab&preview=1");
                intent.setData(url);
                startActivity(intent);
                break;
            case R.id.v_FixCar:
                StoreTabFragment fragment1 = new StoreTabFragment();
                Bundle bundle = new Bundle();
                bundle.putString("Name", "購車維修保養");
                fragment1.setArguments(bundle);
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_activity_main, fragment1);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.v_CarLease:
                DynamicTabFragment fragment2 = new DynamicTabFragment();
                Bundle args = new Bundle();
                args.putString("Name", "i租車");
                args.putString("type", "Rent");
//                args.putString("type", "03");
                fragment2.setArguments(args);
                FragmentTransaction transaction1 = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction1.replace(R.id.nav_host_fragment_activity_main, fragment2);
                transaction1.addToBackStack(null);
                transaction1.commit();
//                intent = new Intent(Intent.ACTION_VIEW);
//                url = Uri.parse("https://rilink.shopstore.tw/category/%E7%A7%9F%E8%BB%8A%E6%9C%8D%E5%8B%99");
//                intent.setData(url);
//                startActivity(intent);
                break;
            case R.id.v_Boutique:
                DynamicTabFragment fragment3 = new DynamicTabFragment();
                Bundle arg = new Bundle();
                arg.putString("Name", "改裝精品配件");
                arg.putString("type", "SC001");
//                arg.putString("type", "02");
                fragment3.setArguments(arg);
                FragmentTransaction transaction3 = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction3.replace(R.id.nav_host_fragment_activity_main, fragment3);
                transaction3.addToBackStack(null);
                transaction3.commit();
//                intent = new Intent(Intent.ACTION_VIEW);
//                url = Uri.parse("https://rilink.shopstore.tw/category/%E9%85%8D%E4%BB%B6");
//                intent.setData(url);
//                startActivity(intent);
                break;

        }
    }


}
