package com.jotangi.greentravel.ui.store

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.greentravel.Api.ApiUrl
import com.jotangi.greentravel.Product_list
import com.jotangi.greentravel.R
import com.jotangi.greentravel.Store_List
import com.squareup.picasso.Picasso

class StoreListAdapter(private val mData: List<Store_List?>) :
    RecyclerView.Adapter<StoreListAdapter.myViewHolder>() {
    var ItemClick: (Store_List) -> Unit = {}

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        return myViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_stort_content, parent, false)
        )
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val data = mData[position]
        holder.apply {
            val ProUrl = ApiUrl.API_URL
            store_name.text = data?.store_name
            store_address.text = data?.store_address
            store_id = data?.store_id.toString()
            store_picture = data?.store_picture.toString()

            Picasso.get().load(ProUrl + "ticketec/" + data?.store_picture).into(pic)
            itemView.setOnClickListener {
                data?.let { it1 -> ItemClick.invoke(it1) }
            }
        }
    }

    inner class myViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var store_name: TextView = v.findViewById(R.id.storeName)
        var store_address: TextView = v.findViewById(R.id.storeAddress)

        var pic: ImageView = v.findViewById(R.id.pdIV)
        lateinit var store_picture: String
        lateinit var store_id: String


    }
}
