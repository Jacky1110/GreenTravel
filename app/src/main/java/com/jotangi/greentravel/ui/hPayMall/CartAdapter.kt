package com.jotangi.greentravel.ui.hPayMall

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.greentravel.Api.ApiUrl
import com.jotangi.greentravel.Cart
import com.jotangi.greentravel.R
import com.squareup.picasso.Picasso


/**
 *
 * @Title: CDetailFragment.kt
 * @Package com.jotangi.healthy.HpayMall
 * @Description: CartAdapter購物車的adapter
 * @author Kelly
 * @date 2022-1
 * @version hpay_34版
 */

class CartAdapter(var mData: List<Cart>, val listener: ItemClickListener) :
    RecyclerView.Adapter<CartAdapter.myViewHolder>() {

    var mContext: Context? = null

    var cartItemClick: (Cart) -> Unit = {}

    private val list = mutableListOf<Cart>()


    interface ItemClickListener {
        fun onItemClick(cart: Cart)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        return myViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_scart_detail, parent, false)
        )
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val data = mData[position]
        holder.apply {
            name.text = data.Name
            nt.text = data.Nt
            cdCount.text = data.Count
            count.text = data.Count
            total.text = data.Dollar
            total.text = data.total_amount
            product_picture = data.product_picture.toString()
            product_no = data.product_no.toString()
            product_stock = data.product_stock.toString()
            Picasso.get().load(ApiUrl.API_URL + "ticketec/" + product_picture).into(image)
            var cou = Integer.parseInt(cdCount.text.toString())

            Plus.setOnClickListener {
                /*加商品數目*/
                cou = cou + 1
//                    cdCount.text = cou.toString()
//                    count.text = cdCount.text
                data.Count = cou.toString()
                listener.onItemClick(data)

            }



            Reduce.setOnClickListener {
                /*減商品數目*/
                when {
                    cou > 1 -> {
                        cou = cou - 1
//                        cdCount.text = cou.toString()
                        data.Count = cou.toString()
                        listener.onItemClick(data)
                    }
                    else -> {
                        cartItemClick.invoke(data)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun updateDataSource(newData: List<Cart>) {
        this.mData = newData
        this.notifyDataSetChanged()
    }

    inner class myViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var name: TextView = v.findViewById(R.id.sdName)
        var nt: TextView = v.findViewById(R.id.sdNt)
        var count: TextView = v.findViewById(R.id.sdCount)
        var image: ImageView = v.findViewById(R.id.sdImage)
        var total: TextView = v.findViewById(R.id.sdDollar)
        var cdCount: TextView = v.findViewById(R.id.cdCount)
        var Plus: ImageButton = v.findViewById(R.id.cdPlus)
        var Reduce: ImageButton = v.findViewById(R.id.cdReduce)
        lateinit var product_picture: String
        lateinit var product_no: String
        lateinit var product_stock: String


    }

}