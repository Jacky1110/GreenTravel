package com.jotangi.greentravel;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.jotangi.greentravel.PagerStore.NewPageAdapter;
import com.jotangi.greentravel.R;

import java.util.ArrayList;
import java.util.List;

public class CarFixAdapter extends RecyclerView.Adapter<CarFixAdapter.ViewHolder> {

    private String TAG = NewPageAdapter.class.getSimpleName() + "(TAG)";
    private List<CarFixModel> mData = new ArrayList<>();
    private ItemClickListener clickListener;
    private ItemClick itemClick;
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
            imgDown = itemView.findViewById(R.id.img_down);
            imgDown.setOnClickListener(this);
            conLayout = itemView.findViewById(R.id.cl_fixCar);
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

        public void bindView(int position, CarFixModel carFixModel) {

            if (carFixModel.isOpen) {
                imgDown.setImageDrawable(ContextCompat.getDrawable(imgDown.getContext(), R.drawable.ic_baseline_expand_less));
                conLayout.setVisibility(View.VISIBLE);
            } else {
                imgDown.setImageDrawable(ContextCompat.getDrawable(imgDown.getContext(), R.drawable.ic_baseline_expand_more));
                conLayout.setVisibility(View.GONE);
            }

            txtContent.setText(carFixModel.motorNo + " " + carFixModel.bookingDate + " " + carFixModel.duration);
            txtStoreName.setText(carFixModel.storeName);
            txtCarNumber.setText(carFixModel.motorNo);
            txtCarModel.setText(carFixModel.motorType);
            txtCarType.setText(carFixModel.fixType);
            txtCarCondition.setText(carFixModel.description);
            txtCarName.setText(carFixModel.name);
            txtCarPhone.setText(carFixModel.phone);

            imgDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d(TAG, "isOpen: " + carFixModel.isOpen);

                    if (carFixModel.isOpen) {

                        mData.get(position).isOpen = false;
                        imgDown.setImageDrawable(ContextCompat.getDrawable(imgDown.getContext(), R.drawable.ic_baseline_expand_more));
                        Log.d(TAG, "GONE: ");
                        conLayout.setVisibility(View.GONE);
                        notifyItemChanged(position);
                        itemClick.onItemClick(position);

                    } else {

                        mData.get(position).isOpen = true;
                        imgDown.setImageDrawable(ContextCompat.getDrawable(imgDown.getContext(), R.drawable.ic_baseline_expand_less));
                        Log.d(TAG, "VISIBLE: ");
                        conLayout.setVisibility(View.VISIBLE);
                        notifyItemChanged(position);
                        itemClick.onItemClick(position);
                    }
                }
            });


            if (carFixModel.cancel.equals("0") && carFixModel.canCancel.equals("1")) {
                btnReserve.setText("取消預約");
                btnReserve.setBackgroundColor(btnReserve.getContext().getResources().getColor(R.color.orangeButton));
                btnReserve.setEnabled(true);
                btnReserve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickListener.onItemClick(position);
                    }
                });

            }
            if (carFixModel.cancel.equals("1") && carFixModel.canCancel.equals("0")) {
                btnReserve.setText("已取消預約");
                btnReserve.setBackgroundColor(btnReserve.getContext().getResources().getColor(R.color.lightGray));
                btnReserve.setEnabled(false);
            }
            if (carFixModel.cancel.equals("1") && carFixModel.canCancel.equals("1")) {
                btnReserve.setText("已取消預約");
                btnReserve.setBackgroundColor(btnReserve.getContext().getResources().getColor(R.color.lightGray));
                btnReserve.setEnabled(false);
            }
            if (carFixModel.cancel.equals("0") && carFixModel.canCancel.equals("0")) {
                btnReserve.setText("取消預約");
                btnReserve.setBackgroundColor(btnReserve.getContext().getResources().getColor(R.color.lightGray));
                btnReserve.setEnabled(false);
            }
        }
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public interface ItemClick {
        void onItemClick(int position);
    }

    public void setClickListener(ItemClickListener listener) {
        this.clickListener = listener;
    }

    public void setClick(ItemClick listener) {
        this.itemClick = listener;
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


        holder.bindView(position, mData.get(position));

    }


    public void setData(List<CarFixModel> data) {
        mData = data;
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

    Boolean isOpen = false;


}
