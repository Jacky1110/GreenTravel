package com.jotangi.greentravel.ui.main;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jotangi.greentravel.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    private final ImageView categoryTitleImageView;
    private final TextView categoryTitleTextView;

    public CategoryViewHolder(View view) {
        super(view);

        categoryTitleImageView = view.findViewById(R.id.category_icon_imageView);
        categoryTitleTextView = view.findViewById(R.id.category_name_textView);
    }

    public void onBind(String data) {
        updateImageView(data);

        categoryTitleTextView.setText(data);
    }

    private void updateImageView(String title) {
        switch (title) {
            case "新車":
                categoryTitleImageView.setImageResource(R.drawable.icon_new_car);
                break;
            case "二手車":
                categoryTitleImageView.setImageResource(R.drawable.icon_used_car);
                break;
            case "維修保養":
                categoryTitleImageView.setImageResource(R.drawable.icon_repair);
                break;
            case "機車租賃":
                categoryTitleImageView.setImageResource(R.drawable.icon_rent_car);
                break;
            case "精品配件":
                categoryTitleImageView.setImageResource(R.drawable.icon_accessory);
                break;
        }
    }
}
