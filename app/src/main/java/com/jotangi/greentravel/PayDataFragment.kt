package com.jotangi.greentravel

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
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

    private var invoiceType: Int = 0

    //    private lateinit var paydisAdapter: PaydisAdapter

    private var orderAmountTrue: Int = 0 // 應付金額
    private var orderPay: Int = 0 // 實付金額
    private var subPoint: Int = 0  // 點數折抵
    private var dialog: AlertDialog? = null

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        resetRecy()
        getPoint()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding == null
    }


    private fun initView() {
        activityTitleRid = R.string.title_pay_data
        apiEnqueue = ApiEnqueue()

        binding.apply {
            pdBack.setOnClickListener {
                requireActivity().onBackPressed()
            }
            pdPay.setOnClickListener {
                checkValue()
            }

            etUniformNo.keyListener = DigitsKeyListener.getInstance("0123456789");

            etInvoiceNumber.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable) {
                    if (!s.toString().startsWith("/") || s.toString() == "") {
                        etInvoiceNumber.setText("/")
                        etInvoiceNumber.setSelection(1)
                    }
                }
            })

            /*依據發票選擇切換ui高度*/
            pdBillC.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {

                        val text: String =
                            binding.pdBillC.selectedItem.toString()
                        val height: Int = ViewGroup.LayoutParams.WRAP_CONTENT
                        when (position) {
                            0 -> {
                                pdBillC.setSelection(0)
                                pdBill001.layoutParams.height = height
                                pdBill001.text = getString(R.string.pd_22)
                                etInvoiceNumber.layoutParams.height = 0//edittext
                                pdBill003.layoutParams.height = 0
                                etCompanyTitle.layoutParams.height = 0
                                pdBill00302.layoutParams.height = 0
                                etUniformNo.layoutParams.height = 0
                                pdBill00304.layoutParams.height = 0
                                pdBill00305.layoutParams.height = 0
                                pdBill00201.layoutParams.height = 0
                                pdBill00202.layoutParams.height = 0

                            }
                            1 -> {
                                pdBillC.setSelection(1)
                                etInvoiceNumber.layoutParams.height = height//edittext
                                pdBill001.text = getString(R.string.pd_23)
                                pdBill00201.layoutParams.height = height
                                pdBill003.layoutParams.height = 0
                                etCompanyTitle.layoutParams.height = 0
                                pdBill00302.layoutParams.height = 0
                                etUniformNo.layoutParams.height = 0
                                pdBill00304.layoutParams.height = 0
                                pdBill00305.layoutParams.height = 0
                                pdBill00202.layoutParams.height = 0

                            }
                            2 -> {
                                pdBillC.setSelection(2)
                                pdBill00202.layoutParams.height = height
                                etInvoiceNumber.layoutParams.height = 0
                                pdBill001.layoutParams.height = 0
                                pdBill00201.layoutParams.height = 0
                                pdBill003.layoutParams.height = height
                                etCompanyTitle.layoutParams.height = height//edittext
                                pdBill00302.layoutParams.height = height
                                etUniformNo.layoutParams.height = height//edittext
                                pdBill00304.layoutParams.height = 0
                                pdBill00305.layoutParams.height = 0//edittext
                            }
                        }
                        invoiceType = position
                        Log.d(TAG, "text: $position")
                    }
                }
        }
    }

    private fun checkValue() {
        binding.apply {

            if (etCompanyTitle.text.equals("")) {

                showDialogView("注意", "發票抬頭未填寫")

            } else if (etCompanyTitle.text.equals("") && etUniformNo.text.equals("")) {

                showDialogView("注意", "統一編號格式不符\n發票抬頭未填寫")
            }

            when (invoiceType) {

                0 -> {
                    Pay()
                }

                1 -> {
                    if (etInvoiceNumber.length() == 8 || etInvoiceNumber.text.toString()
                            .contains("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ+-.")
                    ) {

                        Pay()

                    } else {

                        showDialogView("注意", "手機載具條碼條件格式不符")

                    }
                }

                2 -> {
                    if (etUniformNo.length() == 8 && etCompanyTitle.length() > 1) {

                        Pay()

                    }
                    if (etCompanyTitle.length() < 1 && etUniformNo.length() < 1) {

                        showDialogView("注意", "統一編號格式不符\n發票抬頭未填寫")

                    } else if (etCompanyTitle.length() < 1) {

                        showDialogView("注意", "發票抬頭未填寫")

                    }
                    if (etUniformNo.length() != 8 && etCompanyTitle.length() > 1) {

                        showDialogView("注意", "統一編號格式不符")

                    }

                }
            }
        }
    }


    private var listSize: Int = 0

    /*購物車的商品列表*/
    private fun resetRecy() {
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
                                when (code) {
                                    "0x0201" -> {
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
                            } catch (_: Exception) {

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
                        var sum = 0
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
                                        if (s.toString().toInt() > MemberBean.point.toInt()) {

                                            etPoint.setText(MemberBean.point)
                                            etPoint.setSelection(etPoint.text.length)
                                        }
                                        //若點數折抵金額大於租金，點數設成可使用的最高金額
                                        if (etPoint.text.toString()
                                                .toInt() * 2 > orderAmountTrue
                                        ) {
                                            etPoint.setText("" + orderAmountTrue / 2)
                                            etPoint.setSelection(etPoint.text.length)
                                        }
                                        //讓總消費金額扣掉使用的點數(一點折2元)
                                        subPoint = etPoint.text.toString().toInt() * 2
                                        orderPay = orderAmountTrue - subPoint
                                        Log.d(TAG, "orderPay: + $orderPay")

                                        viewPoint.text =
                                            (MemberBean.point.toInt() - etPoint.text.toString()
                                                .toInt()).toString()
                                        tvBounsPoint.text = "$ $subPoint"
                                        pdM05.text = "$ $orderPay"


                                    } catch (e: java.lang.Exception) {
                                        etPoint.setSelection(etPoint.text.length)
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


    /*立即付款的api*/
    private fun Pay() {
        orderPay = orderAmountTrue - subPoint
        /*新增訂單的api*/
        apiEnqueue.addEcorder(
            orderAmountTrue.toString(),
            subPoint.toString(),
            orderPay.toString(),
            object : resultListener {
                override fun onSuccess(message: String?) {
                    activity?.runOnUiThread {
                        AlertDialog.Builder(requireContext()).apply {
                            setTitle("新增訂單成功")
                            setMessage(message)
                            setNegativeButton("確認") { dialog, _ ->

                                val fra = MallPayFragment.newInstance()
                                val data = Bundle()
                                data.putString("ResOrder", message)
                                fra.arguments = data
                                val transaction: FragmentTransaction =
                                    requireActivity().supportFragmentManager
                                        .beginTransaction()
                                transaction.replace(
                                    R.id.nav_host_fragment_activity_main,
                                    fra
                                )
                                transaction.commit()
                                dialog.dismiss()
                            }
                            setCancelable(false)
                        }.create().show()
                    }
                    if (message != null) {
                        getInvoice(message)
                    }
                }

                override fun onFailure(message: String?) {

                }

            })

    }

    private fun getInvoice(message: String) {

        binding.apply {


            val companyTitle = etCompanyTitle.text.toString().trim()
            val uniformNo = etUniformNo.text.toString().trim()
            val invoicePhone = etInvoiceNumber.text.toString().trim()
            var type = ""

            when (invoiceType) {
                0 -> {
                    type = "1"
                }

                1 -> {
                    type = "2"
                }

                2 -> {
                    type = "3"
                }
            }

            apiEnqueue.ecorderInvoice(
                message,
                type,
                companyTitle,
                uniformNo,
                invoicePhone,
                object : resultListener {
                    override fun onSuccess(message: String?) {
                        activity?.runOnUiThread {
                            Log.d(TAG, "onSuccess: ${"成功"}")
                        }
                    }

                    override fun onFailure(message: String?) {
                        Log.d(TAG, "onSuccess: ${"失敗"}")
                    }

                })
        }
    }

    fun showDialog(
        title: String,
        message: String,
        listener: DialogInterface.OnClickListener
    ) {

        if (dialog != null && dialog!!.isShowing) {
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


    private fun showDialogView(title: String, message: String) {
        val dialog = AlertDialog.Builder(requireContext()).create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.setTitle(title)
        dialog.setMessage(message)
        dialog.setButton(
            android.app.AlertDialog.BUTTON_POSITIVE, "確認"
        ) { _: DialogInterface?, _: Int ->

        }
        dialog.show()
    }
}



