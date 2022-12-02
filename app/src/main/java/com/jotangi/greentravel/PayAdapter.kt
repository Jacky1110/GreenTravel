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
 * @Title: PayAdapter.kt
 * @Package com.jotangi.healthy.HpayMall
 * @Description: PayAdapter
 * @author Kelly
 * @date 2022-1
 * @version hpay_34版
 */
class PayAdapter(private val mData: List<Cart>) :
    RecyclerView.Adapter<PayAdapter.myViewHolder2>() {
    var PayItemClick: (Cart) -> Unit = {}

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayAdapter.myViewHolder2 {
        return myViewHolder2(
            LayoutInflater.from(parent.context).inflate(R.layout.item_paydata, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PayAdapter.myViewHolder2, position: Int) {
        val data = mData[position]
        holder.apply {
            val ProUrl = ApiUrl.API_URL
            name.text = data.Name
            nt.text = data.Nt
            count.text = data.Count
            total.text = data.Dollar
            product_picture = data.product_picture.toString()
            product_no = data.product_no.toString()
            Picasso.get().load(ProUrl + "ticketec/" + product_picture).into(image)
            itemView.setOnClickListener { PayItemClick.invoke(data) }
        }

    }

    inner class myViewHolder2(v: View) : RecyclerView.ViewHolder(v) {
        var name: TextView = v.findViewById(R.id.pdName)
        var nt: TextView = v.findViewById(R.id.pdNt)
        var count: TextView = v.findViewById(R.id.pdCount)
        var image: ImageView = v.findViewById(R.id.pdImage)
        var delete01: TextView = v.findViewById(R.id.pd01)
        var total: TextView = v.findViewById(R.id.pdDollar)

        lateinit var product_picture: String
        lateinit var product_no: String

    }


}
/*
*
*
* */
//class PaydisAdapter(private val mData: List<Cart>) :
//    RecyclerView.Adapter<PaydisAdapter.myViewHolder2>() {
//    var PayItemClick: (Cart) -> Unit = {}
//
//    override fun getItemCount(): Int { return mData.size }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaydisAdapter.myViewHolder2 {
//        return myViewHolder2(
//            LayoutInflater.from(parent.context).inflate(R.layout.item_paydata_dis, parent, false)
//        )
//    }
//
//    override fun onBindViewHolder(holder: PaydisAdapter.myViewHolder2, position: Int) {
//        val data = mData[position]
//        holder.apply {
//            name.text = data.Name
//            nt.text = data.Nt
//            count.text = data.Count
//            count1.text=data.channel_price
//            var a=data.Count?.toInt()
//            var b=data. channel_price?.toInt()
//            var  c= a!! *b!!
//            if(DymaticTabFragment.isDiscount==true){
//                total.text=c.toString()
//            }else{
//                total.text=data.Dollar
//            }
//
////            total.text=data.Dollar
//            val ProUrl = ApiUrl.API_URL
//            product_picture=data.product_picture.toString()
//            product_no=data.product_no.toString()
//            Picasso.get().load(ProUrl +"medicalec/"+   product_picture).into( image)
//            itemView.setOnClickListener { PayItemClick.invoke(data) }
//        }
//
//    }
//
//    inner class myViewHolder2(v: View) : RecyclerView.ViewHolder(v) {
//        var name: TextView = v.findViewById(R.id.pdName)
//        var nt: TextView = v.findViewById(R.id.pdNt)
//        var count: TextView = v.findViewById(R.id.pdCount)
//        var count1: TextView = v.findViewById(R.id.pdNt1)
//        var image: ImageView = v.findViewById(R.id.pdImage)
//        var delete01: TextView = v.findViewById(R.id.pd01)
//        var total: TextView = v.findViewById(R.id.pdDollar)
//
//        lateinit var product_picture: String
//        lateinit var product_no: String
//
//    }
//
//
//}