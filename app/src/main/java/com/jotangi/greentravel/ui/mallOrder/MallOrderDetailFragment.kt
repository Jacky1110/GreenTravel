package com.jotangi.greentravel.ui.mallOrder

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.jotangi.greentravel.*
import com.jotangi.greentravel.Api.ApiEnqueue
import com.jotangi.greentravel.Api.ApiUrl
import com.jotangi.greentravel.ui.hPayMall.MemberBean
import com.jotangi.greentravel.databinding.FragmentMallOrderDetailBinding
import com.jotangi.jotangi2022.ApiConUtils
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 *
 * @Title: MallOrderDetailFragment.kt
 * @Package com.jotangi.healthy.HpayMall
 * @Description: MallOrderDetailFragment商城訂單部分
 * @author Kelly
 * @date 2021-12
 * @version hpay_32版
 */
class MallOrderDetailFragment : ProjConstraintFragment() {

    private var _binding: FragmentMallOrderDetailBinding? = null
    val lists = arrayListOf<Cart>()
    private lateinit var cartAdapter: PayAdapter
    private lateinit var apiEnqueue: ApiEnqueue

    var assigntype: Int = 0
//    private lateinit var carDisAdapter:PaydisAdapter
//    var isDiscount = DymaticTabFragment.isDiscount

    companion object {
        const val TAG = "MallOrderDetailFragment"
        fun newInstance() = MallOrderDetailFragment()
    }

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMallOrderDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val data: Bundle? = arguments
        apiEnqueue = ApiEnqueue()

        if (data != null) {
            requireActivity().runOnUiThread {

                val mallNo = data.getString("Mallorder").toString()
                binding.rcPdd.removeAllViews()
                ecorder_info(mallNo)
            }

        }
        binding.apply {


        }
        //rvInitial()
        return root
    }

    override fun onStart() {
        super.onStart()
        activityTitleRid = R.string.account_listitem_order

        binding.rcPdd.apply {
            cartAdapter = PayAdapter(lists)
            layoutManager = LinearLayoutManager(context)
            adapter = cartAdapter

        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    /*
    * setting txt result
    * */
//    fun txtSetting(
//        pdtotal: String,
//        pdbonusdis: String, pdneedpay: String
//    ) {
//        binding.fdTotal.text = pdtotal
//        binding.fdBon.text = pdbonusdis
//        binding.fdRes.text = pdneedpay
//    }

//    fun DetailValue(
//        Id: String,
//        Date: String,
//        Count: String,
//        OrderStatus: String,
//        PayStatus: String,
//    ) {
//        binding.fdID.setText(Id)
//        binding.fdDate.setText(Date)
//        binding.fdCount.setText(Count)
//        binding.fdOstatus.setText(OrderStatus)
//        binding.fdPstatus.setText(PayStatus)
//
//    }

    private var priceList: ArrayList<Int> = ArrayList<Int>()

    private fun ecorder_info(No: String) {
        val errCode = arrayOf(
            "0x0201", "0x0202", "0x0203", "0x0204", "0x0205"
        )
        var result: Int = 0
        val id = MemberBean.member_id
        val pwd = MemberBean.member_pwd
        val no = No
        if (id != null && pwd != null) {
            ApiConUtils.ecorder_info(
                ApiUrl.API_URL,
                ApiUrl.ecorder_info,
                id,
                pwd, no,
                object : ApiConUtils.OnConnect {
                    override fun onSuccess(jsonString: String) {
                        activity!!.runOnUiThread {
                            Log.e("Mall's   info 細節", "ecorder_list $jsonString")
                            try {
                                val jsonObject = JSONObject(jsonString)
                                val status = jsonObject.getString("status")
                                val code = jsonObject.getString("code")
                                val responseMessage = jsonObject.getString("responseMessage")
                                when {
                                    (Arrays.asList<String>(*errCode).contains(code)) -> {
                                        CustomDaialog.showNormal(
                                            requireActivity(),
                                            "",
                                            code + "  " + responseMessage,
                                            "確定",
                                            object : CustomDaialog.OnBtnClickListener {
                                                override fun onCheck() {}
                                                override fun onCancel() {
                                                    requireActivity().onBackPressed()
                                                    CustomDaialog.closeDialog()
                                                }
                                            })
                                    }

                                }
                            } catch (e: Exception) {
                            }
                            try {
                                requireActivity().runOnUiThread {
                                    binding.rcPdd.apply {


                                        val jA = JSONArray(jsonString)
                                        for (i in 0 until jA.length()) {
                                            val pro = jA[i] as JSONObject
                                            binding.fdID.text = pro.getString("order_no")
                                            binding.fdDate.text = pro.getString("order_date")
                                            binding.fdCount.text = pro.getString("order_amount")
                                            binding.fdTotal.text =
                                                "$${pro.getString("order_amount")}"
                                            binding.fdBon.text =
                                                "$${pro.getString("discount_amount")}"
                                            binding.fdRes.text = "$${pro.getString("order_pay")}"
                                            StausText(
                                                pro.getString("order_status"),
                                                pro.getString("pay_status")
                                            )
                                            try {
                                                val jsonArray = pro.getJSONArray("product_list")
                                                for (i in 0 until jsonArray.length()) {
                                                    val proAray = jsonArray[i] as JSONObject

                                                    lists.add(
                                                        Cart(
                                                            Name = proAray.getString("product_name"),
                                                            Nt = proAray.getString("product_price"),
                                                            Count = proAray.getString("order_qty"),
                                                            Dollar = proAray.getString("total_amount"),
                                                            product_picture = proAray.getString("product_picture"),
                                                            product_no = proAray.getString("product_no"),
                                                        )
                                                    )

                                                    result = proAray.getString("order_qty")
                                                        .toInt() * proAray.getString("product_price")
                                                        .toInt()
                                                    priceList.add(result)


                                                }

                                                handlePage(no)
                                            } catch (e: Exception) {
                                                CustomDaialog.showNormal(
                                                    requireActivity(),
                                                    "",
                                                    "很抱歉，訂單異常",
                                                    "謝謝",
                                                    object : CustomDaialog.OnBtnClickListener {
                                                        override fun onCheck() {}
                                                        override fun onCancel() {
                                                            requireActivity().onBackPressed()
                                                            CustomDaialog.closeDialog()
                                                        }
                                                    })
                                            }


                                        }


//                                        var sum: Int = 0
//                                        for (i in 0 until priceList.size) {
//
//                                            sum += priceList.get(i);
//                                        }
//                                        Log.e("是啥", " " + sum)
//                                        binding.fdTotal.text = "$" + sum.toString()
//                                        binding.fdBon.text = "$" + "0"
//                                        binding.fdRes.text = "$" + sum.toString()


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
    }

    private fun handlePage(No: String) {
        apiEnqueue.ecorderInfo(No, object : ApiEnqueue.resultListener {
            override fun onSuccess(message: String?) {
                requireActivity().runOnUiThread {
                    try {
                        val jA = JSONArray(message)
                        for (i in 0 until jA.length()) {
                            val pro = jA[i] as JSONObject

                            val jsonArray = pro.getJSONArray("package_list")
                            for (i in 0 until jsonArray.length()) {
                                val proAray = jsonArray[i] as JSONObject

                                lists.add(
                                    Cart(
                                        Name = proAray.getString("product_name"),
                                        Nt = proAray.getString("product_price"),
                                        Count = proAray.getString("order_qty"),
                                        Dollar = proAray.getString("total_amount"),
                                        product_picture = proAray.getString("product_picture"),
                                        pkglist_id = proAray.getString("pkglist_id"),
                                    )
                                )
                                assigntype = proAray.getString("assigntype").toInt()

                            }
                        }
                    } catch (e: JSONException) {
//                        CustomDaialog.showNormal(
//                            requireActivity(),
//                            "",
//                            "很抱歉，訂單異常",
//                            "謝謝",
//                            object : CustomDaialog.OnBtnClickListener {
//                                override fun onCheck() {}
//                                override fun onCancel() {
//                                    requireActivity().onBackPressed()
//                                    CustomDaialog.closeDialog()
//                                }
//                            })
                    }
                    cartAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(message: String?) {

            }

        })
    }


    private fun StausText(s1: String, s2: String) {
        when (s1) {
            "0" -> {
                binding.fdOstatus.text = "處理中"
            }
            "1" -> {
                binding.fdOstatus.text = "完成"
            }
            "2" -> {
                binding.fdOstatus.text = "取消"
            }
            else -> {
                binding.fdPstatus.text = "等待處理"
            }
        }
        when (s2) {
            "0" -> {
                binding.fdPstatus.text = "處理中"
            }
            "1" -> {
                if (assigntype.equals("1")) {
                    binding.fdPstatus.text = "店家派發"
                } else {
                    binding.fdPstatus.text = "付款完成"
                }
            }
            "-1" -> {
                binding.fdPstatus.text = "尚未付款"
            }
            else -> {
                binding.fdPstatus.text = "尚未付款"
            }
        }
    }

}