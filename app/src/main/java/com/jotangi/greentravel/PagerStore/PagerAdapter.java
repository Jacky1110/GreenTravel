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

public class PagerAdapter extends RecyclerView.Adapter<PagerAdapter.ViewHolder> {

    private String TAG = PagerAdapter.class.getSimpleName() + "(TAG)";
    private List<CouponModel> mData = new ArrayList<>();
    private ItemClickListener clickListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iconIV;
        TextView commodityNameTV, buyDateTV, orderNoTV, mathTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iconIV = itemView.findViewById(R.id.iv_item_icon);
            commodityNameTV = itemView.findViewById(R.id.tv_commodity_name);
            buyDateTV = itemView.findViewById(R.id.tv_buy_date);
            orderNoTV = itemView.findViewById(R.id.tv_order_no);
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
                .inflate(R.layout.item_use, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(
                ApiUrl.API_URL + "ticketec/" + mData.get(position).product_picture
        ).into(holder.iconIV);

        holder.commodityNameTV.setText(mData.get(position).product_name);
        if (mData.get(position).store_name.isEmpty()) {
            holder.buyDateTV.setText("購買日期: " + mData.get(position).order_date);
            holder.orderNoTV.setText("訂單編號: " + mData.get(position).order_no);
            holder.mathTV.setText("數量: " + mData.get(position).order_qty);
        } else {
            holder.orderNoTV.setText("店家名稱: " + mData.get(position).store_name);
            holder.buyDateTV.setVisibility(View.GONE);
        }if (mData.get(position).product.isEmpty()) {
            holder.buyDateTV.setText("購買日期: " + mData.get(position).order_date);
            holder.orderNoTV.setText("訂單編號: " + mData.get(position).order_no);
            holder.mathTV.setText("數量: " + mData.get(position).order_qty);
        } else{
            holder.buyDateTV.setText("購買日期: " + mData.get(position).order_date);
            holder.orderNoTV.setText("訂單編號: " + mData.get(position).order_no);
            holder.mathTV.setText("票券數量: " + mData.get(position).order_qty);

        }

    }

    public void setData(List<CouponModel> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}

class CouponModel {
    // 圖片網址
    String product_picture = "";
    // 產品名稱
    String product_name = "";
    // 購買日期
    String order_date = "";
    // 訂單編號
    String order_no = "";
    // QR code
    String qrconfirm = "";
    // 店家名稱
    String store_name = "";
    //
    String order_qty = "";

    String product = "";
}


