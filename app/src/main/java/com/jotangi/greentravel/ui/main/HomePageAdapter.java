package com.jotangi.greentravel.ui.main;

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

public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.ViewHolder> {

    private String TAG = HomePageAdapter.class.getSimpleName() + "(TAG)";
    private List<HomePageModel> mData = new ArrayList<>();
    private ItemClickListener clickListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pager, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomePageAdapter.ViewHolder holder, int position) {
        String imgUrl = ApiUrl.API_URL + "ticketec/" + mData.get(position).pic;
        Picasso.get().load(imgUrl).into(holder.pic);
//        holder.Id.setText(mData.get(position).id);
        holder.productTitleTextView.setText(mData.get(position).name);
        String priceStr = "NT$ " + mData.get(position).pric;
        holder.productPriceTextView.setText(priceStr);
    }

    public void setmData(List<HomePageModel> data) {
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

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView pic;
        TextView productTitleTextView;
        TextView productPriceTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pic = itemView.findViewById(R.id.ivPag);
            productTitleTextView = itemView.findViewById(R.id.product_title_textView);
            productPriceTextView = itemView.findViewById(R.id.product_price_textView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

}

class HomePageModel {
    String pic = "";
    String name = "";
    String pric = "";
    String id = "";
    String description = "";
    String product_stock = "";
}
