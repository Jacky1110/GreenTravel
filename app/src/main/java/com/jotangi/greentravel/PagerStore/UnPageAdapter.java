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
        Picasso.get().load(ApiUrl.API_URL + "ticketec/" + mData.get(position).pic).into(holder.pic);
        holder.Id.setText(mData.get(position).id);
        holder.Name.setText(mData.get(position).name);
        holder.Day.setText(mData.get(position).day);

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
}


