package com.jotangi.greentravel.ui.account.accountOrder

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.greentravel.Ecorder_list
import com.jotangi.greentravel.R

class EcoderAdapter(private val mData: List<Ecorder_list>) :
    RecyclerView.Adapter<EcoderAdapter.myViewHolder>() {
    var EcoderClick: (Ecorder_list) -> Unit = {}
    var payClick: (Ecorder_list) -> Unit = {}
    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        return myViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_mall, parent, false)
        )
    }

    inner class myViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var id: TextView = v.findViewById(R.id.moId)
        var date: TextView = v.findViewById(R.id.moDate)
        var dollar: TextView = v.findViewById(R.id.moDollar)
        var status: TextView = v.findViewById(R.id.moPaySt)
        var view: Button = v.findViewById(R.id.moView)
        var pay: Button = v.findViewById(R.id.moPay)

    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val data = mData[position]
        holder.apply {
            id.text = data.order_no
            date.text = data.order_date
            dollar.text = data.order_amount
            status.text = data.pay_status
            pay.visibility = (if (data.IsPay) View.INVISIBLE else View.INVISIBLE)
            Log.d("tag", "" + data.order_status.toBoolean())
            when {

                (data.pay_status.equals("0")) -> {
                    status.text = "處理中"
                    pay.visibility = View.VISIBLE
                }
                (data.pay_status.equals("-1")) -> {
                    status.text = "付款中"
                    pay.visibility = View.VISIBLE
                }
                (data.pay_status.equals("1")) -> {
                    if (data.assigntype.equals("1")) {
                        status.text = "店家派發"
                        pay.visibility = View.INVISIBLE
                    } else if (data.assigntype.equals("0")){
                        status.text = "付款完成"
                        pay.visibility = View.INVISIBLE
                    }
                }
            }
            view.setOnClickListener { EcoderClick.invoke(data) }
            pay.setOnClickListener { payClick.invoke(data) }

        }

    }
}
