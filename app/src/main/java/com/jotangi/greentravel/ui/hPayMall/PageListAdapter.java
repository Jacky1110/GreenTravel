package com.jotangi.greentravel.ui.hPayMall;

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


public class PageListAdapter extends RecyclerView.Adapter<PageListAdapter.ViewHolder> {

    private String TAG = PageListAdapter.class.getSimpleName() + "(TAG)";
    private List<PageListModel> mData = new ArrayList<>();
    private ItemClickListener clickListener;


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView pic;
        TextView Name, Pric;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.pagName);
            Pric = itemView.findViewById(R.id.pagPric);
            pic = itemView.findViewById(R.id.pd_iv);
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
    public PageListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_store_page, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PageListAdapter.ViewHolder holder, int position) {
        holder.Name.setText(mData.get(position).name);
        holder.Pric.setText(mData.get(position).pric);
        Picasso.get().load(ApiUrl.API_URL + "ticketec/" + mData.get(position).pic).into(holder.pic);

    }

    public void setmData(List<PageListModel> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void setClickListener(ItemClickListener listener) {
        this.clickListener = listener;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}

class PageListModel{
    String name = "";
    String pric = "";
    String pic = "";
    String id = "";
    String description = "";

}