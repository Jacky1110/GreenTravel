package com.jotangi.greentravel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.greentravel.ui.hPayMall.Commodity

/**
 *
 * @Title: WatchAdapter.kt
 * @Package com.jotangi.healthy.HpayMall
 * @Description: WatchAdapter
 * @author Kelly
 * @date 2022-1
 * @version hpay_34版
 */
class WatchAdapter (private val mData: List<Commodity>)
    : RecyclerView.Adapter<WatchAdapter.myViewHolder>() {
    var WatchItemClick: (Commodity) -> Unit = {}

    override fun getItemCount(): Int {
        return mData.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):WatchAdapter.myViewHolder {
        return myViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_pointstore_order,parent,false))
    }
    override fun onBindViewHolder(holder: WatchAdapter.myViewHolder, position: Int) {
        val data = mData[position]
        holder.apply {
            id.text=data.id
            des.text=data.des
            itemView.setOnClickListener {

                notifyDataSetChanged()
                val bundle = Bundle()
                bundle.putString( "id",id.text.toString())
                bundle.putString("des",des.text.toString())
                WatchItemClick.invoke(data)
            }
        }

    }inner class myViewHolder(v: View) : RecyclerView.ViewHolder(v){
        var id: TextView = v.findViewById(R.id.tv_name)
        var des: TextView = v.findViewById(R.id.tv_money)


    }
}

