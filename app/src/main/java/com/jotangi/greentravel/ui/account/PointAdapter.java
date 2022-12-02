package com.jotangi.greentravel.ui.account;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jotangi.greentravel.PagerStore.PagerAdapter;
import com.jotangi.greentravel.R;

import java.util.ArrayList;
import java.util.List;


public class PointAdapter extends RecyclerView.Adapter<PointAdapter.ViewHolder> {

    private String TAG = PointAdapter.class.getSimpleName() + "(TAG)";
    private List<PointModel> mData = new ArrayList<>();
    private PagerAdapter.ItemClickListener clickListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvName, tvTime, tvPoint;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_Name);
            tvTime = itemView.findViewById(R.id.tv_PointTime);
            tvPoint = itemView.findViewById(R.id.tv_UsePoint);
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
    public PointAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_point, parent, false);
        return new PointAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PointAdapter.ViewHolder holder, int position) {
        holder.tvName.setText(mData.get(position).name);
        holder.tvTime.setText(mData.get(position).time);
        holder.tvPoint.setText(mData.get(position).point);
    }

    public void setmData(List<PointModel> data) {
        mData = data;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }
}

class PointModel {
    String name = "";
    String time = "";
    String point = "";
}
