package com.jotangi.greentravel.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.jotangi.greentravel.MyBaseFragment;
import com.jotangi.greentravel.ProjBaseFragment;
import com.jotangi.greentravel.R;

import java.util.ArrayList;
import java.util.List;

public class AccountCouponAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CouponModel> mData = new ArrayList<>();
    private final int VIEW_TYPE_NORMAL = 10;
    private final int VIEW_TYPE_EMPTY = 11;
    MyBaseFragment.FragmentListener myListener;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==VIEW_TYPE_NORMAL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coupon, parent, false);
            return new CouponViewHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty, parent, false);
            return new EmptyViewHolder(view);
        }
    }

    public void setFragmentListener(MyBaseFragment.FragmentListener fragmentListener){
        myListener = fragmentListener;
    }

    public void setData(List<CouponModel> data){
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CouponViewHolder) {
            ((CouponViewHolder) holder).bind(mData.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if(mData.size()==0) return 1;
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(mData.size()==0) return VIEW_TYPE_EMPTY;
        else return VIEW_TYPE_NORMAL;
    }

    class CouponViewHolder extends RecyclerView.ViewHolder{
        ImageView img_coupon;
        public CouponViewHolder(@NonNull View itemView) {
            super(itemView);
//            img_coupon = itemView.findViewById(R.id.img_coupon);
        }

        void bind(CouponModel model){
            img_coupon.setOnClickListener(view -> {
                if(myListener != null){
                    Bundle data = new Bundle();
                    data.putString("id",model.id);
                    data.putString("url",model.url);
                    data.putString("title",model.title);
                    data.putString("content",model.content);
                    myListener.onAction(ProjBaseFragment.FUNC_ACCOUNT_COUPON_TO_COUPON_DETAIL,data);
                }
            });
        }
    }

    class EmptyViewHolder extends RecyclerView.ViewHolder{

        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}

class CouponModel{
    String url = "";
    String title = "";
    String content = "";
    String id="";
}

