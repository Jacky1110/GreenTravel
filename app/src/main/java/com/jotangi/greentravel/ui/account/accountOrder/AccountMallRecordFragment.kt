package com.jotangi.greentravel.ui.account.accountOrder

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.jotangi.greentravel.*
import com.jotangi.greentravel.Api.ApiUrl
import com.jotangi.greentravel.ui.hPayMall.MemberBean
import com.jotangi.greentravel.databinding.FragmentMallOrderBinding
import com.jotangi.greentravel.ui.mallOrder.MallOrderAdapter
import com.jotangi.greentravel.ui.mallOrder.MallOrderDetailFragment
import com.jotangi.jotangi2022.ApiConUtils
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.Exception

class AccountMallRecordFragment : ProjConstraintFragment() {

    private var _binding: FragmentMallOrderBinding? = null
    private lateinit var moAdapter: MallOrderAdapter

    companion object {
        const val TAG = "AccountMallRecordFragment"
        fun newInstance() = AccountMallRecordFragment()
    }

    lateinit var orderNo: String
    lateinit var orderPay: String

    private lateinit var ListAdapter: EcoderAdapter

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMallOrderBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.apply {
//           requireActivity(). runOnUiThread(Runnable {
//                CustomDaialog.showNormal(
//                    requireActivity(),
//                    "",
//                    "Coming Soon",
//                    "確定",
//                    object : CustomDaialog.OnBtnClickListener {
//                        override fun onCheck() {}
//                        override fun onCancel() {
//                            requireActivity().onBackPressed()
//                            CustomDaialog.closeDialog()
//                        }
//                    })
//            })

        }
        ecorder_list()

        //rvInitial()

        return root
    }

    override fun onStart() {
        super.onStart()
        activityTitleRid = R.string.title_myorder
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

    val lists = arrayListOf<Ecorder_list>()

    private fun ecorder_list() {
        val errCode = arrayOf(
            "0x0201", "0x0202", "0x0203", "0x0204", "0x0205"
        )

        //Check if the shopping cart has value
        val id = MemberBean.member_id
        val pwd = MemberBean.member_pwd
        if (id != null && pwd != null) {
            ApiConUtils.ecorder_list(
                ApiUrl.API_URL,
                ApiUrl.ecorder_list,
                id,
                pwd,
                object : ApiConUtils.OnConnect {
                    override fun onSuccess(jsonString: String) {
                        activity!!.runOnUiThread {
                            try {
                                val jsonObject = JSONObject(jsonString)
                                val status = jsonObject.getString("status")
                                val code = jsonObject.getString("code")
                                val responseMessage = jsonObject.getString("responseMessage")
                                Log.e("Mall's 細節BBBB", "ecorder_list $jsonString" + "\n\r" + status)

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
//                                status.contentEquals("false") -> {
//                                    CustomDaialog.showNormal(
//                                        requireActivity(),
//                                        "Error Code:",
//                                        code+"  "+responseMessage,
//                                        "確定",
//                                        object : CustomDaialog.OnBtnClickListener {
//                                            override fun onCheck() {}
//                                            override fun onCancel() {
//                                                requireActivity().onBackPressed()
//                                                CustomDaialog.closeDialog()
//                                            }
//                                        })
//                                }


                            } catch (e: Exception) {

                            }

                            try {

                                binding.rvMall.apply {
                                    lists.clear()
                                    val jsonArray = JSONArray(jsonString)
                                    for (i in 0 until jsonArray.length()) {
                                        val jsonObject = jsonArray[i] as JSONObject
                                        lists.add(
                                            Ecorder_list(
                                                order_no = jsonObject.getString("order_no"),
                                                order_date = jsonObject.getString("order_date"),
                                                order_amount = jsonObject.getString("order_amount"),
                                                order_pay = jsonObject.getString("order_pay"),
                                                pay_status = jsonObject.getString("pay_status"),
                                                order_status = jsonObject.getString("order_status"),
                                                assigntype = jsonObject.getString("assigntype"),

                                            )
                                        )


                                    }
                                    ListAdapter = EcoderAdapter(lists)
                                    binding.rvMall.layoutManager = LinearLayoutManager(context)
                                    binding.rvMall.adapter = ListAdapter
                                    ListAdapter.notifyDataSetChanged()
                                    ListAdapter.EcoderClick = {
                                        orderNo = it.order_no.toString()
                                        val fragment = MallOrderDetailFragment()
                                        val args = Bundle()
                                        args.putString("Mallorder", it.order_no.toString())
                                        fragment.setArguments(args)
                                        val transaction: FragmentTransaction =
                                            requireActivity().getSupportFragmentManager()
                                                .beginTransaction();
                                        transaction.replace(
                                            R.id.nav_host_fragment_activity_main,
                                            fragment
                                        )
                                        transaction.addToBackStack(AccountMallRecordFragment.javaClass.simpleName)
                                        transaction.commit()
                                    }
                                    ListAdapter.payClick = {
                                        orderNo = it.order_no.toString()
//                                        val uri: Uri = Uri.parse(ApiUrl.payUrl + it.order_no.toString()) //要跳轉的網址
//                                        val intent = Intent(Intent.ACTION_VIEW, uri)
//                                        intent.setData(uri)
//                                        startActivity(intent)

                                        val fra = MallPayFragment.newInstance()
                                        val data = Bundle()
                                        data.putString("ResOrder", it.order_no.toString())
                                        /*ResOrder->導向付款頁面皆需要付款編號*/
                                        fra.arguments = data
                                        val transaction: FragmentTransaction =
                                            requireActivity().getSupportFragmentManager()
                                                .beginTransaction();
                                        transaction.replace(
                                            R.id.nav_host_fragment_activity_main,
                                            fra
                                        )
                                        transaction.addToBackStack(AccountMallRecordFragment.javaClass.simpleName)
                                        transaction.commit()
                                    }


                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    override fun onFailure(message: String?) {

                    }


                })


        }
    }


    fun test() {
        //DymaticTabFragment
        val transaction: FragmentTransaction =
            requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_activity_main, MallPayFragment.newInstance())
        transaction.addToBackStack(MallPayFragment.javaClass.getSimpleName())
        transaction.commit()
    }

}