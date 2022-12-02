package com.jotangi.greentravel.ui.store;

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


public class ChooseAdapter extends RecyclerView.Adapter<ChooseAdapter.ViewHolder> {

    private String TAG = PagerAdapter.class.getSimpleName() + "(TAG)";
    private List<ChooseModel> mData = new ArrayList<>();
    private ItemClickListener clickListener;


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTime, txtChange, txtFull;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTime = itemView.findViewById(R.id.txTime);
            txtChange = itemView.findViewById(R.id.tv_change);
            txtFull = itemView.findViewById(R.id.tv_full);
        }
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.clickListener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_appointment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int quota = Integer.parseInt(mData.get(position).quota);
        int used = Integer.parseInt(mData.get(position).used);
        int remaining = quota - used;

        holder.txtTime.setText(mData.get(position).time);

        if (remaining > 0) {
            holder.txtChange.setOnClickListener(view -> {
                clickListener.onItemClick(position);
            });
        }else {
            holder.txtChange.setVisibility(View.GONE);
            holder.txtFull.setVisibility(View.VISIBLE);
        }
    }

    public void setmData(List<ChooseModel> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}

class ChooseModel {

    String time = "";
    String quota = "";
    String used = "";
    String bookingdate = "";
}
