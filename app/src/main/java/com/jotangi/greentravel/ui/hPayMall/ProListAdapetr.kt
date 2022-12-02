package com.jotangi.greentravel

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.jotangi.greentravel.Api.ApiUrl


/**
 *
 * @Title: ProListAdapter.kt
 * @Package com.jotangi.healthy.HpayMall
 * @Description: ProListAdapter
 * @author Kelly
 * @date 2021-12
 * @version hpay_34版
 */
//val mData: List<Product_list?>
class ProListAdapter :
    RecyclerView.Adapter<ProListAdapter.myViewHolder>() {

    val TAG : String? = "ProListAdapter(TAG)"
    var dataSource: List<Product_list> = listOf()
    var WatchItemClick: (Product_list) -> Unit = {}

    override fun getItemCount(): Int {
        return dataSource.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProListAdapter.myViewHolder {
        return myViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_pointstore_order, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProListAdapter.myViewHolder, position: Int) {
        val data = dataSource[position]
        holder.apply {
            val ProUrl = ApiUrl.API_URL
            Log.d(TAG, "ProUrl: ${ProUrl}")
            product_name.text = data?.product_name
            Log.d(TAG, "data?.product_name: ${data?.product_name}")
            product_price.text = "NT$：${data?.product_price}"
            Log.d(TAG, "data?.product_price: ${data?.product_price}")
            product_no = data?.product_no.toString()
            product_picture = data?.product_picture.toString()


            Picasso.get().load(ProUrl +"ticketec/"+ data?.product_picture).into(pic)
            itemView.setOnClickListener {
                data?.let { it1 -> WatchItemClick.invoke(it1) }
            }
        }

    }

    fun updateDataSource(list: List<Product_list>) {
        dataSource = list
        notifyDataSetChanged()
    }

    inner class myViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var product_name: TextView = v.findViewById(R.id.tv_name)
        var product_price: TextView = v.findViewById(R.id.tv_money)

        var pic: ImageView = v.findViewById(R.id.img_package)
        lateinit var product_no: String
        lateinit var product_picture: String

    }


}


