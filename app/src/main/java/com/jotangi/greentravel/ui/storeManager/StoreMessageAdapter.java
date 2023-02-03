package com.jotangi.greentravel.ui.storeManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jotangi.greentravel.CarFixAdapter;
import com.jotangi.greentravel.R;

import java.util.ArrayList;
import java.util.List;

public class StoreMessageAdapter extends RecyclerView.Adapter<StoreMessageAdapter.ViewHolder> {

    private List<MessageModel> mData = new ArrayList<>();
    private CarFixAdapter.ItemClickListener clickListener;


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtTitle, txtDate, txtContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txt_message);
            txtDate = itemView.findViewById(R.id.tv_date);
            txtContent = itemView.findViewById(R.id.tv_content);

        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onItemClick(getAdapterPosition());
            }
        }
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public void setClickListener(CarFixAdapter.ItemClickListener listener) {
        this.clickListener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_merch_message, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.txtTitle.setText(mData.get(position).title);
        holder.txtDate.setText(mData.get(position).date);
        holder.txtContent.setText(mData.get(position).content);

    }

    public void setData(List<MessageModel> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}

class MessageModel {

    String title = "";

    String date = "";

    String content = "";
}
