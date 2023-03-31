package com.jotangi.greentravel.PagerStore;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jotangi.greentravel.Api.ApiUrl;
import com.jotangi.greentravel.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UnPageAdapter extends RecyclerView.Adapter<UnPageAdapter.ViewHolder> {

    private String TAG = PagerAdapter.class.getSimpleName() + "(TAG)";
    private List<UnCouponModel> mData = new ArrayList<>();
    private ItemClickListener clickListener;


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView pic;
        TextView Id, Name, Day, math;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.sdIg2);
            Id = itemView.findViewById(R.id.ex_id);
            Name = itemView.findViewById(R.id.ex_pager);
            Day = itemView.findViewById(R.id.ex_day);
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


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_nouse, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String imagerUrl = mData.get(position).pic;
        holder.Name.setText(mData.get(position).name);
        if (!mData.get(position).couponId.isEmpty()) {
            Log.d(TAG, "imagerUrl123: " + mData.get(position).pic);
            Glide.with(holder.itemView)
                    .load(imagerUrl)
                    .into(holder.pic);

//            Picasso.get()
//                    .load(mData.get(position).pic)
//                    .resize(100, 0)
//                    .into(holder.pic);
            holder.Day.setText("有效期限：" + mData.get(position).day);
            holder.Id.setVisibility(View.INVISIBLE);


        } else {
            Log.d(TAG, "imagerUrl: " + mData.get(position).pic);
            Picasso.get().load(ApiUrl.API_URL + "ticketec/" + mData.get(position).pic).into(holder.pic);
            holder.Day.setText("購買日期：" + mData.get(position).day);
            holder.Id.setText("訂單編號：" + mData.get(position).id);
        }


    }

    public void setmData(List<UnCouponModel> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}

class UnCouponModel {
    String pic = "";

    String name = "";

    String day = "";

    String id = "";

    String product = "";

    String couponId = "";

    String couponDescription = "";
}


