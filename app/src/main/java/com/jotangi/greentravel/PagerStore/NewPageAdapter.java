package com.jotangi.greentravel.PagerStore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jotangi.greentravel.Api.ApiUrl;
import com.jotangi.greentravel.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NewPageAdapter extends RecyclerView.Adapter<NewPageAdapter.ViewHolder> {

    private String TAG = NewPageAdapter.class.getSimpleName() + "(TAG)";
    private List<NewCouponModel> mData = new ArrayList<>();
    private ItemClickListener clickListener;


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView iconIV;
        TextView productNameTV, storeNameTV, mathTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iconIV = itemView.findViewById(R.id.iv_item_icon);
            productNameTV = itemView.findViewById(R.id.tv_commodity_name);
            storeNameTV = itemView.findViewById(R.id.tv_storeName);
            mathTV = itemView.findViewById(R.id.tv_math);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setClickListener(ItemClickListener listener) {
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_coupon, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(
                ApiUrl.API_URL + "ticketec/" + mData.get(position).product_picture
        ).into(holder.iconIV);
        holder.productNameTV.setText(mData.get(position).productName);
        holder.storeNameTV.setText("店家名稱" + mData.get(position).storeName);
        holder.mathTV.setText("商品數量" + mData.get(position).math);
    }

    public void setData(List<NewCouponModel> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}

class NewCouponModel {

    // 圖片網址
    String product_picture = "";

    // 產品名稱
    String productName = "";

    // QR code
    String qrconfirm = "";

    // 商店名稱
    String storeName = "";

    // 數量
    String math = "";
}
