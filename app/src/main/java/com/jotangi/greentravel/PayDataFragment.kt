package com.jotangi.greentravel

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.jotangi.greentravel.Api.ApiEnqueue
import com.jotangi.greentravel.Api.ApiEnqueue.resultListener
import com.jotangi.greentravel.Api.ApiUrl
import com.jotangi.greentravel.databinding.FragmentPayDataBinding
import com.jotangi.greentravel.ui.hPayMall.MemberBean
import com.jotangi.jotangi2022.ApiConUtils
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 *
 * @Title: PayDataFragment.kt
 * @Package com.jotangi.healthy.HpayMall
 * @Description: 付款頁面
 * @author Kelly
 * @date 2021-12
 * @version hpay_34版
 */
class PayDataFragment : ProjConstraintFragment() {
    private lateinit var binding: FragmentPayDataBinding

    private lateinit var apiEnqueue: ApiEnqueue

    private lateinit var payAdapter: PayAdapter

    //    private lateinit var paydisAdapter: PaydisAdapter

    private var orderAmountTrue: Int = 0 // 應付金額
    private var orderPay: Int = 0 // 實付金額
    private var subPoint: Int = 0  // 點數折抵

    val lists = arrayListOf<Cart>()
    private var priceList: ArrayList<Int> = ArrayList<Int>()

    companion object {
        const val TAG = "PayDataFragment"
        fun newInstance() = PayDataFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPayDataBinding.inflate(inflater, container, false)
        val root: View = binding.root
        apiEnqueue = ApiEnqueue()

        binding.apply {
            pdBack.setOnClickListener {
                requireActivity().onBackPressed()
            }
            pdPay.setOnClickListener {
                Pay()
            }
//
            rvInitial()
            getPoint()


        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding == null
    }

    override fun onStart() {
        super.onStart()
        activityTitleRid = R.string.title_pay_data
        ResetRecy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun rvInitial() {
        ResetRecy()
    }

    private var listSize: Int = 0

    /*購物車的商品列表*/
    fun ResetRecy() {
        ApiConUtils.shoppingcart_list(
            ApiUrl.API_URL,
            ApiUrl.shoppingcart_list,
            MemberBean.member_id,
            MemberBean.member_pwd,
            object : ApiConUtils.OnConnect {
                @Throws(JSONException::class)
                override fun onSuccess(jsonString: String) {
                    requireActivity().runOnUiThread {
                        Log.e("購物車內商品列表 : ", "  " + jsonString)
                        try {
                            lists.clear()
                            try {
                                val jsonObject = JSONObject(jsonString)
                                val code: String = jsonObject.getString("code")
                                when {
                                    code.equals("0x0201") -> {
                                        MemberBean.isShoppingCarPoint = false
                                        /*通知購物車清空，button紅點清除*/
                                        fragmentListener.onAction(
                                            FUNC_FRAGMENT_change,
                                            MemberBean.isShoppingCarPoint
                                        )
                                        binding.rvPay.removeAllViews()
                                        binding.pa2.visibility = View.GONE
                                        binding.pdNodata.visibility = View.VISIBLE
                                        binding.pdNd0.visibility = View.VISIBLE
                                    }
                                }
                            } catch (r: Exception) {

                            }
                            var result: Int = 0
                            val jsonArray = JSONArray(jsonString)
                            listSize = jsonArray.length()
                            for (i in 0 until jsonArray.length()) {
                                val jsonObject = jsonArray[i] as JSONObject
                                lists.add(
                                    Cart(
                                        Name = jsonObject.getString("product_name"),
                                        Nt = jsonObject.getString("product_price"),
                                        Count = jsonObject.getString("order_qty"),
                                        Dollar = jsonObject.getString("total_amount"),
                                        product_picture = jsonObject.getString("product_picture"),
                                        product_no = jsonObject.getString("product_no"),
                                    )

                                )
                                //購買金額計算,之後要扣點數
                                result = jsonObject.getString("order_qty")
                                    .toInt() * jsonObject.getString("product_price").toInt()
                                priceList.add(result)
                            }
                            payAdapter = PayAdapter(lists)
                            binding.rvPay.layoutManager = LinearLayoutManager(context)
                            binding.rvPay.adapter = payAdapter
                            payAdapter.PayItemClick = {}

                            if (lists.isEmpty()) {
                                binding.pa2.visibility = View.GONE
                                binding.pdNodata.visibility = View.VISIBLE
                                binding.pdNd0.visibility = View.VISIBLE

                            }

                        } catch (e: Exception) {
                        }
                        var sum: Int = 0
                        for (i in 0 until listSize) {
                            /*價格加總*/
                            sum += priceList[i]
                            Log.d(TAG, "priceList.get(i): " + priceList.get(i));
                        }

                        orderAmountTrue = sum
                        Log.d(TAG, "orderAmountTrue: + $orderAmountTrue")
                        binding.tvOrderAmount.text = "$ $orderAmountTrue"
//                        binding.tvBounsPoint.text = "$ " + "0"
//                        binding.pdM05.text = "$ $orderAmountTrue"
//                        orderPay = orderAmountTrue - subPoint

                        binding.apply {
                            etPoint.setText("0")
                            tvBounsPoint.setText("$ " + "0")
                            pdM05.text = "$ $orderAmountTrue"
                            etPoint.addTextChangedListener(object : TextWatcher {
                                override fun beforeTextChanged(
                                    s: CharSequence?, start: Int, count: Int, after: Int
                                ) {

                                }

                                override fun onTextChanged(
                                    s: CharSequence?, start: Int, before: Int, count: Int
                                ) {
                                    try {
                                        //若輸入點數大於全部點數，點數設成最大點數
                                        if (s.toString().toInt() > MemberBean.point.toInt()){

                                            etPoint.setText(MemberBean.point)
                                            etPoint.setSelection(etPoint.getText().length)
                                        }
                                        //若點數折抵金額大於租金，點數設成可使用的最高金額
                                        if (etPoint.getText().toString()
                                                .toInt() * 2 > orderAmountTrue
                                        ) {
                                            etPoint.setText("" + orderAmountTrue / 2)
                                            etPoint.setSelection(etPoint.getText().length)
                                        }
                                        //讓總消費金額扣掉使用的點數(一點折2元)
                                        subPoint = etPoint.getText().toString().toInt() * 2
                                        orderPay = orderAmountTrue - subPoint
                                        Log.d(TAG, "orderPay: + $orderPay")

                                        viewPoint.text = (MemberBean.point.toInt() - etPoint.getText().toString().toInt()).toString()
                                        tvBounsPoint.text = "$ $subPoint"
                                        pdM05.text = "$ $orderPay"


                                    } catch (e: java.lang.Exception) {
                                        etPoint.setSelection(etPoint.getText().length)
                                    }
                                }

                                override fun afterTextChanged(s: Editable?) {
                                }

                            })

                        }

                    }


                }

                override fun onFailure(message: String) {

                }
            })
    }


    private fun getPoint() {
        apiEnqueue.userGetdata(object : resultListener {
            override fun onSuccess(message: String) {
                requireActivity().runOnUiThread {
                    try {
                        val jsonObject = JSONObject(message)
                        Log.d(TAG, "jsonObject: $jsonObject")
                        MemberBean.point = jsonObject.getString("point")
                        Log.d(TAG, "MemberBean.point: " + MemberBean.point)
                        binding.viewPoint.text = MemberBean.point
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(message: String) {}
        })
    }


    var invoice_type: String? = null

    /*立即付款的api*/
    private fun Pay() {
        orderPay = orderAmountTrue - subPoint
        /*新增訂單的api*/
        ApiConUtils.add_ecorder(
            ApiUrl.API_URL,
            ApiUrl.add_ecorder,
            MemberBean.member_id,
            MemberBean.member_pwd,
            orderAmountTrue.toString(),
            subPoint.toString(),
            orderPay.toString(),
            object : ApiConUtils.OnConnect {
                @Throws(JSONException::class)
                override fun onSuccess(jsonString: String) {
                    Log.e("onSuccess", jsonString)
                    Handler(Looper.getMainLooper()).post {
                        try {
                            val jsonObject = JSONObject(jsonString)
                            val status: String = jsonObject.getString("status")
                            val code: String = jsonObject.getString("code")
                            val responseMessage: String =
                                jsonObject.getString("responseMessage")
                            when {
                                code.equals("0x0200") -> {
                                    AlertDialog.Builder(requireContext()).apply {
                                        setTitle("新增訂單成功")
                                        setMessage(responseMessage)
//                                            setCanceledOnTouchOutside(false)
                                        setNegativeButton("確認") { dialog, _ ->
//                                            val uri: Uri = Uri.parse(ApiUrl.payUrl + responseMessage) //要跳轉的網址
//                                            val intent = Intent(Intent.ACTION_VIEW, uri)
//                                            intent.setData(uri)
//                                            startActivity(intent)

                                            val fra = MallPayFragment.newInstance()
                                            val data = Bundle()
                                            data.putString("ResOrder", responseMessage)
                                            fra.arguments = data
                                            val transaction: FragmentTransaction =
                                                requireActivity().getSupportFragmentManager()
                                                    .beginTransaction();
                                            transaction.replace(
                                                R.id.nav_host_fragment_activity_main,
                                                fra
                                            )
//                                                    transaction.addToBackStack(DymaticTabFragment.javaClass.getSimpleName())
                                            transaction.commit()
                                            dialog.dismiss()
                                        }
                                        setCancelable(false)
                                    }.create().show()
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }


                }

                override fun onFailure(message: String) {

                }
            })

    }


    var dialog: AlertDialog? = null
    fun showDialog(
        title: String,
        message: String,
        listener: DialogInterface.OnClickListener
    ) {

        if (dialog != null && dialog!!.isShowing()) {
            dialog!!.dismiss()
        }
        dialog = AlertDialog.Builder(requireContext()).create()
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setTitle(title)
        dialog!!.setMessage(message)
        dialog!!.setButton(
            AlertDialog.BUTTON_NEUTRAL, "確認",
            DialogInterface.OnClickListener { dialog, which -> listener.onClick(dialog, which) })
        dialog!!.show()
    }


}



