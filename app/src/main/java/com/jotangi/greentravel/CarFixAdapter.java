package com.jotangi.greentravel;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.jotangi.greentravel.PagerStore.NewPageAdapter;
import com.jotangi.greentravel.R;

import java.util.ArrayList;
import java.util.List;

public class CarFixAdapter extends RecyclerView.Adapter<CarFixAdapter.ViewHolder> {

    private String TAG = NewPageAdapter.class.getSimpleName() + "(TAG)";
    private List<CarFixModel> mData = new ArrayList<>();
    private ItemClickListener clickListener;
    private int opened = -1;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtContent, txtStoreName, txtCarNumber, txtCarModel, txtCarType, txtCarCondition, txtCarName, txtCarPhone;
        Button btnReserve;
        ImageView imgUp, imgDown;
        ConstraintLayout conLayout, llItem;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtContent = itemView.findViewById(R.id.tv_content);
            txtStoreName = itemView.findViewById(R.id.tv_storeName);
            txtCarNumber = itemView.findViewById(R.id.tv_carNumber);
            txtCarModel = itemView.findViewById(R.id.tv_carModel);
            txtCarType = itemView.findViewById(R.id.tv_carType);
            txtCarCondition = itemView.findViewById(R.id.tv_carCondition);
            txtCarName = itemView.findViewById(R.id.tv_carName);
            txtCarPhone = itemView.findViewById(R.id.tv_carPhone);
            btnReserve = itemView.findViewById(R.id.btn_Reserve);
            imgUp = itemView.findViewById(R.id.img_up);
            imgUp.setVisibility(View.VISIBLE);
            imgUp.setOnClickListener(this);
            imgDown = itemView.findViewById(R.id.img_down);
            imgDown.setOnClickListener(this);
            conLayout = itemView.findViewById(R.id.constraintLayout);
            llItem = itemView.findViewById(R.id.ll_item);

        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onItemClick(getAdapterPosition());
            }
//            if (opened == getAdapterPosition()) {
//                //当点击的item已经被展开了, 就关闭.
//                opened = -1;
//                imgUp.setVisibility(View.INVISIBLE);
//                imgDown.setVisibility(View.VISIBLE);
//                notifyItemChanged(getAdapterPosition());
//            } else {
//                int oldOpened = opened;
//                imgUp.setVisibility(View.VISIBLE);
//                imgDown.setVisibility(View.INVISIBLE);
//                opened = getAdapterPosition();
//                notifyItemChanged(oldOpened);
//                notifyItemChanged(opened);
//            }
        }

        public void bindView(int pos, CarFixModel carFixModel) {
            if (pos == opened) {
                conLayout.setVisibility(View.VISIBLE);
            } else {
                conLayout.setVisibility(View.GONE);
            }
        }
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public void setClickListener(ItemClickListener listener) {
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public CarFixAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_fix_car, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarFixAdapter.ViewHolder holder, int position) {
        holder.txtContent.setText(mData.get(position).motorNo + " " + mData.get(position).bookingDate + " " + mData.get(position).duration);
        holder.txtStoreName.setText(mData.get(position).storeName);
        holder.txtCarNumber.setText(mData.get(position).motorNo);
        holder.txtCarModel.setText(mData.get(position).motorType);
        holder.txtCarType.setText(mData.get(position).fixType);
        holder.txtCarCondition.setText(mData.get(position).description);
        holder.txtCarName.setText(mData.get(position).name);
        holder.txtCarPhone.setText(mData.get(position).phone);
//        holder.conLayout.setVisibility(View.GONE);
        holder.bindView(position, mData.get(position));

        holder.imgUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (opened == position) {
                    //当点击的item已经被展开了, 就关闭.
                    opened = -1;
                    holder.imgUp.setVisibility(View.INVISIBLE);
                    holder.imgDown.setVisibility(View.VISIBLE);
                    notifyItemChanged(position);
                } else {
                    int oldOpened = opened;
                    holder.imgUp.setVisibility(View.VISIBLE);
                    holder.imgDown.setVisibility(View.INVISIBLE);
                    opened = position;
                    notifyItemChanged(oldOpened);
                    notifyItemChanged(opened);
                }
            }
        });

        holder.imgDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (opened == position) {
                    //当点击的item已经被展开了, 就关闭.
                    opened = -1;
                    holder.imgUp.setVisibility(View.INVISIBLE);
                    holder.imgDown.setVisibility(View.VISIBLE);
                    notifyItemChanged(position);
                } else {
                    int oldOpened = opened;
                    holder.imgUp.setVisibility(View.VISIBLE);
                    holder.imgDown.setVisibility(View.INVISIBLE);
                    opened = position;
                    notifyItemChanged(oldOpened);
                    notifyItemChanged(opened);
                }
            }
        });


        if (mData.get(position).cancel.equals("0") && mData.get(position).canCancel.equals("1")) {
            holder.btnReserve.setText("取消預約");
            holder.btnReserve.setBackgroundColor(holder.btnReserve.getContext().getResources().getColor(R.color.orangeButton));
            holder.btnReserve.setEnabled(true);
            holder.btnReserve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(position);
                }
            });

        }
        if (mData.get(position).cancel.equals("1") && mData.get(position).canCancel.equals("0")) {
            holder.btnReserve.setText("已取消預約");
            holder.btnReserve.setBackgroundColor(holder.btnReserve.getContext().getResources().getColor(R.color.lightGray));
            holder.btnReserve.setEnabled(false);
        }
        if (mData.get(position).cancel.equals("1") && mData.get(position).canCancel.equals("1")) {
            holder.btnReserve.setText("已取消預約");
            holder.btnReserve.setBackgroundColor(holder.btnReserve.getContext().getResources().getColor(R.color.lightGray));
            holder.btnReserve.setEnabled(false);
        }
        if (mData.get(position).cancel.equals("0") && mData.get(position).canCancel.equals("0")) {
            holder.btnReserve.setText("取消預約");
            holder.btnReserve.setBackgroundColor(holder.btnReserve.getContext().getResources().getColor(R.color.lightGray));
            holder.btnReserve.setEnabled(false);
        }

    }


    public void setData(List<CarFixModel> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}

class CarFixModel {

    // 是否有被取消
    String cancel = "";
    // 預約編號
    String bid = "";
    // 預約姓名
    String name = "";
    // 預約電話
    String phone = "";
    // 車牌號碼
    String motorNo = "";
    // 車型
    String motorType = "";
    // 預約類型
    String fixType = "";
    // 簡述車況
    String description = "";
    // 店家名稱
    String storeName = "";
    // 預約時間
    String duration = "";
    // 預約日期
    String bookingDate = "";
    // 是否可被取消
    String canCancel = "";


}
