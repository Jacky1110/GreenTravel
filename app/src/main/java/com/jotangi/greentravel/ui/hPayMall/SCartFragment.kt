package com.jotangi.greentravel.ui.hPayMall

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.jotangi.greentravel.*
import com.jotangi.greentravel.Api.ApiEnqueue
import com.jotangi.greentravel.Api.ApiEnqueue.resultListener
import com.jotangi.greentravel.Api.ApiUrl
import com.jotangi.greentravel.databinding.FragmentScartBinding
import com.jotangi.jotangi2022.ApiConUtils
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class SCartFragment : ProjConstraintFragment(), CartAdapter.ItemClickListener {
    private var _binding: FragmentScartBinding? = null
    private lateinit var cartAdapter: CartAdapter

    //    private lateinit var cartdisAdapter: CartdisAdapter
    var isReset: Boolean = false
    var no: String = ""
    var count: String = ""
    var isDiscount = false
    var product_stock: Int = 0

    private var apiEnqueue: ApiEnqueue? = null

    companion object {
        const val TAG = "SCartFragment"
        fun newInstance() = SCartFragment()
    }

    private val binding get() = _binding!!
    val lists = arrayListOf<Cart>()
    val list2 = arrayListOf<Cartdis>()
    private var priceList: ArrayList<Int> = ArrayList<Int>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScartBinding.inflate(inflater, container, false)
        val root: View = binding.root
        apiEnqueue = ApiEnqueue()
        binding.apply {
            scC1.setOnClickListener {
                // ClientManager.instance.init()
                /*付款條款頁面*/

                if (fragmentListener != null) {
                    fragmentListener.onAction(fraShopTerms, null)
                }
            }
            scPay.setOnClickListener {
                /*立即付款頁面 */
                when {
                    lists.isEmpty() -> {
                        AlertDialog.Builder(requireContext()).apply {
                            setTitle("沒有商品")
                            setNegativeButton("確認") { dialog, _ ->

                                dialog.dismiss()
                            }
                            setCancelable(true)
                        }.create().show()
                    }
                    scC.isChecked == false -> {
                        AlertDialog.Builder(requireContext()).apply {
                            setTitle("請確認是否已詳細閱讀購物條款")
                            setNegativeButton("確認") { dialog, _ ->

                                dialog.dismiss()
                            }
                            setCancelable(true)
                        }.create().show()

                    }
                    else -> {
                        if (fragmentListener != null) {
                            fragmentListener.onAction(fraPayData, null)
                        }
                    }
                }


            }
            scDelete.setOnClickListener {
                clearShoppingCart()
            }
            scContinue.setOnClickListener {
                /*繼續購物頁面*/
                if (fragmentListener != null) {
                    fragmentListener.onAction(fraDymaticTab, null)
                }

            }
            rvScart.apply {


            }
        }



        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        activityTitleRid = R.string.title_shopcart
        ResetRecy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.apply {
            cartAdapter = CartAdapter(lists, this@SCartFragment)
            rvScart.layoutManager = LinearLayoutManager(context)
            rvScart.adapter = cartAdapter
        }
    }

    private fun RemoveShopping_Cart(proNo: String) {
        binding.progressBar.visibility = View.VISIBLE
        ApiConUtils.del_shoppingcart(
            ApiUrl.API_URL,
            ApiUrl.del_shoppingcart,
            MemberBean.member_id,
            MemberBean.member_pwd,
            proNo,
            object : ApiConUtils.OnConnect {
                @Throws(JSONException::class)
                override fun onSuccess(jsonString: String) {
                    requireActivity().runOnUiThread(Runnable {
                        binding.progressBar.visibility = View.GONE
                        Log.e("修改購物車內商品數量 : ", "  " + jsonString)
                        try {
                            val jsonObject = JSONObject(jsonString)
                            val status: String = jsonObject.getString("status")
                            val code: String = jsonObject.getString("code")
                            val responseMessage: String = jsonObject.getString("responseMessage")
                            when {
                                (code.equals("0x0200")) -> {
                                    AlertDialog.Builder(requireContext()).apply {
                                        setTitle("")
                                        setMessage("修改購物車商品成功")
                                        setNegativeButton("確認") { dialog, _ ->
                                            Pshow()
                                            ResetRecy()
                                            dialog.dismiss()
                                        }
                                        setCancelable(true)
                                    }.create().show()
                                }
                                else -> {
                                    ResetRecy()
                                    Pnoshow()
                                }
                            }

                        } catch (e: Exception) {
                        }
                    })
                }

                override fun onFailure(message: String) {}
            })


    }

    private fun ResetRecy() {
        Log.e("購物車內參數 : ", "  " + MemberBean.member_id + " " + MemberBean.member_pwd)

        binding.progressBar.visibility = View.VISIBLE

        ApiConUtils.shoppingcart_list(
            ApiUrl.API_URL,
            ApiUrl.shoppingcart_list,
            MemberBean.member_id,
            MemberBean.member_pwd,
            object : ApiConUtils.OnConnect {
                @Throws(JSONException::class)
                override fun onSuccess(jsonString: String) {
                    requireActivity().runOnUiThread(Runnable {
                        binding.progressBar.visibility = View.GONE
                        Log.e("購物車內商品列表 : ", "  " + jsonString)
                        try {
                            lists.clear()
                            try {
                                val jsonObject = JSONObject(jsonString)
                                val code: String = jsonObject.getString("code")
                                when {
                                    code.equals("0x0201") -> {
                                        MemberBean.isShoppingCarPoint = false
                                        fragmentListener.onAction(
                                            FUNC_FRAGMENT_change, MemberBean.isShoppingCarPoint
                                        )
                                        binding.rvScart.removeAllViews()
                                        Nodata()
                                        Pnoshow()
                                    }
                                }
                            } catch (r: Exception) {
                            }
                            var result: Int = 0
                            val jsonArray = JSONArray(jsonString)
                            for (i in 0 until jsonArray.length()) {
                                val jsonObject = jsonArray[i] as JSONObject
                                lists.add(
                                    Cart(
                                        Name = jsonObject.getString("product_name"),
                                        Nt = jsonObject.getString("product_price"),
                                        Count = jsonObject.getString("order_qty"),
                                        Dollar = jsonObject.getString("total_amount"),
                                        total_amount = jsonObject.getString("total_amount"),
                                        product_picture = jsonObject.getString("product_picture"),
                                        product_no = jsonObject.getString("product_no"),
                                        product_stock = jsonObject.getString("product_stock"),
//                                        channel_price = jsonObject.getString("channel_price"),
                                    )
                                )


                                result = jsonObject.getString("total_amount").toInt()
                                priceList.add(result)


                            }


                            var sum: Int = 0
                            for (i in 0 until priceList.size) {
                                /*價格加總*/
                                sum += priceList.get(i);
                            }

                            Log.e("是啥", " " + sum)

                            requireActivity().runOnUiThread {
                                binding.apply {
                                    cartAdapter.updateDataSource(lists)
//                                    cartAdapter = CartAdapter(lists, this@SCartFragment)
//                                    rvScart.layoutManager = LinearLayoutManager(context)
//                                    rvScart.adapter = cartAdapter
                                    Pnoshow()
                                }
                            }

                            cartAdapter.cartItemClick = {
                                isReset == true
                                no = it.product_no.toString()
                                Log.d(TAG, "onSuccess: ${MemberBean.no}")
                                count = it.Count.toString()
                                Log.d(TAG, "onSuccess: ${MemberBean.count}")
                                RemoveShopping_Cart(it.product_no.toString())
                            }


//                        else {
//                            cartdisAdapter = CartdisAdapter(lists)
//                            binding.rvScart.layoutManager = LinearLayoutManager(context)
//                            binding.rvScart.adapter = cartdisAdapter
//                            Pnoshow()
//                            cartdisAdapter.Click = {
//                                isReset == true
//                                no = it.product_no.toString()
//                                EditShopping_Cart(it.product_no.toString())
//                            }
//
//                        }

                            if (lists.isEmpty()) {
                                Nodata()
                            }

                        } catch (e: Exception) {
                        } finally {

                        }


                    })


                }

                override fun onFailure(message: String) {
                }
            })
    }

    private fun Pshow() {
        requireActivity().runOnUiThread(Runnable {
            val pb: ProgressBar =
                requireActivity().findViewById(R.id.progressBar)
            pb.visibility = View.VISIBLE
            requireActivity().getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )

        })

    }

    private fun Pnoshow() {
        requireActivity().runOnUiThread(Runnable {
            val pb: ProgressBar =
                requireActivity().findViewById(R.id.progressBar)
            pb.visibility = View.GONE
            requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        })

    }

    private fun Nodata() {
        requireActivity().runOnUiThread {
            val img: ImageView =
                requireActivity().findViewById(R.id.scNodata)
            val imgt: TextView =
                requireActivity().findViewById(R.id.scNd0)
            binding.rvScart.visibility = View.INVISIBLE
            img.visibility = View.VISIBLE
            imgt.visibility = View.VISIBLE

        }

    }

    private fun clearShoppingCart() {
        ApiConUtils.clear_shoppingcart(
            ApiUrl.API_URL,
            ApiUrl.clear_shoppingcart,
            MemberBean.member_id,
            MemberBean.member_pwd,

            object : ApiConUtils.OnConnect {
                @Throws(JSONException::class)
                override fun onSuccess(jsonString: String) {
                    requireActivity().runOnUiThread(Runnable {
                        Log.e("修改購物車內商品數量 : ", "  " + jsonString)
                        try {
                            val jsonObject = JSONObject(jsonString)
                            val status: String = jsonObject.getString("status")
                            val code: String = jsonObject.getString("code")
                            val responseMessage: String = jsonObject.getString("responseMessage")
                            when {
                                (code.equals("0x0200")) -> {

                                    AlertDialog.Builder(requireContext()).apply {
                                        setTitle("")
                                        setMessage("清空購物車商品成功")
                                        setNegativeButton("確認") { dialog, _ ->
                                            Pshow()
                                            ResetRecy()
                                            dialog.dismiss()
                                        }
                                        setCancelable(true)
                                    }.create().show()
                                }
                                else -> {

                                    ResetRecy()
                                    Pnoshow()
                                }
                            }

                        } catch (e: Exception) {
                        }


                    })


                }

                override fun onFailure(message: String) {
                }
            })


    }

    override fun onItemClick(data: Cart) {
        var proNo = data.product_no
        var Count = data.Count
        binding.progressBar.visibility = View.VISIBLE
        apiEnqueue!!.editShoppingcart(proNo, Count, object : resultListener {
            override fun onSuccess(message: String) {
                Log.d(TAG, "product_stock:  ${lists[0].product_stock}")
                requireActivity().runOnUiThread(Runnable {
                    binding.progressBar.visibility = View.GONE
                    ResetRecy()
                })


            }

            override fun onFailure(message: String) {
                requireActivity().runOnUiThread(Runnable {
                    binding.progressBar.visibility = View.GONE
                    AlertDialog.Builder(requireContext()).apply {
                        setTitle("")
                        setMessage("超過庫存數量")
                        setNegativeButton("確認") { dialog, _ ->
                            dialog.dismiss()
                        }
                        setCancelable(true)
                    }.create().show()
                })
            }
        })


    }


}


