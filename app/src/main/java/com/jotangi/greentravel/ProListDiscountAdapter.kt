package com.jotangi.greentravel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.greentravel.Api.ApiUrl
import com.squareup.picasso.Picasso

/**
 *
 * @Title: ProListDiscountAdapter.kt
 * @Package com.jotangi.healthy.HpayMall
 * @Description: ProListDiscountAdapter
 * @author Kelly
 * @date 2021-12
 * @version hpay_34版
 */
class ProListDiscountAdapter(private val mData2: List<Product_list?>) :
    RecyclerView.Adapter<ProListDiscountAdapter.myViewHolder>() {
    var disClick: (Product_list) -> Unit = {}

    override fun getItemCount(): Int {
        return mData2.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProListDiscountAdapter.myViewHolder {
        return myViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_commodity_discount, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProListDiscountAdapter.myViewHolder, position: Int) {
        val data = mData2[position]
        holder.apply {
            val ProUrl = ApiUrl.API_URL
            mallName.text = data?.product_name
            mallPrice.text = data?.product_price
            product_no = data?.product_no.toString()
            product_picture = data?.product_picture.toString()
//
            Picasso.get().load(ProUrl+"ticketec/" + data?.product_picture).into(pic)
            itemView.setOnClickListener {

                //notifyDataSetChanged()
                data?.let { it1 ->disClick.invoke(it1) }
            }
        }

    }


    inner class myViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var mallName: TextView = v.findViewById(R.id.mallName)
        var mallPrice: TextView = v.findViewById(R.id.mallPrice)

        var pic: ImageView = v.findViewById(R.id.mallDis)
        lateinit var product_no: String
        lateinit var product_picture: String

    }


}